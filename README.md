# WheelMenuView
Custom view for user input that wheel menu view.



<br/><br/>
##Demo
![demo gif](https://github.com/doonje/WheelMenuView/blob/master/static/wheelmenuview_480.gif)




<br/><br/>
##How to use

#####1. Make CustomAdapter

```javascript

    public class CustomWheelAdapter extends WheelAdapter {

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
    }

```


#####2. Set Adapter

```javascript

    WheelMenuView wheelMenuView = (WheelMenuView) findViewById(R.id.wheelmenuview);
    wheelMenuView.setAdapter(adapter);
    wheelMenuView.setInterpolator(new DecelerateInterpolator(1.0F));
    wheelMenuView.setItemClickListener(new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

        }
    });

```


<br/><br/>
##API
Method | Description
--- | ---
`void setAdapter(WheelAdapter wheelAdapter)` | 뷰 생성에 필요한 어댑터 세팅
`void setInterpolator(Interpolator interpolator)` | Set the scrolling interpolator
`void setItemClickListener(OnItemClickListener itemClickListener)` | Add a listener the will be invoked when the user item clicked
`void setBackgroundResource(int resid)` | Set the background image resouce
`void setBackground(Drawable background)` | Set the background drawable

