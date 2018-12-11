package com.shrxc.sc.app;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ע��
 *
 * @author CH
 */
public class RegisterActivity extends AppCompatActivity {

    private Context context = RegisterActivity.this;
    @BindView(R.id.register_activity_back_icon)
    ImageView backImageView;// ����
    @BindView(R.id.register_activity_phonenum_edit)
    EditText phoneEditText;// �ֻ���
    @BindView(R.id.register_activity_code_edit)
    EditText codeEditText;// ��֤��
    @BindView(R.id.register_activity_pw_edit)
    EditText pwEditText;// ����
    @BindView(R.id.register_activity_sure_pw_edit)
    EditText surePwEditText;// ȷ������
    @BindView(R.id.register_activity_get_code_button)
    Button getCodeButton;// ��ȡ��֤��
    @BindView(R.id.register_activity_register_button)
    Button registerButton;// ע��
    private Timer timer;
    private int resetCodeTime, regState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initEvent();
    }

//    private void initData() {
//
//        GradientDrawable myGrad = (GradientDrawable) getCodeButton
//                .getBackground();
//        myGrad.setColor(getResources().getColor(
//                R.color.app_ui_gray_text_color_cccccc));
//        getCodeButton.setTextColor(getResources().getColor(
//                R.color.app_ui_title_bg_color_384965));
//        getCodeButton.setText("��ȡ��֤��");
//        getCodeButton.setEnabled(true);
//        getRegiWay();
//
//    }

//    /**
//     * ��ȡע�᷽ʽ
//     */
//    private void getRegiWay() {
//        if (!JkAppUtil.IsNet(context)) {
//            return;
//        }
//
//        JkHttpUtil.sendHttpByGet(regiWayUrl, null,
//                new AsyncHttpResponseHandler() {
//
//                    @Override
//                    public void onSuccess(int arg0, String arg1) {
//                        super.onSuccess(arg0, arg1);
//                        System.out.println("=========regiWayResult======="
//                                + arg1);
//                        JSONObject object = JSONObject.parseObject(arg1);
//                        if (object.getString("state").equals("1")) {
//                            String data = object.getString("data");
//                            if (data.equals("0")) {
//                                regState = 0;
//                            } else {
//                                regState = 1;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Throwable arg0, String arg1) {
//                        super.onFailure(arg0, arg1);
//                        regState = 0;
//                    }
//                });
//
//    }

//    private Handler timerHandler = new Handler() {
//
//        public void handleMessage(Message msg) {
//            if (msg.what == 0x123) {
//                int time = (Integer) msg.obj;
//                if (time != 0) {
//                    getCodeButton.setTextColor(getResources().getColor(
//                            R.color.app_ui_title_bg_color_384965));
//                    GradientDrawable myGrad = (GradientDrawable) getCodeButton
//                            .getBackground();
//                    myGrad.setColor(getResources().getColor(
//                            R.color.app_ui_gray_text_color_cccccc));
//                    getCodeButton.setText(time + "s�����»�ȡ");
//                    getCodeButton.setEnabled(false);
//                } else {
//                    getCodeButton.setTextColor(Color.WHITE);
//                    GradientDrawable myGrad = (GradientDrawable) getCodeButton
//                            .getBackground();
//                    myGrad.setColor(getResources().getColor(
//                            R.color.app_ui_title_bg_color_384965));
//                    getCodeButton.setText("���»�ȡ");
//                    getCodeButton.setEnabled(true);
//                    timer.cancel();
//                }
//            }
//        }
//
//        ;
//    };

