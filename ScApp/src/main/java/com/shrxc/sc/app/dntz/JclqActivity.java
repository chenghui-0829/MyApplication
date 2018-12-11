package com.shrxc.sc.app.dntz;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.adapter.JlDgtzElvAdapter;
import com.shrxc.sc.app.adapter.JlDxfElvAdapter;
import com.shrxc.sc.app.adapter.JlHhggElvAdapter;
import com.shrxc.sc.app.adapter.JlRfsfElvAdapter;
import com.shrxc.sc.app.adapter.JlSfElvAdapter;
import com.shrxc.sc.app.adapter.JlSfcElvAdapter;
import com.shrxc.sc.app.bean.JlChildEntity;
import com.shrxc.sc.app.bean.JlGroupEntity;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 竞彩篮球
 */
public class JclqActivity extends AppCompatActivity {


    private Context context = JclqActivity.this;
    @BindView(R.id.jl_activity_select_type_text)
    TextView selectTextView;
    @BindView(R.id.jl_activity_title_layout)
    RelativeLayout titleLayout;
    @BindViews({R.id.jl_activity_sf_elv, R.id.jl_activity_rfsf_elv, R.id.jl_activity_sfc_elv,
            R.id.jl_activity_dxf_elv, R.id.jl_activity_hhgg_elv, R.id.jl_activity_dgtz_elv})
    List<ExpandableListView> elvList;
    @BindView(R.id.jl_activity_tm_view)
    View tmView;
    @BindView(R.id.jl_activity_select_edit)
    EditText bsEditText;
    @BindViews({R.id.jl_activity_select_ggfs_dg_text, R.id.jl_activity_select_ggfs_2_1_text, R.id.jl_activity_select_ggfs_3_1_text,
            R.id.jl_activity_select_ggfs_4_1_text, R.id.jl_activity_select_ggfs_5_1_text, R.id.jl_activity_select_ggfs_6_1_text,
            R.id.jl_activity_select_ggfs_7_1_text, R.id.jl_activity_select_ggfs_8_1_text})
    List<TextView> ggfsList;
    private List<TextView> textList;
    private int select = 0, isShowGgfs = 1, bsNum = 0;
    private List<JlGroupEntity> gameList;
    private JlSfElvAdapter sfElvAdapter;
    private JlRfsfElvAdapter rfsfElvAdapter;
    private JlSfcElvAdapter sfcElvAdapter;
    private JlDxfElvAdapter dxfElvAdapter;
    private JlHhggElvAdapter hhggElvAdapter;
    private JlDgtzElvAdapter dgtzElvAdapter;
    private String teamsCountry[] = {"亚特兰大", "波士顿", "芝加哥", "夏洛特", "布鲁克林", "克利夫兰", "迈阿密",
            "纽约", "底特律", "奥兰多", "费城", "印第安纳", "华盛顿", "多伦多", "密尔沃基", "达拉斯", "丹佛",
            "金州", "金州", "休斯敦", "明尼苏达", "洛杉矶", "孟菲斯", "俄克拉荷马城", "新奥尔良", "波特兰", "菲尼克斯",
            "圣安东尼奥", "犹他", "萨克拉门托", "印第安那"};
    public static TextView selectNumTextView;
    private List<JlChildEntity> selectList;
    public static Map<Integer, Integer> ggfsMap, stateMap;
    public static LinearLayout ggfsLayout;
    public static TextView ggfsTextView;
    private String ggfs = "";
    private List<String> ssTypeList;
    public static List<JlGroupEntity> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jclq);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        selectNumTextView = findViewById(R.id.jl_activity_select_num_text);
        ggfsTextView = findViewById(R.id.jl_activity_select_ggfs_text);
        ggfsLayout = findViewById(R.id.jl_activity_select_ggfs_layout);
        orderList = new ArrayList<>();
        showTzTypeElv(0, "胜负");
        initEvent();
    }

    private void initEvent() {
        bsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().equals("0")) {
                    bsEditText.setText("1");
                }
            }
        });
    }

    @OnClick({R.id.jl_activity_select_type_text, R.id.jl_activity_more_icon, R.id.jl_activity_sure_text, R.id.jl_activity_select_ggfs_text,
            R.id.jl_activity_select_add_icon, R.id.jl_activity_select_sub_icon, R.id.jl_activity_sx_icon})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jl_activity_select_type_text:
                showTzTypeDialog();
                break;
            case R.id.jl_activity_more_icon:
                break;
            case R.id.jl_activity_sx_icon:
                Intent intent = new Intent(context, JlsxActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("sstype", (Serializable) ssTypeList);
                intent.putExtras(bundle);
                startActivityForResult(intent, 200);
                break;
            case R.id.jl_activity_sure_text:
                selectButEvent();
                break;
            case R.id.jl_activity_select_ggfs_text:
                changeGgfsLayout();
                break;
            case R.id.jl_activity_select_add_icon:
                String bs = bsEditText.getText().toString();
                if (bs.equals("") || bs.equals("0")) {
                    bsNum = 1;
                } else {
                    bsNum = Integer.parseInt(bs);
                }

                if (bsNum < 99) {
                    bsNum++;
                } else {
                    bsNum = 99;
                }
                bsEditText.setText(bsNum + "");
                break;
            case R.id.jl_activity_select_sub_icon:
                String mbs = bsEditText.getText().toString();
                if (mbs.equals("") || mbs.equals("0")) {
                    bsNum = 1;
                } else {
                    bsNum = Integer.parseInt(mbs);
                }
                if (bsNum > 1) {
                    bsNum--;
                } else {
                    bsNum = 1;
                }
                bsEditText.setText(bsNum + "");
                break;
        }
    }

    private void selectButEvent() {

        switch (select) {
            case 0:
                selectList = sfElvAdapter.getSelectList();
                break;
            case 1:
                selectList = rfsfElvAdapter.getSelectList();
                break;
            case 2:
                selectList = sfcElvAdapter.getSelectList();
                break;
            case 3:
                selectList = dxfElvAdapter.getSelectList();
                break;
            case 4:
                selectList = hhggElvAdapter.getSelectList();
                break;
            case 5:
                selectList = dgtzElvAdapter.getSelectList();
                break;
        }
        if (selectList.size() < 2) {
            if (select == 6) {
                if (selectList.size() == 0) {
                    Toast.makeText(context, "请至少选择1场比赛", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(context, "请至少选择2场比赛", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        ggfs = "";
        for (Integer key : ggfsMap.keySet()) {
            if (ggfsMap.get(key) == 1) {
                if (key == 0) {
                    ggfs += "1,";
                } else if (key == 1) {
                    ggfs += "2,";
                } else if (key == 2) {
                    ggfs += "3,";
                } else if (key == 3) {
                    ggfs += "4,";
                } else if (key == 4) {
                    ggfs += "5,";
                } else if (key == 5) {
                    ggfs += "6,";
                } else if (key == 6) {
                    ggfs += "7,";
                } else if (key == 7) {
                    ggfs += "8,";
                }
            }
        }
        Intent intent = new Intent(context, JlSelectTeamActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected", (Serializable) selectList);
        intent.putExtras(bundle);
        intent.putExtra("ggfs", select);
        intent.putExtra("mcn", ggfs);
        intent.putExtra("bs", bsEditText.getText().toString());
        startActivity(intent);
    }

    private void initGgfs() {
        ggfsMap = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            ggfsMap.put(i, 0);
        }
        for (int i = 0; i < ggfsList.size(); i++) {
            ggfsList.get(i).setEnabled(false);
            ggfsList.get(i).setBackgroundResource(R.drawable.app_gray_stroke_circle_color);
        }
        ggfsList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                changeGgfsState(0);
            }
        });
        ggfsList.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeGgfsState(1);
            }
        });
        ggfsList.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeGgfsState(2);
            }
        });
        ggfsList.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeGgfsState(3);
            }
        });
        ggfsList.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeGgfsState(4);
            }
        });
        ggfsList.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeGgfsState(5);
            }
        });
        ggfsList.get(6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeGgfsState(6);
            }
        });
        ggfsList.get(7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeGgfsState(7);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x888) {
            List<JlGroupEntity> sxList = new ArrayList<>();
            Bundle bundle = data.getExtras();
            List<String> selectList = (List<String>) bundle.getSerializable("select");
            for (int i = 0; i < gameList.size(); i++) {
                JlGroupEntity gameEntity = new JlGroupEntity();
                gameEntity.setTime(gameList.get(i).getTime());
//                gameEntity.setGameNum(gameList.get(i).getGameNum());
                List<JlChildEntity> childList = new ArrayList<>();
                for (int j = 0; j < gameList.get(i).getList().size(); j++) {
                    if (selectList.contains(gameList.get(i).getList().get(j).getLeague())) {
                        childList.add(gameList.get(i).getList().get(j));
                    }
                }
                gameEntity.setList(childList);
                sxList.add(gameEntity);
            }
            System.out.println("--------" + sxList.get(0).getList().size() + "-----------" + gameList.get(0).getList().size());

            for (int i = 0; i < sxList.size(); i++) {
                if (sxList.get(i).getList().size() == 0) {
                    sxList.remove(i);
                }
            }

            switch (select) {
                case 0:
                    sfElvAdapter = new JlSfElvAdapter(context, sxList);
                    elvList.get(select).setAdapter(sfElvAdapter);
                    elvList.get(select).expandGroup(0);
                    break;
                case 1:
                    rfsfElvAdapter = new JlRfsfElvAdapter(context, sxList);
                    elvList.get(select).setAdapter(rfsfElvAdapter);
                    elvList.get(select).expandGroup(0);
                    break;
                case 2:
                    sfcElvAdapter = new JlSfcElvAdapter(context, sxList);
                    elvList.get(select).setAdapter(sfcElvAdapter);
                    elvList.get(select).expandGroup(0);
                    break;
                case 3:
                    dxfElvAdapter = new JlDxfElvAdapter(context, sxList);
                    elvList.get(select).setAdapter(dxfElvAdapter);
                    elvList.get(select).expandGroup(0);
                    break;
                case 4:
                    hhggElvAdapter = new JlHhggElvAdapter(context, sxList);
                    elvList.get(select).setAdapter(hhggElvAdapter);
                    elvList.get(select).expandGroup(0);
                    break;
                case 5:
                    dgtzElvAdapter = new JlDgtzElvAdapter(context, sxList);
                    elvList.get(select).setAdapter(dgtzElvAdapter);
                    elvList.get(select).expandGroup(0);
                    break;
            }
        }
    }

    private void changeGgfsState(int index) {

        if (ggfsMap.get(index) == 0) {
            ggfsMap.put(index, 1);
            ggfsList.get(index).setBackgroundResource(R.drawable.red_circle_stroke_bg);
        } else {
            ggfsMap.put(index, 0);
            ggfsList.get(index).setBackgroundResource(R.drawable.app_gray_stroke_circle_color);
        }
    }

    private void changeGgfsLayout() {
        if (isShowGgfs == 0) {
            String text = "";
            for (Integer key : ggfsMap.keySet()) {
                if (ggfsMap.get(key) == 1) {
                    if (key == 0) {
                        text += "单关  ";
                    } else if (key == 1) {
                        text += "2串1  ";
                    } else if (key == 2) {
                        text += "3串1  ";
                    } else if (key == 3) {
                        text += "4串1  ";
                    } else if (key == 4) {
                        text += "5串1  ";
                    } else if (key == 5) {
                        text += "6串1  ";
                    } else if (key == 6) {
                        text += "7串1  ";
                    } else if (key == 7) {
                        text += "8串1  ";
                    }
                }
            }
            if (text.replaceAll(" ", "").length() == 0) {
                ggfsTextView.setText("过关方式");
            } else {
                ggfsTextView.setText(text);
            }
            ggfsLayout.setVisibility(View.GONE);
            isShowGgfs = 1;
        } else {
            isShowGgfs = 0;
            ggfsTextView.setText("收起");
            ggfsLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showTzTypeElv(int type, String title) {
        isShowGgfs = 0;
//        if (type != select) {
        initGgfs();
        changeGgfsLayout();
//        } else {
//            changeGgfsLayout();
//        }
        bsEditText.setText(bsNum + "");
        selectTextView.setText(title);
        select = type;
        for (int i = 0; i < elvList.size(); i++) {
            if (i == type) {
                elvList.get(i).setVisibility(View.VISIBLE);
            } else {
                elvList.get(i).setVisibility(View.GONE);
            }
        }

        switch (type) {
            case 0:
                getSfGameData();
                break;
            case 1:
                getRfsfGameData();
                break;
            case 2:
                getSfcGameData();
                break;
            case 3:
                getDxfGameData();
                break;
            case 4:
                getHhggameData();
                break;
            case 5:
                getDgtzGameData();
                break;

        }
    }

    /**
     * 单关投注
     */
    private void getDgtzGameData() {

        gameList = new ArrayList<>();
        HttpRequestUtil.getInstance(context).getByNoParams("TicketData/GetBTDGList", new RequestCallback() {

            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if (data == null || "".equals(data)) {
                    Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (state != null && "1".equals(state)) {
                    JSONObject object = JSONObject.parseObject(data);
                    JSONArray gameArray = object.getJSONArray("list");
                    if (gameArray.size() == 0) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<JlChildEntity> childEntities = new ArrayList<>();
                    for (int i = 0; i < gameArray.size(); i++) {
                        JlChildEntity childEntity = new JlChildEntity();
                        JSONObject entityObject = gameArray.getJSONObject(i);
                        childEntity.setPlanid(entityObject.getString("planid"));
                        childEntity.setGameid(entityObject.getString("gameid"));
                        childEntity.setLeague(entityObject.getString("league"));
                        childEntity.setUrlid(entityObject.getString("urlid"));
                        childEntity.setEndtime(entityObject.getString("endtime"));
                        childEntity.setHometeam(getTeamName(entityObject.getString("hometeam")));
                        childEntity.setGuestteam(getTeamName(entityObject.getString("guestteam")));
//                        childEntity.setZspl(entityObject.getString("zspl"));
//                        childEntity.setZfpl(entityObject.getString("kspl"));
//                        childEntity.setRangfen(entityObject.getString("rangfen"));
//                        childEntity.setMinpl(entityObject.getString("minpl"));
//                        childEntity.setMaxpl(entityObject.getString("maxpl"));
//                        childEntity.setZongfen(entityObject.getString("zongfen"));
                        childEntities.add(childEntity);
                    }
                    ssTypeList = new ArrayList<>();
                    JSONArray types = object.getJSONArray("leas");
                    for (int i = 0; i < types.size(); i++) {
                        ssTypeList.add(types.getString(i));
                    }

                    stateMap = new HashMap<>();
                    for (int i = 0; i < ssTypeList.size(); i++) {
                        stateMap.put(i, 1);
                    }

                    Map<String, List<JlChildEntity>> listMap = new HashMap<>();
                    JSONArray times = object.getJSONArray("times");
                    for (int i = 0; i < times.size(); i++) {
                        listMap.put(times.getString(i), null);
                    }
                    gameList = getGameList(listMap, childEntities);
                    if (gameList.size() != 0) {
                        dgtzElvAdapter = new JlDgtzElvAdapter(context, gameList);
                        elvList.get(5).setAdapter(dgtzElvAdapter);
                        elvList.get(5).expandGroup(0);
                    }
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
                System.out.println("-----erro---------" + erro);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    /**
     * 混合过关
     */
    private void getHhggameData() {

        gameList = new ArrayList<>();
        HttpRequestUtil.getInstance(context).getByNoParams("TicketData/GetBTList", new RequestCallback() {

            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if (data == null || "".equals(data)) {
                    Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (state != null && "1".equals(state)) {
                    JSONObject object = JSONObject.parseObject(data);
                    JSONArray gameArray = object.getJSONArray("list");
                    if (gameArray.size() == 0) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<JlChildEntity> childEntities = new ArrayList<>();
                    for (int i = 0; i < gameArray.size(); i++) {
                        JlChildEntity childEntity = new JlChildEntity();
                        JSONObject entityObject = gameArray.getJSONObject(i);
                        childEntity.setPlanid(entityObject.getString("planid"));
                        childEntity.setGameid(entityObject.getString("gameid"));
                        childEntity.setLeague(entityObject.getString("league"));
                        childEntity.setUrlid(entityObject.getString("urlid"));
                        childEntity.setEndtime(entityObject.getString("endtime"));
                        childEntity.setHometeam(getTeamName(entityObject.getString("hometeam")));
                        childEntity.setGuestteam(getTeamName(entityObject.getString("guestteam")));
                        childEntity.setRzspl(entityObject.getString("zspl"));
                        childEntity.setRzfpl(entityObject.getString("kspl"));
                        childEntity.setRangfen(entityObject.getString("rangfen"));
                        childEntity.setMinpl(entityObject.getString("minpl"));
                        childEntity.setMaxpl(entityObject.getString("maxpl"));
                        childEntity.setZongfen(entityObject.getString("zongfen"));
                        childEntities.add(childEntity);
                    }
                    ssTypeList = new ArrayList<>();
                    JSONArray types = object.getJSONArray("leas");
                    for (int i = 0; i < types.size(); i++) {
                        ssTypeList.add(types.getString(i));
                    }

                    stateMap = new HashMap<>();
                    for (int i = 0; i < ssTypeList.size(); i++) {
                        stateMap.put(i, 1);
                    }

                    Map<String, List<JlChildEntity>> listMap = new HashMap<>();
                    JSONArray times = object.getJSONArray("times");
                    for (int i = 0; i < times.size(); i++) {
                        listMap.put(times.getString(i), null);
                    }
                    gameList = getGameList(listMap, childEntities);
                    if (gameList.size() != 0) {
                        hhggElvAdapter = new JlHhggElvAdapter(context, gameList);
                        elvList.get(4).setAdapter(hhggElvAdapter);
                        elvList.get(4).expandGroup(0);
                    }
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
                System.out.println("-----erro---------" + erro);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    /**
     * 胜分差
     */
    private void getSfcGameData() {

        gameList = new ArrayList<>();
        HttpRequestUtil.getInstance(context).getByNoParams("TicketData/GetBTSFCList", new RequestCallback() {

            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if (data == null || "".equals(data)) {
                    Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (state != null && "1".equals(state)) {
                    JSONObject object = JSONObject.parseObject(data);
                    JSONArray gameArray = object.getJSONArray("list");
                    if (gameArray.size() == 0) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<JlChildEntity> childEntities = new ArrayList<>();
                    for (int i = 0; i < gameArray.size(); i++) {
                        JlChildEntity childEntity = new JlChildEntity();
                        JSONObject entityObject = gameArray.getJSONObject(i);
                        childEntity.setPlanid(entityObject.getString("planid"));
                        childEntity.setGameid(entityObject.getString("gameid"));
                        childEntity.setLeague(entityObject.getString("league"));
                        childEntity.setUrlid(entityObject.getString("urlid"));
                        childEntity.setEndtime(entityObject.getString("endtime"));
                        childEntity.setHometeam(getTeamName(entityObject.getString("hometeam")));
                        childEntity.setGuestteam(getTeamName(entityObject.getString("guestteam")));
                        childEntity.setZS1_5(entityObject.getString("ZS1_5"));
                        childEntity.setZS6_10(entityObject.getString("ZS6_10"));
                        childEntity.setZS11_15(entityObject.getString("ZS11_15"));
                        childEntity.setZS16_20(entityObject.getString("ZS16_20"));
                        childEntity.setZS21_25(entityObject.getString("ZS21_25"));
                        childEntity.setZS26(entityObject.getString("ZS26"));
                        childEntity.setKS1_5(entityObject.getString("KS1_5"));
                        childEntity.setKS6_10(entityObject.getString("KS6_10"));
                        childEntity.setKS11_15(entityObject.getString("KS11_15"));
                        childEntity.setKS16_20(entityObject.getString("KS16_20"));
                        childEntity.setKS21_25(entityObject.getString("KS21_25"));
                        childEntity.setKS26(entityObject.getString("KS26"));
                        childEntities.add(childEntity);
                    }
                    ssTypeList = new ArrayList<>();
                    JSONArray types = object.getJSONArray("leas");
                    for (int i = 0; i < types.size(); i++) {
                        ssTypeList.add(types.getString(i));
                    }

                    stateMap = new HashMap<>();
                    for (int i = 0; i < ssTypeList.size(); i++) {
                        stateMap.put(i, 1);
                    }

                    Map<String, List<JlChildEntity>> listMap = new HashMap<>();
                    JSONArray times = object.getJSONArray("times");
                    for (int i = 0; i < times.size(); i++) {
                        listMap.put(times.getString(i), null);
                    }
                    gameList = getGameList(listMap, childEntities);
                    if (gameList.size() != 0) {
                        sfcElvAdapter = new JlSfcElvAdapter(context, gameList);
                        elvList.get(2).setAdapter(sfcElvAdapter);
                        elvList.get(2).expandGroup(0);
                    }
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
                System.out.println("-----erro---------" + erro);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    /**
     * 大小分
     */
    private void getDxfGameData() {

        gameList = new ArrayList<>();
        HttpRequestUtil.getInstance(context).getByNoParams("TicketData/GetBTDXFList", new RequestCallback() {

            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if (data == null || "".equals(data)) {
                    Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (state != null && "1".equals(state)) {
                    JSONObject object = JSONObject.parseObject(data);
                    JSONArray gameArray = object.getJSONArray("list");
                    if (gameArray.size() == 0) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<JlChildEntity> childEntities = new ArrayList<>();
                    for (int i = 0; i < gameArray.size(); i++) {
                        JlChildEntity childEntity = new JlChildEntity();
                        JSONObject entityObject = gameArray.getJSONObject(i);
                        childEntity.setPlanid(entityObject.getString("planid"));
                        childEntity.setGameid(entityObject.getString("gameid"));
                        childEntity.setUrlid(entityObject.getString("urlid"));
                        childEntity.setLeague(entityObject.getString("league"));
                        childEntity.setEndtime(entityObject.getString("endtime"));
                        childEntity.setHometeam(getTeamName(entityObject.getString("hometeam")));
                        childEntity.setGuestteam(getTeamName(entityObject.getString("guestteam")));
                        childEntity.setMinpl(entityObject.getString("Min"));
                        childEntity.setMaxpl(entityObject.getString("Max"));
                        childEntity.setZongfen(entityObject.getString("ZF"));
                        childEntities.add(childEntity);
                    }
                    ssTypeList = new ArrayList<>();
                    JSONArray types = object.getJSONArray("leas");
                    for (int i = 0; i < types.size(); i++) {
                        ssTypeList.add(types.getString(i));
                    }

                    stateMap = new HashMap<>();
                    for (int i = 0; i < ssTypeList.size(); i++) {
                        stateMap.put(i, 1);
                    }

                    Map<String, List<JlChildEntity>> listMap = new HashMap<>();
                    JSONArray times = object.getJSONArray("times");
                    for (int i = 0; i < times.size(); i++) {
                        listMap.put(times.getString(i), null);
                    }
                    gameList = getGameList(listMap, childEntities);
                    if (gameList.size() != 0) {
                        dxfElvAdapter = new JlDxfElvAdapter(context, gameList);
                        elvList.get(3).setAdapter(dxfElvAdapter);
                        elvList.get(3).expandGroup(0);
                    }
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
                System.out.println("-----erro---------" + erro);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    /**
     * 让分胜负
     */
    private void getRfsfGameData() {

        gameList = new ArrayList<>();
        HttpRequestUtil.getInstance(context).getByNoParams("TicketData/GetBTRSFList", new RequestCallback() {

            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if (data == null || "".equals(data)) {
                    Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (state != null && "1".equals(state)) {
                    JSONObject object = JSONObject.parseObject(data);
                    JSONArray gameArray = object.getJSONArray("list");
                    if (gameArray.size() == 0) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<JlChildEntity> childEntities = new ArrayList<>();
                    for (int i = 0; i < gameArray.size(); i++) {
                        JlChildEntity childEntity = new JlChildEntity();
                        JSONObject entityObject = gameArray.getJSONObject(i);
                        childEntity.setPlanid(entityObject.getString("planid"));
                        childEntity.setGameid(entityObject.getString("gameid"));
                        childEntity.setLeague(entityObject.getString("league"));
                        childEntity.setEndtime(entityObject.getString("endtime"));
                        childEntity.setUrlid(entityObject.getString("urlid"));
                        childEntity.setHometeam(getTeamName(entityObject.getString("hometeam")));
                        childEntity.setGuestteam(getTeamName(entityObject.getString("guestteam")));
                        childEntity.setRzspl(entityObject.getString("zspl"));
                        childEntity.setRzfpl(entityObject.getString("zfpl"));
                        childEntity.setRangfen(entityObject.getString("rangfen"));
                        childEntities.add(childEntity);
                    }
                    ssTypeList = new ArrayList<>();
                    JSONArray types = object.getJSONArray("leas");
                    for (int i = 0; i < types.size(); i++) {
                        ssTypeList.add(types.getString(i));
                    }
                    stateMap = new HashMap<>();
                    for (int i = 0; i < ssTypeList.size(); i++) {
                        stateMap.put(i, 1);
                    }
                    Map<String, List<JlChildEntity>> listMap = new HashMap<>();
                    JSONArray times = object.getJSONArray("times");
                    for (int i = 0; i < times.size(); i++) {
                        listMap.put(times.getString(i), null);
                    }
                    gameList = getGameList(listMap, childEntities);
                    if (gameList.size() != 0) {
                        rfsfElvAdapter = new JlRfsfElvAdapter(context, gameList);
                        elvList.get(1).setAdapter(rfsfElvAdapter);
                        elvList.get(1).expandGroup(0);
                    }
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
                System.out.println("-----erro---------" + erro);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    /***
     * 胜负
     */
    private void getSfGameData() {

        gameList = new ArrayList<>();
        HttpRequestUtil.getInstance(context).getByNoParams("TicketData/GetBTSFList", new RequestCallback() {

            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if (data == null || "".equals(data)) {
                    Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (state != null && "1".equals(state)) {
                    JSONObject object = JSONObject.parseObject(data);
                    JSONArray gameArray = object.getJSONArray("list");
                    if (gameArray.size() == 0) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<JlChildEntity> childEntities = new ArrayList<>();
                    for (int i = 0; i < gameArray.size(); i++) {
                        JlChildEntity childEntity = new JlChildEntity();
                        JSONObject entityObject = gameArray.getJSONObject(i);
                        childEntity.setPlanid(entityObject.getString("planid"));
                        childEntity.setGameid(entityObject.getString("gameid"));
                        childEntity.setLeague(entityObject.getString("league"));
                        childEntity.setEndtime(entityObject.getString("endtime"));
                        childEntity.setUrlid(entityObject.getString("urlid"));
                        childEntity.setHometeam(getTeamName(entityObject.getString("hometeam")));
                        childEntity.setGuestteam(getTeamName(entityObject.getString("guestteam")));
                        childEntity.setZspl(entityObject.getString("zspl"));
                        childEntity.setZfpl(entityObject.getString("zfpl"));
                        childEntities.add(childEntity);
                    }
                    ssTypeList = new ArrayList<>();
                    JSONArray types = object.getJSONArray("leas");
                    for (int i = 0; i < types.size(); i++) {
                        ssTypeList.add(types.getString(i));
                    }
                    stateMap = new HashMap<>();
                    for (int i = 0; i < ssTypeList.size(); i++) {
                        stateMap.put(i, 1);
                    }

                    Map<String, List<JlChildEntity>> listMap = new HashMap<>();
                    JSONArray times = object.getJSONArray("times");
                    for (int i = 0; i < times.size(); i++) {
                        listMap.put(times.getString(i), null);
                    }
                    gameList = getGameList(listMap, childEntities);
                    if (gameList.size() != 0) {
                        sfElvAdapter = new JlSfElvAdapter(context, gameList);
                        elvList.get(0).setAdapter(sfElvAdapter);
                        elvList.get(0).expandGroup(0);
                    }
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
                System.out.println("-----erro---------" + erro);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void showTzTypeDialog() {


        View view = LayoutInflater.from(context).inflate(R.layout.jc_lqtz_type_window_layout, null);
        TextView sfTextView = view.findViewById(R.id.jc_lqtz_type_dialog_sf_text);
        TextView rfsfTextView = view.findViewById(R.id.jc_lqtz_type_dialog_rfsf_text);
        TextView sfcTextView = view.findViewById(R.id.jc_lqtz_type_dialog_sfc_text);
        TextView dxfTextView = view.findViewById(R.id.jc_lqtz_type_dialog_dxf_text);
        TextView hhggTextView = view.findViewById(R.id.jc_lqtz_type_dialog_hhgg_text);
        TextView dgtzTextView = view.findViewById(R.id.jc_lqtz_type_dialog_dgtz_text);

        textList = new ArrayList<>();
        textList.add(sfTextView);
        textList.add(rfsfTextView);
        textList.add(sfcTextView);
        textList.add(dxfTextView);
        textList.add(hhggTextView);
        textList.add(dgtzTextView);

        for (int i = 0; i < textList.size(); i++) {
            if (i == select) {
                textList.get(i).setTextColor(getResources().getColor(R.color.app_white_color_ffffff));
                textList.get(i).setBackgroundResource(R.drawable.jc_type_button_bg);
            } else {
                textList.get(i).setTextColor(getResources().getColor(R.color.app_text_color_666666));
                textList.get(i).setBackgroundResource(R.drawable.app_gray_stroke_circle_color);
            }
        }

        final PopupWindow pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.showAsDropDown(titleLayout);
        tmView.setVisibility(View.VISIBLE);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tmView.setVisibility(View.GONE);
            }
        });
        /**
         * 胜负
         */
        sfTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                if (select == 0) {
                    return;
                }
                showTzTypeElv(0, "胜负");
            }
        });

        /**
         * 让分胜负
         */
        rfsfTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                if (select == 1) {
                    return;
                }
                showTzTypeElv(1, "让分胜负");
            }
        });
        /**
         * 胜分差
         */
        sfcTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                if (select == 2) {
                    return;
                }
                showTzTypeElv(2, "胜分差");
            }
        });
        /**
         * 大小分
         */
        dxfTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                if (select == 3) {
                    return;
                }
                showTzTypeElv(3, "大小分");
            }
        });

        /**
         * 混合过关
         */
        hhggTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                if (select == 4) {
                    return;
                }
                showTzTypeElv(4, "混合过关");
            }
        });
        /**
         * 单关投注
         */
        dgtzTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                if (select == 5) {
                    return;
                }
                showTzTypeElv(5, "单关投注");
            }
        });
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    private Map<String, List<JlChildEntity>> sortMapByKey(Map<String, List<JlChildEntity>> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, List<JlChildEntity>> sortMap = new TreeMap<>(
                new Comparator<String>() {
                    @Override
                    public int compare(String str1, String str2) {
                        return str1.compareTo(str2);
                    }
                });
        sortMap.putAll(map);
        return sortMap;
    }

    private List<JlGroupEntity> getGameList(Map<String, List<JlChildEntity>> listMap, List<JlChildEntity> childList) {

        List<JlGroupEntity> gameList = new ArrayList<>();
        for (int i = 0; i < childList.size(); i++) {
            for (String key : listMap.keySet()) {
                if (childList.get(i).getEndtime().contains(key)) {
                    List<JlChildEntity> list = listMap.get(key);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(childList.get(i));
                    listMap.put(key, list);
                }
            }
        }
        listMap = sortMapByKey(listMap);
        for (String key : listMap.keySet()) {
            JlGroupEntity entity = new JlGroupEntity();
            entity.setTime(key);
            entity.setList(listMap.get(key));
            gameList.add(entity);
        }
        return gameList;
    }

    private String getTeamName(String name) {
        String result = name;
        for (int i = 0; i < teamsCountry.length; i++) {
            if (name.contains(teamsCountry[i])) {
                result = name.replace(teamsCountry[i], "");
                break;
            }
        }
        return result;
    }
}
