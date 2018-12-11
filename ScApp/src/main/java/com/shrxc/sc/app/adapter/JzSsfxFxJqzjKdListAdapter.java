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

public class JzSsfxFxJqzjKdListAdapter extends BaseAdapter {

    private Context mContext;
    private int count = 0;
    private List<JzssfxZjEntity> list;

    public JzSsfxFxJqzjKdListAdapter(Context context, List<JzssfxZjEntity> list) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.jz_ssfx_fx_jqzj_zd_list_adapter_layout, viewGroup, false);
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
        } else if (result.equals("draw")) {
            vh.resultTextView.setText("平");
        } else {
            vh.resultTextView.setText("-");
        }
        vh.bfTextView.setText((list.get(i).getBf().equals("") ? "-" : list.get(i).getBf()) + "(" +
                (list.get(i).getBcbf().equals("") ? "-" : list.get(i).getBcbf()) + ")");
        return view;
    }

    private class ViewHolder {

        private TextView ssNameTextView, timeTextView, zdNameTextView, kdNameTextView, bfTextView, resultTextView;

        public ViewHolder(View view) {
            ssNameTextView = view.findViewById(R.id.jz_ssfx_fx_jqzj_list_ssname_text);
            timeTextView = view.findViewById(R.id.jz_ssfx_fx_jqzj_list_time_text);
            zdNameTextView = view.findViewById(R.id.jz_ssfx_fx_jqzj_list_zdname_text);
            kdNameTextView = view.findViewById(R.id.jz_ssfx_fx_jqzj_list_kdname_text);
            bfTextView = view.findViewById(R.id.jz_ssfx_fx_jqzj_list_bf_text);
            resultTextView = view.findViewById(R.id.jz_ssfx_fx_jqzj_list_result_text);
        }
    }

}
