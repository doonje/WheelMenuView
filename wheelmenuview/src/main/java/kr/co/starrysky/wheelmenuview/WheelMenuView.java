package kr.co.starrysky.wheelmenuview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;


/**
 * Created by starrysky on 2016. 12. 14..
 */

public class WheelMenuView extends FrameLayout implements ViewTreeObserver.OnPreDrawListener, ScrollingListener {
    private static final String TAG = WheelMenuView.class.getSimpleName();

    private FrameLayout rootView;

    private int dialerHeight = 0;
    private int dialerWidth = 0;

    private ArrayList<FrameLayout> menuView;
    private OnItemClickListener onItemClickListener;

    private ImageView backgroundImageView;
    private FrameLayout boardLayout;

    private float differenceAngle = 0;

    private WheelAdapter mWheelAdapter;
    private WheelScroller wheelScroll;
    private RotateViewHandler rotateHandler;

    private double startAngle;
    private float startX = 0, startY = 0;

    private GestureDetectorCompat mDetector;


    public WheelMenuView(Context context) {
        super(context);
        initView();
    }

    public WheelMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WheelMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WheelMenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    public void setItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
        setItemViews();
    }

    private int DpToPixel(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return Math.round(px);
    }


    @Override
    public void setBackgroundResource(int resid) {
        backgroundImageView.setBackgroundResource(resid);
    }

    @Override
    public void setBackground(Drawable background) {
        backgroundImageView.setBackground(background);
    }


    public void setInterpolator(Interpolator interpolator) {
        if (wheelScroll != null) {
            wheelScroll.setInterpolator(interpolator);
        }
    }

    private void initView() {
        rootView = this;

        wheelScroll = new WheelScroller(getContext(), this);

        mDetector = new GestureDetectorCompat(getContext(), gestureListener);
        mDetector.setIsLongpressEnabled(false);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        boardLayout = new FrameLayout(getContext());
        backgroundImageView = new ImageView(getContext());

        addView(backgroundImageView, layoutParams);
        addView(boardLayout, layoutParams);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // method called more than once, but the values only need to be initialized one time
                if (dialerHeight == 0 || dialerWidth == 0) {
                    try {
                        dialerHeight = rootView.getHeight();
                        dialerWidth = rootView.getWidth();
                        Log.v(TAG, "size1 width : " + dialerWidth + ", height : " + dialerHeight);


                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "menu rotation onGlobalLayout Exception - " + e.getLocalizedMessage());
                    }
                }
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width > height) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(heightMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
        }
    }


    @Override
    public boolean onPreDraw() {
        return false;
    }


    /**
     * @return The angle of the unit circle with the image view's center
     */
    private double getAngle(double xTouch, double yTouch) {
        double x = xTouch - (dialerWidth / 2d);
        double y = dialerHeight - yTouch - (dialerHeight / 2d);

        // atan2 <-> asin 처리타임에는 거의 비슷함.
        // 방법1. atan
        // return 180 * Math.atan2(y, x) / Math.PI;

        // 방법. asin
        switch (getQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 2:
                return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 3:
                return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                return 0;
        }
    }

    /**
     * @return The selected quadrant.
     */
    private int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }

    private void rotateDialer(float degrees) {
        if (rotateHandler == null) {
            rotateHandler = new RotateViewHandler();
        }

        Message msg = new Message();
        msg.obj = degrees;
        rotateHandler.sendMessage(msg);
    }

    // View를 회전하는 Handler
    class RotateViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mWheelAdapter == null || mWheelAdapter.getCount() == 0 || menuView == null) {
                return;
            }

            float degrees = (float) msg.obj;
            boardLayout.setRotation(boardLayout.getRotation() + degrees);
            for (int i = 0; i < menuView.size(); i++) {
                for (int n = 0; n < menuView.get(i).getChildCount(); n++) {
                    menuView.get(i).getChildAt(n).setRotation(menuView.get(i).getChildAt(n).getRotation() - degrees);
                }
            }
        }

    }


    public void setAdapter(WheelAdapter wheelAdapter) {
        mWheelAdapter = wheelAdapter;

        menuView = new ArrayList<>(mWheelAdapter.getCount());
        differenceAngle = 360 / mWheelAdapter.getCount();
        Log.v(TAG, "difference of angle - " + differenceAngle);

        setItemViews();
    }


    private void setItemViews() {
        if (mWheelAdapter == null || mWheelAdapter.getCount() == 0) {
            return;
        }

        boardLayout.removeAllViewsInLayout();
        for (int i = 0; i < mWheelAdapter.getCount(); i++) {
            final int position = i;
            final View itemView = mWheelAdapter.getView(i, boardLayout);

            final int itemSize = Math.min(itemView.getWidth(), itemView.getHeight());
            Log.v(TAG, "itemSize - " + itemSize);
            if (itemSize > dialerHeight / 3) {
                Log.w(TAG, ":: warning :: Menu item view size is big.");
            }

            final FrameLayout menuLayout = new FrameLayout(getContext());
            menuLayout.addView(itemView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL));

            int paddingSize = DpToPixel(12);
            itemView.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);

            menuView.add(menuLayout);
            boardLayout.addView(menuLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            menuLayout.setRotation(i * differenceAngle);
            itemView.setRotation(-1 * i * differenceAngle);


            if(onItemClickListener != null){
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(itemView, position);
                    }
                });
            }

            itemView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_CANCEL:
