package com.shrxc.sc.app.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by CH on 2018/9/17.
 */

/**
 * Author：Biligle.
 * 自定义布局
 */

public class TestScrollView extends ScrollView {

    private MyViewGroupListener listener;//接口，监听滑动事件
    private int vertical = 0;//布局距离顶端距离（默认0）

    public TestScrollView(Context context) {
        super(context);
    }

    public TestScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private int downY = 0;//按下时的点
    private int slide = 0;//最终移动距离

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                slide = downY - (int) event.getY();
                if (slide < 0) {//下滑
                    vertical = listener.marginTop(Math.abs(slide));
                } else if (slide > 0) {//上滑
                    vertical = listener.marginTop(-slide);
                }
            case MotionEvent.ACTION_UP:
                if (vertical < 300) {
                    //布局距离屏幕顶部小于300，就让布局充满整个屏幕
                    vertical = listener.marginTop(0);
                }
                break;
        }
        return true;
    }

//    /**
//     * 测量子View
//     *
//     * @param widthMeasureSpec
//     * @param heightMeasureSpec
//     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            //系统测量
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);
//        }
//    }

//    /**
//     * 安排子View的位置
//     *
//     * @param changed
//     * @param l       左边距
//     * @param t       上边距
//     * @param r       右边距
//     * @param b       下边距
//     */
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int left = 0, top = 0, right = 0, bottom = 0;
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            right = left + child.getMeasuredWidth();
//            bottom = top + child.getMeasuredHeight();
//            child.layout(left, top, right, bottom);
//        }
//    }

    public void setListener(MyViewGroupListener listener) {
        this.listener = listener;
    }

    public interface MyViewGroupListener {
        /**
         * 设置topMargin，上下滑动时触发
         *
         * @param slide 滑动距离
         * @return 当前上边距
         */
        int marginTop(int slide);
    }
}
