package com.shrxc.sc.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.shrxc.sc.app.R;

import java.util.List;

/**
 * Created by CH on 2018/10/11.
 */

public class PoiListAdapter extends BaseAdapter {
    private Context context;
    private List<PoiInfo> pois;

    public PoiListAdapter(Context context, List<PoiInfo> pois) {
        this.context = context;
        this.pois = pois;
    }

    @Override
    public int getCount() {
        return pois.size();
    }

    @Override
    public Object getItem(int position) {
        return pois.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.get_address_activity_adapter_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PoiInfo poiInfo = pois.get(position);
        if (position == 0) {
            holder.iconImage.setVisibility(View.VISIBLE);
            holder.nameTextview.setTextColor(context.getResources().getColor(R.color.app_red_color_fa3243));
            holder.addressTextview.setTextColor(context.getResources().getColor(R.color.app_red_color_fa3243));
            holder.nameTextview.setText(poiInfo.getName());
            holder.addressTextview.setText(poiInfo.getAddress());
        } else {
            holder.iconImage.setVisibility(View.INVISIBLE);
            holder.nameTextview.setTextColor(context.getResources().getColor(R.color.app_text_color_333333));
            holder.addressTextview.setTextColor(context.getResources().getColor(R.color.app_text_color_999999));
            holder.nameTextview.setText(poiInfo.getName());
            holder.addressTextview.setText(poiInfo.getAddress());
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView iconImage;
        private TextView nameTextview;
        private TextView addressTextview;

        public ViewHolder(View view) {
            nameTextview = view.findViewById(R.id.get_address_activity_adapter_name_text);
            addressTextview = view.findViewById(R.id.get_address_activity_adapter_address_text);
            iconImage = view.findViewById(R.id.get_address_activity_adapter_icon);
        }
    }
}