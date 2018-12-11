package com.shrxc.sc.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.shrxc.sc.app.find.FindFragment;
import com.shrxc.sc.app.fwdt.FwdtFragment;
import com.shrxc.sc.app.mine.MineFragment;
import com.shrxc.sc.app.order.OrderFragment;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //当前显示的fragment
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private int selectMenu = 0;//当前选中页
    private FragmentManager fragmentManager;
    private FwdtFragment gcFragment = new FwdtFragment();
    private FindFragment kjFragment;
    private MineFragment mineFragment;
    private OrderFragment orderFragment;
    private Fragment currentFragment = new Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.main_activity_navigation);
        new BottomNavigationViewHelper().disableShiftMode(navigation);
        navigation.setItemIconTintList(null);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initView(savedInstanceState);

    }

    private void initView(Bundle savedInstanceState) {


        fragmentManager = getSupportFragmentManager();


        if (savedInstanceState != null) { // “内存重启”时调用
            System.out.println("=========内存重启========");
            //获取“内存重启”时保存的索引下标
            selectMenu = savedInstanceState.getInt(CURRENT_FRAGMENT, 0);
            //注意，添加顺序要跟下面添加的顺序一样！！！！
            fragments.removeAll(fragments);
            fragments.add(fragmentManager.findFragmentByTag(0 + ""));
            fragments.add(fragmentManager.findFragmentByTag(1 + ""));
            fragments.add(fragmentManager.findFragmentByTag(2 + ""));
            fragments.add(fragmentManager.findFragmentByTag(3 + ""));
            //恢复fragment页面
            restoreFragment();
        } else {//正常启动时调用
            System.out.println("=========正常启动时调用========");
            fragments.add(gcFragment == null ? new FwdtFragment() : gcFragment);
            fragments.add(kjFragment == null ? new FindFragment() : kjFragment);
            fragments.add(orderFragment == null ? new OrderFragment() : orderFragment);
            fragments.add(mineFragment == null ? new MineFragment() : mineFragment);

            showFragment();
        }

    }

    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    private void showFragment() {


        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //如果之前没有添加过
        if (!fragments.get(selectMenu).isAdded()) {
            transaction
                    .hide(currentFragment)
                    .add(R.id.main_activity_root_layout, fragments.get(selectMenu), "" + selectMenu);  //第三个参数为添加当前的fragment时绑定一个tag

        } else {
            transaction
                    .hide(currentFragment)
                    .show(fragments.get(selectMenu));
        }

        currentFragment = fragments.get(selectMenu);

        transaction.commit();

    }

    /**
     * 恢复fragment
     */
    private void restoreFragment() {


        FragmentTransaction mBeginTreansaction = fragmentManager.beginTransaction();

        for (int i = 0; i < fragments.size(); i++) {

            if (i == selectMenu) {
                mBeginTreansaction.show(fragments.get(i));
            } else {
                mBeginTreansaction.hide(fragments.get(i));
            }

        }

        mBeginTreansaction.commit();

        //把当前显示的fragment记录下来
        currentFragment = fragments.get(selectMenu);

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.menu_1_fwdt:
                    changFragment(0);
                    return true;
                case R.id.menu_2_kjgg:
                    changFragment(1);
                    return true;
                case R.id.menu_3_order:
                    changFragment(2);
                    return true;
                case R.id.menu_4_mine:
                    changFragment(3);
                    return true;
            }

            return false;
        }

        private void changFragment(int position) {
            if (selectMenu == position) {
                return;
            }
            System.out.println("========>" + position);
            System.out.println(position);
            selectMenu = position;
            showFragment();
        }
    };

    /**
     * 去除动画
     */
    private class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }
}
