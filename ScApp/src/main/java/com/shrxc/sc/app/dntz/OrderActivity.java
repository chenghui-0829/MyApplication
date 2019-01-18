package com.shrxc.sc.app.dntz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.adapter.JzAdapterUtil;
import com.shrxc.sc.app.bean.ChildOrder;
import com.shrxc.sc.app.bean.ContOrder;
import com.shrxc.sc.app.bean.JzChildGameEntity;
import com.shrxc.sc.app.bean.SelectOrder;
import com.shrxc.sc.app.bean.UpOrderEntity;
import com.shrxc.sc.app.utils.DesUtil;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderActivity extends AppCompatActivity {

    private Context context = OrderActivity.this;
    @BindView(R.id.order_activity_order_list)
    ListView orderListView;
    @BindView(R.id.order_activity_total_money_text)
    TextView totalMoneyTextView;
    public static List<JzChildGameEntity> selectOrderMap;
    private int ggfs, zs, count = 1, bs;
    private String mcn, mcnString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ggfs = intent.getIntExtra("ggfs", 0);
        mcn = intent.getStringExtra("mcn");
        bs = Integer.parseInt(intent.getStringExtra("bs"));
        selectOrderMap = (List<JzChildGameEntity>) bundle.getSerializable("selected");


        Collections.sort(selectOrderMap, new Comparator<JzChildGameEntity>() {
            @Override
            public int compare(JzChildGameEntity o1, JzChildGameEntity o2) {

                int mark = 1;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date0 = sdf.parse(o1.getEndtime().replace("T", " "));
                    Date date1 = sdf.parse(o2.getEndtime().replace("T", " "));

                    if (date0.getTime() < date1.getTime()) {
                        mark = -1;
                    }

                } catch (ParseException e) {
                    System.out.println("-------sort--------" + e);
                    e.printStackTrace();
                }
                return mark;
            }
        });


        if (mcn.equals("")) {
            mcn = selectOrderMap.size() + ",";
        }

        String[] mcns = mcn.substring(0, mcn.length() - 1).split(",");
        int selects[] = new int[selectOrderMap.size()];
        for (int i = 0; i < selectOrderMap.size(); i++) {
            selects[i] = selectOrderMap.get(i).getSelectedList().size();
        }
        if (mcns.length == 1) {
            zs = JzAdapterUtil.getBetCountWithGamesCount(selectOrderMap.size(), selects, Integer.parseInt(mcns[0]));
        } else if (mcns.length > 1) {
            for (int i = 0; i < mcns.length; i++) {
                zs += JzAdapterUtil.getBetCountWithGamesCount(selectOrderMap.size(), selects, Integer.parseInt(mcns[i]));
            }
        }
        if (zs == 0) {
            zs = 1;
        }
        System.out.println("--------" + zs);
        totalMoneyTextView.setText("￥" + zs * 2);
        orderListView.setAdapter(new ListAdapter());
    }

    @OnClick({R.id.order_activity_qxd_but, R.id.order_activity_back_icon})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.order_activity_qxd_but:
                updataOrder();
