package com.shrxc.sc.app.dntz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.utils.SystemBarUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CzdtActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_czdt);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
    }


    @OnClick({R.id.czdt_activity_jczq_linear, R.id.czdt_activity_jclq_linear})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.czdt_activity_jczq_linear:
                startActivity(new Intent(this, JczqActivity.class));
                break;
            case R.id.czdt_activity_jclq_linear:
                startActivity(new Intent(this, JclqActivity.class));
                break;
        }

    }

}
