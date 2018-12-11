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

public class JzSsfxPlOpPlsjListAdapter extends BaseAdapter {

    private Context mContext;
    private List<JzssfxOpEntity> list;

    public JzSsfxPlOpPlsjListAdapter(List<JzssfxOpEntity> dataList, Context context) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.jz_ssfx_activity_pl_op_plsj_list_adapter_layout, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.nameTextView.setText(list.get(i).getCn());
        holder.plsj_s_1_text.setText(list.get(i).getWin_s());
        holder.plsj_p_1_text.setText(list.get(i).getDraw_s());
        holder.plsj_f_1_text.setText(list.get(i).getLose_s());

        String jspkC1 = list.get(i).getWin_change();
        if (jspkC1.equals("down")) {
            holder.plsj_s_2_text.setText(list.get(i).getWin() + "↓");
        } else if (jspkC1.equals("up")) {
            holder.plsj_s_2_text.setText(list.get(i).getWin() + "↑");
        } else {
            holder.plsj_s_2_text.setText(list.get(i).getWin());
        }
        String jspkC2 = list.get(i).getDraw_change();
        if (jspkC2.equals("down")) {
            holder.plsj_p_2_text.setText(list.get(i).getDraw() + "↓");
        } else if (jspkC2.equals("up")) {
            holder.plsj_p_2_text.setText(list.get(i).getDraw() + "↑");
        } else {
            holder.plsj_p_2_text.setText(list.get(i).getDraw());
        }
        String jspkC3 = list.get(i).getLose_change();
        if (jspkC3.equals("down")) {
            holder.plsj_f_2_text.setText(list.get(i).getLose() + "↓");
        } else if (jspkC3.equals("up")) {
            holder.plsj_f_2_text.setText(list.get(i).getLose() + "↑");
        } else {
            holder.plsj_f_2_text.setText(list.get(i).getLose());
        }

        return view;
    }

    private class ViewHolder {

        private TextView nameTextView, plsj_s_1_text, plsj_p_1_text, plsj_f_1_text, plsj_s_2_text, plsj_p_2_text, plsj_f_2_text;

        public ViewHolder(View v) {
            nameTextView = v.findViewById(R.id.jz_ssfx_activity_pl_op_plsj_list_name_text);
            plsj_s_1_text = v.findViewById(R.id.jz_ssfx_activity_pl_op_plsj_list_s_text);
            plsj_p_1_text = v.findViewById(R.id.jz_ssfx_activity_pl_op_plsj_list_p_text);
            plsj_f_1_text = v.findViewById(R.id.jz_ssfx_activity_pl_op_plsj_list_f_text);
            plsj_s_2_text = v.findViewById(R.id.jz_ssfx_activity_pl_op_plsj_list_js_s_text);
            plsj_p_2_text = v.findViewById(R.id.jz_ssfx_activity_pl_op_plsj_list_js_p_text);
            plsj_f_2_text = v.findViewById(R.id.jz_ssfx_activity_pl_op_plsj_list_js_f_text);
        }
    }
}
