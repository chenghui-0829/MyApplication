package com.shrxc.sc.app.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by CH on 2018/9/14.
 */
public class MyScrollView extends ScrollView {

    private OnScrollListener scrollListener;
    private OnScrollToBottomListener onScrollBottomListener;
    private OnScrollToTopListener onScrollTopListener;
    private OnScrollTopAndBottomScrollListener onScrollTopAndBottomScrollListener;
    float e2DownY = 0;
    float e2MoveY = 0;
    float e2UpY = 0;

    GestureDetector gestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            e2DownY = e.getY();
            Log.e("滑动事件  按下： ", "e2DownY:" + (e2DownY));
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            e2UpY = e.getY();
            Log.e("滑动事件 抬起： ", "e2UpY:" + (e2UpY));
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // 已经滚动到底部 继续向上滑动

            if (isScrollBottom) {

                switch (e2.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        e2MoveY = e2.getY();
//                        Log.e("滑动事件： 滑动","e2MoveY:"+(e2MoveY));
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                    default:
                        break;

                }

            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    });

    public void bottomUp(boolean isBottonAndUpScroll, boolean isTopAndDownScroll) {
        if (onScrollTopAndBottomScrollListener != null) {
            onScrollTopAndBottomScrollListener.onScrollTopAndBottomScroll(isBottonAndUpScroll, isTopAndDownScroll);
        }
    }


    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnScrollListener(OnScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public void setOnScrollBottomListener(OnScrollToBottomListener onScrollBottomListener) {
        this.onScrollBottomListener = onScrollBottomListener;
    }

    public void setOnScrollTopAndBottomScrollListener(OnScrollTopAndBottomScrollListener onScrollTopAndBottomScrollListener) {
        this.onScrollTopAndBottomScrollListener = onScrollTopAndBottomScrollListener;
    }

    boolean isFirst = true;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        gestureDetector.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                e2DownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isFirst) {
                    e2DownY = ev.getY();
                    isFirst = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                isFirst = true;
                if (isScrollBottom) {
                    e2UpY = ev.getY();
                    if ((e2UpY - e2DownY) < 0) {
                        Log.e("滑动事件： ", "相减后" + (e2UpY - e2DownY));
//                e2DownY = 0;
                        e2MoveY = 0;
                        e2UpY = 0;
                        bottomUp(isScrollBottom, false);
                        isScrollBottom = false;
                    } else {
//                        Log.e("滑动事件： ","相减后"+(e2UpY - e2DownY));
                    }
                }
                if (isScrollTop) {
                    e2UpY = ev.getY();
                    if ((e2UpY - e2DownY) > 0) {
                        Log.e("滑动事件： ", "相减后" + (e2UpY - e2DownY));
//                e2DownY = 0;
                        e2MoveY = 0;
                        e2UpY = 0;
                        bottomUp(false, isScrollTop);
                        isScrollTop = false;
                    } else {
//                        Log.e("滑动事件： ","相减后"+(e2UpY - e2DownY));
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /*
        * @param l Current horizontal scroll origin.
        * @param t Current vertical scroll origin.
        * @param oldl Previous horizontal scroll origin.
        * @param oldt Previous vertical scroll origin.
        * */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollListener != null) {
            scrollListener.onScrollListener(l, t, oldl, oldt);
        }

    }

    private boolean isScrollBottom;
    private boolean isScrollTop;

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY != 0 && null != onScrollBottomListener) {
            onScrollBottomListener.onScrollBottomListener(isScrollBottom);
        }

        if (scrollY == 0 && null != onScrollTopListener) {
            onScrollTopListener.onScrollTopListener(isScrollTop);
        }

        if (scrollY != 0) {
            isScrollTop = false;
            isScrollBottom = clampedY;
        } else {
            isScrollTop = clampedY;
            isScrollBottom = false;
        }
    }

    /**
     * 滑动到顶部 和 底部的监听
     */
    public interface OnScrollTopAndBottomScrollListener {
        /**
         * @param isUpScroll   是滑动到底部 继续向上滑动
         * @param isDownScroll 是滑动到顶部 继续向下滑动
         */
        void onScrollTopAndBottomScroll(boolean isUpScroll, boolean isDownScroll);
    }


    public interface OnScrollListener {
        /**
         * Called when the scroll position of a view changes.
         * <p>
         * //     * @param v The view whose scroll position has changed.
         *
         * @param scrollX    Current horizontal scroll origin.
         * @param scrollY    Current vertical scroll origin.
         * @param oldScrollX Previous horizontal scroll origin.
         * @param oldScrollY Previous vertical scroll origin.
         */
        void onScrollListener(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    public interface OnScrollToTopListener {
        void onScrollTopListener(boolean isTop);
    }

    public interface OnScrollToBottomListener {
        void onScrollBottomListener(boolean isBottom);
    }

}