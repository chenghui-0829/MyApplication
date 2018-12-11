package com.shrxc.sc.app.dntz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.AddressEntity;
import com.shrxc.sc.app.bean.UpOrderEntity;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
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
    private Context context = TxddActivity.this;
    @BindView(R.id.txdd_activity_buy_zdzz_layout)
    LinearLayout zdzzLayout;
    @BindView(R.id.txdd_activity_buy_jjmd_text)
    TextView jjmdText;
    @BindView(R.id.txdd_activity_select_md_layout)
    RelativeLayout selectMdLayout;
    private UpOrderEntity entity;
    private AddressEntity address;

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
    }

    @OnClick({R.id.txdd_activity_buy_zdzz_layout, R.id.txdd_activity_buy_jjmd_text, R.id.txdd_activity_address_select_address_layout,
            R.id.txdd_activity_select_md_layout, R.id.txdd_activity_tjdd_text})
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
        }
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
}
