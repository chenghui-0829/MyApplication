package com.shrxc.sc.app.dntz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.adapter.JzSsfxFxJqzjKdListAdapter;
import com.shrxc.sc.app.adapter.JzSsfxFxJqzjZdListAdapter;
import com.shrxc.sc.app.adapter.JzSsfxFxLsjfListAdapter;
import com.shrxc.sc.app.adapter.JzSsfxPlDxpKlzsListAdapter;
import com.shrxc.sc.app.adapter.JzSsfxPlDxpPlsjListAdapter;
import com.shrxc.sc.app.adapter.JzSsfxPlOpKlzsListAdapter;
import com.shrxc.sc.app.adapter.JzSsfxPlOpPlsjListAdapter;
import com.shrxc.sc.app.adapter.JzSsfxPlYpKlzsListAdapter;
import com.shrxc.sc.app.adapter.JzSsfxPlYpPlsjListAdapter;
import com.shrxc.sc.app.bean.JzChildGameEntity;
import com.shrxc.sc.app.bean.JzssfxOpEntity;
import com.shrxc.sc.app.bean.JzssfxYpEntity;
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

public class JzSsfxActivity extends AppCompatActivity {


    @BindView(R.id.jz_ssfx_activity_time_text)
    TextView timeTextView;
    @BindView(R.id.jz_ssfx_activity_zd_name_text)
    TextView zdTextView;
    @BindView(R.id.jz_ssfx_activity_kd_name_text)
    TextView kdTextView;
    @BindView(R.id.jz_ssfx_activity_lsjf_num_text)
    TextView lsjfNumTextView;
    @BindView(R.id.jz_ssfx_activity_lsjf_zd_name_text)
    TextView lsjfZdNameTextView;
    @BindView(R.id.jz_ssfx_activity_lsjf_zj_text)
    TextView lsjfZjTextView;
    @BindView(R.id.jz_ssfx_activity_lsjf_sl_text)
    TextView lsjfSlTextView;
    @BindView(R.id.jz_ssfx_activity_fx_layout_lsjf_icon)
    ImageView lsjfImageView;
    @BindView(R.id.jz_ssfx_activity_jqzj_zd_name_text)
    TextView jqzjZdNameTextView;
    @BindView(R.id.jz_ssfx_activity_jqzj_zd_zj_text)
    TextView jqzjZdZjTextView;
    @BindView(R.id.jz_ssfx_activity_jqzj_zd_sl_text)
    TextView jqzjZdSlTextView;
    @BindView(R.id.jz_ssfx_activity_jqzj_kd_name_text)
    TextView jqzjKdNameTextView;
    @BindView(R.id.jz_ssfx_activity_jqzj_kd_zj_text)
    TextView jqzjKdZjTextView;
    @BindView(R.id.jz_ssfx_activity_jqzj_kd_sl_text)
    TextView jqzjKdSlTextView;
    @BindViews({R.id.jz_ssfx_activity_tj_layout, R.id.jz_ssfx_activity_fx_layout, R.id.jz_ssfx_activity_pl_layout,
            R.id.jz_ssfx_activity_sk_layout, R.id.jz_ssfx_activity_zr_layout})
    List<LinearLayout> typeLayouts;//推荐、分析、赔率、赛况、必发
    @BindView(R.id.jz_ssfx_activity_content_layout)
    FrameLayout contentLayout;
    @BindView(R.id.jz_ssfx_activity_pl_content_layout)
    FrameLayout plContentLayout;
    @BindView(R.id.jz_ssfx_activity_fx_layout_lsjf_list)
    MyListView fxLayoutLsjfdList;//分析->历史交锋
    @BindView(R.id.jz_ssfx_activity_fx_layout_jqzj_zd_list)
    MyListView fxLayoutJqzjZdList;//分析->主队近10次战绩列表
    @BindView(R.id.jz_ssfx_activity_fx_layout_jqzj_kd_list)
    MyListView fxLayoutJqzjKdList;//分析->客队队近10次战绩列表
    @BindView(R.id.jz_ssfx_activity_fx_layout_jqzj_zd_list_icon)
    ImageView fxLayoutJqzjZdListIcon;//分析->显示主队近10次全部战绩
    @BindView(R.id.jz_ssfx_activity_fx_layout_jqzj_kd_list_icon)
    ImageView fxLayoutJqzjKdListIcon;//分析->显示客队近10次全部战绩
    @BindViews({R.id.jz_ssfx_activity_pl_layout_op_text, R.id.jz_ssfx_activity_pl_layout_yp_text,
            R.id.jz_ssfx_activity_pl_layout_dxf_text, R.id.jz_ssfx_activity_pl_layout_bf_text})
    List<TextView> plTypeTexts;//欧赔、亚盘、大小分、必发
    @BindViews({R.id.jz_ssfx_activity_pl_plsj_layout, R.id.jz_ssfx_activity_pl_klzs_layout})
    List<LinearLayout> plLayoutPlType;//赔率数据、凯利指数
    @BindView(R.id.jz_ssfx_activity_pl_op_plsj_list)
    ListView plLayoutOpPlsjList;//赔率->欧赔->赔率数据列表
    @BindView(R.id.jz_ssfx_activity_pl_op_klzs_list)
    ListView plLayoutOpKlzsList;//赔率->欧赔->凯利指数列表
    @BindView(R.id.jz_ssfx_activity_pl_yp_plsj_list)
    ListView plLayoutYpPlsjList;//赔率->亚盘->赔率数据列表
    @BindView(R.id.jz_ssfx_activity_pl_yp_klzs_list)
    ListView plLayoutYpKlzsList;//赔率->亚盘->凯利指数列表
    @BindView(R.id.jz_ssfx_activity_pl_dxp_plsj_list)
    ListView plLayoutDxpPlsjList;//赔率->大小盘->赔率数据列表
    @BindView(R.id.jz_ssfx_activity_pl_dxp_klzs_list)
    ListView plLayoutDxpKlzsList;//赔率->大小盘->凯利指数列表
    private Context context = JzSsfxActivity.this;
    private JzSsfxFxLsjfListAdapter fxLsjfListAdapter;
    private JzSsfxFxJqzjZdListAdapter fxJqzjZdListAdapter;
    private JzSsfxFxJqzjKdListAdapter fxJqzjKdListAdapter;
    private JzSsfxPlOpPlsjListAdapter plOpPlsjListAdapter;
    private JzSsfxPlYpPlsjListAdapter plYpPlsjListAdapter;
    private JzSsfxPlOpKlzsListAdapter plOpKlzsListAdapter;
    private JzSsfxPlYpKlzsListAdapter plYpKlzsListAdapter;
    private JzSsfxPlDxpPlsjListAdapter plDxpPlsjListAdapter;
    private JzSsfxPlDxpKlzsListAdapter plDxpKlzsListAdapter;
    private int fxJqzjZdShowAll = 0, fxJqzjKdShowAll = 0, plType = 0, fxLsjfShowAll = 0;
    private List<JzssfxYpEntity> ypDataList;
    private List<JzssfxOpEntity> opDataList;
    private List<JzssfxZjEntity> lsjfList, zdjqzjList, kdjqzjList;
    private static final String baseUrl = "http://i.sporttery.cn/api/fb_match_info/";
    private String gameid = "", zdId = "", kdId = "";
    private JzChildGameEntity gameEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jz_ssfx);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        gameEntity = (JzChildGameEntity) bundle.getSerializable("game");
        gameid = gameEntity.getUrlid();
        String endTime = AppUtil.formatString(gameEntity.getStarttime().replace("T", " "), "MM-dd HH:mm");
        timeTextView.setText(endTime + "  " + gameEntity.getLeague());
        zdTextView.setText(gameEntity.getHometeam());
        kdTextView.setText(gameEntity.getGuestteam());
        getGameInfo();
        plDxpPlsjListAdapter = new JzSsfxPlDxpPlsjListAdapter(context);
        plDxpKlzsListAdapter = new JzSsfxPlDxpKlzsListAdapter(context);
        plLayoutDxpPlsjList.setAdapter(plDxpPlsjListAdapter);
        plLayoutDxpKlzsList.setAdapter(plDxpKlzsListAdapter);
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
                        System.out.println("---------zdId--------" + zdId + "---------kdId--------" + kdId);
                    }

                    @Override
                    public void onErro(String erro) {
                        super.onErro(erro);
                    }
                });
    }

    @OnClick({R.id.jz_ssfx_activity_pl_layout, R.id.jz_ssfx_activity_fx_layout_lsjf_icon, R.id.jz_ssfx_activity_fx_layout_jqzj_zd_list_icon, R.id.jz_ssfx_activity_fx_layout_jqzj_kd_list_icon,
            R.id.jz_ssfx_activity_tj_layout, R.id.jz_ssfx_activity_fx_layout, R.id.jz_ssfx_activity_sk_layout,
            R.id.jz_ssfx_activity_zr_layout, R.id.jz_ssfx_activity_pl_layout_op_text, R.id.jz_ssfx_activity_pl_layout_yp_text,
            R.id.jz_ssfx_activity_pl_layout_dxf_text, R.id.jz_ssfx_activity_pl_layout_bf_text, R.id.jz_ssfx_activity_pl_plsj_layout,
            R.id.jz_ssfx_activity_pl_klzs_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jz_ssfx_activity_fx_layout_lsjf_icon:
                if (fxLsjfShowAll == 0) {
                    fxLsjfListAdapter.setCount(10);
                    fxLsjfListAdapter.notifyDataSetChanged();
                    fxLsjfShowAll = 1;
                } else {
                    fxLsjfListAdapter.setCount(5);
                    fxLsjfListAdapter.notifyDataSetChanged();
                    fxLsjfShowAll = 0;
                }
                break;
            case R.id.jz_ssfx_activity_fx_layout_jqzj_zd_list_icon:
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
            case R.id.jz_ssfx_activity_fx_layout_jqzj_kd_list_icon:
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
            case R.id.jz_ssfx_activity_tj_layout:
                showSelectContent(0, typeLayouts, contentLayout);
                fxLayoutJqzjKdList.setAdapter(fxJqzjKdListAdapter);
                fxLayoutJqzjZdList.setAdapter(fxJqzjZdListAdapter);
                break;
            case R.id.jz_ssfx_activity_fx_layout:
                showSelectContent(1, typeLayouts, contentLayout);
                getLsjfData();
                getZdjqzjData();
                getKdjqzjData();
                break;
            case R.id.jz_ssfx_activity_pl_layout:
                showSelectContent(2, typeLayouts, contentLayout);
                showPlTypeContent(0);
                showSelectPlList(0);
                showSelectContent(0, plLayoutPlType, null);
                getPlopData();//获取欧盘赔率数据
                break;
            case R.id.jz_ssfx_activity_sk_layout:
                showSelectContent(3, typeLayouts, contentLayout);
                break;
            case R.id.jz_ssfx_activity_zr_layout:
                showSelectContent(4, typeLayouts, contentLayout);
                break;
            case R.id.jz_ssfx_activity_pl_layout_op_text:
                showPlTypeContent(0);
                showSelectPlList(0);
                showSelectContent(0, plLayoutPlType, null);
                getPlopData();//获取欧盘赔率数据
                break;
            case R.id.jz_ssfx_activity_pl_layout_yp_text:
                showPlTypeContent(1);
                showSelectPlList(0);
                showSelectContent(0, plLayoutPlType, null);
                getPlypData();//获取亚盘赔率数据
                break;
            case R.id.jz_ssfx_activity_pl_layout_dxf_text:
                showPlTypeContent(2);
                showSelectPlList(0);
                showSelectContent(0, plLayoutPlType, null);
                break;
            case R.id.jz_ssfx_activity_pl_layout_bf_text:
                showPlTypeContent(3);
                showSelectPlList(0);
                showSelectContent(0, plLayoutPlType, null);
                break;
            case R.id.jz_ssfx_activity_pl_plsj_layout://赔率数据
                showSelectContent(0, plLayoutPlType, null);
                showSelectPlList(0);
                break;
            case R.id.jz_ssfx_activity_pl_klzs_layout://凯利指数
                showSelectContent(1, plLayoutPlType, null);
                showSelectPlList(1);
                break;
        }
    }

    /**
     * 获取主队近期战绩 http://i.sporttery.cn/api/fb_match_info/get_team_rec_data?tid=932&limit=10
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
                        String p = zjObject.getString("draw");
                        String f = zjObject.getString("lose");
                        double sl = Double.parseDouble(s) / (Double.parseDouble(s) + Double.parseDouble(p) + Double.parseDouble(f));
                        jqzjZdZjTextView.setText(s + "胜" + p + "平" + f + "负");
                        jqzjZdSlTextView.setText(AppUtil.sswrNum(sl * 100, 0) + "%");
                        JSONArray gameArray = object.getJSONArray("data");
                        for (int i = 0; i < gameArray.size(); i++) {
                            JzssfxZjEntity entity = new JzssfxZjEntity();
                            JSONObject dataObject = gameArray.getJSONObject(i);
                            entity.setBf(dataObject.getString("final"));
                            entity.setBcbf(dataObject.getString("half"));
                            entity.setGameTime(dataObject.getString("date_cn"));
                            entity.setSsName(dataObject.getString("l_cn_abbr"));
                            entity.setZdName(dataObject.getString("h_cn_abbr"));
                            entity.setKdName(dataObject.getString("a_cn_abbr"));
                            entity.setResult(dataObject.getString("team_rs"));
                            zdjqzjList.add(entity);
                        }
                        fxJqzjZdListAdapter = new JzSsfxFxJqzjZdListAdapter(context, zdjqzjList);
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
                        String p = zjObject.getString("draw");
                        String f = zjObject.getString("lose");
                        double sl = Double.parseDouble(s) / (Double.parseDouble(s) + Double.parseDouble(p) + Double.parseDouble(f));
                        jqzjKdZjTextView.setText(s + "胜" + p + "平" + f + "负");
                        jqzjKdSlTextView.setText(AppUtil.sswrNum(sl * 100, 0) + "%");
                        JSONArray gameArray = object.getJSONArray("data");
                        for (int i = 0; i < gameArray.size(); i++) {
                            JzssfxZjEntity entity = new JzssfxZjEntity();
                            JSONObject dataObject = gameArray.getJSONObject(i);
                            entity.setBf(dataObject.getString("final"));
                            entity.setBcbf(dataObject.getString("half"));
                            entity.setGameTime(dataObject.getString("date_cn"));
                            entity.setSsName(dataObject.getString("l_cn_abbr"));
                            entity.setZdName(dataObject.getString("h_cn_abbr"));
                            entity.setKdName(dataObject.getString("a_cn_abbr"));
                            entity.setResult(dataObject.getString("team_rs"));
                            kdjqzjList.add(entity);
                        }
                        fxJqzjKdListAdapter = new JzSsfxFxJqzjKdListAdapter(context, kdjqzjList);
                        fxJqzjKdListAdapter.setCount(5);
                        fxLayoutJqzjKdList.setAdapter(fxJqzjKdListAdapter);
                    }

                    @Override
                    public void onErro(String erro) {
                        super.onErro(erro);
                    }
                });
    }

    /**
     * 获取历史交锋数据 http://i.sporttery.cn/api/fb_match_info/get_result_his?limit=10&mid=112924
     */
    private void getLsjfData() {

        lsjfList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 10);
        params.put("mid", gameid);
        HttpRequestUtil.getInstance(context).get(baseUrl, "get_result_his",
                params, new RequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result, String state, String msg, String data) {
                        super.onSuccess(result, state, msg, data);
                        JSONObject object = result.getJSONObject("result");
                        JSONObject zjObject = object.getJSONObject("total");
                        String s = zjObject.getString("win");
                        String p = zjObject.getString("draw");
                        String f = zjObject.getString("lose");
                        double sl = Double.parseDouble(s) / (Double.parseDouble(s) + Double.parseDouble(p) + Double.parseDouble(f));
                        System.out.println("----------sl-------" + sl);
                        lsjfZjTextView.setText(s + "胜" + p + "平" + f + "负");
                        lsjfSlTextView.setText(AppUtil.sswrNum(sl * 100, 0) + "%");
                        JSONArray gameArray = object.getJSONArray("data");
                        for (int i = 0; i < gameArray.size(); i++) {
                            JzssfxZjEntity entity = new JzssfxZjEntity();
                            JSONObject dataObject = gameArray.getJSONObject(i);
                            entity.setBf(dataObject.getString("final"));
                            entity.setBcbf(dataObject.getString("half"));
                            entity.setGameTime(dataObject.getString("date_cn"));
                            entity.setSsName(dataObject.getString("l_cn_abbr"));
                            entity.setZdName(dataObject.getString("h_cn_abbr"));
                            entity.setKdName(dataObject.getString("a_cn_abbr"));
                            entity.setResult(dataObject.getString("team_rs"));
                            lsjfList.add(entity);
                        }

                        lsjfNumTextView.setText("近" + lsjfList.size() + "场交战");
                        fxLsjfListAdapter = new JzSsfxFxLsjfListAdapter(context, lsjfList);
                        if (lsjfList.size() < 5) {
                            lsjfImageView.setVisibility(View.GONE);
                            fxLsjfListAdapter.setCount(lsjfList.size());
                        } else {
                            fxLsjfListAdapter.setCount(5);
                            lsjfImageView.setVisibility(View.VISIBLE);
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
     * 获取欧赔赔率数据  http://i.sporttery.cn/api/fb_match_info/get_europe/?mid=112870
     */
    private void getPlopData() {

        opDataList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("mid", gameid);
        HttpRequestUtil.getInstance(context).get(baseUrl, "get_europe",
                params, new RequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result, String state, String msg, String data) {
                        super.onSuccess(result, state, msg, data);
                        JSONObject resultString = result.getJSONObject("result");
                        JSONArray dataArray = resultString.getJSONArray("data");
                        for (int i = 0; i < dataArray.size(); i++) {
                            JzssfxOpEntity entity = JSON.parseObject(dataArray.get(i).toString(), JzssfxOpEntity.class);
                            opDataList.add(entity);
                        }
                        plOpPlsjListAdapter = new JzSsfxPlOpPlsjListAdapter(opDataList, context);
                        plLayoutOpPlsjList.setAdapter(plOpPlsjListAdapter);
                        plOpKlzsListAdapter = new JzSsfxPlOpKlzsListAdapter(opDataList, context);
                        plLayoutOpKlzsList.setAdapter(plOpKlzsListAdapter);
                    }

                    @Override
                    public void onErro(String erro) {
                        super.onErro(erro);
                        System.out.println("--------erro-------" + erro);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });


    }

    /**
     * 获取亚盘赔率数据  http://i.sporttery.cn/api/fb_match_info/get_asia?mid=112870
     */
    private void getPlypData() {

        ypDataList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("mid", gameid);
        HttpRequestUtil.getInstance(context).get(baseUrl, "get_asia",
                params, new RequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result, String state, String msg, String data) {
                        super.onSuccess(result, state, msg, data);
                        JSONObject resultString = result.getJSONObject("result");
                        JSONArray dataArray = resultString.getJSONArray("data");
                        for (int i = 0; i < dataArray.size(); i++) {
                            JzssfxYpEntity entity = JSON.parseObject(dataArray.get(i).toString(), JzssfxYpEntity.class);
                            ypDataList.add(entity);
                        }
                        plYpPlsjListAdapter = new JzSsfxPlYpPlsjListAdapter(ypDataList, context);
                        plLayoutYpPlsjList.setAdapter(plYpPlsjListAdapter);
                        plYpKlzsListAdapter = new JzSsfxPlYpKlzsListAdapter(ypDataList, context);
                        plLayoutYpKlzsList.setAdapter(plYpKlzsListAdapter);
                    }

                    @Override
                    public void onErro(String erro) {
                        super.onErro(erro);
                        System.out.println("--------erro-------" + erro);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });


    }

    private void showSelectPlList(int type) {

        for (int i = 0; i < plContentLayout.getChildCount(); i++) {
            if (i == plType * 2 + type) {
                plContentLayout.getChildAt(i).setVisibility(View.VISIBLE);
            } else {
                plContentLayout.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }

    private void showPlTypeContent(int type) {

        plType = type;
        for (int i = 0; i < plTypeTexts.size(); i++) {
            if (i == type) {
                plTypeTexts.get(i).setTextColor(getResources().getColor(R.color.app_white_color_ffffff));
                if (type == 0) {
                    plTypeTexts.get(i).setBackgroundResource(R.drawable.left_gray_circle_bg);
                } else if (type == 3) {
                    plTypeTexts.get(i).setBackgroundResource(R.drawable.right_gray_circle_bg);
                } else {
                    plTypeTexts.get(i).setBackgroundResource(R.color.app_gray_color_b1b1b7);
                }
            } else {
                plTypeTexts.get(i).setTextColor(getResources().getColor(R.color.app_text_color_333333));
                if (i == 0) {
                    plTypeTexts.get(i).setBackgroundResource(R.drawable.left_whilte_circle_bg);
                } else if (i == 3) {
                    plTypeTexts.get(i).setBackgroundResource(R.drawable.right_whilte_circle_bg);
                } else {
                    plTypeTexts.get(i).setBackgroundResource(R.color.app_white_color_ffffff);
                }
            }
        }
    }

    private void showSelectContent(int type, List<LinearLayout> layouts, FrameLayout layout) {

        for (int i = 0; i < layouts.size(); i++) {
            if (i == type) {
                ((TextView) layouts.get(i).getChildAt(0)).setTextColor(getResources().
                        getColor(R.color.app_red_color_fa3243));
                layouts.get(i).getChildAt(1).setVisibility(View.VISIBLE);
                if (layout != null) {
                    layout.getChildAt(i).setVisibility(View.VISIBLE);
                }
            } else {
                ((TextView) layouts.get(i).getChildAt(0)).setTextColor(getResources().
                        getColor(R.color.app_text_color_333333));
                layouts.get(i).getChildAt(1).setVisibility(View.GONE);
                if (layout != null) {
                    layout.getChildAt(i).setVisibility(View.GONE);
                }
            }
        }
    }
}
