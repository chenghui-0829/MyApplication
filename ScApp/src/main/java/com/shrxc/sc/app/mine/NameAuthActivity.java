package com.shrxc.sc.app.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.AppUtil;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NameAuthActivity extends AppCompatActivity {

    private Context context = NameAuthActivity.this;
    @BindView(R.id.name_auth_activity_name_text)
    EditText nameText;
    @BindView(R.id.name_auth_activity_sfzh_text)
    EditText sfzhText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_auth);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
    }

    @OnClick({R.id.name_auth_activity_back_icon, R.id.name_auth_activity_sq_button})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.name_auth_activity_back_icon:
                finish();
                break;
            case R.id.name_auth_activity_sq_button:
                auth_name();
                break;
        }

    }

    private void auth_name() {

        final String name = nameText.getText().toString().trim();
        String sfzh = sfzhText.getText().toString().trim();

        if ("".equals(name) || name == null) {
            AppUtil.showHintDiaolog(context, "请输入姓名", null);
            return;
        }
        if ("".equals(sfzh) || sfzh == null) {
            AppUtil.showHintDiaolog(context, "请输入身份证号", null);
            return;
        }
        if (!AppUtil.isIdNum(sfzh)) {
            AppUtil.showHintDiaolog(context, "身份证号有误", null);
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("idnum", sfzh);
        HttpRequestUtil.getInstance(context).get("IdCard/AddIdcard", params, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if (state.equals("1")) {
                    Intent intent = new Intent(context, AddBankActivity.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
            }
        });

    }
}
