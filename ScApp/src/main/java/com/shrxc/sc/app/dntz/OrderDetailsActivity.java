package com.shrxc.sc.app.dntz;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.utils.MyScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CH on 2018/9/14.
 */

public class OrderDetailsActivity extends AppCompatActivity {

    @BindView(R.id.order_details_content_layout)
    LinearLayout contentLayout;
    private Context context = OrderDetailsActivity.this;
    @BindView(R.id.order_details_scrollView)
    MyScrollView orderScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_layout);
        ButterKnife.bind(this);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        final int height = wm.getDefaultDisplay().getHeight();


        orderScrollView.setOnScrollTopAndBottomScrollListener(new MyScrollView.OnScrollTopAndBottomScrollListener() {
            @Override
            public void onScrollTopAndBottomScroll(boolean isUpScroll, boolean isDownScroll) {

                System.out.println(isUpScroll + "=======" + isDownScroll);

//                if (orderScrollView.getScrollY() == 0 && isDownScroll == true) {
//                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(orderScrollView, "translationY", 0f, 0.7f * height);
//                    //设置移动时间
//                    objectAnimator.setDuration(1000);
//                    //开始动画
//                    objectAnimator.start();
//                }
            }
        });

        orderScrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScrollListener(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                System.out.println("===scrolly====" + scrollY);
            }
        });
    }
}
