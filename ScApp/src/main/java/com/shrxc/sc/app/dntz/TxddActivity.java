package com.shrxc.sc.app.dntz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.LoginActivity;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.adapter.JzAdapterUtil;
import com.shrxc.sc.app.bean.AddressEntity;
import com.shrxc.sc.app.bean.JzChildGameEntity;
import com.shrxc.sc.app.bean.UpOrderEntity;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.AppUtil;
import com.shrxc.sc.app.utils.DesUtil;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TxddActivity extends AppCompatActivity {

    @BindView(R.id.txdd_activity_address_address_text)
    TextView addressText;
    @BindView(R.id.txdd_activity_address_tel_text)
    TextView telText;
    @BindView(R.id.txdd_activity_bz_edit)
    EditText bzEdit;
    @BindView(R.id.txdd_activity_order_title_text)
    TextView orderTitleText;
    @BindView(R.id.txdd_activity_order_money_text)
    TextView orderMoneyText;
    @BindView(R.id.txdd_activity_order_bs_text)
    TextView orderBsText;
    @BindView(R.id.txdd_activity_order_dj_text)
    TextView orderDjText;
    @BindView(R.id.txdd_activity_order_num_text)
    TextView orderNumText;
    private Context context = TxddActivity.this;
    @BindView(R.id.txdd_activity_buy_zdzz_layout)
    LinearLayout zdzzLayout;
    @BindView(R.id.txdd_activity_buy_jjmd_text)
    TextView jjmdText;
    @BindView(R.id.txdd_activity_select_md_layout)
    RelativeLayout selectMdLayout;
    private UpOrderEntity entity;
    private AddressEntity address;
    private ListAdapter adapter;
    private String ggfs, mcn;
    private int gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txdd);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        entity = (UpOrderEntity) bundle.getSerializable("order");
        orderMoneyText.setText("￥" + entity.getTotalMoney());

        gameType = intent.getIntExtra("ggfs", 0);
        ggfs = JzAdapterUtil.getGgfsString(Integer.parseInt(entity.getFootBallBuyNumJson().get(0).getPlayType()));
        mcn = intent.getStringExtra("mcn");
        orderTitleText.setText("竞彩足球  " + ggfs);
        orderBsText.setText(entity.getFootBallBuyNumJson().get(0).getMultiple());
        orderNumText.setText(entity.getFootBallBuyNumJson().get(0).getSheets());
        orderDjText.setText(intent.getStringExtra("dj"));
    }

    @OnClick({R.id.txdd_activity_buy_zdzz_layout, R.id.txdd_activity_buy_jjmd_text, R.id.txdd_activity_address_select_address_layout,
            R.id.txdd_activity_select_md_layout, R.id.txdd_activity_tjdd_text, R.id.txdd_activity_back_icon, R.id.txdd_activity_order_details_text})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txdd_activity_buy_zdzz_layout:
                zdzzLayout.setBackgroundResource(R.drawable.app_red_stroke_color);
                jjmdText.setBackgroundResource(R.drawable.app_gray_stroke_color);
                selectMdLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.txdd_activity_buy_jjmd_text:
                zdzzLayout.setBackgroundResource(R.drawable.app_gray_stroke_color);
                jjmdText.setBackgroundResource(R.drawable.app_red_stroke_color);
                selectMdLayout.setVisibility(View.GONE);
                break;
            case R.id.txdd_activity_address_select_address_layout:
                Intent intent = new Intent(context, AddressActivity.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.txdd_activity_select_md_layout:
                startActivity(new Intent(context, MdActivity.class));
                break;
            case R.id.txdd_activity_tjdd_text:
                updataOrder();
                break;
            case R.id.txdd_activity_back_icon:
                finish();
                break;
            case R.id.txdd_activity_order_details_text:
                showOrderDetails();
                break;
        }
    }

    private void showOrderDetails() {

        final Dialog dialog = new Dialog(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(
                R.layout.order_details_dialog_layout, null);
        LinearLayout layout = view.findViewById(R.id.order_details_dialog_list_layout);
        TextView mcnText = view.findViewById(R.id.order_details_dialog_list_mcn_text);
        TextView bsText = view.findViewById(R.id.order_details_dialog_list_bs_text);
        ListView listView = view.findViewById(R.id.order_details_dialog_list);
        adapter = new ListAdapter();
        listView.setAdapter(adapter);

        bsText.setText(entity.getFootBallBuyNumJson().get(0).getMultiple());
        mcnText.setText("竞彩足球，" + mcn);

        if (OrderActivity.selectOrderMap.size() > 3) {
            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
            layoutParams.height = AppUtil.dip2px(context, 80) * 3;
            listView.setLayoutParams(layoutParams);
        }

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout.getLayoutParams();
        lp.height = AppUtil.dip2px(context, 80) * OrderActivity.selectOrderMap.size();
        layout.setLayoutParams(lp);

        dialog.setContentView(view);
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wl = dialogWindow.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(wl);


    }

    private void updataOrder() {

        if (addressText.getText().toString().equals("")) {
            Toast.makeText(context, "请选择收货地址", Toast.LENGTH_SHORT).show();
            return;
        }
        entity.setRemark(bzEdit.getText().toString().replaceAll(" ", ""));
        entity.setAid(address.getId());
        String buynum = JSON.toJSONString(entity);
        try {
            Map<String, String> params = new HashMap<>();
            params.put("buynum", DesUtil.EncryptAsDoNet(buynum, DesUtil.DesKey).trim());
            params.put("wifiId", DesUtil.EncryptAsDoNet("123456", DesUtil.DesKey).trim());
            HttpRequestUtil.getInstance(context).post("FootBall/BuyFTAr", params, new RequestCallback() {

                @Override
                public void onSuccess(JSONObject result, String state, String msg, String data) {
                    super.onSuccess(result, state, msg, data);
                    if (state.equals("1")) {
                        Intent intent = new Intent(context, WaitOrderActivity.class);
                        intent.putExtra("orderid", result.getString("Expand"));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x888) {
            Bundle bundle = data.getExtras();
            address = (AddressEntity) bundle.getSerializable("address");
            addressText.setText(address.getDetailAddress());
            int sex = address.getGender();
            if (sex == 1) {
                telText.setText(address.getName() + "先生  " + address.getTel());
            } else {
                telText.setText(address.getName() + "女士  " + address.getTel());
            }
        }
    }


    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return OrderActivity.selectOrderMap.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.order_details_dialog_list_adapter_layout, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            String zd = OrderActivity.selectOrderMap.get(position).getHometeam();
            String kd = OrderActivity.selectOrderMap.get(position).getGuestteam();

            if (zd.length() > 3) {
                zd = zd.substring(0, 3) + "..";
            }
            if (kd.length() > 3) {
                kd = kd.substring(0, 3) + "..";
            }
            vh.dateText.setText(OrderActivity.selectOrderMap.get(position).getGameid());
            vh.nameText.setText(zd + "VS" + kd);
            vh.typeText.setText(ggfs);

            JzChildGameEntity entity = OrderActivity.selectOrderMap.get(position);
            String select = "";
            for (int i = 0; i < entity.getSelectedList().size(); i++) {
                String selectType[] = JzAdapterUtil.getSelectType(entity.getSelectedList().get(i),
                        gameType, entity);
                select += (selectType[0] + "  ");
            }
            vh.selectText.setText(select);

            return convertView;
        }


        private class ViewHolder {

            private TextView dateText, nameText, typeText, selectText;

            public ViewHolder(View view) {

                dateText = view.findViewById(R.id.order_details_dialog_list_adapter_date_text);
                nameText = view.findViewById(R.id.order_details_dialog_list_adapter_name_text);
                typeText = view.findViewById(R.id.order_details_dialog_list_adapter_type_text);
                selectText = view.findViewById(R.id.order_details_dialog_list_adapter_selected_text);

            }


        }

    }

}
