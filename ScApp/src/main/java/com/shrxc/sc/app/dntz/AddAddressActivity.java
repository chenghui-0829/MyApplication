package com.shrxc.sc.app.dntz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.AddressEntity;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAddressActivity extends AppCompatActivity {

    @BindView(R.id.add_address_activity_shdz_text)
    TextView shdzText;
    @BindView(R.id.add_address_activity_mph_edit)
    EditText mphEdit;
    @BindView(R.id.add_address_activity_lxr_edit)
    EditText lxrEdit;
    @BindView(R.id.add_address_activity_man_radiobutton)
    RadioButton manRadiobutton;
    @BindView(R.id.add_address_activity_woman_radiobutton)
    RadioButton womanRadiobutton;
    @BindView(R.id.add_address_activity_tel_edit)
    EditText telEdit;
    @BindView(R.id.add_address_activity_title_text)
    TextView titleText;
    @BindView(R.id.add_address_activity_delete_text)
    TextView deleteText;
    private Context context = AddAddressActivity.this;
    @BindView(R.id.add_address_activity_selelct_address_layout)
    RelativeLayout selelctAddressLayout;
    private String provincep = "", city = "", area = "", detailAddress = "", type = "", addUrl = "";
    private AddressEntity address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (type.equals("add")) {
            titleText.setText("添加收货地址");
            deleteText.setVisibility(View.GONE);
        } else {
            Bundle bundle = intent.getExtras();
            address = (AddressEntity) bundle.getSerializable("address");
            titleText.setText("编辑收货地址");
            deleteText.setVisibility(View.VISIBLE);
            shdzText.setText(address.getDetailAddress());
            mphEdit.setText(address.getDoor());
            lxrEdit.setText(address.getName());
            telEdit.setText(address.getTel());
            if (address.getGender() == 1) {
                manRadiobutton.setChecked(true);
            } else {
                womanRadiobutton.setChecked(true);
            }
        }

    }

    @OnClick({R.id.add_address_activity_selelct_address_layout, R.id.add_address_activity_save_but,
            R.id.add_address_activity_delete_text, R.id.add_address_activity_back_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_address_activity_selelct_address_layout:
                Intent intent = new Intent(context, GetAddressActivity.class);
                startActivityForResult(intent, 0x123);
                break;
            case R.id.add_address_activity_save_but:
                saveAddress();
                break;
            case R.id.add_address_activity_delete_text:
                deteleAddress();
                break;
            case R.id.add_address_activity_back_icon:
                finish();
                break;
        }
    }

    private void deteleAddress() {

        HashMap<String, Object> params = new HashMap<>();
        params.put("adId", address.getId());

        HttpRequestUtil.getInstance(context).get("DeliveryAddress/RemoveAddress", params, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if (state.equals("1")) {
                    finish();
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

    private void saveAddress() {

        String shdz = shdzText.getText().toString().replaceAll(" ", "");
        String mph = mphEdit.getText().toString().replaceAll(" ", "");
        String lxr = lxrEdit.getText().toString().replaceAll(" ", "");
        String tel = telEdit.getText().toString().replaceAll(" ", "");

        if (shdz.length() == 0) {
            Toast.makeText(context, "请选择收货地址", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tel.length() == 0) {
            Toast.makeText(context, "请填写手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        int gender = 1;
        if (manRadiobutton.isChecked()) {
            gender = 1;
        } else if (womanRadiobutton.isChecked()) {
            gender = 0;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("Province", "上海市");
        params.put("City", "上海市");
        params.put("Area", "虹口区");
        params.put("DetailAddress", detailAddress);
        params.put("IsDefault", false);
        params.put("Tel", tel);
        params.put("Name", lxr);
        params.put("Door", mph);
        params.put("Tag", "");
        params.put("Gender", gender);
        if (type.equals("bj")) {
            params.put("adId", address.getId());
            addUrl = "DeliveryAddress/UpdateAddress";
        } else {
            addUrl = "DeliveryAddress/AddDeliveryAddress";
        }

        HttpRequestUtil.getInstance(context).get(addUrl, params, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                System.out.println("-------result------" + result);
                if (state.equals("1")) {
                    finish();
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
                System.out.println("-------erro------" + erro);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0x888) {
            detailAddress = data.getStringExtra("name");
            provincep = data.getStringExtra("province");
            city = data.getStringExtra("city");
            area = data.getStringExtra("area");
            shdzText.setText(detailAddress);
        } else if (resultCode == 0x887) {
            city = data.getStringExtra("city");
            area = data.getStringExtra("address");
            detailAddress = data.getStringExtra("name");
            shdzText.setText(detailAddress);
        }
    }
}
