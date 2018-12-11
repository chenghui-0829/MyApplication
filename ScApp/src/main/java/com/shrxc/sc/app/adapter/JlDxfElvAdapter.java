package com.shrxc.sc.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.JlChildEntity;
import com.shrxc.sc.app.bean.JlGroupEntity;
import com.shrxc.sc.app.dntz.JclqActivity;
import com.shrxc.sc.app.dntz.JlSsfxActivity;
import com.shrxc.sc.app.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CH on 2018/8/17.
 */

public class JlDxfElvAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<Integer, Map<Integer, Map<Integer, Integer>>> childs;
    private List<JlGroupEntity> gameList;

    public JlDxfElvAdapter(Context context, List<JlGroupEntity> gameList) {
        this.context = context;
        this.gameList = gameList;
        int childCount = 0;
        for (int i = 0; i < gameList.size(); i++) {
            childCount += gameList.get(i).getList().size();
        }
        childs = JlAdapterUtil.initChilds(gameList.size(), childCount, 3);
    }

    @Override
    public int getGroupCount() {
        return gameList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return gameList.get(i).getList().size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        GroupViewHolder gvh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.jz_elv_group_layout, viewGroup, false);
            gvh = new GroupViewHolder(view);
            view.setTag(gvh);
        } else {
            gvh = (GroupViewHolder) view.getTag();
        }

        if (b) {
            gvh.arrowImage.setImageResource(R.mipmap.app_up_gray_arow_icon);
        } else {
            gvh.arrowImage.setImageResource(R.mipmap.app_down_gray_arow_icon);
        }
        gvh.nyrText.setText(gameList.get(i).getTime());
        gvh.xqText.setText(AppUtil.dateToWeek(gameList.get(i).getTime()));
        gvh.csText.setText(gameList.get(i).getList().size() + "场比赛");
        return view;
    }

    @Override
    public View getChildView(final int gPos, final int cPos, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder cvh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.jl_dxf_elv_child_layout, viewGroup, false);
            cvh = new ChildViewHolder(view);
            view.setTag(cvh);
        } else {
            cvh = (ChildViewHolder) view.getTag();
        }

        for (Integer key : childs.get(gPos).get(cPos).keySet()) {
            if (key == 0) {
                if (childs.get(gPos).get(cPos).get(key) == 0) {
                    cvh.fxDetailLayout.setVisibility(View.GONE);
                    cvh.fxImageView.setImageResource(R.mipmap.app_down_gray_arow_icon);
                } else {
                    cvh.fxDetailLayout.setVisibility(View.VISIBLE);
                    cvh.fxImageView.setImageResource(R.mipmap.app_up_gray_arow_icon);
                }
            } else {
                JlAdapterUtil.initViewState(context, childs.get(gPos).get(cPos).get(key),
                        (LinearLayout) cvh.selectLayout.getChildAt(key - 1), key - 1, 2);
            }
        }

        //重置选中的内容
        gameList.get(gPos).getList().get(cPos).setSelected(null);
        cvh.xqTextView.setText(gameList.get(gPos).getList().get(cPos).getGameid());
        cvh.typeTextView.setText(gameList.get(gPos).getList().get(cPos).getLeague());
        cvh.zdNameTextView.setText(gameList.get(gPos).getList().get(cPos).getHometeam());
        cvh.dfPlTextView.setText(gameList.get(gPos).getList().get(cPos).getMaxpl());
        cvh.xfPlTextView.setText(gameList.get(gPos).getList().get(cPos).getMinpl());
        cvh.dfTextView.setText("大于" + gameList.get(gPos).getList().get(cPos).getZongfen());
        cvh.kdNameTextView.setText(gameList.get(gPos).getList().get(cPos).getGuestteam());
        cvh.xfTextView.setText("小于" + gameList.get(gPos).getList().get(cPos).getZongfen());
        String endTime = AppUtil.formatString(gameList.get(gPos).getList().get(cPos).getEndtime().
                replace("T", " "), "HH:mm");
        cvh.timeTextView.setText(endTime + "截止");
        /**
         * 分析
         */
        cvh.fxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JlAdapterUtil.changeViewSet(context, JlDxfElvAdapter.this, gPos, cPos, 0, childs, null);
            }
        });
        cvh.selectLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JlAdapterUtil.changeViewSet(context, JlDxfElvAdapter.this, gPos, cPos, 1, childs, gameList);
            }
        });
        cvh.selectLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JlAdapterUtil.changeViewSet(context, JlDxfElvAdapter.this, gPos, cPos, 2, childs, gameList);
            }
        });
        /***
         * 详细赛事分析
         */
        cvh.ssfxTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, JlSsfxActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("game", gameList.get(gPos).getList().get(cPos));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        List<JlChildEntity> list = JlAdapterUtil.getSelectList(childs, gameList);
        if (list.size() == 0) {
            JclqActivity.selectNumTextView.setText("请至少选择2场比赛");
        } else if (list.size() == 1) {
            JclqActivity.selectNumTextView.setText("已选1场,还差1场");
        } else if (list.size() <= 8) {
            JclqActivity.selectNumTextView.setText(Html.fromHtml("已选" + "<font color='#fa3243'>" + list.size() + "</font>" + "场"));
        }

        if (list.size() > 0) {
            List<TextView> ggfsList = new ArrayList<>();
            LinearLayout layout1 = (LinearLayout) JclqActivity.ggfsLayout.getChildAt(0);
            for (int i = 0; i < layout1.getChildCount(); i++) {
                ggfsList.add((TextView) layout1.getChildAt(i));
            }
            LinearLayout layout2 = (LinearLayout) JclqActivity.ggfsLayout.getChildAt(1);
            for (int i = 0; i < layout2.getChildCount(); i++) {
                ggfsList.add((TextView) layout2.getChildAt(i));
            }

            for (int i = 0; i < ggfsList.size(); i++) {
                if (i < list.size()) {
                    if (i > 0) {
                        ggfsList.get(i).setEnabled(true);
                        if (JclqActivity.ggfsMap.get(i) != 1) {
                            ggfsList.get(i).setBackgroundResource(R.drawable.app_gray_stroke_circle_color);
                        }
                    }
                } else {
                    JclqActivity.ggfsMap.put(i, 0);
                    ggfsList.get(i).setEnabled(false);
                    ggfsList.get(i).setBackgroundResource(R.drawable.app_gray_color_e6e6e6);
                }
            }
        }
    }

    /**
     * 获取选中的投注内容
     */
    public List<JlChildEntity> getSelectList() {
        return JlAdapterUtil.getSelectList(childs, gameList);
    }

    private class GroupViewHolder {

        private ImageView arrowImage;
        private TextView nyrText, xqText, csText;

        public GroupViewHolder(View view) {
            arrowImage = view.findViewById(R.id.jz_group_arrow_icon);
            nyrText = view.findViewById(R.id.jz_elv_group_nyr_text);
            xqText = view.findViewById(R.id.jz_elv_group_xq_text);
            csText = view.findViewById(R.id.jz_elv_group_cs_text);
        }
    }

    private class ChildViewHolder {

        private ImageView fxImageView;
        private GridLayout selectLayout;
        private LinearLayout fxLayout, fxDetailLayout;
        private TextView xqTextView, typeTextView, timeTextView, zdNameTextView, dfPlTextView, ssfxTextView,
                kdNameTextView, xfPlTextView, dfTextView, xfTextView;


        public ChildViewHolder(View view) {
            fxLayout = view.findViewById(R.id.jl_dxf_elv_child_fx_layout);
            fxDetailLayout = view.findViewById(R.id.jl_dxf_elv_child_fx_detail_layout);
            fxImageView = view.findViewById(R.id.jl_dxf_elv_child_fx_icon);
            selectLayout = view.findViewById(R.id.jl_dxf_elv_select_layout);
            xqTextView = view.findViewById(R.id.jl_dxf_elv_xq_text);
            typeTextView = view.findViewById(R.id.jl_dxf_elv_type_text);
            timeTextView = view.findViewById(R.id.jl_dxf_elv_time_text);
            zdNameTextView = view.findViewById(R.id.jl_dxf_elv_zd_name_text);
            dfPlTextView = view.findViewById(R.id.jl_dxf_elv_df_pl_text);
            kdNameTextView = view.findViewById(R.id.jl_dxf_elv_kd_name_text);
            xfPlTextView = view.findViewById(R.id.jl_dxf_elv_xf_pl_text);
            dfTextView = view.findViewById(R.id.jl_dxf_elv_df_text);
            xfTextView = view.findViewById(R.id.jl_dxf_elv_xf_text);
            ssfxTextView = view.findViewById(R.id.jl_dxf_child_ssfx_text);
        }
    }
}