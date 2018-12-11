package com.shrxc.sc.app.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.adapter.MsgEntity;
import com.shrxc.sc.app.utils.AppUtil;
import com.shrxc.sc.app.utils.SystemBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MsgDetailsActivity extends AppCompatActivity {

    private Context context = MsgDetailsActivity.this;
    @BindView(R.id.msg_details_activity_title_text)
    TextView titleText;
    @BindView(R.id.msg_details_activity_time_text)
    TextView timeText;
    @BindView(R.id.msg_details_activity_content_text)
    TextView contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_details);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        getData();
    }

    private void getData() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        MsgEntity entity = (MsgEntity) bundle.getSerializable("msg");

        String time = AppUtil.formatString(entity.getCreatetime().
                replace("T", " "), "yyyy-MM-dd HH:mm:ss");
        timeText.setText(time);
        titleText.setText(entity.getTitle());
        contentText.setText("        " + entity.getContent());

    }
}
