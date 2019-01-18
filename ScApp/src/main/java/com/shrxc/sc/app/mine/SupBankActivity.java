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
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.SupBankEntity;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupBankActivity extends AppCompatActivity {

    private Context context = SupBankActivity.this;
    @BindView(R.id.sup_bank_activity_list)
    ListView listView;
    private List<SupBankEntity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_bank);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initData();
        initEvent();
    }

    private void initEvent() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent();
                intent.putExtra("name", list.get(arg2).getName());
                intent.putExtra("bankid", list.get(arg2).getBankid());
                setResult(500, intent);
                finish();
            }
        });
    }

    private void initData() {

        list = new ArrayList<>();
        HttpRequestUtil.getInstance(context).getByNoParams("BankCard/GetBankList", new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if ("1".equals(state)) {
                    JSONArray bankArray = result.getJSONArray("Data");
                    for (int i = 0; i < bankArray.size(); i++) {
                        SupBankEntity entity = JSON.toJavaObject(bankArray.getJSONObject(i), SupBankEntity.class);
                        list.add(entity);

                    }
                    listView.setAdapter(new ListAdapter());
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
            }
        });

    }

    private class ListAdapter extends BaseAdapter {

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

            View view = LayoutInflater.from(context).inflate(
                    R.layout.sup_bank_list_adapter, arg2, false);
            ImageView logoImageView = view.findViewById(R.id.sup_bank_dialog_adapter_logo_icon);
            TextView nameTextView = view.findViewById(R.id.sup_bank_dialog_adapter_name_text);
            TextView moneyTextView = view.findViewById(R.id.sup_bank_dialog_adapter_money_text);
            RequestOptions options = new RequestOptions()
                    .fitCenter();
            Glide.with(context).load(HttpRequestUtil.imgUrl + list.get(arg0).getLogo())
                    .apply(options)
                    .into(logoImageView);
            nameTextView.setText(list.get(arg0).getName());
            moneyTextView.setText(list.get(arg0).getXiane());
            return view;
        }

    }

}
