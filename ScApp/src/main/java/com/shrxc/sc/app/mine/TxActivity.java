package com.shrxc.sc.app.mine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.utils.SystemBarUtil;

import butterknife.ButterKnife;

public class TxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
    }
}
