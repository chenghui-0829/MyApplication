package com.shrxc.sc.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.JzssfxOpEntity;

import java.util.List;

/**
 * Created by CH on 2018/9/21.
 */

public class JzSsfxPlOpKlzsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<JzssfxOpEntity> list;

    public JzSsfxPlOpKlzsListAdapter(List<JzssfxOpEntity> dataList, Context context) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.jz_ssfx_activity_pl_op_klzs_list_adapter_layout, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.nameTextView.setText(list.get(i).getCn());
        holder.s_1_text.setText(list.get(i).getWin_ratio());
        holder.p_1_text.setText(list.get(i).getDraw_ratio());
        holder.f_1_text.setText(list.get(i).getLose_ratio());
        holder.s_2_text.setText(list.get(i).getWin_index());
        holder.p_2_text.setText(list.get(i).getDraw_index());
        holder.f_2_text.setText(list.get(i).getLose_index());
        holder.fhl_text.setText(list.get(i).getPer());
        return view;
    }

    private class ViewHolder {

        private TextView nameTextView, s_1_text, p_1_text, f_1_text, s_2_text, p_2_text, f_2_text, fhl_text;

        public ViewHolder(View v) {
            nameTextView = v.findViewById(R.id.jz_ssfx_activity_pl_op_klzs_list_name_text);
            s_1_text = v.findViewById(R.id.jz_ssfx_activity_pl_op_klzs_list_s_text);
            p_1_text = v.findViewById(R.id.jz_ssfx_activity_pl_op_klzs_list_p_text);
            f_1_text = v.findViewById(R.id.jz_ssfx_activity_pl_op_klzs_list_f_text);
            s_2_text = v.findViewById(R.id.jz_ssfx_activity_pl_op_klzs_list_s_1_text);
            p_2_text = v.findViewById(R.id.jz_ssfx_activity_pl_op_klzs_list_p_1_text);
            f_2_text = v.findViewById(R.id.jz_ssfx_activity_pl_op_klzs_list_f_1_text);
            fhl_text = v.findViewById(R.id.jz_ssfx_activity_pl_op_klzs_list_fhl_text);
        }
    }
}
