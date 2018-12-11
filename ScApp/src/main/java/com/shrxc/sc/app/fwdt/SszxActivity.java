package com.shrxc.sc.app.fwdt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.utils.SystemBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SszxActivity extends AppCompatActivity {

    @BindView(R.id.sszx_activity_zq_text)
    TextView zqTextView;
    @BindView(R.id.sszx_activity_lq_text)
    TextView lqTextView;
    @BindView(R.id.sszx_activity_rmss_zq_layout)
    LinearLayout zqRmssLayout;
    @BindView(R.id.sszx_activity_rmss_lq_layout)
    LinearLayout lqRmssLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sszx);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
    }

    @OnClick({R.id.sszx_activity_yc_layout, R.id.sszx_activity_zq_text, R.id.sszx_activity_lq_text,
            R.id.sszx_activity_zq_other_layout, R.id.sszx_activity_lq_other_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sszx_activity_yc_layout:
                startActivity(new Intent(SszxActivity.this, SszxListActivity.class));
                break;
            case R.id.sszx_activity_zq_text:
                zqTextView.setBackgroundResource(R.drawable.left_red_circle_bg);
                zqTextView.setTextColor(getResources().getColor(R.color.app_white_color_ffffff));
                lqTextView.setBackgroundResource(R.color.app_transparent_color);
                lqTextView.setTextColor(getResources().getColor(R.color.app_red_color_fa3243));
                zqRmssLayout.setVisibility(View.VISIBLE);
                lqRmssLayout.setVisibility(View.GONE);
                break;
            case R.id.sszx_activity_lq_text:
                lqTextView.setBackgroundResource(R.drawable.right_red_circle_bg);
                lqTextView.setTextColor(getResources().getColor(R.color.app_white_color_ffffff));
                zqTextView.setBackgroundResource(R.color.app_transparent_color);
                zqTextView.setTextColor(getResources().getColor(R.color.app_red_color_fa3243));
                zqRmssLayout.setVisibility(View.GONE);
                lqRmssLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.sszx_activity_zq_other_layout:
                startActivity(new Intent(SszxActivity.this, OtherZqSszxActivity.class));
                break;
            case R.id.sszx_activity_lq_other_layout:
                startActivity(new Intent(SszxActivity.this, OtherLqSszxActivity.class));
                break;
        }
    }
}
