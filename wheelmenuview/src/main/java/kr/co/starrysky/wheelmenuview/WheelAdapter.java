package kr.co.starrysky.wheelmenuview;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by starrysky on 2016. 12. 14..
 */

public abstract class WheelAdapter {


    public WheelAdapter() {

    }

    public abstract int getCount();

    public abstract View getView(int i, ViewGroup viewGroup);


}
