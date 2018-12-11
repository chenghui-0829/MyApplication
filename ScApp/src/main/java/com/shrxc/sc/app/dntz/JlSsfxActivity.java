package com.shrxc.sc.app.dntz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.adapter.JlSsfxFxJqzjKdListAdapter;
import com.shrxc.sc.app.adapter.JlSsfxFxJqzjZdListAdapter;
import com.shrxc.sc.app.adapter.JlSsfxFxLsjfListAdapter;
import com.shrxc.sc.app.bean.JlChildEntity;
import com.shrxc.sc.app.bean.JzssfxZjEntity;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.AppUtil;
import com.shrxc.sc.app.utils.MyListView;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 赛事分析(蓝球)
 */
public class JlSsfxActivity extends AppCompatActivity {

    private Context context = JlSsfxActivity.this;
    @BindView(R.id.jl_ssfx_activity_ssname_text)
    TextView ssnameTextView;
    @BindView(R.id.jl_ssfx_activity_time_text)
    TextView timeTextView;
    @BindView(R.id.jl_ssfx_activity_zd_name_text)
    TextView zdTextView;
    @BindView(R.id.jl_ssfx_activity_kd_name_text)
    TextView kdTextView;
    @BindView(R.id.jl_ssfx_activity_lsjf_num_text)
    TextView lsjfNumTextView;
    @BindView(R.id.jl_ssfx_activity_lsjf_zd_name_text)
    TextView lsjfZdNameTextView;
    @BindView(R.id.jl_ssfx_activity_lsjf_zj_text)
    TextView lsjfZjTextView;
    @BindView(R.id.jl_ssfx_activity_lsjf_sl_text)
    TextView lsjfSlTextView;
    @BindView(R.id.jl_ssfx_activity_fx_layout_lsjf_icon)
    ImageView lsjfImageView;
    @BindView(R.id.jl_ssfx_activity_jqzj_zd_name_text)
    TextView jqzjZdNameTextView;
    @BindView(R.id.jl_ssfx_activity_jqzj_zd_zj_text)
    TextView jqzjZdZjTextView;
    @BindView(R.id.jl_ssfx_activity_jqzj_zd_sl_text)
    TextView jqzjZdSlTextView;
    @BindView(R.id.jl_ssfx_activity_jqzj_kd_name_text)
    TextView jqzjKdNameTextView;
    @BindView(R.id.jl_ssfx_activity_jqzj_kd_zj_text)
    TextView jqzjKdZjTextView;
    @BindView(R.id.jl_ssfx_activity_jqzj_kd_sl_text)
    TextView jqzjKdSlTextView;
    @BindViews({R.id.ssfx_activity_pl_sf_list, R.id.ssfx_activity_pl_rf_list, R.id.ssfx_activity_pl_dxf_list})
    List<ListView> plListViews;
    @BindViews({R.id.ssfx_activity_fx_con_layout, R.id.ssfx_activity_sl_con_layout})
    List<View> contViews;
    @BindViews({R.id.ssfx_activity_fx_text, R.id.ssfx_activity_sl_text, R.id.ssfx_activity_tj_text, R.id.ssfx_activity_sk_text})
    List<TextView> textViews;
    @BindViews({R.id.ssfx_activity_fx_line, R.id.ssfx_activity_sl_line, R.id.ssfx_activity_tj_line, R.id.ssfx_activity_sk_line})
    List<View> lineViews;
    @BindView(R.id.jl_ssfx_activity_fx_layout_lsjf_list)
    MyListView fxLayoutLsjfdList;//分析->历史交锋
    @BindView(R.id.jl_ssfx_activity_fx_layout_jqzj_zd_list)
    MyListView fxLayoutJqzjZdList;//分析->主队近10次战绩列表
    @BindView(R.id.jl_ssfx_activity_fx_layout_jqzj_kd_list)
    MyListView fxLayoutJqzjKdList;//分析->客队队近10次战绩列表
    @BindViews({R.id.ssfx_activity_pl_sf_text, R.id.ssfx_activity_pl_rf_text, R.id.ssfx_activity_pl_dxf_text})
    List<TextView> plTypeList;
    @BindView(R.id.ssfx_activity_pl_sf_list_title_1_text)
    TextView listTitle1TextView;
    @BindView(R.id.ssfx_activity_pl_sf_list_title_2_text)
    TextView listTitle2TextView;
    @BindView(R.id.ssfx_activity_pl_sf_list_title_3_text)
    TextView listTitle3TextView;
    private int index = 0;
    private List<JzssfxZjEntity> lsjfList, zdjqzjList, kdjqzjList;
    private static final String baseUrl = "http://i.sporttery.cn/api/bk_match_info/";
    private String gameid = "", zdId = "", kdId = "";
    private JlChildEntity gameEntity;
    private JlSsfxFxLsjfListAdapter fxLsjfListAdapter;
    private JlSsfxFxJqzjKdListAdapter fxJqzjKdListAdapter;
    private JlSsfxFxJqzjZdListAdapter fxJqzjZdListAdapter;
    private int fxJqzjZdShowAll = 0, fxJqzjKdShowAll = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssfx);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        gameEntity = (JlChildEntity) bundle.getSerializable("game");
        gameid = gameEntity.getUrlid();
        getGameInfo();
    }

    private void getGameInfo() {

        Map<String, Object> params = new HashMap<>();
        params.put("mid", gameid);
        HttpRequestUtil.getInstance(context).get(baseUrl, "get_match_info",
                params, new RequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result, String state, String msg, String data) {
                        super.onSuccess(result, state, msg, data);
                        JSONObject object = result.getJSONObject("result");
                        zdId = object.getString("h_id_dc");
                        kdId = object.getString("a_id_dc");
                        String endTime = AppUtil.formatString(object.getString("date_cn") + " " +
                                object.getString("time_cn"), "MM-dd HH:mm");
                        timeTextView.setText(endTime);
                        ssnameTextView.setText(object.getString("l_cn_abbr"));
                        zdTextView.setText(object.getString("h_cn_abbr"));
                        kdTextView.setText(object.getString("a_cn_abbr"));
                        lsjfZdNameTextView.setText(object.getString("h_cn_abbr"));
                        jqzjZdNameTextView.setText(object.getString("h_cn_abbr"));
                        jqzjKdNameTextView.setText(object.getString("a_cn_abbr"));
                        getLsjfData();
                        getZdjqzjData();
                        getKdjqzjData();
                    }

                    @Override
                    public void onErro(String erro) {
                        super.onErro(erro);
                    }
                });
    }

    @OnClick({R.id.jl_ssfx_activity_fx_layout_jqzj_kd_list_icon, R.id.jl_ssfx_activity_fx_layout_jqzj_zd_list_icon, R.id.ssfx_activity_fx_layout,
            R.id.ssfx_activity_sl_layout, R.id.ssfx_activity_tj_layout, R.id.ssfx_activity_sk_layout, R.id.ssfx_activity_pl_sf_text, R.id.ssfx_activity_pl_rf_text,
            R.id.ssfx_activity_pl_dxf_text})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jl_ssfx_activity_fx_layout_jqzj_zd_list_icon:
                if (fxJqzjZdShowAll == 0) {
                    fxJqzjZdListAdapter.setCount(10);
                    fxJqzjZdListAdapter.notifyDataSetChanged();
                    fxJqzjZdShowAll = 1;
                } else {
                    fxJqzjZdListAdapter.setCount(5);
                    fxJqzjZdListAdapter.notifyDataSetChanged();
                    fxJqzjZdShowAll = 0;
                }
                break;
            case R.id.jl_ssfx_activity_fx_layout_jqzj_kd_list_icon:
                if (fxJqzjKdShowAll == 0) {
                    fxJqzjKdListAdapter.setCount(10);
                    fxJqzjKdListAdapter.notifyDataSetChanged();
                    fxJqzjKdShowAll = 1;
                } else {
                    fxJqzjKdListAdapter.setCount(5);
                    fxJqzjKdListAdapter.notifyDataSetChanged();
                    fxJqzjKdShowAll = 0;
                }
                break;
            case R.id.ssfx_activity_fx_layout:
                if (index != 0) {
                    showContentView(0);
                }
                break;
            case R.id.ssfx_activity_sl_layout:
                if (index != 1) {
                    showContentView(1);
                }
                break;
            case R.id.ssfx_activity_tj_layout:
                if (index != 2) {
                    showContentView(2);
                }
                break;
            case R.id.ssfx_activity_sk_layout:
                if (index != 3) {
                    showContentView(3);
                }
            case R.id.ssfx_activity_pl_sf_text:
                showPlType(0);
                break;
            case R.id.ssfx_activity_pl_rf_text:
                showPlType(1);
                break;
            case R.id.ssfx_activity_pl_dxf_text:
                showPlType(2);
                break;
        }
    }

    private void showPlType(int type) {

        for (int i = 0; i < plTypeList.size(); i++) {
            if (type == i) {
                plTypeList.get(i).setTextColor(getResources().getColor(R.color.app_text_color_333333));
                plListViews.get(i).setVisibility(View.VISIBLE);
            } else {
                plTypeList.get(i).setTextColor(getResources().getColor(R.color.app_text_color_999999));
                plListViews.get(i).setVisibility(View.GONE);
            }
        }
        switch (type) {
            case 0:
                listTitle1TextView.setText("主负");
                listTitle2TextView.setText("主胜");
                listTitle3TextView.setText("返还率");
                break;
            case 1:
                listTitle1TextView.setText("客队");
                listTitle2TextView.setText("盘口");
                listTitle3TextView.setText("主队");
                break;
            case 2:
                listTitle1TextView.setText("大分");
                listTitle2TextView.setText("盘口");
                listTitle3TextView.setText("小分");
                break;
        }

    }

    private void showContentView(int index) {

        this.index = index;

        for (int i = 0; i < contViews.size(); i++) {
            if (i == index) {
                contViews.get(i).setVisibility(View.VISIBLE);
                textViews.get(i).setTextColor(getResources().getColor(R.color.app_red_color_fa3243));
                lineViews.get(i).setVisibility(View.VISIBLE);
            } else {
                contViews.get(i).setVisibility(View.GONE);
                textViews.get(i).setTextColor(getResources().getColor(R.color.app_text_color_333333));
                lineViews.get(i).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 获取历史交锋数据 http://i.sporttery.cn/api/fb_match_info/get_result_his?limit=10&mid=112924
     */
    private void getLsjfData() {

        lsjfList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 5);
        params.put("mid", gameid);
        HttpRequestUtil.getInstance(context).get(baseUrl, "get_result_his",
                params, new RequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result, String state, String msg, String data) {
                        super.onSuccess(result, state, msg, data);
                        JSONObject object = result.getJSONObject("result");
                        JSONObject zjObject = object.getJSONObject("total");
                        String s = zjObject.getString("win");
                        String f = zjObject.getString("lose");
                        double sl = Double.parseDouble(s) / (Double.parseDouble(s) + Double.parseDouble(f));
                        lsjfZjTextView.setText(s + "胜" + f + "负");
                        lsjfSlTextView.setText(AppUtil.sswrNum(sl * 100, 0) + "%");
                        JSONArray gameArray = object.getJSONArray("data");
                        for (int i = 0; i < gameArray.size(); i++) {
                            JzssfxZjEntity entity = new JzssfxZjEntity();
                            JSONObject dataObject = gameArray.getJSONObject(i);
                            entity.setBf(dataObject.getString("fs_B") + ":" + dataObject.getString("fs_A"));
                            entity.setGameTime(dataObject.getString("date_cn"));
                            entity.setSsName(dataObject.getString("l_cn_abbr"));
                            entity.setZdName(dataObject.getString("h_cn_abbr"));
                            entity.setKdName(dataObject.getString("a_cn_abbr"));
                            entity.setResult(dataObject.getString("team_rs"));
                            lsjfList.add(entity);
                        }
                        lsjfNumTextView.setText("近" + lsjfList.size() + "场交战");
                        lsjfImageView.setVisibility(View.GONE);
                        fxLsjfListAdapter = new JlSsfxFxLsjfListAdapter(context, lsjfList);
                        if (lsjfList.size() < 5) {
                            fxLsjfListAdapter.setCount(lsjfList.size());
                        } else {
                            fxLsjfListAdapter.setCount(5);
                        }
                        fxLayoutLsjfdList.setAdapter(fxLsjfListAdapter);
                    }

                    @Override
                    public void onErro(String erro) {
                        super.onErro(erro);
                        System.out.println("--------erro-------" + erro);
                    }
                });
    }

    /**
     * 获取主队近期战绩
     */
    private void getZdjqzjData() {
        zdjqzjList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 10);
        params.put("tid", zdId);
        HttpRequestUtil.getInstance(context).get(baseUrl, "get_team_rec_data",
                params, new RequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result, String state, String msg, String data) {
                        super.onSuccess(result, state, msg, data);
                        JSONObject object = result.getJSONObject("result");
                        JSONObject zjObject = object.getJSONObject("total");
                        String s = zjObject.getString("win");
                        String f = zjObject.getString("lose");
                        double sl = Double.parseDouble(s) / (Double.parseDouble(s) + Double.parseDouble(f));
                        jqzjZdZjTextView.setText(s + "胜" + f + "负");
                        jqzjZdSlTextView.setText(AppUtil.sswrNum(sl * 100, 0) + "%");
                        JSONArray gameArray = object.getJSONArray("data");
                        for (int i = 0; i < gameArray.size(); i++) {
                            JzssfxZjEntity entity = new JzssfxZjEntity();
                            JSONObject dataObject = gameArray.getJSONObject(i);
                            entity.setBf(dataObject.getString("fs_B") + ":" + dataObject.getString("fs_A"));
                            entity.setGameTime(dataObject.getString("date_cn"));
                            entity.setSsName(dataObject.getString("l_cn_abbr"));
                            entity.setZdName(dataObject.getString("h_cn_abbr"));
                            entity.setKdName(dataObject.getString("a_cn_abbr"));
                            entity.setResult(dataObject.getString("team_rs"));
                            zdjqzjList.add(entity);
                        }
                        fxJqzjZdListAdapter = new JlSsfxFxJqzjZdListAdapter(context, zdjqzjList);
                        fxJqzjZdListAdapter.setCount(5);
                        fxLayoutJqzjZdList.setAdapter(fxJqzjZdListAdapter);
                    }

                    @Override
                    public void onErro(String erro) {
                        super.onErro(erro);
                    }
                });
    }

    /**
     * 获取客队近期战绩
     */
    private void getKdjqzjData() {

        kdjqzjList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 10);
        params.put("tid", kdId);
        HttpRequestUtil.getInstance(context).get(baseUrl, "get_team_rec_data",
                params, new RequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result, String state, String msg, String data) {
                        super.onSuccess(result, state, msg, data);
                        JSONObject object = result.getJSONObject("result");
                        JSONObject zjObject = object.getJSONObject("total");
                        String s = zjObject.getString("win");
                        String f = zjObject.getString("lose");
                        double sl = Double.parseDouble(s) / (Double.parseDouble(s) + Double.parseDouble(f));
                        jqzjKdZjTextView.setText(s + "胜" + f + "负");
                        jqzjKdSlTextView.setText(AppUtil.sswrNum(sl * 100, 0) + "%");
                        JSONArray gameArray = object.getJSONArray("data");
                        for (int i = 0; i < gameArray.size(); i++) {
                            JzssfxZjEntity entity = new JzssfxZjEntity();
                            JSONObject dataObject = gameArray.getJSONObject(i);
                            entity.setBf(dataObject.getString("fs_B") + ":" + dataObject.getString("fs_A"));
                            entity.setGameTime(dataObject.getString("date_cn"));
                            entity.setSsName(dataObject.getString("l_cn_abbr"));
                            entity.setZdName(dataObject.getString("h_cn_abbr"));
                            entity.setKdName(dataObject.getString("a_cn_abbr"));
                            entity.setResult(dataObject.getString("team_rs"));
                            kdjqzjList.add(entity);
                        }
                        fxJqzjKdListAdapter = new JlSsfxFxJqzjKdListAdapter(context, kdjqzjList);
                        fxJqzjKdListAdapter.setCount(5);
                        fxLayoutJqzjKdList.setAdapter(fxJqzjKdListAdapter);
                    }

                    @Override
                    public void onErro(String erro) {
                        super.onErro(erro);
                    }
                });
    }
}
