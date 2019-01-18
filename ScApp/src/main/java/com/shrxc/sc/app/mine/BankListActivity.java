package com.shrxc.sc.app.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shrxc.sc.app.LoginActivity;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.BankListEntity;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.MyListView;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BankListActivity extends AppCompatActivity {

    private Context context = BankListActivity.this;
    @BindView(R.id.bank_list_activity_add_icon)
    ImageView addIcon;
    @BindView(R.id.bank_list_activity_list)
    MyListView listView;
    @BindView(R.id.bank_list_activity_hint_text)
    TextView hintText;
    private List<BankListEntity> list;
    private BankCardListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initEvent() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, BankDetailsActivity.class);
                intent.putExtra("bankinfo", list.get(position));
                startActivity(intent);
            }
        });

    }

    @OnClick(R.id.bank_list_activity_add_icon)
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bank_list_activity_add_icon:
                checkUserInfo();
//                Intent intent = new Intent(context, AddBankActivity.class);
//                startActivity(intent);
                break;
        }
    }

    private void checkUserInfo() {

        HttpRequestUtil.getInstance(context).getByNoParams("IdCard/GetIdcard", new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if ("1".equals(state)) {
                    if (data.equals("")) {
                        Intent intent = new Intent(context, NameAuthActivity.class);
                        startActivity(intent);
                    } else {
                        JSONObject object = result.getJSONObject("Data");
                        Intent intent = new Intent(context, AddBankActivity.class);
                        intent.putExtra("name", object.getString("RealName"));
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
            }
        });

    }

    private void initData() {

        list = new ArrayList<>();
        HttpRequestUtil.getInstance(context).getByNoParams("BankCard/GetBankCardList", new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if (state.equals("1")) {
                    JSONArray bankArray = result.getJSONArray("Data");
                    for (int i = 0; i < bankArray.size(); i++) {
                        BankListEntity entity = JSON.toJavaObject(bankArray.getJSONObject(i), BankListEntity.class);
                        list.add(entity);
                    }
                    adapter = new BankCardListAdapter();
                    listView.setAdapter(adapter);
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
            }
        });

    }

    private class BankCardListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {

            ViewHolder holder = null;
            if (arg1 == null) {
                arg1 = LayoutInflater.from(context).inflate(
                        R.layout.bank_list_adapter, arg2, false);
                holder = new ViewHolder();
                holder.bgLayout = arg1
                        .findViewById(R.id.bank_card_list_adapter_bg_layout);
                holder.logoImageView = arg1
                        .findViewById(R.id.bank_card_list_adapter_logo_icon);
                holder.nameTextView = arg1
                        .findViewById(R.id.bank_card_list_adapter_name_text);
                holder.numTextView = arg1
                        .findViewById(R.id.bank_card_list_adapter_num_text);
                arg1.setTag(holder);
            } else {
                holder = (ViewHolder) arg1.getTag();
            }

            RequestOptions options = new RequestOptions()
                    .fitCenter();
            Glide.with(context).load(HttpRequestUtil.imgUrl + list.get(arg0).getBankLogo())
                    .apply(options)
                    .into(holder.logoImageView);
            String name = list.get(arg0).getBankBelong();
            holder.nameTextView.setText(name);

//            if (name.equals("") || name == null) {
//                holder.bgLayout
//                        .setBackgroundResource(R.drawable.bank_red_circle_bg);
//            } else if (name.contains("工商银行") || name.contains("中国银行")
//                    || name.contains("中信银行") || name.contains("平安银行")
//                    || name.contains("招商银行")) {
//                holder.bgLayout
//                        .setBackgroundResource(R.drawable.bank_red_circle_bg);
//            } else if (name.contains("建设银行") || name.contains("交通银行")
//                    || name.contains("兴业银行") || name.contains("浦发银行")
//                    || name.contains("民生银行") || name.contains("上海银行")) {
//                holder.bgLayout
//                        .setBackgroundResource(R.drawable.bank_blue_circle_bg);
//            } else {
//                holder.bgLayout
//                        .setBackgroundResource(R.drawable.bank_green_circle_bg);
//            }

            String num = list.get(arg0).getBankNum();
            holder.numTextView.setText("* * * *   * * * *   * * * *   "
                    + num.substring(num.length() - 4, num.length()));
            return arg1;
        }

        private class ViewHolder {

            private ImageView logoImageView;
            private TextView nameTextView, numTextView;
            private LinearLayout bgLayout;

        }
    }
}
