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
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.adapter.JlAdapterUtil;
import com.shrxc.sc.app.adapter.JzAdapterUtil;
import com.shrxc.sc.app.bean.ChildOrder;
import com.shrxc.sc.app.bean.ContOrder;
import com.shrxc.sc.app.bean.JlChildEntity;
import com.shrxc.sc.app.bean.JlGroupEntity;
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

public class JlSelectTeamActivity extends AppCompatActivity {

    private Context context = JlSelectTeamActivity.this;
    @BindView(R.id.jl_select_activity_jl_select_list)
    ListView orderListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jl_select_team);
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
        List<JlChildEntity> selectOrderMap = (List<JlChildEntity>) bundle.getSerializable("selected");
        JlGroupEntity entity = new JlGroupEntity();
        entity.setGgfs(ggfs);
        entity.setMcn(mcn);
        entity.setBs(bs);
        entity.setList(selectOrderMap);
        if (JclqActivity.orderList.size() > 0) {
            for (int i = 0; i < JclqActivity.orderList.size(); i++) {
                if (JclqActivity.orderList.get(i).getGgfs() == ggfs) {
                    JclqActivity.orderList.remove(i);
                }
            }
        }
        JclqActivity.orderList.add(entity);
        orderListView.setAdapter(new ListAdapter());
    }

    @OnClick(R.id.jl_select_activity_qxd_but)
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.jl_select_activity_qxd_but:
                updataOrder();
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
        upOrderEntity.setTotalMoney(2 * JclqActivity.orderList.size() + "");
        List<ChildOrder> childList = new ArrayList<>();

        for (int k = 0; k < JclqActivity.orderList.size(); k++) {
            List<JlChildEntity> selectOrder = JclqActivity.orderList.get(k).getList();
            ChildOrder childOrder = new ChildOrder();
            childOrder.setSheets("1");

            int zs = 0;
            String mcn = JclqActivity.orderList.get(k).getMcn();
            String bs = JclqActivity.orderList.get(k).getBs();
            if (mcn.equals("")) {
                mcn = selectOrder.size() + ",";
            }
            String[] mcns = mcn.substring(0, mcn.length() - 1).split(",");
            int selects[] = new int[selectOrder.size()];
            for (int i = 0; i < selectOrder.size(); i++) {
                selects[i] = selectOrder.get(i).getSelected().size();
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
            int ggfs = JclqActivity.orderList.get(k).getGgfs();
            if (ggfs == 0) {
                childOrder.setPlayType("41");
            } else if (ggfs == 1) {
                childOrder.setPlayType("42");
            } else if (ggfs == 2) {
                childOrder.setPlayType("43");
            } else if (ggfs == 3) {
                childOrder.setPlayType("44");
            } else if (ggfs == 4) {
                childOrder.setPlayType("45");
            } else if (ggfs == 5) {
                childOrder.setPlayType("46");
            }
            childOrder.setRemark("");
            List<ContOrder> contList = new ArrayList<>();
            for (int i = 0; i < selectOrder.size(); i++) {
                ContOrder contOrder = new ContOrder();
                JlChildEntity entity = selectOrder.get(i);
                contOrder.setGameid(entity.getGameid());
                contOrder.setGuestteam(entity.getGuestteam());
                contOrder.setHometeam(entity.getHometeam());
                contOrder.setLeague(entity.getLeague());
                contOrder.setPlanid(entity.getPlanid());
                contOrder.setResult("");
                contOrder.setType("41");
                List<SelectOrder> selectList = new ArrayList<>();
                List<Integer> selcets = entity.getSelected();
                for (int j = 0; j < selcets.size(); j++) {
                    SelectOrder order = JlAdapterUtil.getUpOrderType(selcets.get(j), ggfs, entity);
                    selectList.add(order);
                }
                contOrder.setJson(selectList);
                contList.add(contOrder);
            }
            childOrder.setBuyNumJson(contList);
            childList.add(childOrder);
        }
        upOrderEntity.setBasketBallBuyNumJson(childList);
        final String buynum = JSON.toJSONString(upOrderEntity);

        try {
            Map<String, String> params = new HashMap<>();
            params.put("buynum", DesUtil.EncryptAsDoNet(buynum, DesUtil.DesKey).trim());
            params.put("wifiId", DesUtil.EncryptAsDoNet("123456", DesUtil.DesKey).trim());
            HttpRequestUtil.getInstance(context).post("BasketBall/BuyBT", params, new RequestCallback() {

                @Override
                public void onSuccess(JSONObject result, String state, String msg, String data) {
                    super.onSuccess(result, state, msg, data);
                    if (state.equals("1")) {
                        Intent intent = new Intent(context, TxddActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("order", upOrderEntity);
                        intent.putExtras(bundle);
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
            return JclqActivity.orderList.size();
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
                view = LayoutInflater.from(context).inflate(R.layout.jl_select_team_activity_list_adapter_layout, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            JlChildEntity entity = JclqActivity.orderList.get(position).getList().get(0);
            String name = "单场  " + entity.getGameid() + "  " + entity.getGuestteam() + "VS" + entity.getHometeam();
            holder.nameTextView.setText(name);
            holder.typeTextView.setText("体彩·竞彩-" + JlAdapterUtil.getGgfs(JclqActivity.orderList.get(position).getGgfs()));
            String select = "";
            for (int i = 0; i < entity.getSelected().size(); i++) {
                String selectType[] = JlAdapterUtil.getSelectType(entity.getSelected().get(i), JclqActivity.orderList.get(position).getGgfs(), entity);
                select += (selectType[0] + selectType[1] + "  ");
            }
            holder.selectTextview.setText(select);
            return view;
        }

        private class ViewHolder {

            private TextView nameTextView, selectTextview, typeTextView;

            public ViewHolder(View view) {
                nameTextView = view.findViewById(R.id.jl_select_team_activity_list_adapter_name_text);
                selectTextview = view.findViewById(R.id.jl_select_team_activity_list_adapter_select_text);
                typeTextView = view.findViewById(R.id.jl_select_team_activity_list_adapter_type_text);
            }
        }
    }
}
