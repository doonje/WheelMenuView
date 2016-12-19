package kr.co.starrysky.wheelmenuview;

/**
 * Created by starrysky on 2016. 12. 14..
 *
 * reference souce - https://github.com/android-cjj/android-wheel/blob/master/wheel/src/kankan/wheel/widget/WheelScroller.java
 *
 */

public interface ScrollingListener {


    /**
     * Starting callback called when scrolling is started
     */
    void onStarted();

    /**
     * Finishing callback called after justifying
     */
    void onFinished();

    /**
     * Justifying callback called to justify a view when scrolling is ended
     */
    void onJustify();


    /**
     * Spinning callback called when spinning is performed.
     * @param degrees the distance to spin
     */
    void onSpin(float degrees);
}
