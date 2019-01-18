package com.shrxc.sc.app.dntz;

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
import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.LoginActivity;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.AddressEntity;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressActivity extends AppCompatActivity {

    private Context context = AddressActivity.this;
    @BindView(R.id.address_activity_list)
    ListView addressListView;
    @BindView(R.id.address_activity_add_text)
    TextView addTextView;
    List<AddressEntity> addressList;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initEvent();
    }

    private void initEvent() {

        addressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("address", addressList.get(i));
                intent.putExtras(bundle);
                setResult(0x888, intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {

        addressList = new ArrayList<>();
        adapter = new ListAdapter();
        addressListView.setAdapter(adapter);
        HttpRequestUtil.getInstance(context).getByNoParams("DeliveryAddress/GetDeliveryAddressList", new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if (state.equals("1")) {
                    addressList = JSON.parseArray(data, AddressEntity.class);
                    adapter.notifyDataSetChanged();
                }else if ("-1".equals(state)) {
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


    }

    @OnClick({R.id.address_activity_add_text, R.id.address_activity_back_icon})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.address_activity_add_text:
                Intent intent = new Intent(context, AddAddressActivity.class);
                intent.putExtra("type", "add");
                startActivity(intent);
                break;
            case R.id.address_activity_back_icon:
                finish();
                break;
        }
    }


    private class ListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return addressList == null ? 0 : addressList.size();
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
        public View getView(final int pos, View view, ViewGroup viewGroup) {

            ViewHolder vh = null;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.address_activity_list_adapter_layout, viewGroup, false);
                vh = new ViewHolder(view);
                view.setTag(vh);
            } else {
                vh = (ViewHolder) view.getTag();
            }

            String tag = addressList.get(pos).getTag();
            if (!"".equals(tag) && tag != null) {
                vh.bqTextView.setText(tag + "   ");
            }
            vh.addressTextView.setText(addressList.get(pos).getDetailAddress());
            vh.telTextView.setText(addressList.get(pos).getTel());
            int sex = addressList.get(pos).getGender();
            if (sex == 1) {
                vh.nameTextView.setText(addressList.get(pos).getName() + "  先生");
            } else {
                vh.nameTextView.setText(addressList.get(pos).getName() + "  女士");
            }

            vh.xgImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, AddAddressActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("address", addressList.get(pos));
                    intent.putExtras(bundle);
                    intent.putExtra("type", "bj");
                    startActivity(intent);
                }
            });

            return view;
        }

        private class ViewHolder {

            private TextView bqTextView, addressTextView, nameTextView, telTextView;
            private ImageView xgImageView;

            public ViewHolder(View view) {
                xgImageView = view.findViewById(R.id.address_activity_list_adapter_xg_icon);
                bqTextView = view.findViewById(R.id.address_activity_list_adapter_bq_text);
                addressTextView = view.findViewById(R.id.address_activity_list_adapter_details_text);
                nameTextView = view.findViewById(R.id.address_activity_list_adapter_nane_text);
                telTextView = view.findViewById(R.id.address_activity_list_adapter_tel_text);
            }
        }
    }
}
