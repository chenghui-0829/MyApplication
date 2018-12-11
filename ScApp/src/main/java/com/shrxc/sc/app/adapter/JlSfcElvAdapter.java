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
import com.shrxc.sc.app.bean.JlChildEntity;
import com.shrxc.sc.app.bean.JlGroupEntity;
import com.shrxc.sc.app.dntz.JclqActivity;
import com.shrxc.sc.app.dntz.JlSsfxActivity;
import com.shrxc.sc.app.utils.AppUtil;
import com.shrxc.sc.app.utils.MyCheckLinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CH on 2018/8/17.
 */

public class JlSfcElvAdapter extends BaseExpandableListAdapter {

    @BindView(R.id.jz_sfc_elv_dialog_select_layout)
    GridLayout dialogSelectLayout;
    @BindView(R.id.jz_sfc_elv_dialog_cancle_text)
    TextView cancleText;
    @BindView(R.id.jz_sfc_elv_dialog_sure_text)
    TextView sureText;
    private Context context;
    private Map<Integer, Map<Integer, Map<Integer, Integer>>> childs;
    private List<JlGroupEntity> gameList;

    public JlSfcElvAdapter(Context context, List<JlGroupEntity> gameList) {
        this.context = context;
        this.gameList = gameList;
        int childCount = 0;
        for (int i = 0; i < gameList.size(); i++) {
            childCount += gameList.get(i).getList().size();
        }
        childs = JlAdapterUtil.initChilds(gameList.size(), childCount, 13);
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
            view = LayoutInflater.from(context).inflate(R.layout.jl_sfc_elv_child_layout, viewGroup, false);
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
                    if (childs.get(gPos).get(cPos).get(key) == 1) {
                        String text = ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(key - 1)).getChildAt(0)).getText().toString().trim();
                        if (key > 0 && key < 7) {
                            sb.append("客胜" + text + " ");
                        } else if (key > 6) {
                            sb.append("主胜" + text + " ");
                        }
                    }
                }
            }
        }
        if (sb.length() == 0) {
            cvh.sfcxxTextView.setText("点击展开胜分差选项");
            cvh.sfcxxTextView.setTextColor(context.getResources().getColor(R.color.app_text_color_666666));
        } else {
            cvh.sfcxxTextView.setText(sb.toString());
            cvh.sfcxxTextView.setTextColor(context.getResources().getColor(R.color.app_red_color_fa3243));
        }

        //重置选中的内容
        gameList.get(gPos).getList().get(cPos).setSelected(null);
        cvh.xqTextView.setText(gameList.get(gPos).getList().get(cPos).getGameid());
        cvh.typeTextView.setText(gameList.get(gPos).getList().get(cPos).getLeague());
        cvh.zdNameTextView.setText(gameList.get(gPos).getList().get(cPos).getHometeam());
        cvh.kdNameTextView.setText(gameList.get(gPos).getList().get(cPos).getGuestteam());
        String endTime = AppUtil.formatString(gameList.get(gPos).getList().get(cPos).getEndtime().
                replace("T", " "), "HH:mm");
        cvh.timeTextView.setText(endTime + "截止");

        cvh.fxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JzAdapterUtil.changeViewSet(context, JlSfcElvAdapter.this, gPos, cPos, 0, childs, null);
            }
        });
        cvh.sfcxxTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSfcDialog(gPos, cPos);
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

    private void showSfcDialog(final int gPos, final int cPos) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(
                R.layout.jl_sfc_elv_sfcxx_dialog_layout, null);
        dialog.setContentView(view);
        dialog.show();
        ButterKnife.bind(this, view);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);

        for (Integer key : childs.get(gPos).get(cPos).keySet()) {
            if (key > 0 && childs.get(gPos).get(cPos).get(key) == 1) {
                ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(key - 1)).setChecked(false);
                dialoChangeViewState((MyCheckLinearLayout) dialogSelectLayout.getChildAt(key - 1));
            }
        }

        sureText.setOnClickListener(new View.OnClickListener() {
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
        cancleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        JlChildEntity entity = gameList.get(gPos).getList().get(cPos);
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(0)).getChildAt(1)).setText(entity.getKS1_5());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(1)).getChildAt(1)).setText(entity.getKS6_10());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(2)).getChildAt(1)).setText(entity.getKS11_15());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(3)).getChildAt(1)).setText(entity.getKS16_20());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(4)).getChildAt(1)).setText(entity.getKS21_25());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(5)).getChildAt(1)).setText(entity.getKS26());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(6)).getChildAt(1)).setText(entity.getZS1_5());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(7)).getChildAt(1)).setText(entity.getZS6_10());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(8)).getChildAt(1)).setText(entity.getZS11_15());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(9)).getChildAt(1)).setText(entity.getZS16_20());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(10)).getChildAt(1)).setText(entity.getZS21_25());
        ((TextView) ((MyCheckLinearLayout) dialogSelectLayout.getChildAt(11)).getChildAt(1)).setText(entity.getZS26());

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
        private LinearLayout fxLayout, fxDetailLayout;
        private TextView xqTextView, typeTextView, timeTextView, zdNameTextView, ssfxTextView,
                kdNameTextView, sfcxxTextView;

        public ChildViewHolder(View view) {

            fxImageView = view.findViewById(R.id.jl_sfc_elv_fx_icon);
            fxLayout = view.findViewById(R.id.jl_sfc_elv_fx_layout);
            fxDetailLayout = view.findViewById(R.id.jl_sfc_elv_fx_detail_layout);
            xqTextView = view.findViewById(R.id.jl_sfc_elv_xq_text);
            typeTextView = view.findViewById(R.id.jl_sfc_elv_type_text);
            timeTextView = view.findViewById(R.id.jl_sfc_elv_time_text);
            zdNameTextView = view.findViewById(R.id.jl_sfc_elv_zd_name_text);
            kdNameTextView = view.findViewById(R.id.jl_sfc_elv_kd_name_text);
            sfcxxTextView = view.findViewById(R.id.jl_sfc_elv_child_sfcxx_text);
            ssfxTextView = view.findViewById(R.id.jl_sfc_child_ssfx_text);
        }
    }

}