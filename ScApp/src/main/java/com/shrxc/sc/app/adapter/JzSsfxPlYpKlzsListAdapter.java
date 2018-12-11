package com.shrxc.sc.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.JzssfxYpEntity;

import java.util.List;

/**
 * Created by CH on 2018/9/21.
 */

public class JzSsfxPlYpKlzsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<JzssfxYpEntity> list;

    public JzSsfxPlYpKlzsListAdapter(List<JzssfxYpEntity> dataList, Context context) {
        this.mContext = context;
        this.list = dataList;
    }

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
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.jz_ssfx_activity_pl_yp_klzs_list_adapter_layout, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.nameTextView.setText(list.get(i).getCn());
        holder.klzs_1_text.setText(list.get(i).getO1_ratio() + "%");
        holder.klzs_2_text.setText(list.get(i).getO2_ratio() + "%");
        holder.klzs_3_text.setText(list.get(i).getO1_index());
        holder.klzs_4_text.setText(list.get(i).getO2_index());
        return view;
    }

    private class ViewHolder {

        private TextView nameTextView, klzs_1_text, klzs_2_text, klzs_3_text, klzs_4_text;

        public ViewHolder(View v) {
            nameTextView = v.findViewById(R.id.jz_ssfx_activity_pl_yp_klzs_list_name_text);
            klzs_1_text = v.findViewById(R.id.jz_ssfx_activity_pl_yp_klzs_list_1_text);
            klzs_2_text = v.findViewById(R.id.jz_ssfx_activity_pl_yp_klzs_list_2_text);
            klzs_3_text = v.findViewById(R.id.jz_ssfx_activity_pl_yp_klzs_list_3_text);
            klzs_4_text = v.findViewById(R.id.jz_ssfx_activity_pl_yp_klzs_list_4_text);
        }
    }
}