//                        default:
                            v.clearFocus();
                            break;
                    }
                    return false;
                }
            });

        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (startX > motionEvent.getX() + 20 || startX < motionEvent.getX() - 20) {
                    return true;
                }
                break;

            case MotionEvent.ACTION_DOWN:
                wheelScroll.stopScrolling();
                wheelScroll.clearMessages();

                startX = motionEvent.getX();
                startY = motionEvent.getY();
                startAngle = getAngle(motionEvent.getX(), motionEvent.getY());
                break;
        }
        return super.onInterceptTouchEvent(motionEvent);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }


    /**
     * implements ScrollingListener func.
     *
     */
    @Override
    public void onStarted() {
        Log.v(TAG, "scroller onStarted");
    }

    @Override
    public void onFinished() {
        Log.v(TAG, "scroller onFinished");
    }

    @Override
    public void onJustify() {
        Log.v(TAG, "scroller onJustify");
    }

    @Override
    public void onSpin(float degrees) {
        rotateDialer(degrees);
    }




    /***
     * rootView touchListener Gesture.
     */
    GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            double currentAngle = getAngle(motionEvent1.getX(), motionEvent1.getY());
            rotateDialer((float) (startAngle - currentAngle));
            startAngle = currentAngle;
            return true;
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {
            int time = 0;
            double distance = Math.sqrt((velocityX * velocityX) + (velocityY * velocityY));

            double x = motionEvent1.getX() - (dialerWidth / 2d);
            double y = dialerHeight - motionEvent1.getY() - (dialerHeight / 2d);
            int isQuadrant = getQuadrant(x, y);
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                switch (isQuadrant) {
                    case 1:
                    case 2:
                        if (velocityX >= 0) {
                            wheelScroll.scroll((int) distance, true, time);
                        } else {
                            wheelScroll.scroll((int) distance, false, time);
                        }
                        break;
                    case 3:
                    case 4:
                        if (velocityX >= 0) {
                            wheelScroll.scroll((int) distance, false, time);
                        } else {
                            wheelScroll.scroll((int) distance, true, time);
                        }

                        break;
                }
            } else {
                switch (isQuadrant) {
                    case 1:
                    case 4:
                        if (velocityY >= 0) {
                            wheelScroll.scroll((int) distance, true, time);
                        } else {
                            wheelScroll.scroll((int) distance, false, time);
                        }
                        break;
                    case 2:
                    case 3:
                        if (velocityY >= 0) {
                            wheelScroll.scroll((int) distance, false, time);
                        } else {
                            wheelScroll.scroll((int) distance, true, time);
                        }
                        break;
                }
            }
            return true;
        }
    };

}