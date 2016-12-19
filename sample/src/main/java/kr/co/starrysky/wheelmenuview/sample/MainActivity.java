package kr.co.starrysky.wheelmenuview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.starrysky.wheelmenuview.OnItemClickListener;
import kr.co.starrysky.wheelmenuview.WheelMenuView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        ArrayList<String> menuItems = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            menuItems.add("menu" + i);
        }

        CustomWheelAdapter adapter = new CustomWheelAdapter();
        adapter.setItems(menuItems);

        WheelMenuView wheelMenuView = (WheelMenuView) findViewById(R.id.wheelmenuview);
        wheelMenuView.setAdapter(adapter);
        wheelMenuView.setInterpolator(new DecelerateInterpolator(1.0F));
        wheelMenuView.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Click - " + position, Toast.LENGTH_LONG).show();
            }
        });
        wheelMenuView.setBackgroundResource(R.mipmap.ic_launcher);

    }
}
