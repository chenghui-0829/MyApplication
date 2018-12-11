package com.shrxc.sc.app.fwdt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dlk3Activity extends AppCompatActivity {

    private Context context = Dlk3Activity.this;
    @BindView(R.id.dlk3_activity_hz_num_grid)
    GridView hzGridView;
    @BindViews({R.id.dlk3_activity_k3_hz_layout, R.id.dlk3_activity_k3_3th_layout, R.id.dlk3_activity_k3_2th_layout,
            R.id.dlk3_activity_k3_3lh_layout, R.id.dlk3_activity_k3_2thdx_layout, R.id.dlk3_activity_k3_2bth_layout,
            R.id.dlk3_activity_k3_3bth_layout})
    List<View> layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlk3);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);

        initView();

        hzGridView.setAdapter(new HzGridAdapter());

    }

    private void initView() {
    }


    @OnClick({R.id.dlk3_activity_k3_hz_select, R.id.dlk3_activity_k3_3th_select, R.id.dlk3_activity_k3_2th_select,
            R.id.dlk3_activity_k3_3lh_select, R.id.dlk3_activity_k3_2thdx_select, R.id.dlk3_activity_k3_2bth_select,
            R.id.dlk3_activity_k3_3bth_select, R.id.dlk3_activity_sure_text})
    public void onClickEvent(View v) {

        switch (v.getId()) {

            case R.id.dlk3_activity_k3_hz_select:
                showLayout(0);
                break;
            case R.id.dlk3_activity_k3_3th_select:
                showLayout(1);
                break;
            case R.id.dlk3_activity_k3_2th_select:
                showLayout(2);
                break;
            case R.id.dlk3_activity_k3_3lh_select:
                showLayout(3);
                break;
            case R.id.dlk3_activity_k3_2thdx_select:
                showLayout(4);
                break;
            case R.id.dlk3_activity_k3_2bth_select:
                showLayout(5);
                break;
            case R.id.dlk3_activity_k3_3bth_select:
                showLayout(6);
                break;
            case R.id.dlk3_activity_sure_text:

                startActivity(new Intent(context, K3orderActivity.class));

                break;


        }


    }


    private void showLayout(int selected) {


        System.out.println("------------->" + selected);

        for (int i = 0; i < layouts.size(); i++) {
            if (i == selected) {
                layouts.get(selected).setVisibility(View.VISIBLE);
            } else {
                layouts.get(i).setVisibility(View.GONE);
            }
        }
    }


    private class HzGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 14;
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
        public View getView(int i, View arg1, ViewGroup viewGroup) {

            View view = LayoutInflater.from(context).inflate(R.layout.dlk3_hz_num_list_adapter_layout,
                    viewGroup, false);

            return view;
        }
    }

}
