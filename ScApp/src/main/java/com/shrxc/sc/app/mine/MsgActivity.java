package com.shrxc.sc.app.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.LoginActivity;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.adapter.MsgEntity;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.AppUtil;
import com.shrxc.sc.app.utils.SystemBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MsgActivity extends AppCompatActivity {

    @BindView(R.id.msg_activity_list)
    ListView msgListView;
    private Context context = MsgActivity.this;
    private String url = "User/GetNotifyList";
    private ListAdapter adapter;
    private List<MsgEntity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        adapter = new ListAdapter();
        msgListView.setAdapter(adapter);
        initEvent();
    }

    private void initEvent() {

        msgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                changeMsgState(position);
                Intent intent = new Intent(context, MsgDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("msg", list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void changeMsgState(int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("Id", list.get(position).getId());
        HttpRequestUtil.getInstance(context).get("User/UpdateNotify", params, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {

        list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("state", "");
        params.put("page", "1");
        HttpRequestUtil.getInstance(context).get(url, params, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                if ("1".equals(state)) {
                    JSONArray jsonArray = result.getJSONArray("Data");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        MsgEntity entity = JSONObject.toJavaObject(object, MsgEntity.class);
                        list.add(entity);
                    }
                    adapter.notifyDataSetChanged();
                } else if ("-1".equals(state)) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
                System.out.println("----------erro--------" + erro);
            }
        });
    }

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
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
        public View getView(int arg0, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(
                        R.layout.msg_activity_list_adapter_layout, viewGroup, false);
                holder = new ViewHolder();
                holder.titleTextView = view
                        .findViewById(R.id.msg_adapter_title_text);
                holder.timeTextView = view
                        .findViewById(R.id.msg_adapter_time_text);
                holder.conTextView = view
                        .findViewById(R.id.msg_adapter_content_text);
                holder.stateView = view
                        .findViewById(R.id.msg_adapter_state_view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            String time = AppUtil.formatString(list.get(arg0).getCreatetime().
                    replace("T", " "), "yyyy-MM-dd HH:mm:ss");
            holder.titleTextView.setText(list.get(arg0).getTitle());
            holder.timeTextView.setText(time);
            holder.conTextView.setText(list.get(arg0).getContent().trim());
            if (list.get(arg0).getStatu().equals("0")) {
                holder.stateView.setVisibility(View.VISIBLE);
            } else {
                holder.stateView.setVisibility(View.GONE);
            }
            return view;
        }

        private class ViewHolder {

            private TextView titleTextView, timeTextView, conTextView;
            private View stateView;

        }
    }

}
