package com.shrxc.sc.app.dntz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JlsxActivity extends AppCompatActivity {

    private Context context = JlsxActivity.this;
    @BindView(R.id.jlsx_activity_all_text)
    TextView allTextView;
    @BindView(R.id.jlsx_activity_fx_text)
    TextView fxTextView;
    @BindView(R.id.jlsx_activity_back_icon)
    ImageView ackIcon;
    @BindView(R.id.jl_activity_ss_grid)
    GridView gridView;
    private List<String> ssTypeList;
    private int selected = 0;
    private Map<Integer, Integer> stateMap;
    private GridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jlsx);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ssTypeList = (List<String>) bundle.getSerializable("sstype");
        stateMap = new HashMap<>();
        stateMap = JclqActivity.stateMap;
        adapter = new GridAdapter();
        gridView.setAdapter(adapter);
    }

    @OnClick({R.id.jlsx_activity_all_text, R.id.jlsx_activity_fx_text, R.id.jlsx_activity_sure_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jlsx_activity_all_text:
                if (selected != 0) {
                    allTextView.setTextColor(getResources().getColor(R.color.app_white_color_ffffff));
                    allTextView.setBackgroundResource(R.drawable.left_red_circle_bg);
                    fxTextView.setTextColor(getResources().getColor(R.color.app_text_color_333333));
                    fxTextView.setBackgroundResource(R.color.app_transparent_color);
                    selected = 0;
                }
                for (int i = 0; i < ssTypeList.size(); i++) {
                    stateMap.put(i, 1);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.jlsx_activity_fx_text:
                if (selected != 1) {
                    fxTextView.setTextColor(getResources().getColor(R.color.app_white_color_ffffff));
                    fxTextView.setBackgroundResource(R.color.app_red_color_fa3243);
                    allTextView.setTextColor(getResources().getColor(R.color.app_text_color_333333));
                    allTextView.setBackgroundResource(R.color.app_transparent_color);
                    selected = 1;
                }

                for (Integer key : stateMap.keySet()) {
                    if (stateMap.get(key) == 0) {

                        stateMap.put(key, 1);
                    } else {
                        stateMap.put(key, 0);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.jlsx_activity_sure_text:
                boolean isNoResult = true;
                for (Integer key : stateMap.keySet()) {
                    if (stateMap.get(key) == 1) {
                        isNoResult = false;
                        break;
                    }
                }
                if (isNoResult) {
                    Toast.makeText(context, "筛选结果为0条数据，请重新筛选", Toast.LENGTH_SHORT).show();
                    return;
                }
                JczqActivity.stateMap = stateMap;
                List<String> select = new ArrayList<>();
                for (Integer key : stateMap.keySet()) {
                    if (stateMap.get(key) == 1) {
                        select.add(ssTypeList.get(key));
                    }
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("select", (Serializable) select);
                intent.putExtras(bundle);
                setResult(0x888, intent);
                finish();
                break;
        }
    }

    private class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ssTypeList == null ? 0 : ssTypeList.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = LayoutInflater.from(context).inflate(R.layout.jzsx_acticity_grid_adapter_layout, viewGroup, false);

            TextView textView = view.findViewById(R.id.jzsx_activity_adapter_ss_text);
            textView.setText(ssTypeList.get(i));

            if (stateMap.get(i) == 1) {
                textView.setTextColor(getResources().getColor(R.color.app_red_color_fa3243));
                textView.setBackgroundResource(R.drawable.red_circle_stroke_bg);
            } else {
                textView.setTextColor(getResources().getColor(R.color.app_text_color_333333));
                textView.setBackgroundResource(R.drawable.app_whilte_circle_color);
            }

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (stateMap.get(i) == 1) {
                        stateMap.put(i, 0);
                    } else {
                        stateMap.put(i, 1);
                    }
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }
}
