package com.shrxc.sc.app.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.utils.SystemBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QbActivity extends AppCompatActivity {

    private Context context = QbActivity.this;
    @BindView(R.id.qb_activity_tx_layout)
    RelativeLayout txLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qb);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
    }

    @OnClick({R.id.qb_activity_tx_layout, R.id.qb_activity_zjmx_layout, R.id.qb_activity_bank_card_layout})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.qb_activity_tx_layout:
                startActivity(new Intent(context, TxActivity.class));
                break;
            case R.id.qb_activity_zjmx_layout:
                startActivity(new Intent(context, ZjmxActivity.class));
                break;
            case R.id.qb_activity_bank_card_layout:
                startActivity(new Intent(context, BankListActivity.class));
                break;
        }
    }
}
