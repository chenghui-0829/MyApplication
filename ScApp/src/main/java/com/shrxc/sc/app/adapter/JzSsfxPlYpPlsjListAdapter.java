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

public class JzSsfxPlYpPlsjListAdapter extends BaseAdapter {

    private Context mContext;
    private List<JzssfxYpEntity> list;

    public JzSsfxPlYpPlsjListAdapter(List<JzssfxYpEntity> dataList, Context context) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.jz_ssfx_activity_pl_yp_plsj_list_adapter_layout, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.nameTextView.setText(list.get(i).getCn());
        holder.cspk_1_text.setText(list.get(i).getO1_s());
        holder.cspk_2_text.setText(list.get(i).getO3_s().replaceAll("受让", ""));
        holder.cspk_3_text.setText(list.get(i).getO2_s());

        String jspkC1 = list.get(i).getO1_change();
        if (jspkC1.equals("down")) {
            holder.jspk_1_text.setText(list.get(i).getO1() + "↓");
        } else if (jspkC1.equals("up")) {
            holder.jspk_1_text.setText(list.get(i).getO1() + "↑");
        } else {
            holder.jspk_1_text.setText(list.get(i).getO1());
        }

        String jspkC3 = list.get(i).getO2_change();
        if (jspkC3.equals("down")) {
            holder.jspk_3_text.setText(list.get(i).getO2() + "↓");
        } else if (jspkC3.equals("up")) {
            holder.jspk_3_text.setText(list.get(i).getO2() + "↑");
        } else {
            holder.jspk_3_text.setText(list.get(i).getO2());
        }

        holder.jspk_2_text.setText(list.get(i).getO3().replaceAll("受让", ""));
        return view;
    }

    private class ViewHolder {

        private TextView nameTextView, cspk_1_text, cspk_2_text, cspk_3_text, jspk_1_text, jspk_2_text, jspk_3_text;

        public ViewHolder(View v) {
            nameTextView = v.findViewById(R.id.jz_ssfx_activity_pl_yp_plsj_list_name_text);
            cspk_1_text = v.findViewById(R.id.jz_ssfx_activity_pl_yp_plsj_list_cspk_1_text);
            cspk_2_text = v.findViewById(R.id.jz_ssfx_activity_pl_yp_plsj_list_cspk_2_text);
            cspk_3_text = v.findViewById(R.id.jz_ssfx_activity_pl_yp_plsj_list_cspk_3_text);
            jspk_1_text = v.findViewById(R.id.jz_ssfx_activity_pl_yp_plsj_list_jspk_1_text);
            jspk_2_text = v.findViewById(R.id.jz_ssfx_activity_pl_yp_plsj_list_jspk_2_text);
            jspk_3_text = v.findViewById(R.id.jz_ssfx_activity_pl_yp_plsj_list_jspk_3_text);
        }
    }
}
