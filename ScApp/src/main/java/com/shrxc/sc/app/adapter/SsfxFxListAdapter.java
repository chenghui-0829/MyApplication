package com.shrxc.sc.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shrxc.sc.app.R;

/**
 * Created by CH on 2018/8/30.
 */

public class SsfxFxListAdapter extends BaseAdapter {

    private Context mContext;


    public SsfxFxListAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return 6;
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

        View v = LayoutInflater.from(mContext).inflate(R.layout.ssfx_fx_list_adapter_layout,
                viewGroup, false);
        return v;
    }

}
