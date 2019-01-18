package com.shrxc.sc.app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.shrxc.sc.app.utils.MyCheckLinearLayout;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CH on 2018/8/17.
 */

public class JzBfElvAdapter extends BaseExpandableListAdapter {

    @BindView(R.id.jz_bf_elv_child_select_layout)
    GridLayout dialogSelectLayout;
    @BindView(R.id.jz_bf_elv_child_dialog_cancle_text)
    TextView dialogCancleText;
    @BindView(R.id.jz_bf_elv_child_dialog_sure_text)
    TextView dialogSureText;
    private Context context;
    private Map<Integer, Map<Integer, Map<Integer, Integer>>> childs;
    private List<JzGroupGameEntity> games;

    public JzBfElvAdapter(Context context, List<JzGroupGameEntity> games) {
        this.context = context;
        this.games = games;
        int childCount = 0;
        for (int i = 0; i < games.size(); i++) {
            childCount += games.get(i).getGames().size();
        }
        childs = JzAdapterUtil.initChilds(games == null ? 0 : games.size(), childCount, 31);
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
        gvh.csText.setText(games.get(i).getGames().size() + "场比赛");
        return view;
    }

    @Override
    public View getChildView(final int gPos, final int cPos, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder cvh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.jz_bf_elv_child_layout, viewGroup, false);
            cvh = new ChildViewHolder(view);
            view.setTag(cvh);
        } else {
            cvh = (ChildViewHolder) view.getTag();
        }

        StringBuffer sb = new StringBuffer();
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
                if (childs.get(gPos).get(cPos).get(key) == 1) {
                    String text = ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(key - 1)).getChildAt(0)).getText().toString().trim();
                    sb.append(text + " ");
                }
            }
        }
        if (sb.length() == 0) {
            cvh.bfxxTextView.setText("点击展开比分选项");
            cvh.bfxxTextView.setTextColor(context.getResources().getColor(R.color.app_text_color_666666));
        } else {
            cvh.bfxxTextView.setText(sb.toString());
            cvh.bfxxTextView.setTextColor(context.getResources().getColor(R.color.app_red_color_fa3243));
        }

        //重置选中的内容
        games.get(gPos).getGames().get(cPos).setSelectedList(null);
        cvh.xqTextView.setText(games.get(gPos).getGames().get(cPos).getGameid());
        cvh.typeTextView.setText(games.get(gPos).getGames().get(cPos).getLeague());
        cvh.zdNameTextView.setText(games.get(gPos).getGames().get(cPos).getHometeam());
        cvh.kdNameTextView.setText(games.get(gPos).getGames().get(cPos).getGuestteam());
        String endTime = AppUtil.formatString(games.get(gPos).getGames().get(cPos).getEndtime().replace("T", " "), "HH:mm");
        cvh.jzrqTextView.setText(endTime + "截止");

        cvh.bfxxTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBqcDialog(gPos, cPos);
            }
        });

        cvh.fxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JzAdapterUtil.changeViewSet(context, JzBfElvAdapter.this, gPos, cPos, 0, childs, null);
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

    private void showBqcDialog(final int gPos, final int cPos) {

        final Dialog dialog = new Dialog(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(
                R.layout.jz_bf_elv_child_bfxx_dialog_layout, null);
        dialog.setContentView(view);
        ButterKnife.bind(this, view);
        dialog.show();

        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);

        JzTzTypeEntity entity = games.get(gPos).getGames().get(cPos).getTzTypeEntity();
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(0)).getChildAt(1)).setText(entity.getBf_1_0());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(1)).getChildAt(1)).setText(entity.getBf_2_0());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(2)).getChildAt(1)).setText(entity.getBf_2_1());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(3)).getChildAt(1)).setText(entity.getBf_3_0());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(4)).getChildAt(1)).setText(entity.getBf_3_1());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(5)).getChildAt(1)).setText(entity.getBf_3_2());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(6)).getChildAt(1)).setText(entity.getBf_4_0());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(7)).getChildAt(1)).setText(entity.getBf_4_1());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(8)).getChildAt(1)).setText(entity.getBf_4_2());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(9)).getChildAt(1)).setText(entity.getBf_5_0());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(10)).getChildAt(1)).setText(entity.getBf_5_1());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(11)).getChildAt(1)).setText(entity.getBf_5_2());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(12)).getChildAt(1)).setText(entity.getBf_sqt());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(13)).getChildAt(1)).setText(entity.getBf_0_0());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(14)).getChildAt(1)).setText(entity.getBf_1_1());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(15)).getChildAt(1)).setText(entity.getBf_2_2());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(16)).getChildAt(1)).setText(entity.getBf_3_3());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(17)).getChildAt(1)).setText(entity.getBf_pqt());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(18)).getChildAt(1)).setText(entity.getBf_0_1());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(19)).getChildAt(1)).setText(entity.getBf_0_2());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(20)).getChildAt(1)).setText(entity.getBf_1_2());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(21)).getChildAt(1)).setText(entity.getBf_0_3());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(22)).getChildAt(1)).setText(entity.getBf_1_3());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(23)).getChildAt(1)).setText(entity.getBf_2_3());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(24)).getChildAt(1)).setText(entity.getBf_0_4());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(25)).getChildAt(1)).setText(entity.getBf_1_4());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(26)).getChildAt(1)).setText(entity.getBf_2_4());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(27)).getChildAt(1)).setText(entity.getBf_0_5());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(28)).getChildAt(1)).setText(entity.getBf_1_5());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(29)).getChildAt(1)).setText(entity.getBf_2_5());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(30)).getChildAt(1)).setText(entity.getBf_fqt());


        for (Integer key : childs.get(gPos).get(cPos).keySet()) {
            if (key > 0 && childs.get(gPos).get(cPos).get(key) == 1) {
                ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(key - 1)).setChecked(false);
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(key - 1));
            }
        }

        dialogSureText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < dialogSelectLayout.getChildCount(); i++) {
                    if (((MyCheckLinearLayout) dialogSelectLayout.getChildAt(i)).isChecked()) {
                        childs.get(gPos).get(cPos).put(i + 1, 1);
                    } else {
                        childs.get(gPos).get(cPos).put(i + 1, 0);
                    }
                }
                dialog.dismiss();
                dialog.cancel();
                notifyDataSetChanged();
            }
        });
        dialogCancleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
            }
        });


        dialogSelectLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(0));
            }
        });
        dialogSelectLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(1));
            }
        });
        dialogSelectLayout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(2));
            }
        });
        dialogSelectLayout.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(3));
            }
        });
        dialogSelectLayout.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(4));
            }
        });
        dialogSelectLayout.getChildAt(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(5));
            }
        });
        dialogSelectLayout.getChildAt(6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(6));
            }
        });
        dialogSelectLayout.getChildAt(7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(7));
            }
        });
        dialogSelectLayout.getChildAt(8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(8));
            }
        });
        dialogSelectLayout.getChildAt(9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(9));
            }
        });
        dialogSelectLayout.getChildAt(10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(10));
            }
        });
        dialogSelectLayout.getChildAt(11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(11));
            }
        });
        dialogSelectLayout.getChildAt(12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(12));
            }
        });
        dialogSelectLayout.getChildAt(13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(13));
            }
        });
        dialogSelectLayout.getChildAt(14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(14));
            }
        });
        dialogSelectLayout.getChildAt(15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(15));
            }
        });
        dialogSelectLayout.getChildAt(16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(16));
            }
        });
        dialogSelectLayout.getChildAt(17).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(17));
            }
        });
        dialogSelectLayout.getChildAt(18).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(18));
            }
        });
        dialogSelectLayout.getChildAt(19).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(19));
            }
        });
        dialogSelectLayout.getChildAt(20).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(20));
            }
        });
        dialogSelectLayout.getChildAt(21).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(21));
            }
        });
        dialogSelectLayout.getChildAt(22).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(22));
            }
        });
        dialogSelectLayout.getChildAt(23).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(23));
            }
        });
        dialogSelectLayout.getChildAt(24).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(24));
            }
        });
        dialogSelectLayout.getChildAt(25).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(25));
            }
        });
        dialogSelectLayout.getChildAt(26).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(26));
            }
        });
        dialogSelectLayout.getChildAt(27).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(27));
            }
        });
        dialogSelectLayout.getChildAt(28).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(28));
            }
        });
        dialogSelectLayout.getChildAt(29).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(29));
            }
        });
        dialogSelectLayout.getChildAt(30).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(30));
            }
        });
    }

    private void dialoChangeViewState(MyCheckLinearLayout layout) {

        TextView t1 = (TextView) layout.getChildAt(0);
        TextView t2 = (TextView) layout.getChildAt(1);
        if (layout.isChecked()) {
            layout.setBackgroundResource(R.color.app_white_color_ffffff);
            t1.setTextColor(context.getResources().getColor(R.color.app_text_color_333333));
            t2.setTextColor(context.getResources().getColor(R.color.app_text_color_999999));
            layout.setChecked(false);
        } else {
            layout.setBackgroundResource(R.color.app_red_color_fa3243);
            t1.setTextColor(context.getResources().getColor(R.color.app_white_color_ffffff));
            t2.setTextColor(context.getResources().getColor(R.color.app_white_color_ffffff));
            layout.setChecked(true);
        }
    }

    /**
     * 获取选中的投注内容
     */
    public List<JzChildGameEntity> getSelectList() {
        return JzAdapterUtil.getSelectList(childs, games);
    }

    public void clearSelect() {
        JzAdapterUtil.clearSelected(childs, JzBfElvAdapter.this);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        List<JzChildGameEntity> list = JzAdapterUtil.getSelectList(childs, games);
        if (list.size() == 0) {
            JczqActivity.selectNumTextView.setText("请至少选择1场比赛");
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


        private TextView bfxxTextView, xqTextView, typeTextView, jzrqTextView, zdNameTextView, kdNameTextView, xxssfxTextView;
        private ImageView fxImageView;
        private LinearLayout fxLayout, fxDetailLayout;

        public ChildViewHolder(View view) {
            bfxxTextView = view.findViewById(R.id.jz_bf_elv_child_bfxx_text);
            fxLayout = view.findViewById(R.id.jz_bf_elv_fx_layout);
            fxDetailLayout = view.findViewById(R.id.jz_bf_elv_child_fx_detail_layout);
            fxImageView = view.findViewById(R.id.jz_bf_elv_fx_icon);
            xqTextView = view.findViewById(R.id.jz_bf_elv_child_xq_text);
            typeTextView = view.findViewById(R.id.jz_bf_elv_child_type_text);
            jzrqTextView = view.findViewById(R.id.jz_bf_elv_child_jzrq_text);
            zdNameTextView = view.findViewById(R.id.jz_bf_elv_child_zd_name_text);
            kdNameTextView = view.findViewById(R.id.jz_bf_elv_child_kd_name_text);
            xxssfxTextView = view.findViewById(R.id.jz_bf_child_ssfx_text);
        }

    }

}