package com.nsk.app.bussiness.index;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.nsk.app.caikangyu.R;
import com.nsk.cky.ckylibrary.widget.SwipeActivity;

import org.jetbrains.annotations.Nullable;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class MainActivity extends SwipeActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int  phoneWidth  = dm.widthPixels ;
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        swipeBackLayout.setEdgeSize(phoneWidth/2);


//        setSwipeMpde(SwipeBackLayout.EDGE_LEFT);
    }
}