//                startActivity(new Intent(context, TxddActivity.class));
                break;
            case R.id.order_activity_back_icon:
                finish();
                break;

        }
    }

    private void updataOrder() {

        final UpOrderEntity upOrderEntity = new UpOrderEntity();
        upOrderEntity.setRemark("");
        try {
            upOrderEntity.setWifiId(DesUtil.EncryptAsDoNet("123456", DesUtil.DesKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        upOrderEntity.setTotalMoney(totalMoneyTextView.getText().toString().replace("￥", ""));
        List<ChildOrder> childList = new ArrayList<>();
        ChildOrder childOrder = new ChildOrder();
        childOrder.setSheets(count + "");
        childOrder.setMCN(mcn.substring(0, mcn.length() - 1));

        childOrder.setTcount(zs + "");
        childOrder.setMultiple(bs + "");
        childOrder.setTotalMoney(totalMoneyTextView.getText().toString().replace("￥", ""));
        childOrder.setLastTime(selectOrderMap.get(selectOrderMap.size() - 1).getEndtime());

        if (ggfs == 0) {
            childOrder.setPlayType("6");
        } else if (ggfs == 1) {
            childOrder.setPlayType("40");
        } else if (ggfs == 2) {
            childOrder.setPlayType("10");
        } else if (ggfs == 3) {
            childOrder.setPlayType("7");
        } else if (ggfs == 4) {
            childOrder.setPlayType("8");
        } else if (ggfs == 5) {
            childOrder.setPlayType("9");
        } else if (ggfs == 6) {
            childOrder.setPlayType("39");
        }
        childOrder.setRemark("");
        List<ContOrder> contList = new ArrayList<>();
        for (int i = 0; i < selectOrderMap.size(); i++) {
            ContOrder contOrder = new ContOrder();
            JzChildGameEntity entity = selectOrderMap.get(i);
            contOrder.setGameid(entity.getGameid());
            contOrder.setGuestteam(entity.getGuestteam());
            contOrder.setHometeam(entity.getHometeam());
            contOrder.setLeague(entity.getLeague());
            contOrder.setPlanid(entity.getPlanid());
            contOrder.setResult("");
            contOrder.setType("6");
            List<SelectOrder> selectList = new ArrayList<>();
            List<Integer> selcets = entity.getSelectedList();
            for (int j = 0; j < selcets.size(); j++) {
                SelectOrder order = JzAdapterUtil.getUpOrderType(selcets.get(j), ggfs, entity);
                selectList.add(order);
            }
            contOrder.setJson(selectList);
            contList.add(contOrder);
        }
        childOrder.setBuyNumJson(contList);
        childList.add(childOrder);

        upOrderEntity.setFootBallBuyNumJson(childList);

        final String buynum = JSON.toJSONString(upOrderEntity);

        System.out.println("---------buynum--------" + buynum);

//        try {
//            Map<String, String> params = new HashMap<>();
//            params.put("buynum", DesUtil.EncryptAsDoNet(buynum, DesUtil.DesKey).trim());
//            params.put("wifiId", DesUtil.EncryptAsDoNet("123456", DesUtil.DesKey).trim());
//            HttpRequestUtil.getInstance(context).post("FootBall/BuyFT", params, new RequestCallback() {
//
//                @Override
//                public void onSuccess(JSONObject result, String state, String msg, String data) {
//                    super.onSuccess(result, state, msg, data);
//                    if (state.equals("1")) {
        Intent intent = new Intent(context, TxddActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", upOrderEntity);
        intent.putExtras(bundle);
        intent.putExtra("dj", "￥" + 2 * zs * bs);
        intent.putExtra("ggfs", ggfs);
        intent.putExtra("mcn", mcnString);
        startActivity(intent);
//                    } else if ("-1".equals(state)) {
//                        Intent intent = new Intent(context, LoginActivity.class);
//                        startActivity(intent);
//                    }
//                }
//
//                @Override
//                public void onErro(String erro) {
//                    super.onErro(erro);
//                    System.out.println("-------erro------->" + erro);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    private class ListAdapter extends BaseAdapter {

        private ViewHolder holder = null;

        @Override
        public int getCount() {
            return 1;
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
        public View getView(int position, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.order_activity_list_adapter_layout, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            JzChildGameEntity entity = selectOrderMap.get(0);
            final String name = "单场  " + entity.getGameid() + "  " + entity.getHometeam() + "VS" + entity.getGuestteam();
            holder.nameTextView.setText(name);
            holder.typeTextView.setText("体彩·竞彩-" + JzAdapterUtil.getGgfs(ggfs));
            String select = "";
            for (int i = 0; i < entity.getSelectedList().size(); i++) {
                String selectType[] = JzAdapterUtil.getSelectType(entity.getSelectedList().get(i), ggfs, entity);
                select += (selectType[0] + selectType[1] + "  ");
            }
            holder.selectTextview.setText(select);
            holder.bsTextView.setText(bs + "倍");
            String[] mcns = mcn.substring(0, mcn.length() - 1).split(",");
            if (mcns.length == 1) {
                if (mcns[0].equals("1")) {
                    holder.tzTextView.setText("单关");
                } else {
                    holder.tzTextView.setText(mcns[0] + "串1");
                }

            } else {
                String tzfs = "";
                for (int i = 0; i < mcns.length; i++) {
                    tzfs += mcns[i] + "串1  ";

                }
                holder.tzTextView.setText(tzfs);
            }

            mcnString = holder.tzTextView.getText().toString();
            holder.djTextView.setText("￥" + 2 * zs * bs);

            holder.subIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.numEdit.getText().toString().equals("")) {
                        return;
                    }
                    count = Integer.parseInt(holder.numEdit.getText().toString());
                    if (count > 1) {
                        count--;
                        holder.numEdit.setText(count + "");
                        totalMoneyTextView.setText("￥" + count * zs * 2 * bs);
                    }
                }
            });

            holder.addIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String num = holder.numEdit.getText().toString();
                    if (num.equals("") || num.equals("0")) {
                        count = 1;
                    } else {
                        count = Integer.parseInt(num);
                    }
                    if (count < 99) {
                        count++;
                        holder.numEdit.setText(count + "");
                        totalMoneyTextView.setText("￥" + count * zs * 2 * bs);
                    }
                }
            });

            holder.numEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    int num = Integer.parseInt(s.toString().equals("") ? "1" : s.toString());
                    if (num < 1) {
                        num = 1;
                        holder.numEdit.setText("1");
                    } else if (num > 99) {
                        holder.numEdit.setText("99");
                        num = 99;
                    }
                    totalMoneyTextView.setText("￥" + num * zs * 2);
                }
            });
            return view;
        }

        private class ViewHolder {

            private TextView nameTextView, selectTextview, typeTextView, bsTextView, djTextView, tzTextView;
            private ImageView subIcon, addIcon;
            private EditText numEdit;

            public ViewHolder(View view) {

                tzTextView = view.findViewById(R.id.order_activity_list_adapter_tz_text);
                bsTextView = view.findViewById(R.id.order_activity_list_adapter_bs_text);
                djTextView = view.findViewById(R.id.order_activity_list_adapter_dj_text);
                numEdit = view.findViewById(R.id.order_activity_list_adapter_edit);
                subIcon = view.findViewById(R.id.order_activity_list_adapter_sub_icon);
                addIcon = view.findViewById(R.id.order_activity_list_adapter_add_icon);
                nameTextView = view.findViewById(R.id.order_activity_list_adapter_name_text);
                selectTextview = view.findViewById(R.id.order_activity_list_adapter_select_text);
                typeTextView = view.findViewById(R.id.order_activity_list_adapter_type_text);
            }
        }
    }
}
