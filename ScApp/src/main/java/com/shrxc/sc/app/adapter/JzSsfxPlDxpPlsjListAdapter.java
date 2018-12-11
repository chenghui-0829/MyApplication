package com.shrxc.sc.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shrxc.sc.app.R;

/**
 * Created by CH on 2018/9/21.
 */

public class JzSsfxPlDxpPlsjListAdapter extends BaseAdapter {

    private Context mContext;

    public JzSsfxPlDxpPlsjListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 15;
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

        view = LayoutInflater.from(mContext).inflate(R.layout.jz_ssfx_activity_pl_dxp_plsj_list_adapter_layout, viewGroup, false);
        return view;
    }
}
