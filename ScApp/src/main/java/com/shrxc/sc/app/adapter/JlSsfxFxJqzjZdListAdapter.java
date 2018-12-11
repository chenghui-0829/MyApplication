package com.shrxc.sc.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.JzssfxZjEntity;

import java.util.List;

/**
 * Created by CH on 2018/9/21.
 */

public class JlSsfxFxJqzjZdListAdapter extends BaseAdapter {

    private Context mContext;
    private int count = 0;
    private List<JzssfxZjEntity> list;

    public JlSsfxFxJqzjZdListAdapter(Context context, List<JzssfxZjEntity> list) {
        this.mContext = context;
        this.list = list;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder vh = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.jl_ssfx_fx_zj_list_adapter_layout, viewGroup, false);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        vh.ssNameTextView.setText(list.get(i).getSsName());
        vh.timeTextView.setText(list.get(i).getGameTime());
        vh.zdNameTextView.setText(list.get(i).getZdName());
        vh.kdNameTextView.setText(list.get(i).getKdName());
        String result = list.get(i).getResult();
        if (result.equals("win")) {
            vh.resultTextView.setText("胜");
        } else if (result.equals("lose")) {
            vh.resultTextView.setText("负");
        } else {
            vh.resultTextView.setText("-");
        }
        vh.bfTextView.setText((list.get(i).getBf().equals("") ? "-" : list.get(i).getBf()));
        return view;
    }

    private class ViewHolder {

        private TextView ssNameTextView, timeTextView, zdNameTextView, kdNameTextView, bfTextView, resultTextView;

        public ViewHolder(View view) {

            ssNameTextView = view.findViewById(R.id.jl_ssfx_fx_list_ssname_text);
            timeTextView = view.findViewById(R.id.jl_ssfx_fx_list_time_text);
            zdNameTextView = view.findViewById(R.id.jl_ssfx_fx_list_zdname_text);
            kdNameTextView = view.findViewById(R.id.jl_ssfx_fx_list_kdname_text);
            bfTextView = view.findViewById(R.id.jl_ssfx_fx_list_bf_text);
            resultTextView = view.findViewById(R.id.jl_ssfx_fx_list_result_text);
        }
    }
}
