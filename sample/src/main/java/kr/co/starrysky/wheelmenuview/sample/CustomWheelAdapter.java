package kr.co.starrysky.wheelmenuview.sample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.starrysky.wheelmenuview.WheelAdapter;

/**
 * Created by starrysky on 2016. 12. 14..
 */
public class CustomWheelAdapter extends WheelAdapter {


    private ArrayList<String> items;

    public CustomWheelAdapter() {

    }

    public void setItems(ArrayList<String> _items) {
        items = _items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(final int position, ViewGroup viewGroup) {
        final ItemViewHolder holder = new ItemViewHolder(viewGroup);
        holder.setContent(items.get(position));
        return holder.mView;
    }


    private class ItemViewHolder {
        View mView;
        ImageView mImageView;
        TextView mTextView;

        public ItemViewHolder(ViewGroup viewGroup) {
            mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.i_wheel_item, viewGroup, false);

            mImageView = (ImageView) mView.findViewById(R.id.i_wheel_image);
            mTextView = (TextView) mView.findViewById(R.id.i_wheel_text);
        }

        public void setContent(String text) {
            mTextView.setText(text);
        }
    }
}
