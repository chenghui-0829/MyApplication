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
import com.shrxc.sc.app.adapter.JzAdapterUtil;
import com.shrxc.sc.app.adapter.JzBfElvAdapter;
import com.shrxc.sc.app.adapter.JzBqcElvAdapter;
import com.shrxc.sc.app.adapter.JzDgtzElvAdapter;
import com.shrxc.sc.app.adapter.JzHhggElvAdapter;
import com.shrxc.sc.app.adapter.JzJqsElvAdapter;
import com.shrxc.sc.app.adapter.JzRqspfElvAdapter;
import com.shrxc.sc.app.adapter.JzSpfElvAdapter;
import com.shrxc.sc.app.bean.JzChildGameEntity;
import com.shrxc.sc.app.bean.JzGroupGameEntity;
import com.shrxc.sc.app.bean.JzTzTypeEntity;
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

/**
 * 竞彩足球
 */
public class JczqActivity extends AppCompatActivity {

    private Context context = JczqActivity.this;
    @BindView(R.id.jz_activity_select_type_text)
    TextView selectTextView;
    @BindView(R.id.jz_activity_title_layout)
    RelativeLayout titleLayout;
    @BindViews({R.id.jz_activity_spf_elv, R.id.jz_activity_rqspf_elv, R.id.jz_activity_hhgg_elv,
            R.id.jz_activity_bf_elv, R.id.jz_activity_jqs_elv, R.id.jz_activity_bqc_elv, R.id.jz_activity_dgtz_elv})
    List<ExpandableListView> elvList;
    @BindView(R.id.jz_activity_tm_view)
    View tmView;
    @BindView(R.id.jz_activity_sure_text)
    TextView sureTextView;
    @BindView(R.id.jz_activity_select_edit)
    EditText bsEditText;
    @BindViews({R.id.jz_activity_select_ggfs_dg_text, R.id.jz_activity_select_ggfs_2_1_text, R.id.jz_activity_select_ggfs_3_1_text,
            R.id.jz_activity_select_ggfs_4_1_text, R.id.jz_activity_select_ggfs_5_1_text, R.id.jz_activity_select_ggfs_6_1_text,
            R.id.jz_activity_select_ggfs_7_1_text, R.id.jz_activity_select_ggfs_8_1_text})
    List<TextView> ggfsList;
    public static LinearLayout ggfsLayout;
    public static TextView ggfsTextView;
    public static TextView selectNumTextView;
    private List<TextView> textList;
    private int select = 0, bsNum = 0, isShowGgfs = 1;
    private List<JzGroupGameEntity> gameList;
    private JzSpfElvAdapter spfElvAdapter;
    private JzRqspfElvAdapter rqspfElvAdapter;
    private JzHhggElvAdapter hhggElvAdapter;
    private JzBfElvAdapter bfElvAdapter;
    private JzJqsElvAdapter jqsElvAdapter;
    private JzBqcElvAdapter bqcElvAdapter;
    private JzDgtzElvAdapter dgtzElvAdapter;
    private String spfUrl = "TicketData/GetFTSPFList", rqspfUrl = "TicketData/GetFTRSPFList", hhggUrl = "TicketData/GetFTList",
            bfUrl = "TicketData/GetFTBFList", jqsUrl = "TicketData/GetFTJQList", bqcUrl = "TicketData/GetFBQCList", dgUrl = "TicketData/GetFTDGList";
    private List<JzChildGameEntity> selectList;
    private List<String> ssTypeList;
    public static Map<Integer, Integer> ggfsMap, stateMap;
    private String ggfs = "";
    public static List<JzGroupGameEntity> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jczq);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        selectNumTextView = findViewById(R.id.jz_activity_select_num_text);
        ggfsTextView = findViewById(R.id.jz_activity_select_ggfs_text);
        ggfsLayout = findViewById(R.id.jz_activity_select_ggfs_layout);
        orderList = new ArrayList<>();
        initEvent();
        initGgfs();
        initSpfData();
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

    private void changeGgfsState(int index) {

        if (ggfsMap.get(index) == 0) {
            ggfsMap.put(index, 1);
            ggfsList.get(index).setBackgroundResource(R.drawable.red_circle_stroke_bg);
        } else {
            ggfsMap.put(index, 0);
            ggfsList.get(index).setBackgroundResource(R.drawable.app_gray_stroke_circle_color);
        }
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

    private void showTzTypeElv(int type) {

        stateMap = new HashMap<>();
        for (int i = 0; i < ssTypeList.size(); i++) {
            stateMap.put(i, 1);
        }
        isShowGgfs = 0;
        if (type != select) {
            initGgfs();
            changeGgfsLayout();
        } else {
            changeGgfsLayout();
        }
        bsEditText.setText(bsNum + "");
        selectNumTextView.setText("请至少选择2场比赛");
        selectTextView.setText(JzAdapterUtil.getGgfs(type));
        select = type;
        for (int i = 0; i < elvList.size(); i++) {
            if (i == type && elvList.get(i).getCount() != 0) {
                elvList.get(i).setVisibility(View.VISIBLE);
                elvList.get(i).expandGroup(0);
            } else {
                elvList.get(i).setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x888) {
            List<JzGroupGameEntity> sxList = new ArrayList<>();
            Bundle bundle = data.getExtras();
            List<String> selectList = (List<String>) bundle.getSerializable("select");
            for (int i = 0; i < gameList.size(); i++) {
                JzGroupGameEntity gameEntity = new JzGroupGameEntity();
                gameEntity.setTime(gameList.get(i).getTime());
                gameEntity.setGameNum(gameList.get(i).getGameNum());
                List<JzChildGameEntity> childList = new ArrayList<>();
                for (int j = 0; j < gameList.get(i).getGames().size(); j++) {
                    if (selectList.contains(gameList.get(i).getGames().get(j).getLeague())) {
                        childList.add(gameList.get(i).getGames().get(j));
                    }
                }
                gameEntity.setGames(childList);
                sxList.add(gameEntity);
            }
            System.out.println("--------" + sxList.get(0).getGames().size() + "-----------" + gameList.get(0).getGames().size());

            for (int i = 0; i < sxList.size(); i++) {
                if (sxList.get(i).getGames().size() == 0) {
                    sxList.remove(i);
                }
            }

            switch (select) {
                case 0:
                    spfElvAdapter = new JzSpfElvAdapter(context, sxList);
                    elvList.get(select).setAdapter(spfElvAdapter);
                    elvList.get(select).expandGroup(0);
                    break;
                case 1:
                    rqspfElvAdapter = new JzRqspfElvAdapter(context, sxList);
                    elvList.get(select).setAdapter(rqspfElvAdapter);
                    elvList.get(select).expandGroup(0);
                    break;
                case 2:
                    hhggElvAdapter = new JzHhggElvAdapter(context, sxList);
                    elvList.get(select).setAdapter(hhggElvAdapter);
                    elvList.get(select).expandGroup(0);
                    break;
                case 3:
                    bfElvAdapter = new JzBfElvAdapter(context, sxList);
                    elvList.get(select).setAdapter(bfElvAdapter);
                    elvList.get(select).expandGroup(0);
                    break;
                case 4:
                    jqsElvAdapter = new JzJqsElvAdapter(context, sxList);
                    elvList.get(select).setAdapter(jqsElvAdapter);
                    elvList.get(select).expandGroup(0);
                    break;
                case 5:
                    bqcElvAdapter = new JzBqcElvAdapter(context, sxList);
                    elvList.get(select).setAdapter(bqcElvAdapter);
                    elvList.get(select).expandGroup(0);
                    break;
                case 6:
                    dgtzElvAdapter = new JzDgtzElvAdapter(context, sxList);
                    elvList.get(select).setAdapter(dgtzElvAdapter);
                    elvList.get(select).expandGroup(0);
                    break;
            }
        }
    }

    @OnClick({R.id.jz_activity_select_type_text, R.id.jz_activity_sx_icon, R.id.jz_activity_sure_text, R.id.jz_activity_more_icon,
            R.id.jz_activity_select_add_icon, R.id.jz_activity_select_sub_icon, R.id.jz_activity_select_ggfs_text})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jz_activity_select_type_text:
                showTzTypeDialog();
                break;
            case R.id.jz_activity_sx_icon:
                Intent intent = new Intent(context, JzsxActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("sstype", (Serializable) ssTypeList);
                intent.putExtras(bundle);
                startActivityForResult(intent, 200);
                break;
            case R.id.jz_activity_sure_text://选好了
                selectButEvent();
                break;
            case R.id.jz_activity_more_icon:
//                startActivity(new Intent(context, JzSsfxActivity.class));
                break;
            case R.id.jz_activity_select_add_icon:
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
            case R.id.jz_activity_select_sub_icon:
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
            case R.id.jz_activity_select_ggfs_text:
                changeGgfsLayout();
                break;

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

    private void selectButEvent() {
        switch (select) {
            case 0:
                selectList = spfElvAdapter.getSelectList();
                break;
            case 1:
                selectList = rqspfElvAdapter.getSelectList();
                break;
            case 2:
                selectList = hhggElvAdapter.getSelectList();
                break;
            case 3:
                selectList = bfElvAdapter.getSelectList();
                break;
            case 4:
                selectList = jqsElvAdapter.getSelectList();
                break;
            case 5:
                selectList = bqcElvAdapter.getSelectList();
                break;
            case 6:
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

        Intent intent = new Intent(context, OrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected", (Serializable) selectList);
        intent.putExtras(bundle);
        intent.putExtra("ggfs", select);
        intent.putExtra("mcn", ggfs);
        intent.putExtra("bs", bsEditText.getText().toString());
        startActivity(intent);
    }

    private void showTzTypeDialog() {

        View view = LayoutInflater.from(context).inflate(R.layout.jc_zqtz_type_window_layout, null);
        TextView spfTextView = view.findViewById(R.id.jc_zqtz_type_dialog_spf_text);
        TextView rqspfTextView = view.findViewById(R.id.jc_zqtz_type_dialog_rqspf_text);
        TextView hhggTextView = view.findViewById(R.id.jc_zqtz_type_dialog_hhgg_text);
        TextView bfTextView = view.findViewById(R.id.jc_zqtz_type_dialog_bf_text);
        TextView jqsTextView = view.findViewById(R.id.jc_zqtz_type_dialog_jqs_text);
        TextView bqcTextView = view.findViewById(R.id.jc_zqtz_type_dialog_bqc_text);
        TextView dgtzTextView = view.findViewById(R.id.jc_zqtz_type_dialog_dgtz_text);

        textList = new ArrayList<>();
        textList.add(spfTextView);
        textList.add(rqspfTextView);
        textList.add(hhggTextView);
        textList.add(bfTextView);
        textList.add(jqsTextView);
        textList.add(bqcTextView);
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
         * 胜平负
         */
        spfTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                if (select == 0) {
                    return;
                }
                initSpfData();
            }
        });

        /**
         * 让球胜平负
         */
        rqspfTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                if (select == 1) {
                    return;
                }
                initRqspfData();
            }
        });

        /**
         * 混合过关
         */
        hhggTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                if (select == 2) {
                    return;
                }
                initHhggData();
            }
        });

        /**
         * 比分
         */
        bfTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                if (select == 3) {
                    return;
                }
                initBfData();
            }
        });

        /**
         * 进球数
         */
        jqsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                if (select == 4) {
                    return;
                }
                initJqsData();
            }
        });

        /**
         * 半全场
         */
        bqcTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                if (select == 5) {
                    return;
                }
                initBqcData();
            }
        });

        /**
         * 单关投注
         */
        dgtzTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                if (select == 6) {
                    return;
                }
                initDgData();
            }
        });
    }

    /**
     * 单关
     */
    private void initDgData() {

        HttpRequestUtil.getInstance(context).getByNoParams(dgUrl, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);

                if (state.equals("1")) {
                    if ("".equals(data)) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(data);
                    JSONArray gameArray = object.getJSONArray("list");
                    if (gameArray.size() == 0) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<JzChildGameEntity> childEntities = new ArrayList<>();
                    for (int i = 0; i < gameArray.size(); i++) {
                        JzChildGameEntity childEntity = new JzChildGameEntity();
                        JSONObject entityObject = gameArray.getJSONObject(i);
                        childEntity.setPlanid(entityObject.getString("planid"));
                        childEntity.setGameid(entityObject.getString("gameid"));
                        childEntity.setLeague(entityObject.getString("league"));
                        childEntity.setStarttime(entityObject.getString("starttime"));
                        childEntity.setUrlid(entityObject.getString("urlid"));
                        childEntity.setEndtime(entityObject.getString("endtime"));
                        childEntity.setHometeam(entityObject.getString("hometeam"));
                        childEntity.setGuestteam(entityObject.getString("guestteam"));
                        childEntity.setSpl(entityObject.getString("spl"));
                        childEntity.setPpl(entityObject.getString("ppl"));
                        childEntity.setFpl(entityObject.getString("fpl"));
//                            childEntity.setRspl(entityObject.getString("rspl"));
//                            childEntity.setRppl(entityObject.getString("rppl"));
//                            childEntity.setRfpl(entityObject.getString("rfpl"));
                        childEntity.setRangqiu(entityObject.getString("rangqiu"));
                        childEntities.add(childEntity);
                    }
                    ssTypeList = new ArrayList<>();
                    JSONArray types = object.getJSONArray("leas");
                    for (int i = 0; i < types.size(); i++) {
                        ssTypeList.add(types.getString(i));
                    }
                    Map<String, List<JzChildGameEntity>> listMap = new HashMap<>();
                    JSONArray times = object.getJSONArray("times");
                    for (int i = 0; i < times.size(); i++) {
                        listMap.put(times.getString(i), null);
                    }
                    gameList = getGameList(listMap, childEntities);
                    dgtzElvAdapter = new JzDgtzElvAdapter(context, gameList);
                    elvList.get(6).setAdapter(dgtzElvAdapter);
                    showTzTypeElv(6);
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    /**
     * 半全场
     */
    private void initBqcData() {
        HttpRequestUtil.getInstance(context).getByNoParams(bqcUrl, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);

                if (state.equals("1")) {
                    if ("".equals(data)) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(data);
                    JSONArray gameArray = object.getJSONArray("list");
                    List<JzChildGameEntity> childEntities = new ArrayList<>();
                    for (int i = 0; i < gameArray.size(); i++) {
                        JzChildGameEntity childEntity = new JzChildGameEntity();
                        JSONObject entityObject = gameArray.getJSONObject(i);
                        childEntity.setPlanid(entityObject.getString("planid"));
                        childEntity.setGameid(entityObject.getString("gameid"));
                        childEntity.setLeague(entityObject.getString("league"));
                        childEntity.setUrlid(entityObject.getString("urlid"));
                        childEntity.setStarttime(entityObject.getString("starttime"));
                        childEntity.setEndtime(entityObject.getString("endtime"));
                        childEntity.setHometeam(entityObject.getString("hometeam"));
                        childEntity.setGuestteam(entityObject.getString("guestteam"));
                        JzTzTypeEntity entity = new JzTzTypeEntity();
                        entity.setBqc_ss(entityObject.getString("hh"));
                        entity.setBqc_sp(entityObject.getString("hd"));
                        entity.setBqc_sf(entityObject.getString("ha"));
                        entity.setBqc_ps(entityObject.getString("dh"));
                        entity.setBqc_pp(entityObject.getString("dd"));
                        entity.setBqc_pf(entityObject.getString("da"));
                        entity.setBqc_fs(entityObject.getString("ah"));
                        entity.setBqc_fp(entityObject.getString("ad"));
                        entity.setBqc_ff(entityObject.getString("aa"));
                        childEntity.setTzTypeEntity(entity);
                        childEntities.add(childEntity);
                    }
                    ssTypeList = new ArrayList<>();
                    JSONArray types = object.getJSONArray("leas");
                    for (int i = 0; i < types.size(); i++) {
                        ssTypeList.add(types.getString(i));
                    }
                    Map<String, List<JzChildGameEntity>> listMap = new HashMap<>();
                    JSONArray times = object.getJSONArray("times");
                    for (int i = 0; i < times.size(); i++) {
                        listMap.put(times.getString(i), null);
                    }
                    gameList = getGameList(listMap, childEntities);
                    bqcElvAdapter = new JzBqcElvAdapter(context, gameList);
                    elvList.get(5).setAdapter(bqcElvAdapter);
                    showTzTypeElv(5);
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }

    /**
     * 进球数
     */
    private void initJqsData() {
        HttpRequestUtil.getInstance(context).getByNoParams(jqsUrl, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);

                if (state.equals("1")) {

                    if ("".equals(data)) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject object = JSONObject.parseObject(data);
                    JSONArray gameArray = object.getJSONArray("list");
                    List<JzChildGameEntity> childEntities = new ArrayList<>();
                    for (int i = 0; i < gameArray.size(); i++) {
                        JzChildGameEntity childEntity = new JzChildGameEntity();
                        JSONObject entityObject = gameArray.getJSONObject(i);
                        childEntity.setPlanid(entityObject.getString("planid"));
                        childEntity.setUrlid(entityObject.getString("urlid"));
                        childEntity.setGameid(entityObject.getString("gameid"));
                        childEntity.setStarttime(entityObject.getString("starttime"));
                        childEntity.setLeague(entityObject.getString("league"));
                        childEntity.setEndtime(entityObject.getString("endtime"));
                        childEntity.setHometeam(entityObject.getString("hometeam"));
                        childEntity.setGuestteam(entityObject.getString("guestteam"));
                        JzTzTypeEntity entity = new JzTzTypeEntity();
                        entity.setJqs_0(entityObject.getString("s0"));
                        entity.setJqs_1(entityObject.getString("s1"));
                        entity.setJqs_2(entityObject.getString("s2"));
                        entity.setJqs_3(entityObject.getString("s3"));
                        entity.setJqs_4(entityObject.getString("s4"));
                        entity.setJqs_5(entityObject.getString("s5"));
                        entity.setJqs_6(entityObject.getString("s6"));
                        entity.setJqs_7(entityObject.getString("s7"));
                        childEntity.setTzTypeEntity(entity);
                        childEntities.add(childEntity);
                    }
                    ssTypeList = new ArrayList<>();
                    JSONArray types = object.getJSONArray("leas");
                    for (int i = 0; i < types.size(); i++) {
                        ssTypeList.add(types.getString(i));
                    }
                    Map<String, List<JzChildGameEntity>> listMap = new HashMap<>();
                    JSONArray times = object.getJSONArray("times");
                    for (int i = 0; i < times.size(); i++) {
                        listMap.put(times.getString(i), null);
                    }
                    gameList = getGameList(listMap, childEntities);
                    jqsElvAdapter = new JzJqsElvAdapter(context, gameList);
                    elvList.get(4).setAdapter(jqsElvAdapter);
                    showTzTypeElv(4);
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    /**
     * 比分
     */
    private void initBfData() {
        HttpRequestUtil.getInstance(context).getByNoParams(bfUrl, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);

                if (state.equals("1")) {

                    if ("".equals(data)) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject object = JSONObject.parseObject(data);
                    JSONArray gameArray = object.getJSONArray("list");
                    List<JzChildGameEntity> childEntities = new ArrayList<>();
                    for (int i = 0; i < gameArray.size(); i++) {
                        JzChildGameEntity childEntity = new JzChildGameEntity();
                        JSONObject entityObject = gameArray.getJSONObject(i);
                        childEntity.setPlanid(entityObject.getString("planid"));
                        childEntity.setUrlid(entityObject.getString("urlid"));
                        childEntity.setGameid(entityObject.getString("gameid"));
                        childEntity.setStarttime(entityObject.getString("starttime"));
                        childEntity.setLeague(entityObject.getString("league"));
                        childEntity.setEndtime(entityObject.getString("endtime"));
                        childEntity.setHometeam(entityObject.getString("hometeam"));
                        childEntity.setGuestteam(entityObject.getString("guestteam"));
                        JzTzTypeEntity entity = new JzTzTypeEntity();
                        entity.setBf_fqt(entityObject.getString("s_1_a"));
                        entity.setBf_pqt(entityObject.getString("s_1_d"));
                        entity.setBf_sqt(entityObject.getString("s_1_h"));
                        entity.setBf_0_0(entityObject.getString("s0000"));
                        entity.setBf_0_1(entityObject.getString("s0001"));
                        entity.setBf_0_2(entityObject.getString("s0002"));
                        entity.setBf_0_3(entityObject.getString("s0003"));
                        entity.setBf_0_4(entityObject.getString("s0004"));
                        entity.setBf_0_5(entityObject.getString("s0005"));
                        entity.setBf_1_0(entityObject.getString("s0100"));
                        entity.setBf_1_1(entityObject.getString("s0101"));
                        entity.setBf_1_2(entityObject.getString("s0102"));
                        entity.setBf_1_3(entityObject.getString("s0103"));
                        entity.setBf_1_4(entityObject.getString("s0104"));
                        entity.setBf_1_5(entityObject.getString("s0105"));
                        entity.setBf_2_0(entityObject.getString("s0200"));
                        entity.setBf_2_1(entityObject.getString("s0201"));
                        entity.setBf_2_2(entityObject.getString("s0202"));
                        entity.setBf_2_3(entityObject.getString("s0203"));
                        entity.setBf_2_4(entityObject.getString("s0204"));
                        entity.setBf_2_5(entityObject.getString("s0205"));
                        entity.setBf_3_0(entityObject.getString("s0300"));
                        entity.setBf_3_1(entityObject.getString("s0301"));
                        entity.setBf_3_2(entityObject.getString("s0302"));
                        entity.setBf_3_3(entityObject.getString("s0303"));
                        entity.setBf_4_0(entityObject.getString("s0400"));
                        entity.setBf_4_1(entityObject.getString("s0401"));
                        entity.setBf_4_2(entityObject.getString("s0402"));
                        entity.setBf_5_0(entityObject.getString("s0500"));
                        entity.setBf_5_1(entityObject.getString("s0501"));
                        entity.setBf_5_2(entityObject.getString("s0502"));
                        childEntity.setTzTypeEntity(entity);
                        childEntities.add(childEntity);
                    }

                    ssTypeList = new ArrayList<>();
                    JSONArray types = object.getJSONArray("leas");
                    for (int i = 0; i < types.size(); i++) {
                        ssTypeList.add(types.getString(i));
                    }

                    Map<String, List<JzChildGameEntity>> listMap = new HashMap<>();
                    JSONArray times = object.getJSONArray("times");
                    for (int i = 0; i < times.size(); i++) {
                        listMap.put(times.getString(i), null);
                    }
                    gameList = getGameList(listMap, childEntities);
                    bfElvAdapter = new JzBfElvAdapter(context, gameList);
                    elvList.get(3).setAdapter(bfElvAdapter);
                    showTzTypeElv(3);
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
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
    private void initHhggData() {
        HttpRequestUtil.getInstance(context).getByNoParams(hhggUrl, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);

                if (state.equals("1")) {
                    if ("".equals(data)) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(data);
                    JSONArray gameArray = object.getJSONArray("list");
                    if (gameArray.size() == 0) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<JzChildGameEntity> childEntities = new ArrayList<>();
                    for (int i = 0; i < gameArray.size(); i++) {
                        JzChildGameEntity childEntity = new JzChildGameEntity();
                        JSONObject entityObject = gameArray.getJSONObject(i);
                        childEntity.setPlanid(entityObject.getString("planid"));
                        childEntity.setGameid(entityObject.getString("gameid"));
                        childEntity.setLeague(entityObject.getString("league"));
                        childEntity.setStarttime(entityObject.getString("starttime"));
                        childEntity.setEndtime(entityObject.getString("endtime"));
                        childEntity.setHometeam(entityObject.getString("hometeam"));
                        childEntity.setGuestteam(entityObject.getString("guestteam"));
                        childEntity.setUrlid(entityObject.getString("urlid"));
                        childEntity.setSpl(entityObject.getString("spl"));
                        childEntity.setPpl(entityObject.getString("ppl"));
                        childEntity.setFpl(entityObject.getString("fpl"));
                        childEntity.setRspl(entityObject.getString("rspl"));
                        childEntity.setRppl(entityObject.getString("rppl"));
                        childEntity.setRfpl(entityObject.getString("rfpl"));
                        childEntity.setRangqiu(entityObject.getString("rangqiu"));
                        childEntities.add(childEntity);
                    }
                    ssTypeList = new ArrayList<>();
                    JSONArray types = object.getJSONArray("leas");
                    for (int i = 0; i < types.size(); i++) {
                        ssTypeList.add(types.getString(i));
                    }
                    Map<String, List<JzChildGameEntity>> listMap = new HashMap<>();
                    JSONArray times = object.getJSONArray("times");
                    for (int i = 0; i < times.size(); i++) {
                        listMap.put(times.getString(i), null);
                    }
                    gameList = getGameList(listMap, childEntities);
                    hhggElvAdapter = new JzHhggElvAdapter(context, gameList);
                    elvList.get(2).setAdapter(hhggElvAdapter);
                    showTzTypeElv(2);
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    /***
     * 让球胜平负
     */
    private void initRqspfData() {

        HttpRequestUtil.getInstance(context).getByNoParams(rqspfUrl, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);

                if (state.equals("1")) {
                    if ("".equals(data)) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(data);
                    JSONArray gameArray = object.getJSONArray("list");
                    List<JzChildGameEntity> childEntities = new ArrayList<>();
                    for (int i = 0; i < gameArray.size(); i++) {
                        JzChildGameEntity childEntity = new JzChildGameEntity();
                        JSONObject entityObject = gameArray.getJSONObject(i);
                        childEntity.setPlanid(entityObject.getString("planid"));
                        childEntity.setGameid(entityObject.getString("gameid"));
                        childEntity.setLeague(entityObject.getString("league"));
                        childEntity.setStarttime(entityObject.getString("starttime"));
                        childEntity.setEndtime(entityObject.getString("endtime"));
                        childEntity.setHometeam(entityObject.getString("hometeam"));
                        childEntity.setUrlid(entityObject.getString("urlid"));
                        childEntity.setGuestteam(entityObject.getString("guestteam"));
                        childEntity.setRspl(entityObject.getString("rspl"));
                        childEntity.setRppl(entityObject.getString("rppl"));
                        childEntity.setRfpl(entityObject.getString("rfpl"));
                        childEntity.setRangqiu(entityObject.getString("rangqiu"));
                        childEntities.add(childEntity);
                    }
                    ssTypeList = new ArrayList<>();
                    JSONArray types = object.getJSONArray("leas");
                    for (int i = 0; i < types.size(); i++) {
                        ssTypeList.add(types.getString(i));
                    }

                    Map<String, List<JzChildGameEntity>> listMap = new HashMap<>();
                    JSONArray times = object.getJSONArray("times");
                    for (int i = 0; i < times.size(); i++) {
                        listMap.put(times.getString(i), null);
                    }
                    gameList = getGameList(listMap, childEntities);
                    rqspfElvAdapter = new JzRqspfElvAdapter(context, gameList);
                    elvList.get(1).setAdapter(rqspfElvAdapter);
                    showTzTypeElv(1);
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }

    /**
     * 胜平负
     */
    private void initSpfData() {

        HttpRequestUtil.getInstance(context).getByNoParams(spfUrl, new RequestCallback() {

            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if (state.equals("1")) {
                    if ("".equals(data) || data == null) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject object = JSONObject.parseObject(data);
                    JSONArray gameArray = object.getJSONArray("list");
                    List<JzChildGameEntity> childEntities = new ArrayList<>();
                    if (gameArray.size() == 0) {
                        Toast.makeText(context, "暂无比赛", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < gameArray.size(); i++) {
                        JzChildGameEntity childEntity = new JzChildGameEntity();
                        JSONObject entityObject = gameArray.getJSONObject(i);
                        childEntity.setPlanid(entityObject.getString("planid"));
                        childEntity.setGameid(entityObject.getString("gameid"));
                        childEntity.setLeague(entityObject.getString("league"));
                        childEntity.setEndtime(entityObject.getString("endtime"));
                        childEntity.setStarttime(entityObject.getString("starttime"));
                        childEntity.setHometeam(entityObject.getString("hometeam"));
                        childEntity.setGuestteam(entityObject.getString("guestteam"));
                        childEntity.setUrlid(entityObject.getString("urlid"));
                        childEntity.setSpl(entityObject.getString("spl"));
                        childEntity.setPpl(entityObject.getString("ppl"));
                        childEntity.setFpl(entityObject.getString("fpl"));
                        childEntities.add(childEntity);
                    }

                    ssTypeList = new ArrayList<>();
                    JSONArray types = object.getJSONArray("leas");
                    for (int i = 0; i < types.size(); i++) {
                        ssTypeList.add(types.getString(i));
                    }
                    Map<String, List<JzChildGameEntity>> listMap = new HashMap<>();
                    JSONArray times = object.getJSONArray("times");
                    for (int i = 0; i < times.size(); i++) {
                        listMap.put(times.getString(i), null);
                    }
                    gameList = getGameList(listMap, childEntities);
                    spfElvAdapter = new JzSpfElvAdapter(context, gameList);
                    elvList.get(0).setAdapter(spfElvAdapter);
                    showTzTypeElv(0);
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
                System.out.println("====erro=====>" + erro);
            }
        });
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    private Map<String, List<JzChildGameEntity>> sortMapByKey(Map<String, List<JzChildGameEntity>> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, List<JzChildGameEntity>> sortMap = new TreeMap<>(
                new Comparator<String>() {
                    @Override
                    public int compare(String str1, String str2) {
                        return str1.compareTo(str2);
                    }
                });
        sortMap.putAll(map);
        return sortMap;
    }

    private List<JzGroupGameEntity> getGameList(Map<String, List<JzChildGameEntity>> listMap, List<JzChildGameEntity> childList) {

        List<JzGroupGameEntity> gameList = new ArrayList<>();
        for (int i = 0; i < childList.size(); i++) {
            for (String key : listMap.keySet()) {
                if (childList.get(i).getEndtime().contains(key)) {
                    List<JzChildGameEntity> list = listMap.get(key);
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
            JzGroupGameEntity entity = new JzGroupGameEntity();
            entity.setTime(key);
            entity.setGameNum(listMap.get(key).size());
            entity.setGames(listMap.get(key));
            gameList.add(entity);
        }
        return gameList;
    }
}
