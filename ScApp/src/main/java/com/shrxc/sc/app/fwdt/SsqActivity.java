package com.shrxc.sc.app.fwdt;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.utils.RandomBall;
import com.shrxc.sc.app.utils.SystemBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SsqActivity extends AppCompatActivity {

    private Context context = SsqActivity.this;
    @BindView(R.id.ssq_activity_red_ball_grid)
    GridView redBallGridView;
    @BindView(R.id.ssq_activity_bule_ball_grid)
    GridView buleBallGridView;
    private int[] randomNum_red, randomNum_bule;

    private RedBallGridAdapter redAdapter;
    private BuleBallGridAdapter buleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssq);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initGrid();

    }

    private void initGrid() {

        redAdapter = new RedBallGridAdapter(33);
        redBallGridView.setAdapter(redAdapter);
        buleAdapter = new BuleBallGridAdapter(16);
        buleBallGridView.setAdapter(buleAdapter);

    }

    @OnClick(R.id.ssq_activity_jx_text)
    public void onClick(View view) {

        randomNum_red = RandomBall.selectBall(33, 6);
        randomNum_bule = RandomBall.selectBall(16, 1);
        redAdapter.notifyDataSetChanged();
        buleAdapter.notifyDataSetChanged();
    }

    private class RedBallGridAdapter extends BaseAdapter {

        private int ballNum = 0;

        public RedBallGridAdapter(int ballNum) {
            this.ballNum = ballNum;
        }

        @Override
        public int getCount() {
            return ballNum;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            View ballView = LayoutInflater.from(context).inflate(R.layout.ball_bg_layout, viewGroup, false);

            TextView ballNumTextView = ballView.findViewById(R.id.ball_num_text);
            ballNumTextView.setTextColor(getResources().getColor(R.color.app_red_color_fa3243));
            if (position < 9) {
                ballNumTextView.setText("0" + (position + 1));
            } else {
                ballNumTextView.setText((position + 1) + "");
            }

            if (randomNum_red != null && randomNum_red.length != 0) {

                for (int i = 0; i < randomNum_red.length; i++) {

                    if ((position + 1) == randomNum_red[i]) {
                        ballNumTextView.setTextColor(getResources().getColor(R.color.app_white_color_ffffff));
                        ballNumTextView.setBackgroundResource(R.drawable.red_ball_bg);
                    }
                }

            }
            return ballView;
        }
    }

    private class BuleBallGridAdapter extends BaseAdapter {

        private int ballNum = 0;

        public BuleBallGridAdapter(int ballNum) {
            this.ballNum = ballNum;
        }

        @Override
        public int getCount() {
            return ballNum;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            View ballView = LayoutInflater.from(context).inflate(R.layout.ball_bg_layout, viewGroup, false);

            TextView ballNumTextView = ballView.findViewById(R.id.ball_num_text);
            ballNumTextView.setTextColor(getResources().getColor(R.color.app_blue_color_4d8cf5));
            if (position < 9) {
                ballNumTextView.setText("0" + (position + 1));
            } else {
                ballNumTextView.setText((position + 1) + "");
            }

            if (randomNum_bule != null && randomNum_bule.length != 0) {
                if ((position + 1) == randomNum_bule[0]) {
                    ballNumTextView.setTextColor(getResources().getColor(R.color.app_white_color_ffffff));
                    ballNumTextView.setBackgroundResource(R.drawable.bule_ball_bg);
                }
            }
            return ballView;
        }
    }
}
