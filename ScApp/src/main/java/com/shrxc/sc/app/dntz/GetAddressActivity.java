package com.shrxc.sc.app.dntz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.adapter.AddressSearchListAdapter;
import com.shrxc.sc.app.adapter.PoiListAdapter;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GetAddressActivity extends AppCompatActivity {

    private Context context = GetAddressActivity.this;
    @BindView(R.id.get_address_activity_city_text)
    TextView cityText;
    @BindView(R.id.get_address_activity_search_edit)
    EditText searchEdit;
    @BindView(R.id.get_address_activity_list)
    ListView poiListView;
    @BindView(R.id.get_address_activity_search_list)
    ListView searchListView;
    @BindView(R.id.get_address_activity_mapview)
    MapView mMapView;
    @BindView(R.id.get_address_activity_search_layout)
    RelativeLayout searchLayout;

    private String mSelectCity = "";
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private GeoCoder geoCoder;
    private SuggestionSearch mSuggestionSearch;
    private LatLng mLatLng;
    private boolean isFirstLoc = true; // 是否首次定位
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private List<SuggestionResult.SuggestionInfo> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_address);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initView();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        geoCoder.destroy();
        mSuggestionSearch.destroy();
    }

    private void initEvent() {


        searchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (searchEdit.isFocusable()) {
                    return;
                }
                searchEdit.setFocusable(true);
                searchEdit.setFocusableInTouchMode(true);
            }
        });

        searchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (true) {
                    searchLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        //5、搜索框输入监听， 当输入关键字变化时，动态更新建议列表
        searchEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() <= 0) {
                    searchListView.setVisibility(View.GONE);
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .citylimit(true)
                        .keyword(cs.toString())
                        .city(cityText.getText().toString()));
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent intent = new Intent();
                intent.putExtra("name", searchList.get(i).key.toString());
                intent.putExtra("address", searchList.get(i).address.toString());
//                intent.putExtra("province", poiInfos.get(position).province.toString());
                intent.putExtra("city", searchList.get(i).city.toString());
//                intent.putExtra("area", poiInfos.get(position).area.toString());
                setResult(0x887, intent);
                finish();

            }
        });

    }

    @OnClick({R.id.get_address_activity_fresh_icon, R.id.get_address_activity_search_layout, R.id.get_address_activity_city_text,
            R.id.get_address_activity_back_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_address_activity_fresh_icon:
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(mLatLng).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                break;
            case R.id.get_address_activity_search_layout:
                searchEdit.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchLayout.getWindowToken(), 0); //强制隐藏键盘
                searchLayout.setVisibility(View.GONE);
                break;
            case R.id.get_address_activity_city_text:
                searchEdit.clearFocus();
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(searchLayout.getWindowToken(), 0); //强制隐藏键盘
                searchLayout.setVisibility(View.GONE);
                Intent intent = new Intent(context, SelectCityActivity.class);
                intent.putExtra("city", mSelectCity);
                startActivityForResult(intent, 0x123);
                break;
            case R.id.get_address_activity_back_icon:
                finish();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x888) {
            cityText.setText(data.getStringExtra("select"));
        }
    }

    private void initView() {

        mBaiduMap = mMapView.getMap();

        // 隐藏logo
        final View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        //地图上比例尺
        mMapView.showScaleControl(false);
        // 隐藏缩放控件
        mMapView.showZoomControls(false);
        // 定位图层显示方式
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(this);
        // 注册定位监听。让MainActivity实现BDLocationListener，当系统定位成功则调用onReceiveLocation()方法
        mLocationClient.registerLocationListener(myListener);
        // 定位参数选项
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        // 设置是否需要地址信息，默认为无地址
        option.setIsNeedAddress(true);
        // 设置是否需要返回位置语义化信息，可以在BDLocation.getLocationDescribe()中得到数据，ex:"在天安门附近"，
        // 可以用作地址信息的补充
        option.setIsNeedLocationDescribe(true);
        // 设置是否需要返回位置POI信息，可以在BDLocation.getPoiList()中得到数据
        option.setIsNeedLocationPoiList(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 设置是否打开gps进行定位
        option.setOpenGps(true);
        // 设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
        option.setScanSpan(1000);
        // 设置 LocationClientOption
        mLocationClient.setLocOption(option);
        // 开始定位
        mLocationClient.start();

        //----------------------------地图状态改变设置----------------------------
        MapStatus mapStatus = new MapStatus.Builder().zoom(15).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        // 地图状态改变相关监听
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            /**
             * 当用户拖动地图结束时，调用该方法
             */
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                LatLng cenpt = mapStatus.target;
                System.out.println("最后停止点:" + cenpt.latitude + "," + cenpt.longitude);
//                //判断定位点是否在可配送范围内---如果需要设置配送的范围则加上此段代码
//                if (!SpatialRelationUtil.isPolygonContainsPoint(mPoints, cenpt)) {
//                    Toast.makeText(getApplicationContext(), "该地址不在配送范围，请在黄色区域内选择", Toast.LENGTH_LONG).show();
//                }
                //将中心点坐标转化为具体位置信息，当转化成功后调用onGetReverseGeoCodeResult()方法
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(cenpt));
            }
        });
