package com.shrxc.sc.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.AppUtil;
import com.shrxc.sc.app.utils.DesUtil;
import com.shrxc.sc.app.utils.SPUtil;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LoginActivity extends AppCompatActivity {

    private Context context = LoginActivity.this;
    @BindView(R.id.login_activity_back_icon)
    ImageView backImageView;
    @BindView(R.id.login_activity_login_button)
    Button loginButton;
    @BindView(R.id.login_activity_pnum_edit)
    EditText phoneEditText;
    @BindView(R.id.login_activity_msg_edit)
    EditText msgEditText;
    @BindView(R.id.login_activity_get_msg_but)
    Button getCodeButton;
    private Timer timer;
    private int resetCodeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initData();
        initEvent();
    }

    private void initData() {
    }

    private void initEvent() {

        /**
         * 获取验证码
         */
        getCodeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final String phone = phoneEditText.getText().toString()
                        .replaceAll(" ", "");
                if (phone.length() == 0 || phone == null) {
                    AppUtil.showHintDiaolog(context, "请输入手机号", null);
                    return;
                }
                getMsg();
            }
        });

        /**
         * 登录
         */
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                final String phone = phoneEditText.getText().toString()
                        .replaceAll(" ", "");
                String code = msgEditText.getText().toString().trim();
//                if (phone.length() == 0 || phone == null) {
//                    JkAppUtil.showHintDiaolog(context, "请输入手机号", null);
//                    return;
//                }
//                if (pw.length() == 0 || pw == null) {
//                    JkAppUtil.showHintDiaolog(context, "请输入密码", null);
//                    return;
//                }
//                if (!JkAppUtil.IsNet(context)) {
//                    JkAppUtil.showHintDiaolog(context, "网络连接异常", null);
//                    return;
//                }

                try {
                    Map<String, Object> params = new HashMap<>();
                    params.put("tel", DesUtil.EncryptAsDoNet(phone, DesUtil.DesKey));
                    params.put("code", DesUtil.EncryptAsDoNet(code, DesUtil.DesKey));
                    params.put("wifiid", "123456");
                    params.put("os", "0");
                    params.put("device", Build.MODEL);
                    params.put("ip", DesUtil.EncryptAsDoNet("123456", DesUtil.DesKey));
                    params.put("version", AppUtil.getVersion(context));
                    HttpRequestUtil.getInstance(context).get("Login/LoginByCode", params, new RequestCallback() {
                        @Override
                        public void onSuccess(JSONObject result, String state, String msg, String data) {
                            super.onSuccess(result, state, msg, data);
                            try {

                                if ("1".equals(state)) {
                                    String ticket = result.getJSONObject("Data").getString("Ticket");
                                    String user = DesUtil.DecryptDoNet(result.getJSONObject("Data").getString("User"), DesUtil.DesKey);

                                    System.out.println("-----user------" + user);
                                    JSONObject userObject = JSONObject.parseObject(user);
                                    SPUtil.put(context, "ticket", ticket);
                                    SPUtil.put(context, "param_id", userObject.getString("Param_Id"));

                                    Set<String> tagSet = new LinkedHashSet<String>();
                                    tagSet.add("上彩");
                                    // 调用JPush API设置Tag
                                    mHandler.sendMessage(mHandler
                                            .obtainMessage(
                                                    MSG_SET_TAGS,
                                                    tagSet));
                                    // 调用JPush API设置Alias
                                    mHandler.sendMessage(mHandler
                                            .obtainMessage(
                                                    MSG_SET_ALIAS,
                                                    userObject.getString(
                                                            "Tid")
                                                            .replace(
                                                                    "-",
                                                                    "")));
                                    finish();
                                }
                            } catch (Exception e) {
                                System.out.println("-----Exception-------" + e);
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onStringResult(String result) {
                            super.onStringResult(result);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        /**
         * 返回
         */
        backImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setResult(500);
                finish();
            }
        });
    }

    private void getMsg() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("tel", DesUtil.EncryptAsDoNet(phoneEditText.getText().toString()
                    .replaceAll(" ", ""), DesUtil.DesKey));
            params.put("type", DesUtil.EncryptAsDoNet("Regist", DesUtil.DesKey));

            HttpRequestUtil.getInstance(context).get("BaseMethod/SendMsg", params, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject result, String state, String msg, String data) {
                    super.onSuccess(result, state, msg, data);
                    if (state.equals("1")) {
                        timer = new Timer();
                        resetCodeTime = 60;
                        timer.schedule(new TimerTask() {
                            public void run() {
                                Message message = new Message();
                                message.what = 0x123;
                                message.obj = --resetCodeTime;
                                timerHandler.sendMessage(message);
                            }
                        }, 0, 1000);
                    }
                }

                @Override
                public void onErro(String erro) {
                    super.onErro(erro);
                    System.out.println("--------erro--------" + erro);
                }
            });
        } catch (Exception e) {
            System.out.println("--------erro--------" + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler timerHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                int time = (Integer) msg.obj;
                if (time != 0) {
                    getCodeButton.setBackgroundResource(R.color.app_text_color_999999);
                    getCodeButton.setText(time + "s重新获取");
                    getCodeButton.setEnabled(false);
                } else {
                    getCodeButton.setBackgroundResource(R.drawable.app_title_bg);
                    getCodeButton.setText("获取验证码");
                    getCodeButton.setEnabled(true);
                    timer.cancel();
                }
            }
        }
    };


    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), null,
                            (Set<String>) msg.obj, mTagsCallback);
                    break;
            }
        }
    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    System.out.println("===>Set tag and alias success");
                    break;

                case 6002:
                    System.out
                            .println("====>Failed to set alias and tags due to timeout. Try again after 60s.");
                    mHandler.sendMessageDelayed(
                            mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;

                default:
                    System.out.println("======>Failed with errorCode = " + code);
            }
        }
    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    System.out.println("===>Set tag and alias success");
                    break;

                case 6002:
                    System.out
                            .println("===>Failed to set alias and tags due to timeout. Try again after 60s.");
                    mHandler.sendMessageDelayed(
                            mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    break;

                default:
                    System.out.println("======>Failed with errorCode = " + code);
            }
        }

    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(500);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

