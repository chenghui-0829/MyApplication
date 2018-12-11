package com.shrxc.sc.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.utils.AppUtil;

import java.util.List;

/**
 * Created by CH on 2018/10/11.
 */

public class AddressSearchListAdapter extends BaseAdapter {
    private Context context;
    private List<SuggestionResult.SuggestionInfo> suggestionInfos;
    private LatLng mLatLng;

    public AddressSearchListAdapter(Context context, List<SuggestionResult.SuggestionInfo> suggestionInfos, LatLng latLng) {
        this.context = context;
        this.suggestionInfos = suggestionInfos;
        this.mLatLng = latLng;
    }

    @Override
    public int getCount() {
        return suggestionInfos == null ? 0 : suggestionInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return suggestionInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.get_address_activity_search_adapter_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SuggestionResult.SuggestionInfo suggestionInfo = suggestionInfos.get(position);
        String jl = AppUtil.sswrNum(DistanceUtil.getDistance(mLatLng, suggestionInfo.pt) / 1000, 2);
        holder.nameTextview.setText(suggestionInfo.key);
        holder.addressTextview.setText(suggestionInfo.district);
        holder.jlTextView.setText(jl + "km");
        return convertView;
    }

    private class ViewHolder {
        private TextView jlTextView;
        private TextView nameTextview;
        private TextView addressTextview;

        public ViewHolder(View view) {
            nameTextview = view.findViewById(R.id.get_address_activity_search_adapter_name_text);
            addressTextview = view.findViewById(R.id.get_address_activity_search_adapter_address_text);
            jlTextView = view.findViewById(R.id.get_address_activity_search_adapter_jl_text);
        }
    }
}