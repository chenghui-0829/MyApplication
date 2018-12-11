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

import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.JlChildEntity;
import com.shrxc.sc.app.bean.JlGroupEntity;
import com.shrxc.sc.app.dntz.JclqActivity;
import com.shrxc.sc.app.dntz.JlSsfxActivity;
import com.shrxc.sc.app.http.HttpRequestUtil;
import com.shrxc.sc.app.http.RequestCallback;
import com.shrxc.sc.app.utils.AppUtil;
import com.shrxc.sc.app.utils.MyCheckLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CH on 2018/8/17.
 */

public class JlHhggElvAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<Integer, Map<Integer, Map<Integer, Integer>>> childs;
    private List<JlGroupEntity> gameList;

    public JlHhggElvAdapter(Context context, List<JlGroupEntity> gameList) {
        this.context = context;
        this.gameList = gameList;
        int childCount = 0;
        for (int i = 0; i < gameList.size(); i++) {
            childCount += gameList.get(i).getList().size();
        }
        childs = JlAdapterUtil.initChilds(gameList.size(), childCount, 19);
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
            view = LayoutInflater.from(context).inflate(R.layout.jl_hhgg_elv_child_layout, viewGroup, false);
            cvh = new ChildViewHolder(view);
            view.setTag(cvh);
        } else {
            cvh = (ChildViewHolder) view.getTag();
        }

        //重置选中的内容
        gameList.get(gPos).getList().get(cPos).setSelected(null);
        cvh.xqTextView.setText(gameList.get(gPos).getList().get(cPos).getGameid());
        cvh.typeTextView.setText(gameList.get(gPos).getList().get(cPos).getLeague());
        cvh.zdNameTextView.setText(gameList.get(gPos).getList().get(cPos).getHometeam());
        cvh.zdPlTextView.setText(gameList.get(gPos).getList().get(cPos).getRzspl());
        cvh.kdNameTextView.setText(gameList.get(gPos).getList().get(cPos).getGuestteam());
        cvh.kdPlTextView.setText(gameList.get(gPos).getList().get(cPos).getRzfpl());
        String endTime = AppUtil.formatString(gameList.get(gPos).getList().get(cPos).getEndtime().
                replace("T", " "), "HH:mm");
        cvh.timeTextView.setText(endTime + "截止");
        cvh.rfTextView.setText(gameList.get(gPos).getList().get(cPos).getRangfen());
        cvh.dfPlTextView.setText(gameList.get(gPos).getList().get(cPos).getMaxpl());
        cvh.xfPlTextView.setText(gameList.get(gPos).getList().get(cPos).getMinpl());
        cvh.dfTextView.setText("大于" + gameList.get(gPos).getList().get(cPos).getZongfen());
        cvh.kdNameTextView.setText(gameList.get(gPos).getList().get(cPos).getGuestteam());
        cvh.xfTextView.setText("小于" + gameList.get(gPos).getList().get(cPos).getZongfen());

        String rf = gameList.get(gPos).getList().get(cPos).getRangfen();
        if (rf.contains("-")) {
            cvh.rfTextView.setTextColor(context.getResources().getColor(R.color.app_green_color_1db53a));
        } else {
            if (!rf.contains("+")) {
                cvh.rfTextView.setText("+" + rf);
            }
            cvh.rfTextView.setTextColor(context.getResources().getColor(R.color.app_red_color_fa3243));
        }

        int selected = 0;
        for (Integer key : childs.get(gPos).get(cPos).keySet()) {
            if (key > 0 && childs.get(gPos).get(cPos).get(key) == 1) {
                selected++;
            }
            if (key < 5) {
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
        }

        if (selected > 0) {
            cvh.selectedTextView.setText(Html.fromHtml("<font color='#fa3243'>" + selected + "</font>" + "项"));
            cvh.selectTextview.setText("已选");
        } else {
            cvh.selectTextview.setText("全部");
            cvh.selectedTextView.setText("选项");
        }

        cvh.qbxxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlData(gPos, cPos);
            }
        });
        cvh.fxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JlAdapterUtil.changeViewSet(context, JlHhggElvAdapter.this, gPos, cPos, 0, childs, null);
            }
        });
        cvh.selectLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Integer key : childs.get(gPos).get(cPos).keySet()) {
                    if (key > 2) {
                        if (childs.get(gPos).get(cPos).get(key) == 1) {
                            childs.get(gPos).get(cPos).put(key, 0);
                        }
                    }
                }
                JlAdapterUtil.changeViewSet(context, JlHhggElvAdapter.this, gPos, cPos, 1, childs, gameList);
            }
        });
        cvh.selectLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Integer key : childs.get(gPos).get(cPos).keySet()) {
                    if (key > 2) {
                        if (childs.get(gPos).get(cPos).get(key) == 1) {
                            childs.get(gPos).get(cPos).put(key, 0);
                        }
                    }
                }
                JlAdapterUtil.changeViewSet(context, JlHhggElvAdapter.this, gPos, cPos, 2, childs, gameList);
            }
        });
        cvh.selectLayout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (Integer key : childs.get(gPos).get(cPos).keySet()) {
                    if (key > 2 && key < 4) {
                    } else {
                        if (key != 0) {
                            if (childs.get(gPos).get(cPos).get(key) == 1) {
                                childs.get(gPos).get(cPos).put(key, 0);
                            }
                        }
                    }
                }
                JlAdapterUtil.changeViewSet(context, JlHhggElvAdapter.this, gPos, cPos, 3, childs, gameList);
            }
        });
        cvh.selectLayout.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Integer key : childs.get(gPos).get(cPos).keySet()) {
                    if (key > 2 && key < 4) {
                    } else {
                        if (key != 0) {
                            if (childs.get(gPos).get(cPos).get(key) == 1) {
                                childs.get(gPos).get(cPos).put(key, 0);
                            }
                        }
                    }
                }
                JlAdapterUtil.changeViewSet(context, JlHhggElvAdapter.this, gPos, cPos, 4, childs, gameList);
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

    private void getPlData(final int gPos, final int cPos) {
        final Map<String, Object> params = new HashMap<>();
        params.put("planid", gameList.get(gPos).getList().get(cPos).getPlanid());
        HttpRequestUtil.getInstance(context).get("TicketData/GeBTPLList", params, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                System.out.println("--------data------>" + data);
                if (state.equals("1")) {
                    JSONObject object = JSONObject.parseObject(data);
                    JlChildEntity entity = gameList.get(gPos).getList().get(cPos);
                    JSONObject frfObject = object.getJSONObject("SF");
                    entity.setZspl(frfObject.getString("ZS"));
                    entity.setZfpl(frfObject.getString("ZF"));
                    JSONObject rfObject = object.getJSONObject("RSF");
                    entity.setRzspl(rfObject.getString("ZS"));
                    entity.setRzfpl(rfObject.getString("ZF"));
                    entity.setRangfen(rfObject.getString("RF"));
                    JSONObject dxfObject = object.getJSONObject("DXF");
                    entity.setMaxpl(dxfObject.getString("Max"));
                    entity.setMinpl(dxfObject.getString("Min"));
                    entity.setZongfen(dxfObject.getString("ZF"));
                    JSONObject sfcObject = object.getJSONObject("SFC");
                    entity.setZS1_5(sfcObject.getString("ZS1_5"));
                    entity.setZS6_10(sfcObject.getString("ZS6_10"));
                    entity.setZS11_15(sfcObject.getString("ZS11_15"));
                    entity.setZS16_20(sfcObject.getString("ZS16_20"));
                    entity.setZS21_25(sfcObject.getString("ZS21_25"));
                    entity.setZS26(sfcObject.getString("ZS26"));
                    entity.setKS1_5(sfcObject.getString("KS1_5"));
                    entity.setKS6_10(sfcObject.getString("KS6_10"));
                    entity.setKS11_15(sfcObject.getString("KS11_15"));
                    entity.setKS16_20(sfcObject.getString("KS16_20"));
                    entity.setKS21_25(sfcObject.getString("KS21_25"));
                    entity.setKS26(sfcObject.getString("KS26"));
                    showQbxxDialog(gPos, cPos, entity);
                    gameList.get(gPos).getList().set(cPos, entity);
                }
            }

            @Override
            public void onErro(String erro) {
                super.onErro(erro);
                System.out.println("------erro------>" + erro);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }

    private void showQbxxDialog(final int gPos, final int cPos, JlChildEntity entity) {

        final Dialog dialog = new Dialog(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(
                R.layout.jl_hhgg_elv_qbxx_dialog_layout, null);
        final GridLayout frfLayout = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_frf_layout);
        final GridLayout rfLayout = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_rf_layout);
        final GridLayout dxfqLayout = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_dxf_layout);
        final GridLayout sfcLayout = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_sfc_layout);
        final TextView sureTextView = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_sure_text);
        final TextView cancleTextView = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_cancle_text);

        TextView zd_nameText = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_zd_name_text);
        TextView kd_nameText = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_kd_name_text);
        TextView frf_ksText = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_frf_ks_text);
        TextView frf_zsText = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_frf_zs_text);
        TextView rf_ksText = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_rf_ks_text);
        TextView rf_zsText = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_rf_zs_text);
        TextView rf_numText = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_rq_text);
        TextView dxf_dfText = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_dxf_df_text);
        TextView dxf_xfText = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_dxf_xf_text);
        TextView dxf_dfplText = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_dxf_df_pl_text);
        TextView dxf_xfplText = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_dxf_xf_pl_text);
        TextView ks_1_5_text = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_ks_1_5_text);
        TextView ks_6_10_text = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_ks_6_10_text);
        TextView ks_11_15_text = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_ks_11_15_text);
        TextView ks_16_20_text = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_ks_16_20_text);
        TextView ks_21_25_text = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_ks_21_25_text);
        TextView ks_26_text = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_ks_26_text);
        TextView zs_1_5_text = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_zs_1_5_text);
        TextView zs_6_10_text = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_zs_6_10_text);
        TextView zs_11_15_text = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_zs_11_15_text);
        TextView zs_16_20_text = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_zs_16_20_text);
        TextView zs_21_25_text = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_zs_21_25_text);
        TextView zs_26_text = view.findViewById(R.id.jl_hhgg_elv_qbxx_dialog_zs_26_text);

        dialog.setContentView(view);
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);

        zd_nameText.setText(entity.getHometeam());
        kd_nameText.setText(entity.getGuestteam());
        frf_ksText.setText(entity.getZfpl());
        frf_zsText.setText(entity.getZspl());
        rf_numText.setText(entity.getRangfen());
        rf_ksText.setText(entity.getRzfpl());
        rf_zsText.setText(entity.getRzspl());
        dxf_dfplText.setText(entity.getMaxpl());
        dxf_dfText.setText("大于" + entity.getZongfen());
        dxf_xfText.setText("小于" + entity.getZongfen());
        dxf_xfplText.setText(entity.getMinpl());
        ks_1_5_text.setText(entity.getKS1_5());
        ks_6_10_text.setText(entity.getKS6_10());
        ks_11_15_text.setText(entity.getKS11_15());
        ks_16_20_text.setText(entity.getKS16_20());
        ks_21_25_text.setText(entity.getKS21_25());
        ks_26_text.setText(entity.getKS26());
        zs_1_5_text.setText(entity.getZS1_5());
        zs_6_10_text.setText(entity.getZS6_10());
        zs_11_15_text.setText(entity.getZS11_15());
        zs_16_20_text.setText(entity.getZS16_20());
        zs_21_25_text.setText(entity.getZS21_25());
        zs_26_text.setText(entity.getZS26());


        final GridLayout layouts[] = {frfLayout, rfLayout, dxfqLayout, sfcLayout};

        final List<MyCheckLinearLayout> allSelect = new ArrayList<>();
        allSelect.add((MyCheckLinearLayout) rfLayout.getChildAt(0));
        allSelect.add((MyCheckLinearLayout) rfLayout.getChildAt(1));
        allSelect.add((MyCheckLinearLayout) dxfqLayout.getChildAt(0));
        allSelect.add((MyCheckLinearLayout) dxfqLayout.getChildAt(1));
        for (int i = 0; i < sfcLayout.getChildCount(); i++) {
            allSelect.add((MyCheckLinearLayout) sfcLayout.getChildAt(i));
        }
        allSelect.add((MyCheckLinearLayout) frfLayout.getChildAt(0));
        allSelect.add((MyCheckLinearLayout) frfLayout.getChildAt(1));

        for (Integer key : childs.get(gPos).get(cPos).keySet()) {
            if (key > 0 && childs.get(gPos).get(cPos).get(key) == 1) {
                allSelect.get(key - 1).setChecked(false);
                dialoChangeViewState(allSelect.get(key - 1));
            }
        }

        sureTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < allSelect.size(); i++) {
                    if (allSelect.get(i).isChecked()) {
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
        cancleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        frfLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 0, 0);
            }
        });
        frfLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 0, 1);
            }
        });
        rfLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 1, 0);
            }
        });
        rfLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 1, 1);
            }
        });
        dxfqLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 0);
            }
        });
        dxfqLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 1);
            }
        });
        sfcLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 0);
            }
        });
        sfcLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 1);
            }
        });
        sfcLayout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 2);
            }
        });
        sfcLayout.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 3);
            }
        });
        sfcLayout.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 4);
            }
        });
        sfcLayout.getChildAt(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 5);
            }
        });
        sfcLayout.getChildAt(6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 6);
            }
        });
        sfcLayout.getChildAt(7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 7);
            }
        });
        sfcLayout.getChildAt(8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 8);
            }
        });
        sfcLayout.getChildAt(9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 9);
            }
        });
        sfcLayout.getChildAt(10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 10);
            }
        });
        sfcLayout.getChildAt(11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 11);
            }
        });
    }

    private void changeLayoutCheckState(GridLayout layouts[], int index, int click) {

        for (int i = 0; i < layouts.length; i++) {
            if (i == index) {
                dialoChangeViewState((MyCheckLinearLayout) layouts[i].getChildAt(click));
            } else {
                for (int j = 0; j < layouts[i].getChildCount(); j++) {
                    MyCheckLinearLayout layout = (MyCheckLinearLayout) layouts[i].getChildAt(j);
                    layout.setChecked(true);
                    dialoChangeViewState(layout);
                }
            }
        }
    }

    private void dialoChangeViewState(MyCheckLinearLayout layout) {

        TextView t1, t2, t3 = null;
        if (layout.getChildAt(0) instanceof LinearLayout) {
            LinearLayout childLayout = (LinearLayout) layout.getChildAt(0);
            t1 = (TextView) childLayout.getChildAt(0);
            t3 = (TextView) childLayout.getChildAt(1);
            t2 = (TextView) layout.getChildAt(1);
        } else {
            t1 = (TextView) layout.getChildAt(0);
            t2 = (TextView) layout.getChildAt(1);
        }

        if (layout.isChecked()) {
            layout.setBackgroundResource(R.color.app_white_color_ffffff);
            t1.setTextColor(context.getResources().getColor(R.color.app_text_color_333333));
            t2.setTextColor(context.getResources().getColor(R.color.app_text_color_999999));
            if (layout.getChildAt(0) instanceof LinearLayout) {
                t3.setTextColor(context.getResources().getColor(R.color.app_text_color_999999));
            }
            layout.setChecked(false);
        } else {
            layout.setBackgroundResource(R.color.app_red_color_fa3243);
            t1.setTextColor(context.getResources().getColor(R.color.app_white_color_ffffff));
            t2.setTextColor(context.getResources().getColor(R.color.app_white_color_ffffff));
            if (layout.getChildAt(0) instanceof LinearLayout) {
                t3.setTextColor(context.getResources().getColor(R.color.app_white_color_ffffff));
            }
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
        private LinearLayout qbxxLayout, fxLayout, fxDetailLayout;
        private GridLayout selectLayout;
        private TextView selectedTextView, selectTextview, xqTextView, typeTextView, timeTextView, zdNameTextView, zdPlTextView,
                kdNameTextView, kdPlTextView, rfTextView, dfPlTextView, xfPlTextView, dfTextView, xfTextView, ssfxTextView;

        public ChildViewHolder(View view) {

            fxDetailLayout = view.findViewById(R.id.jl_hhgg_elv_fx_detail_layout);
            fxImageView = view.findViewById(R.id.jl_hhgg_elv_child_fx_icon);
            qbxxLayout = view.findViewById(R.id.jl_hhgg_elv_qbxx_layout);
            fxLayout = view.findViewById(R.id.jl_hhgg_elv_child_fx_layout);
            selectLayout = view.findViewById(R.id.jl_hhgg_elv_select_Layout);
            selectedTextView = view.findViewById(R.id.jl_hhgg_elv_qbxx_selected_size_text);
            selectTextview = view.findViewById(R.id.jl_hhgg_elv_qbxx_selected_text);
            xqTextView = view.findViewById(R.id.jl_hhgg_elv_xq_text);
            typeTextView = view.findViewById(R.id.jl_hhgg_elv_type_text);
            timeTextView = view.findViewById(R.id.jl_hhgg_elv_time_text);
            zdNameTextView = view.findViewById(R.id.jl_hhgg_elv_zd_name_text);
            dfPlTextView = view.findViewById(R.id.jl_hhgg_elv_df_pl_text);
            kdNameTextView = view.findViewById(R.id.jl_hhgg_elv_kd_name_text);
            xfPlTextView = view.findViewById(R.id.jl_hhgg_elv_xf_pl_text);
            dfTextView = view.findViewById(R.id.jl_hhgg_elv_df_text);
            xfTextView = view.findViewById(R.id.jl_hhgg_elv_xf_text);
            zdPlTextView = view.findViewById(R.id.jl_hhgg_elv_zd_pl_text);
            kdPlTextView = view.findViewById(R.id.jl_hhgg_elv_kd_pl_text);
            rfTextView = view.findViewById(R.id.jl_hhgg_elv_rf_text);
            ssfxTextView = view.findViewById(R.id.jl_hhgg_child_ssfx_text);
        }
    }
}