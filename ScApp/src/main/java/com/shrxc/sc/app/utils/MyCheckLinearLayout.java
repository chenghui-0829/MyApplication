package com.shrxc.sc.app.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by CH on 2018/9/4.
 */

public class MyCheckLinearLayout extends LinearLayout {

    boolean mChecked = false;
    //选中状态对应的系统资源
    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public MyCheckLinearLayout(Context context) {
        super(context);
    }

    public MyCheckLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCheckLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected int[] onCreateDrawableState(int extraSpace) {

        //固定写法
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        //判断是否选择
        if (isChecked()) {
            //如果选择, 把选择的状态, 合并到现有的状态中.
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    public boolean isChecked() {
        return mChecked;
    }

    //那么, 当设置选中状态的时候
    public void setChecked(boolean checked) {
        if (mChecked == checked) {
            return;
        }

        //保存当前的选中状态
        mChecked = checked;

        //固定写法, 刷新drawable的状态
        refreshDrawableState();
    }
}
