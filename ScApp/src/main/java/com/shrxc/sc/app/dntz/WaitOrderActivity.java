package com.shrxc.sc.app.dntz;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.AppUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WaitOrderActivity extends AppCompatActivity {

    @BindView(R.id.wait_order_activity_yjsd_time_text)
    TextView yjsdTimeText;
    @BindView(R.id.wait_order_activity_xd_time_text)
    TextView xdTimeText;
    @BindView(R.id.wait_order_activity_cd_name_text)
    TextView cdNameText;
    @BindView(R.id.wait_order_activity_order_name_text)
    TextView orderNameText;
    @BindView(R.id.wait_order_activity_order_money_text)
    TextView orderMoneyText;
    @BindView(R.id.wait_order_activity_order_type_text)
    TextView orderTypeText;
    @BindView(R.id.wait_order_activity_order_num_text)
    TextView orderNumText;
    @BindView(R.id.wait_order_activity_total_money_text)
    TextView totalMoneyText;
    @BindView(R.id.wait_order_activity_psfw_text)
    TextView psfwText;
    @BindView(R.id.wait_order_activity_address_text)
    TextView addressText;
    @BindView(R.id.wait_order_activity_order_text)
    TextView orderText;
    private Context context = WaitOrderActivity.this;
    @BindView(R.id.wait_order_activity_fresh_icon)
    ImageView orderFreshIcon;
    @BindView(R.id.wait_order_activity_sd_time_layout)
    LinearLayout orderSdTimeLayout;
    @BindView(R.id.wait_order_activity_content_layout)
    LinearLayout orderContentLayout;
    @BindView(R.id.wait_order_activity_scrollView)
    ScrollView orderScrollView;
    @BindView(R.id.wait_order_activity_order_layout)
    LinearLayout orderLayout;
    @BindView(R.id.wait_order_activity_order_state_text)
    TextView stateText;
    @BindView(R.id.wait_order_activity_mapview)
    MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    private MyLocationData locData;
    boolean isFirstLoc = true; // 是否首次定位
    int index = 0, wh = 0;

    private float translationY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_order);
        ButterKnife.bind(this);

        initData();
        initView();
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);


        // 隐藏logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        //地图上比例尺
        mMapView.showScaleControl(false);
        // 隐藏缩放控件
        mMapView.showZoomControls(false);


        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private void initData() {

        Intent intent = getIntent();
        String orderid = intent.getStringExtra("orderid");
        Map<String, Object> params = new HashMap<>();
        params.put("Id", orderid);
        HttpRequestUtil.getInstance(context).get("FootBall/FTorderArDetail", params, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if (state.equals("1")) {
                    JSONObject object = result.getJSONObject("Data");
                    String yjsdTime = AppUtil.formatString(object.getString("YJDeliDateTime").replace("T", " "), "HH:mm");
                    yjsdTimeText.setText(yjsdTime);
                    orderText.setText(object.getString("ExpressNo"));
                    totalMoneyText.setText("合计 ￥" + object.getString("TotalMoney"));
                    String cdName = object.getString("MerchantName");
                    cdNameText.setText("由" + cdName + "竞彩店配送");
                    orderNameText.setText("体彩竞彩(" + cdName + "店)");
                    psfwText.setText(cdName + "店");
                    addressText.setText(object.getString("DetailAddress"));
                    String xdTime = AppUtil.formatString(object.getString("Createtime").replace("T", " "), "yyyy-MM-dd HH:mm");
                    xdTimeText.setText(xdTime);
                }

            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
                System.out.println("-------erro-------" + erro);
            }
        });


    }

    private void initView() {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wh = wm.getDefaultDisplay().getHeight();

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        orderFreshIcon.measure(w, h);
        int vh = orderFreshIcon.getMeasuredHeight();

//        int marginTop = wh - vh;
//        System.out.println("------->" + (wh - vh));
        translationY = (float) (wh * 0.6);
        int topMargin = (int) (wh * 0.6 - vh - AppUtil.dip2px(context, 10));
        AppUtil.setViewMargin(orderFreshIcon, AppUtil.dip2px(context, 10), topMargin, 0, 0);
        orderLayout.getBackground().mutate().setAlpha(0);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(orderScrollView, "translationY", translationY);
//      //设置移动时间
        objectAnimator.setDuration(0);
//      //开始动画
        objectAnimator.start();
        Animation animation = new AlphaAnimation(1.0f, 0);
        animation.setDuration(0);
        animation.setFillAfter(true);
        stateText.startAnimation(animation);

        orderScrollView.setOnTouchListener(new View.OnTouchListener() {
            int downY = 0, currentY = 0;

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        downY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        currentY = (int) event.getY();
                        if (translationY > 0) {
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:

                        if (translationY > 0) {
                            if (currentY - downY < 0
                                    && (Math.abs(currentY - downY) > 25)) {

                                Animation animation = new AlphaAnimation(0, 1.0f);
                                animation.setDuration(1500);
                                animation.setFillAfter(true);
                                stateText.startAnimation(animation);

                                //向上滑动
                                ObjectAnimator animator = ObjectAnimator.ofFloat(orderScrollView, "translationY", 0);
                                animator.setDuration(1500);
                                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        float y = (float) animation.getAnimatedValue();
                                        System.out.println("------>" + y);
                                        float f = y / (float) (wh * 0.6);
                                        changeAlpha(f);
                                    }
                                });
                                animator.start();
                                translationY = 0;
                            }
                        } else {
                            if (currentY - downY > 0
                                    && (Math.abs(currentY - downY) > 25) && orderScrollView.getScrollY() == 0) {//向下

                                translationY = (float) (wh * 0.6);
                                ObjectAnimator animator = ObjectAnimator.ofFloat(orderScrollView, "translationY", translationY);
                                animator.setDuration(1500);
                                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        float y = (float) animation.getAnimatedValue();
                                        float f = y / (float) (wh * 0.6);
                                        changeAlpha(f);
                                    }
                                });
                                animator.start();
                                Animation animation = new AlphaAnimation(1.0f, 0);
                                animation.setDuration(1500);
                                animation.setFillAfter(true);
                                stateText.startAnimation(animation);
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void changeAlpha(float alpha) {

        orderLayout.getBackground().mutate().setAlpha((int) (255 - alpha * 255));
        orderFreshIcon.setAlpha(alpha);
    }


    @OnClick({R.id.wait_order_activity_fresh_icon})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wait_order_activity_fresh_icon:
                LatLng ll = new LatLng(mCurrentLat,
                        mCurrentLon);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                break;
        }
    }


    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
//            // 此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
//            // 以下只列举部分获取地址相关的结果信息
//            // 更多结果信息获取说明，请参照类参考中BDLocation类中的说明
//
//            String addr = location.getAddrStr(); // 获取详细地址信息
//            // String country = location.getCountry(); // 获取国家
//            String province = location.getProvince(); // 获取省份
//            String city = location.getCity(); // 获取城市
//            String district = location.getDistrict(); // 获取区县
//            // String street = location.getStreet(); // 获取街道信息
//            System.out.println("-----------address------->" + addr);
//            mLocationClient.stop();


            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(0)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }


            //定义用于显示该InfoWindow的坐标点
            LatLng pt = new LatLng(location.getLatitude(),
                    location.getLongitude());

            View view = LayoutInflater.from(context).inflate(R.layout.wait_order_window_layout, null);

            //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
            InfoWindow mInfoWindow = new InfoWindow(view, pt, 0);

            //显示InfoWindow
            mBaiduMap.showInfoWindow(mInfoWindow);

        }
    }

}

