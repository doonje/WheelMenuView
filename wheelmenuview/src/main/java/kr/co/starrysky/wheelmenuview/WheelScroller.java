package kr.co.starrysky.wheelmenuview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Interpolator;
import android.widget.Scroller;


/**
 *  Created by starrysky on 2016. 12. 14..
 *
 *  reference souce - https://github.com/android-cjj/android-wheel/blob/master/wheel/src/kankan/wheel/widget/WheelScroller.java
 *
 *
 */
public class WheelScroller {
    private static final String TAG = WheelScroller.class.getSimpleName();

    private static final float DEFAULT_FRICTION = 1f;
    private static final float EXCHANGE_DEGREE_VALUE = 0.05f;

    // Messages
    private final int MESSAGE_SCROLL = 0;
    private final int MESSAGE_JUSTIFY = 1;

    private Context context;

    private Scroller scroller;
    private ScrollingListener listener;

    private int lastScrollY;
    private boolean isScrollingPerformed;

    private boolean isRightScrew;

    private float mFriction = DEFAULT_FRICTION;

    /**
     * Scrolling duration
     */
    private static final int SCROLLING_DURATION = 1000;

    /**
     * Minimum delta for scrolling
     */
    public static final int MIN_DELTA_FOR_SCROLLING = 1;


    public WheelScroller(Context c, ScrollingListener scrollingListener) {
        context = c;
        listener = scrollingListener;
        scroller = new Scroller(context);
    }


    /**
     * Set the the specified spinning interpolator
     *
     * @param interpolator the interpolator
     */
    public void setInterpolator(Interpolator interpolator) {
        scroller.forceFinished(true);
        scroller = new Scroller(context, interpolator);
    }


    /**
     * Set the specified spinning friction
     *
     * @param frictionRate the Friction
     */
    public void setFriction(float frictionRate){
        mFriction = frictionRate;
    }

    /**
     * Scroll the wheel
     *
     * @param distance the scrolling distance
     * @param time     the scrolling duration
     */
    public void scroll(int distance, boolean rightScrew, int time) {
        Log.v(TAG, "init scroll - distance : " + distance + ", rightScrew : " + rightScrew + ", time : " + time);

        isRightScrew = rightScrew;

        scroller.forceFinished(true);
        lastScrollY = 0;

        scroller.startScroll(0, 0, 0, distance, time == 0 || time > SCROLLING_DURATION ? SCROLLING_DURATION : time);
        setNextMessage(MESSAGE_SCROLL);

        startScrolling();
    }

    /**
     * Stops scrolling
     */
    public void stopScrolling() {
        scroller.forceFinished(true);
    }


    /**
     * Justifies wheel
     */
    public void justify() {
        listener.onJustify();
        setNextMessage(MESSAGE_JUSTIFY);
    }

    /**
     * Starts scrolling
     */
    private void startScrolling() {
        if (!isScrollingPerformed) {
            isScrollingPerformed = true;
            listener.onStarted();
        }
    }

    /**
     * Finishes scrolling
     */
    void finishScrolling() {
        if (isScrollingPerformed) {
            listener.onFinished();
            isScrollingPerformed = false;
        }
    }

    public boolean isScrolling(){
        if(isScrollingPerformed || !scroller.isFinished()){
            return true;
        }else{
            return false;
        }
    }


    /**
     * Set next message to queue. Clears queue before.
     *
     * @param message the message to set
     */
    private void setNextMessage(int message) {
        clearMessages();
        animationHandler.sendEmptyMessage(message);
    }

    /**
     * Clears messages from queue
     */
    public void clearMessages() {
        animationHandler.removeMessages(MESSAGE_SCROLL);
        animationHandler.removeMessages(MESSAGE_JUSTIFY);
    }

    // animation handler
    private Handler animationHandler = new Handler() {
        public void handleMessage(Message msg) {
            scroller.computeScrollOffset();
            int currY = scroller.getCurrY();
            int delta = lastScrollY - currY;
            lastScrollY = currY;
            if (delta != 0) {
                float degree = (float) ((Math.abs(delta) * EXCHANGE_DEGREE_VALUE * mFriction));
                if (!isRightScrew) {
                    degree = degree * -1;
                }
                listener.onSpin(degree);
            }

            // scrolling is not finished when it comes to final Y
            // so, finish it manually
            if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
                scroller.forceFinished(true);
            }

            if (!scroller.isFinished()) {
                animationHandler.sendEmptyMessage(msg.what);
            } else if (msg.what == MESSAGE_SCROLL) {
                justify();
            } else {
                finishScrolling();
            }
        }
    };


}
