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
import com.shrxc.sc.app.bean.JzChildGameEntity;
import com.shrxc.sc.app.bean.JzGroupGameEntity;
import com.shrxc.sc.app.bean.JzTzTypeEntity;
import com.shrxc.sc.app.dntz.JczqActivity;
import com.shrxc.sc.app.dntz.JzSsfxActivity;
import com.shrxc.sc.app.utils.AppUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by CH on 2018/8/17.
 */

public class JzJqsElvAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<Integer, Map<Integer, Map<Integer, Integer>>> childs;
    private List<JzGroupGameEntity> games;

    public JzJqsElvAdapter(Context context, List<JzGroupGameEntity> games) {
        this.context = context;
        this.games = games;
        int childCount = 0;
        for (int i = 0; i < games.size(); i++) {
            childCount += games.get(i).getGameNum();
        }
        childs = JzAdapterUtil.initChilds(games == null ? 0 : games.size(), childCount, 9);
    }

    @Override
    public int getGroupCount() {
        return games == null ? 0 : games.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return games.get(i).getGames() == null ? 0 : games.get(i).getGames().size();
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

        gvh.nyrText.setText(games.get(i).getTime());
        gvh.xqText.setText(AppUtil.dateToWeek(games.get(i).getTime()));
        gvh.csText.setText(games.get(i).getGameNum() + "场比赛");
        return view;
    }

    @Override
    public View getChildView(final int gPos, final int cPos, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder cvh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.jz_jqs_elv_child_layout, viewGroup, false);
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
                JzAdapterUtil.initViewState(context, childs.get(gPos).get(cPos).get(key),
                        (LinearLayout) cvh.jqsGridLayout.getChildAt(key - 1), key - 1, 1);
            }
        }
        //重置选中的内容
        games.get(gPos).getGames().get(cPos).setSelectedList(null);
        cvh.xqTextView.setText(games.get(gPos).getGames().get(cPos).getGameid());
        cvh.typeTextView.setText(games.get(gPos).getGames().get(cPos).getLeague());
        cvh.zdNameTextView.setText(games.get(gPos).getGames().get(cPos).getHometeam());
        cvh.kdNameTextView.setText(games.get(gPos).getGames().get(cPos).getGuestteam());
        String endTime = AppUtil.formatString(games.get(gPos).getGames().get(cPos).getEndtime().replace("T", " "), "HH:mm");
        cvh.jzrqTextView.setText(endTime + "截止");

        JzTzTypeEntity entity = games.get(gPos).getGames().get(cPos).getTzTypeEntity();
        ((TextView) ((LinearLayout) cvh.jqsGridLayout.getChildAt(0)).getChildAt(1)).setText(entity.getJqs_0());
        ((TextView) ((LinearLayout) cvh.jqsGridLayout.getChildAt(1)).getChildAt(1)).setText(entity.getJqs_1());
        ((TextView) ((LinearLayout) cvh.jqsGridLayout.getChildAt(2)).getChildAt(1)).setText(entity.getJqs_2());
        ((TextView) ((LinearLayout) cvh.jqsGridLayout.getChildAt(3)).getChildAt(1)).setText(entity.getJqs_3());
        ((TextView) ((LinearLayout) cvh.jqsGridLayout.getChildAt(4)).getChildAt(1)).setText(entity.getJqs_4());
        ((TextView) ((LinearLayout) cvh.jqsGridLayout.getChildAt(5)).getChildAt(1)).setText(entity.getJqs_5());
        ((TextView) ((LinearLayout) cvh.jqsGridLayout.getChildAt(6)).getChildAt(1)).setText(entity.getJqs_6());
        ((TextView) ((LinearLayout) cvh.jqsGridLayout.getChildAt(7)).getChildAt(1)).setText(entity.getJqs_7());
        cvh.fxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JzAdapterUtil.changeViewSet(context, JzJqsElvAdapter.this, gPos, cPos, 0, childs, null);
            }
        });

        cvh.jqsGridLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JzAdapterUtil.changeViewSet(context, JzJqsElvAdapter.this, gPos, cPos, 1, childs, null);
            }
        });
        cvh.jqsGridLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JzAdapterUtil.changeViewSet(context, JzJqsElvAdapter.this, gPos, cPos, 2, childs, null);
            }
        });
        cvh.jqsGridLayout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JzAdapterUtil.changeViewSet(context, JzJqsElvAdapter.this, gPos, cPos, 3, childs, null);
            }
        });
        cvh.jqsGridLayout.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JzAdapterUtil.changeViewSet(context, JzJqsElvAdapter.this, gPos, cPos, 4, childs, null);
            }
        });
        cvh.jqsGridLayout.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JzAdapterUtil.changeViewSet(context, JzJqsElvAdapter.this, gPos, cPos, 5, childs, null);
            }
        });
        cvh.jqsGridLayout.getChildAt(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JzAdapterUtil.changeViewSet(context, JzJqsElvAdapter.this, gPos, cPos, 6, childs, null);
            }
        });
        cvh.jqsGridLayout.getChildAt(6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JzAdapterUtil.changeViewSet(context, JzJqsElvAdapter.this, gPos, cPos, 7, childs, null);
            }
        });
        cvh.jqsGridLayout.getChildAt(7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JzAdapterUtil.changeViewSet(context, JzJqsElvAdapter.this, gPos, cPos, 8, childs, null);
            }
        });

        /***
         * 详细赛事分析
         */
        cvh.xxssfxTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, JzSsfxActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("game", games.get(gPos).getGames().get(cPos));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return view;
    }

    /**
     * 获取选中的投注内容
     */
    public List<JzChildGameEntity> getSelectList() {
        return JzAdapterUtil.getSelectList(childs, games);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        List<JzChildGameEntity> list = JzAdapterUtil.getSelectList(childs, games);
        if (list.size() == 0) {
            JczqActivity.selectNumTextView.setText("请至少选择2场比赛");
        } else if (list.size() == 1) {
            JczqActivity.selectNumTextView.setText("已选1场,还差1场");
        } else {
            JczqActivity.selectNumTextView.setText(Html.fromHtml("已选" + "<font color='#fa3243'>" + list.size() + "</font>" + "场"));
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
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

        private LinearLayout fxLayout, fxDetailLayout;
        private ImageView fxImageView;
        private GridLayout jqsGridLayout;
        private TextView xqTextView, typeTextView, jzrqTextView, zdNameTextView, kdNameTextView, xxssfxTextView;

        public ChildViewHolder(View view) {
            fxLayout = view.findViewById(R.id.jz_jqs_elv_fx_layout);
            fxDetailLayout = view.findViewById(R.id.jz_jqs_elv_fx_detail_layout);
            fxImageView = view.findViewById(R.id.jz_jqs_elv_fx_icon);
            jqsGridLayout = view.findViewById(R.id.jz_jqs_elv_jqs_select_layout);
            xqTextView = view.findViewById(R.id.jz_jqs_elv_child_xq_text);
            typeTextView = view.findViewById(R.id.jz_jqs_elv_child_type_text);
            jzrqTextView = view.findViewById(R.id.jz_jqs_elv_child_jzrq_text);
            zdNameTextView = view.findViewById(R.id.jz_jqs_elv_child_zd_name_text);
            kdNameTextView = view.findViewById(R.id.jz_jqs_elv_child_kd_name_text);
            xxssfxTextView = view.findViewById(R.id.jz_jqs_child_ssfx_text);
        }
    }

}