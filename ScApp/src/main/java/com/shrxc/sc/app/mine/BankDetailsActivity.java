package com.shrxc.sc.app.mine;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shrxc.sc.app.LoginActivity;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.BankListEntity;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.AppUtil;
import com.shrxc.sc.app.utils.HintDialogEventImp;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BankDetailsActivity extends AppCompatActivity {

    private Context context = BankDetailsActivity.this;
    @BindView(R.id.bank_details_activity_more_icon)
    ImageView moreIcon;
    @BindView(R.id.bank_details_activity_logo_icon)
    ImageView logoIcon;
    @BindView(R.id.bank_details_activity_name_text)
    TextView nameText;
    @BindView(R.id.bank_details_activity_type_text)
    TextView typeText;
    private BankListEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initData();
    }

    @OnClick(R.id.bank_details_activity_more_icon)
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bank_details_activity_more_icon:
                showEditDialog();
                break;
        }
    }

    private void initData() {
        Intent intent = getIntent();
        entity = (BankListEntity) intent.getSerializableExtra("bankinfo");

        if (entity.getIsDefault().equals("true")) {
            typeText.setText("收款银行卡");
        } else {
            typeText.setText("一般银行卡");
        }
        String num = entity.getBankNum();
        nameText.setText(entity.getBankNum() + "(尾号"
                + num.substring(num.length() - 4, num.length()) + ")");
        RequestOptions options = new RequestOptions()
                .fitCenter();
        Glide.with(context).load(HttpRequestUtil.imgUrl + entity.getBankLogo())
                .apply(options)
                .into(logoIcon);
    }

    private void showEditDialog() {

        View view = LayoutInflater.from(context).inflate(
                R.layout.bank_details_activity_edit_window, null);
        LinearLayout layout = (LinearLayout) view
                .findViewById(R.id.app_edit_bank_dialog_root_layout);
        TextView changeTextView = (TextView) view
                .findViewById(R.id.app_edit_bank_dialog_change_text);
        TextView deleteTextView = (TextView) view
                .findViewById(R.id.app_edit_bank_dialog_delete_text);
        TextView cancleTextView = (TextView) view
                .findViewById(R.id.app_edit_bank_dialog_cancle_text);
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialog.show();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        layout.measure(w, h);
        Animation animation = new TranslateAnimation(0, 0,
                layout.getMeasuredHeight(), 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        layout.startAnimation(animation);
        /**
         * 设为收款银行
         */
        changeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.cancel();
                dialog.dismiss();
                changeBankRequest();
            }
        });

        /**
         * 解绑
         */
        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                dialog.cancel();
                dialog.dismiss();
                if (entity.getIsDefault().equals("true")) {
                    AppUtil.showHintDiaolog(context, "收款银行卡不可解绑", null);
                    return;
                }
                deleteBankRequest();
            }

        });

        /**
         * 取消
         */
        cancleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.cancel();
                dialog.dismiss();
            }
        });

    }

    private void changeBankRequest() {

        if (entity.getIsDefault().equals("true")) {
            AppUtil.showHintDiaolog(context, "该卡为收款银行卡", null);
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("BankId", entity.getId());

        HttpRequestUtil.getInstance(context).get("BankCard/AddBankCardDefault", params, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);

                if (state.equals("1")) {
                    AppUtil.showHintDiaolog(context, "更改成功",
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
                System.out.println("--------erro----" + erro);
            }
        });
    }

    private void deleteBankRequest() {

        Map<String, Object> params = new HashMap<>();
        params.put("BankId", entity.getId());
        HttpRequestUtil.getInstance(context).get("BankCard/RemoveBankCard", params, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if (state.equals("1")) {
                    AppUtil.showHintDiaolog(context, "解绑成功",
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
                System.out.println("--------erro----" + erro);
            }
        });
    }
}
