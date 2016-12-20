[![](https://jitpack.io/v/doonje/wheelmenuview.svg)](https://jitpack.io/#doonje/wheelmenuview)




# WheelMenuView
Custom view for user input that wheel menu view.



<br/><br/>
##Demo
![demo gif](https://github.com/doonje/WheelMenuView/blob/master/static/wheelmenuview_480.gif)



<br/><br/>
##Setup
#####Gradle
Get library from  [jitpack.io](https://jitpack.io/)
```javascript

repositories {
    ...
    maven { url "https://jitpack.io" }

}

dependencies {
    compile 'com.github.doonje:wheelmenuview:1.0.0'
}

```


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



<br/><br/>
MIT License

Copyright (c) 2016 TAEYOUNG KIM

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.