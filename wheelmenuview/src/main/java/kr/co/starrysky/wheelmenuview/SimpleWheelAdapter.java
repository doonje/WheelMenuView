package kr.co.starrysky.wheelmenuview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by starrysky on 2016. 12. 14..
 */
public class SimpleWheelAdapter extends WheelAdapter {

    private ArrayList<String> items;
    private OnItemClickListener onItemClickListener;

    public SimpleWheelAdapter() {

    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
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
        final TextView holder = new TextView(viewGroup.getContext());
        holder.setText(items.get(position));
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) onItemClickListener.onItemClick(holder, position);
            }
        });
        return holder;
    }
}