//    private void isAlreadyRegister(String phoneNum) {
//        try {
//            RequestParams params = new RequestParams();
//            params.put("tel", phone);
//            JkHttpUtil.sendHttpByGet(checkTelUrl, params,
//                    new AsyncHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int arg0, String arg1) {
//                            super.onSuccess(arg0, arg1);
//                            System.out.println("======success======" + arg1);
//                            JSONObject object = JSONObject.parseObject(arg1);
//                            if (object.getString("state").equals("1")) {
//                                getCodeButton.setTextColor(Color.WHITE);
//                                GradientDrawable myGrad = (GradientDrawable) getCodeButton
//                                        .getBackground();
//                                myGrad.setColor(getResources().getColor(
//                                        R.color.app_ui_title_bg_color_384965));
//                                getMsgCode();
//                            } else {
//                                JkAppUtil.showHintDiaolog(context, "���ֻ�����ע��",
//                                        null);
//                                getCodeButton
//                                        .setTextColor(getResources()
//                                                .getColor(
//                                                        R.color.app_ui_title_bg_color_384965));
//                                GradientDrawable myGrad = (GradientDrawable) getCodeButton
//                                        .getBackground();
//                                myGrad.setColor(getResources().getColor(
//                                        R.color.app_ui_gray_text_color_cccccc));
//                                return;
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Throwable arg0, String arg1) {
//                            super.onFailure(arg0, arg1);
//                            System.out.println("======fail======" + arg1);
//                        }
//
//                    });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

//    private void getMsgCode() {
//
//        timer = new Timer();
//        resetCodeTime = 60;
//        timer.schedule(new TimerTask() {
//            public void run() {
//                Message message = new Message();
//                message.what = 0x123;
//                message.obj = --resetCodeTime;
//                timerHandler.sendMessage(message);
//            }
//        }, 0, 1000);
//
//        RequestParams params = new RequestParams();
//        params.put("tel", phone);
//        JkHttpUtil.sendHttpByGet(getCodeUrl, params,
//                new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int arg0, String arg1) {
//                        super.onSuccess(arg0, arg1);
//                        System.out.println("========success==========" + arg1);
//                        JSONObject object = JSONObject.parseObject(arg1);
//                        String state = object.getString("state");
//                        if (state.equals("1")) {
//                            JkAppUtil.showHintDiaolog(context, "���ͳɹ�", null);
//                        } else if (state.equals(-1)) {
//                            JkAppUtil.showHintDiaolog(context, "�պ���", null);
//                        } else if (state.equals("-2")) {
//                            JkAppUtil.showHintDiaolog(context, "��ȡʧ��,��ȡ����Ƶ��",
//                                    null);
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Throwable arg0, String arg1) {
//                        super.onFailure(arg0, arg1);
//                        JkAppUtil.showHintDiaolog(context, getResources()
//                                        .getString(R.string.app_service_erro_string),
//                                null);
//                        System.out.println("=========fail=========" + arg1);
//                    }
//                });
//
//    }

    private void initEvent() {
        /**
         * �ֻ�����������
         */
//        phoneEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//                                      int arg3) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1,
//                                          int arg2, int arg3) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable arg0) {
//                if (arg0.toString().length() == 11) {
//                    if (JkAppUtil.isMobile(arg0.toString())) {
//                        getCodeButton.setTextColor(Color.WHITE);
//                        GradientDrawable myGrad = (GradientDrawable) getCodeButton
//                                .getBackground();
//                        myGrad.setColor(getResources().getColor(
//                                R.color.app_ui_title_bg_color_384965));
//                    } else {
//                        getCodeButton.setTextColor(getResources().getColor(
//                                R.color.app_ui_title_bg_color_384965));
//                        GradientDrawable myGrad = (GradientDrawable) getCodeButton
//                                .getBackground();
//                        myGrad.setColor(getResources().getColor(
//                                R.color.app_ui_gray_text_color_cccccc));
//                    }
//                } else {
//                    getCodeButton.setTextColor(getResources().getColor(
//                            R.color.app_ui_title_bg_color_384965));
//                    GradientDrawable myGrad = (GradientDrawable) getCodeButton
//                            .getBackground();
//                    myGrad.setColor(getResources().getColor(
//                            R.color.app_ui_gray_text_color_cccccc));
//                }
//            }
//
//        });

        /**
         * ��ȡ��֤��
         */
        getCodeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

//                try {
//                    if (!JkAppUtil.IsNet(context)) {
//                        JkAppUtil.showHintDiaolog(context, "���������쳣", null);
//                        return;
//                    }
//                    String phoneN = phoneEditText.getText().toString().trim();
//                    System.out.println("=====regState=======>" + regState);
//
//                    if (regState == 1) {
//
//                        if (!JkAppUtil.isChinaPhoneLegal(phoneN)) {
//                            JkAppUtil.showHintDiaolog(context, "��������ȷ���ֻ���",
//                                    null);
//                            return;
//                        }
//
//                        if (!JkAppUtil.isUserPhoneNum(phoneN, context)) {
//                            JkAppUtil.showHintDiaolog(context, "��ʹ�ñ�������ע��",
//                                    null);
//                            return;
//                        }
//
//                    } else {
//
//                        if (!JkAppUtil.isMobile(phoneN)) {
//                            JkAppUtil.showHintDiaolog(context, "��������ȷ���ֻ���",
//                                    null);
//                            return;
//                        }
//
//                    }
//                    phone = DesUtil.EncryptAsDoNet(phoneN, DesUtil.DesKey);
//                    isAlreadyRegister(phone);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        });

        /**
         * ����
         */
        backImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        /**
         * ע��
         */
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

//                final String phone = phoneEditText.getText().toString()
//                        .replaceAll(" ", "");
//                String pw = pwEditText.getText().toString().trim();
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

                // 在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0
                String macAddress = null;
                WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiinfo = (null == wifiMgr ? null : wifiMgr
                        .getConnectionInfo());
                if (null != wifiinfo) {
                    macAddress = wifiinfo.getMacAddress();
                }
                try {
                    Map<String, Object> params = new HashMap<>();
                    params.put("tel", DesUtil.EncryptAsDoNet("15927299226", DesUtil.DesKey));
                    params.put("pwd", DesUtil.EncryptAsDoNet("123456", DesUtil.DesKey));
                    params.put("pwd2", DesUtil.EncryptAsDoNet("123456", DesUtil.DesKey));
                    params.put("wifiid", "123456");
                    params.put("code", DesUtil.EncryptAsDoNet("1234", DesUtil.DesKey));
                    params.put("os", "0");
                    params.put("device", Build.MODEL);
                    params.put("ip", getLocalIpAddress());
                    params.put("version", AppUtil.getVersion(context));
                    HttpRequestUtil.getInstance(context).get("Login/RegistByTel", params, new RequestCallback() {
                        @Override
                        public void onSuccess(JSONObject result, String state, String msg, String data) {
                            super.onSuccess(result, state, msg, data);
                        }

                        @Override
                        public void onStringResult(String result) {
                            super.onStringResult(result);
                            System.out.println("-------result------" + result);

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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }
}
