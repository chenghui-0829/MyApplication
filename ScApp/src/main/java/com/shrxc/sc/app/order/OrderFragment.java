package com.shrxc.sc.app.order;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.BaseFragment;
import com.shrxc.sc.app.LoginActivity;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.OrderEntity;
import com.shrxc.sc.app.dntz.WaitOrderActivity;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends BaseFragment {

    private Unbinder unbinder;
    @BindView(R.id.order_fragment_list)
    ListView orderListView;
    private List<OrderEntity> orderList;
    private ListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        initEvent();
        return view;
    }

    private void initEvent() {
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), WaitOrderActivity.class));
            }
        });
    }

    private void initData() {

        orderList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("pageStart", 1);
        params.put("pageSize", 10);

        HttpRequestUtil.getInstance(getContext()).get("OrderArrived/GetOrderList", params, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if ("1".equals(state)) {
                    JSONArray array = result.getJSONArray("Data");
                    for (int i = 0; i < array.size(); i++) {
                        OrderEntity entity = new OrderEntity();
                        JSONObject object = array.getJSONObject(i);
                        entity.setOAID(object.getString("OAID"));
                        entity.setMerchantName(object.getString("MerchantName"));
                        entity.setProTypeStr(object.getString("ProTypeStr"));
                        entity.setStatusStr(object.getString("StatusStr"));
                        entity.setTotalMoney(object.getString("TotalMoney"));
                        orderList.add(entity);
                    }
                    adapter = new ListAdapter();
                    orderListView.setAdapter(adapter);
                } else if ("-1".equals(state)) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
                System.out.println("----------erro---------" + erro);
            }
        });
    }

    @Override
    protected void loadData() {
        initData();
    }


    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return orderList == null ? 0 : orderList.size();
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
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.order_fragment_list_adapter_layout, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.nameTextView.setText(orderList.get(i).getProTypeStr() + "(" + orderList.get(i).getMerchantName() + ")");
            holder.stateTextView.setText(orderList.get(i).getStatusStr().replace("订单已提交", ""));
            holder.moneyTextView.setText("￥" + orderList.get(i).getTotalMoney());
            return view;
        }

        private class ViewHolder {

            private TextView nameTextView, stateTextView, moneyTextView;

            public ViewHolder(View view) {
                nameTextView = view.findViewById(R.id.order_fragment_list_order_name);
                stateTextView = view.findViewById(R.id.order_fragment_list_order_state);
                moneyTextView = view.findViewById(R.id.order_fragment_list_order_money);

            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
