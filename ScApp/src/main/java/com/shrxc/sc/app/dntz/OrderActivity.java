package com.shrxc.sc.app.dntz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.LoginActivity;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.adapter.JzAdapterUtil;
import com.shrxc.sc.app.bean.ChildOrder;
import com.shrxc.sc.app.bean.ContOrder;
import com.shrxc.sc.app.bean.JzChildGameEntity;
import com.shrxc.sc.app.bean.JzGroupGameEntity;
import com.shrxc.sc.app.bean.SelectOrder;
import com.shrxc.sc.app.bean.UpOrderEntity;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.DesUtil;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderActivity extends AppCompatActivity {

    private Context context = OrderActivity.this;
    @BindView(R.id.order_activity_order_list)
    ListView orderListView;

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
        int ggfs = intent.getIntExtra("ggfs", 0);
        String mcn = intent.getStringExtra("mcn");
        String bs = intent.getStringExtra("bs");
        List<JzChildGameEntity> selectOrderMap = (List<JzChildGameEntity>) bundle.getSerializable("selected");
        JzGroupGameEntity entity = new JzGroupGameEntity();
        entity.setGgfs(ggfs);
        entity.setMcn(mcn);
        entity.setBs(bs);
        entity.setGames(selectOrderMap);
        if (JczqActivity.orderList.size() > 0) {
            for (int i = 0; i < JczqActivity.orderList.size(); i++) {
                if (JczqActivity.orderList.get(i).getGgfs() == ggfs) {
                    JczqActivity.orderList.remove(i);
                }
            }
        }
        JczqActivity.orderList.add(entity);
        orderListView.setAdapter(new ListAdapter());
    }

    @OnClick(R.id.order_activity_qxd_but)
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.order_activity_qxd_but:
                updataOrder();
//                startActivity(new Intent(context, TxddActivity.class));
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
        upOrderEntity.setTotalMoney(2 * JczqActivity.orderList.size() + "");
        List<ChildOrder> childList = new ArrayList<>();

        for (int k = 0; k < JczqActivity.orderList.size(); k++) {
            List<JzChildGameEntity> selectOrder = JczqActivity.orderList.get(k).getGames();
            ChildOrder childOrder = new ChildOrder();
            childOrder.setSheets("1");

            int zs = 0;
            String mcn = JczqActivity.orderList.get(k).getMcn();
            String bs = JczqActivity.orderList.get(k).getBs();
            if (mcn.equals("")) {
                mcn = selectOrder.size() + ",";
            }
            String[] mcns = mcn.substring(0, mcn.length() - 1).split(",");
            int selects[] = new int[selectOrder.size()];
            for (int i = 0; i < selectOrder.size(); i++) {
                selects[i] = selectOrder.get(i).getSelectedList().size();
            }
            if (mcns.length == 1) {
                zs = JzAdapterUtil.getBetCountWithGamesCount(selectOrder.size(), selects, Integer.parseInt(mcns[0]));
            } else if (mcns.length > 1) {
                for (int i = 0; i < mcns.length; i++) {
                    zs += JzAdapterUtil.getBetCountWithGamesCount(selectOrder.size(), selects, Integer.parseInt(mcns[i]));
                }
            }

            if (mcn.equals("")) {
                childOrder.setMCN(selectOrder.size() + "");
            } else {
                childOrder.setMCN(mcn.substring(0, mcn.length() - 1));
            }

            childOrder.setTcount(zs + "");
            childOrder.setMultiple(bs.equals("") ? "1" : bs);
            childOrder.setTotalMoney(2 * zs + "");
            int ggfs = JczqActivity.orderList.get(k).getGgfs();
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
            for (int i = 0; i < selectOrder.size(); i++) {
                ContOrder contOrder = new ContOrder();
                JzChildGameEntity entity = selectOrder.get(i);
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
        }

        upOrderEntity.setFootBallBuyNumJson(childList);

        final String buynum = JSON.toJSONString(upOrderEntity);

        System.out.println("---------buynum--------" + buynum);

        try {
            Map<String, String> params = new HashMap<>();
            params.put("buynum", DesUtil.EncryptAsDoNet(buynum, DesUtil.DesKey).trim());
            params.put("wifiId", DesUtil.EncryptAsDoNet("123456", DesUtil.DesKey).trim());
            HttpRequestUtil.getInstance(context).post("FootBall/BuyFT", params, new RequestCallback() {

                @Override
                public void onSuccess(JSONObject result, String state, String msg, String data) {
                    super.onSuccess(result, state, msg, data);
                    if (state.equals("1")) {
                        Intent intent = new Intent(context, TxddActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("order", upOrderEntity);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if ("-1".equals(state)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onErro(String erro) {
                    super.onErro(erro);
                    System.out.println("-------erro------->" + erro);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return JczqActivity.orderList.size();
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

            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.order_activity_list_adapter_layout, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }


            JzChildGameEntity entity = JczqActivity.orderList.get(position).getGames().get(0);
            String name = "单场  " + entity.getGameid() + "  " + entity.getHometeam() + "VS" + entity.getGuestteam();
            holder.nameTextView.setText(name);
            holder.typeTextView.setText("体彩·竞彩-" + JzAdapterUtil.getGgfs(JczqActivity.orderList.get(position).getGgfs()));
            String select = "";
            for (int i = 0; i < entity.getSelectedList().size(); i++) {
                String selectType[] = JzAdapterUtil.getSelectType(entity.getSelectedList().get(i), JczqActivity.orderList.get(position).getGgfs(), entity);
                select += (selectType[0] + selectType[1] + "  ");
            }
            holder.selectTextview.setText(select);
            return view;
        }

        private class ViewHolder {

            private TextView nameTextView, selectTextview, typeTextView;

            public ViewHolder(View view) {

                nameTextView = view.findViewById(R.id.order_activity_list_adapter_name_text);
                selectTextview = view.findViewById(R.id.order_activity_list_adapter_select_text);
                typeTextView = view.findViewById(R.id.order_activity_list_adapter_type_text);
            }
        }
    }
}
