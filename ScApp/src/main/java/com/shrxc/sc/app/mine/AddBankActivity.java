package com.shrxc.sc.app.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.LoginActivity;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.AppUtil;
import com.shrxc.sc.app.utils.BankCardEditText;
import com.shrxc.sc.app.utils.HintDialogEventImp;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBankActivity extends AppCompatActivity {

    private Context context = AddBankActivity.this;
    @BindView(R.id.add_bank_activity_name_edit)
    EditText nameEdit;
    @BindView(R.id.add_bank_activity_yhkh_edit)
    BankCardEditText yhkhEdit;
    @BindView(R.id.add_bank_activity_ssyh_text)
    TextView ssyhText;
    @BindView(R.id.add_bank_activity_tel_edit)
    EditText telEdit;
    private String bankid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        nameEdit.setText(intent.getStringExtra("name"));

    }

    @OnClick({R.id.add_bank_activity_ssyh_layout, R.id.add_bank_activity_finish_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_bank_activity_ssyh_layout:
                Intent intent = new Intent(context, SupBankActivity.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.add_bank_activity_finish_button:
                addBank();
                break;
        }
    }

    private void addBank() {

        String bankNum = yhkhEdit.getText().toString().replaceAll(" ", "");
        String tel = telEdit.getText().toString().trim();
        String bankName = ssyhText.getText().toString().trim();

        if ("".equals(bankNum)) {
            AppUtil.showHintDiaolog(context, "请输入您的银行卡号", null);
            return;
        }
        if ("".equals(bankName)) {
            AppUtil.showHintDiaolog(context, "请选择收款银行", null);
            return;
        }
        if ("".equals(tel)) {
            AppUtil.showHintDiaolog(context, "请输入手机号", null);
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("BankNum", bankNum);
        map.put("isDefault", false);
        map.put("bantel", tel);
        map.put("bankid", bankid);
        HttpRequestUtil.getInstance(context).get("BankCard/AddBankCard", map, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if (state.equals("1")) {
                    AppUtil.showHintDiaolog(context, "添加成功",
                            new HintDialogEventImp() {
                                @Override
                                public void eventClickListener() {
                                    finish();
                                }
                            });
                } else if (state.equals("-1")) {
                    Intent intent = new Intent(context,
                            LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
                System.out.println("------erro-----" + erro);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 500) {
            bankid = data.getStringExtra("bankid");
            ssyhText.setText(data.getStringExtra("name"));
        }
    }
}
