package com.shrxc.sc.app.fwdt;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.shrxc.sc.app.BaseFragment;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.dntz.CzdtActivity;
import com.shrxc.sc.app.dntz.JczqActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FwdtFragment extends BaseFragment {

    private Unbinder unbinder;
    @BindView(R.id.fwdt_fragment_banner)
    Banner banner;
    @BindView(R.id.fwdt_fragment_list)
    ListView listView;
    @BindView(R.id.fwdt_fragment_scroll)
    ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fwdt, container, false);
        unbinder = ButterKnife.bind(this, view);
        initBanner();
        listView.setAdapter(new ListAdapter());
        scrollView.smoothScrollTo(0, 20);
        scrollView.setFocusable(true);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void initBanner() {

        List<ImageView> images = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.mipmap.home_ad_b);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            images.add(imageView);
        }

        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);

        // 设置轮播样式（没有标题默认为右边,有标题时默认左边）
        // 可选样式:
        // Banner.LEFT 指示器居左
        // Banner.CENTER 指示器居中
        // Banner.RIGHT 指示器居右
        banner.setIndicatorGravity(BannerConfig.RIGHT);

        // 设置是否自动轮播（不设置则默认自动）
        banner.isAutoPlay(true);
        // 设置轮播图片间隔时间（不设置默认为2000）
        banner.setDelayTime(3000);
        // 设置图片资源:可选图片网址/资源文件，默认用Glide加载,也可自定义图片的加载框架
        // 所有设置参数方法都放在此方法之前执行
        banner.setImages(images);
        //设置banner动画效果
        banner.setImageLoader(new ImageLoaderInterface<ImageView>() {

            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setImageResource(R.mipmap.home_ad_b);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            @Override
            public ImageView createImageView(Context context) {
                ImageView imageView = new ImageView(context);
                return imageView;
            }
        });
        banner.start();

    }

    @Override
    protected void loadData() {

        System.out.println("FwdtFragment~~");

    }


    @OnClick({R.id.fwdt_fragment_jctz_text, R.id.fwdt_fragment_sszx_text, R.id.fwdt_fragment_bfzb_text, R.id.fwdt_fragment_sgkj_text})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fwdt_fragment_jctz_text:
                startActivity(new Intent(getActivity(), JczqActivity.class));

                break;
            case R.id.fwdt_fragment_sszx_text:

                startActivity(new Intent(getActivity(), SszxWebActivity.class));

                break;
            case R.id.fwdt_fragment_bfzb_text:

                startActivity(new Intent(getActivity(), BfzbWebActivity.class));

                break;
            case R.id.fwdt_fragment_sgkj_text:

                startActivity(new Intent(getActivity(), CzdtActivity.class));

                break;
        }

    }

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View arg0, ViewGroup viewGroup) {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.fwdt_jqkj_list_adapter_layout, viewGroup, false);

            return view;
        }
    }

}