//        ----------------------------反地理编码GeoCoder设置-------------------------- -
        // 创建GeoCoder实例对象
        geoCoder = GeoCoder.newInstance();
        // 设置查询结果监听者
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                final List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();

                if (poiInfos != null && !"".equals(poiInfos)) {

                    System.out.println("-------poiInfos------->" + poiInfos);
                    //创建poiAdapter 将获取到的Poi数据传入，更新UI
                    PoiListAdapter poiAdapter = new PoiListAdapter(context, poiInfos);
                    poiListView.setAdapter(poiAdapter);
                    poiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent();
                            intent.putExtra("name", poiInfos.get(position).name);
                            intent.putExtra("address", poiInfos.get(position).address);
                            intent.putExtra("province", poiInfos.get(position).province);
                            intent.putExtra("city", poiInfos.get(position).city);
                            intent.putExtra("area", poiInfos.get(position).area);
                            setResult(0x888, intent);
                            finish();
                        }
                    });
                }
            }
        });


//        searchListAdapter = new AddressSearchListAdapter(context, searchList);

        // 2、实例化POI热词建议检索，注册搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                    return;
                }
//                mSuggestionInfos.clear();//清空数据列表
//                sugAdapter.clear();//清空列表适配器
                List<SuggestionResult.SuggestionInfo> suggestionInfoList = suggestionResult.getAllSuggestions();
                searchList = new ArrayList<>();
                if (suggestionInfoList != null) {
                    for (SuggestionResult.SuggestionInfo info : suggestionInfoList) {
                        if (info.pt != null) {
                            //过滤没有经纬度信息的
                            searchList.add(info);
                            System.out.println("----------->" + info);
                        }
                    }
                }
                if (searchList.size() == 0) {
                    searchListView.setVisibility(View.GONE);
                } else {
                    searchListView.setVisibility(View.VISIBLE);
                    searchListView.setAdapter(new AddressSearchListAdapter(context, searchList, mLatLng));
                }
//                sugAdapter.notifyDataSetChanged();
            }
        });
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
//            String street = location.getStreet(); // 获取街道信息
//            String des = location.getLocationDescribe(); // 获取街道信息

            if (location == null || mBaiduMap == null) {
                return;
            }
            // 定位数据
            MyLocationData data = new MyLocationData.Builder()
                    // 定位精度bdLocation.getRadius()
                    .accuracy(30)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection())
                    // 经度
                    .latitude(location.getLatitude())
                    // 纬度
                    .longitude(location.getLongitude())
                    // 构建
                    .build();

            // 设置定位数据
            mBaiduMap.setMyLocationData(data);
            if (isFirstLoc) {
                isFirstLoc = false;

                // 根据定位的地点，以动画方式更新地图状态
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//            //判断定位点是否在可配送范围内---如果需要设置配送的范围则加上此段代码
//            if (!SpatialRelationUtil.isPolygonContainsPoint(mPoints, ll)) {
//                Toast.makeText(getApplicationContext(), "该地址不在配送范围，请在可选区域内选择", Toast.LENGTH_LONG).show();
//
//                //ll = SpatialRelationUtil.getNearestPointFromLine(mPoints, ll);//获取离配送范围最近的点坐标
//            }
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18.0f);
                mBaiduMap.animateMapStatus(msu);
                mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//             // 获取城市，待会用于POI热词建议检索
                mSelectCity = location.getCity();
                if (mSelectCity.endsWith("市")) {
                    mSelectCity = mSelectCity.substring(0, mSelectCity.length() - 1);
                }
                cityText.setText(mSelectCity);
                // 发起反地理编码请求(经纬度->地址信息)
                ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
                // 设置反地理编码位置坐标
                reverseGeoCodeOption.location(new LatLng(location.getLatitude(), location.getLongitude()));
                geoCoder.reverseGeoCode(reverseGeoCodeOption);
            }
//            LatLng pt = new LatLng(location.getLatitude(),
//                    location.getLongitude());
            //构建Marker图标
//            ImageView view = new ImageView(context);
//            view.setBackgroundResource(R.mipmap.get_address_activity_current_address_icon);
//            //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
//            InfoWindow mInfoWindow = new InfoWindow(view, pt, 0);
//            //显示InfoWindow
//            mBaiduMap.showInfoWindow(mInfoWindow);
        }
    }
}
