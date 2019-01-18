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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.JzChildGameEntity;
import com.shrxc.sc.app.bean.JzGroupGameEntity;
import com.shrxc.sc.app.bean.JzTzTypeEntity;
import com.shrxc.sc.app.dntz.JczqActivity;
import com.shrxc.sc.app.dntz.JzSsfxActivity;
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

public class JzHhggElvAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<Integer, Map<Integer, Map<Integer, Integer>>> childs;
    private List<JzGroupGameEntity> games;

    public JzHhggElvAdapter(Context context, List<JzGroupGameEntity> games) {
        this.context = context;
        this.games = games;
        int childCount = 0;
        for (int i = 0; i < games.size(); i++) {
            childCount += games.get(i).getGames().size();
        }
        childs = JzAdapterUtil.initChilds(games == null ? 0 : games.size(), childCount, 55);
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
            view = LayoutInflater.from(context).inflate(R.layout.jz_hhgg_elv_child_layout, viewGroup, false);
            cvh = new ChildViewHolder(view);
            view.setTag(cvh);
        } else {
            cvh = (ChildViewHolder) view.getTag();
        }

        int selected = 0;

        for (Integer key : childs.get(gPos).get(cPos).keySet()) {

            if (key > 0 && childs.get(gPos).get(cPos).get(key) == 1) {
                selected++;
            }
            if (key < 7) {
                if (key == 0) {
                    if (childs.get(gPos).get(cPos).get(key) == 0) {
                        cvh.fxDetailLayout.setVisibility(View.GONE);
                        cvh.fxImageView.setImageResource(R.mipmap.app_down_gray_arow_icon);
                    } else {
                        cvh.fxDetailLayout.setVisibility(View.VISIBLE);
                        cvh.fxImageView.setImageResource(R.mipmap.app_up_gray_arow_icon);
                    }
                } else {
                    JzAdapterUtil.initViewState(context, childs.get(gPos).get(cPos).get(key), cvh.layouts[key - 1],
                            key - 1, 1);
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

        //重置选中的内容
        games.get(gPos).getGames().get(cPos).setSelectedList(null);

        cvh.xqTextView.setText(games.get(gPos).getGames().get(cPos).getGameid());
        cvh.typeTextView.setText(games.get(gPos).getGames().get(cPos).getLeague());
        cvh.zdNameTextView.setText(games.get(gPos).getGames().get(cPos).getHometeam());
        cvh.s_plTextview.setText(games.get(gPos).getGames().get(cPos).getSpl());
        cvh.p_plTextview.setText(games.get(gPos).getGames().get(cPos).getPpl());
        cvh.kdNameTextView.setText(games.get(gPos).getGames().get(cPos).getGuestteam());
        cvh.f_plTextview.setText(games.get(gPos).getGames().get(cPos).getFpl());
        cvh.rq_s_plTextview.setText(games.get(gPos).getGames().get(cPos).getRspl());
        cvh.rq_p_plTextview.setText(games.get(gPos).getGames().get(cPos).getRppl());
        cvh.rq_f_plTextview.setText(games.get(gPos).getGames().get(cPos).getRfpl());

        String endTime = AppUtil.formatString(games.get(gPos).getGames().get(cPos).getEndtime().replace("T", " "), "HH:mm");
        cvh.jzrqTextView.setText(endTime + "截止");

        String rqs = games.get(gPos).getGames().get(cPos).getRangqiu();
        if (rqs.contains("-")) {
            rqs = rqs.replace("-", "");
            cvh.rqLayout.setBackgroundResource(R.color.app_green_color_1db53a);
            cvh.rqTextView.setText("-" + AppUtil.sswrNum(Double.parseDouble(rqs), 0));
        } else if (rqs.contains("+")) {
            rqs = rqs.replace("+", "");
            cvh.rqLayout.setBackgroundResource(R.color.app_red_color_fa3243);
            cvh.rqTextView.setText("+" + AppUtil.sswrNum(Double.parseDouble(rqs), 0));
        }


        /***
         * 全部选项
         */
        cvh.qbxxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlData(gPos, cPos);
            }
        });
        /**
         * 分析
         */
        cvh.fxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JzAdapterUtil.changeViewSet(context, JzHhggElvAdapter.this, gPos, cPos, 0, childs, null);
            }
        });
        /**
         * 非让球-胜
         */
        cvh.sLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (Integer key : childs.get(gPos).get(cPos).keySet()) {
                    if (key > 3) {
                        if (childs.get(gPos).get(cPos).get(key) == 1) {
                            childs.get(gPos).get(cPos).put(key, 0);
                        }
                    }
                }

                JzAdapterUtil.changeViewSet(context, JzHhggElvAdapter.this, gPos, cPos, 1, childs, games);
            }
        });
        /**
         * 非让球-平
         */
        cvh.pLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Integer key : childs.get(gPos).get(cPos).keySet()) {
                    if (key > 3) {
                        if (childs.get(gPos).get(cPos).get(key) == 1) {
                            childs.get(gPos).get(cPos).put(key, 0);
                        }
                    }
                }
                JzAdapterUtil.changeViewSet(context, JzHhggElvAdapter.this, gPos, cPos, 2, childs, games);
            }
        });
        /**
         * 非让球-负
         */
        cvh.fLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Integer key : childs.get(gPos).get(cPos).keySet()) {
                    if (key > 3) {
                        if (childs.get(gPos).get(cPos).get(key) == 1) {
                            childs.get(gPos).get(cPos).put(key, 0);
                        }
                    }
                }
                JzAdapterUtil.changeViewSet(context, JzHhggElvAdapter.this, gPos, cPos, 3, childs, games);
            }
        });
        /**
         * 让球-胜
         */
        cvh.rq_sLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (Integer key : childs.get(gPos).get(cPos).keySet()) {
                    if (key > 3 && key < 7) {
                    } else {
                        if (key != 0) {
                            if (childs.get(gPos).get(cPos).get(key) == 1) {
                                childs.get(gPos).get(cPos).put(key, 0);
                            }
                        }
                    }
                }

                JzAdapterUtil.changeViewSet(context, JzHhggElvAdapter.this, gPos, cPos, 4, childs, games);
            }
        });
        /**
         * 让球-平
         */
        cvh.rq_pLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Integer key : childs.get(gPos).get(cPos).keySet()) {
                    if (key > 3 && key < 7) {
                    } else {
                        if (key != 0) {
                            if (childs.get(gPos).get(cPos).get(key) == 1) {
                                childs.get(gPos).get(cPos).put(key, 0);
                            }
                        }
                    }
                }
                JzAdapterUtil.changeViewSet(context, JzHhggElvAdapter.this, gPos, cPos, 5, childs, games);
            }
        });
        /**
         * 让球-负
         */
        cvh.rq_fLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Integer key : childs.get(gPos).get(cPos).keySet()) {
                    if (key > 3 && key < 7) {
                    } else {
                        if (key != 0) {
                            if (childs.get(gPos).get(cPos).get(key) == 1) {
                                childs.get(gPos).get(cPos).put(key, 0);
                            }
                        }
                    }
                }
                JzAdapterUtil.changeViewSet(context, JzHhggElvAdapter.this, gPos, cPos, 6, childs, games);
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

    private void getPlData(final int gPos, final int cPos) {
        final Map<String, Object> params = new HashMap<>();
        params.put("planid", games.get(gPos).getGames().get(cPos).getPlanid());
        HttpRequestUtil.getInstance(context).get("TicketData/GePLList", params, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject result, String state, String msg, String data) {
                super.onSuccess(result, state, msg, data);
                System.out.println("--------data------>" + data);
                if (state.equals("1")) {
                    JSONObject object = JSONObject.parseObject(data);
                    JzTzTypeEntity entity = new JzTzTypeEntity();

                    JSONObject spfObject = object.getJSONObject("SPF");
                    entity.setSpl(spfObject.getString("h"));
                    entity.setPpl(spfObject.getString("d"));
                    entity.setFpl(spfObject.getString("a"));

                    JSONObject rspfObject = object.getJSONObject("RQ");
                    entity.setRspl(rspfObject.getString("h"));
                    entity.setRppl(rspfObject.getString("d"));
                    entity.setRfpl(rspfObject.getString("a"));
                    entity.setRqNum(rspfObject.getString("RQ"));

                    JSONObject bfObject = object.getJSONObject("BF");
                    entity.setBf_fqt(bfObject.getString("s_1_a"));
                    entity.setBf_pqt(bfObject.getString("s_1_d"));
                    entity.setBf_sqt(bfObject.getString("s_1_h"));
                    entity.setBf_0_0(bfObject.getString("s0000"));
                    entity.setBf_0_1(bfObject.getString("s0001"));
                    entity.setBf_0_2(bfObject.getString("s0002"));
                    entity.setBf_0_3(bfObject.getString("s0003"));
                    entity.setBf_0_4(bfObject.getString("s0004"));
                    entity.setBf_0_5(bfObject.getString("s0005"));
                    entity.setBf_1_0(bfObject.getString("s0100"));
                    entity.setBf_1_1(bfObject.getString("s0101"));
                    entity.setBf_1_2(bfObject.getString("s0102"));
                    entity.setBf_1_3(bfObject.getString("s0103"));
                    entity.setBf_1_4(bfObject.getString("s0104"));
                    entity.setBf_1_5(bfObject.getString("s0105"));
                    entity.setBf_2_0(bfObject.getString("s0200"));
                    entity.setBf_2_1(bfObject.getString("s0201"));
                    entity.setBf_2_2(bfObject.getString("s0202"));
                    entity.setBf_2_3(bfObject.getString("s0203"));
                    entity.setBf_2_4(bfObject.getString("s0204"));
                    entity.setBf_2_5(bfObject.getString("s0205"));
                    entity.setBf_3_0(bfObject.getString("s0300"));
                    entity.setBf_3_1(bfObject.getString("s0301"));
                    entity.setBf_3_2(bfObject.getString("s0302"));
                    entity.setBf_3_3(bfObject.getString("s0303"));
                    entity.setBf_4_0(bfObject.getString("s0400"));
                    entity.setBf_4_1(bfObject.getString("s0401"));
                    entity.setBf_4_2(bfObject.getString("s0402"));
                    entity.setBf_5_0(bfObject.getString("s0500"));
                    entity.setBf_5_1(bfObject.getString("s0501"));
                    entity.setBf_5_2(bfObject.getString("s0502"));

                    JSONObject jqsObject = object.getJSONObject("JQ");
                    entity.setJqs_0(jqsObject.getString("s0"));
                    entity.setJqs_1(jqsObject.getString("s1"));
                    entity.setJqs_2(jqsObject.getString("s2"));
                    entity.setJqs_3(jqsObject.getString("s3"));
                    entity.setJqs_4(jqsObject.getString("s4"));
                    entity.setJqs_5(jqsObject.getString("s5"));
                    entity.setJqs_6(jqsObject.getString("s6"));
                    entity.setJqs_7(jqsObject.getString("s7"));

                    JSONObject bqcObject = object.getJSONObject("BQ");
                    entity.setBqc_ss(bqcObject.getString("hh"));
                    entity.setBqc_sp(bqcObject.getString("hd"));
                    entity.setBqc_sf(bqcObject.getString("ha"));
                    entity.setBqc_ps(bqcObject.getString("dh"));
                    entity.setBqc_pp(bqcObject.getString("dd"));
                    entity.setBqc_pf(bqcObject.getString("da"));
                    entity.setBqc_fs(bqcObject.getString("ah"));
                    entity.setBqc_fp(bqcObject.getString("ad"));
                    entity.setBqc_ff(bqcObject.getString("aa"));
                    showQbxxDialog(gPos, cPos, entity);
                    games.get(gPos).getGames().get(cPos).setTzTypeEntity(entity);
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

    private void showQbxxDialog(final int gPos, final int cPos, JzTzTypeEntity entity) {

        final Dialog dialog = new Dialog(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(
                R.layout.jz_hhgg_elv_child_qbxx_dialog_layout, null);
        final GridLayout sfLayout = view.findViewById(R.id.jz_hhgg_elv_qbxx_dialog_sf_layout);
        final GridLayout rqsfLayout = view.findViewById(R.id.jz_hhgg_elv_qbxx_dialog_rqsf_layout);
        final GridLayout bfLayout = view.findViewById(R.id.jz_hhgg_elv_qbxx_dialog_bf_layout);
        final GridLayout zjqLayout = view.findViewById(R.id.jz_hhgg_elv_qbxx_dialog_zjq_layout);
        final GridLayout bcqLayout = view.findViewById(R.id.jz_hhgg_elv_qbxx_dialog_bqc_layout);
        final TextView sureTextView = view.findViewById(R.id.jz_hhgg_elv_qbxx_dialog_sure_text);
        final TextView cancleTextView = view.findViewById(R.id.jz_hhgg_elv_qbxx_dialog_cancle_text);
        final TextView rqTextView = view.findViewById(R.id.jz_hhgg_elv_qbxx_dialog_rq_text);
        final TextView zdNameTextView = view.findViewById(R.id.jz_hhgg_elv_qbxx_dialog_zd_name_text);
        final TextView kdNameTextView = view.findViewById(R.id.jz_hhgg_elv_qbxx_dialog_kd_name_text);
        final LinearLayout rqLayout = view.findViewById(R.id.jz_hhgg_elv_qbxx_dialog_rq_layout);

        dialog.setContentView(view);
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);

        zdNameTextView.setText(games.get(gPos).getGames().get(cPos).getHometeam());
        kdNameTextView.setText(games.get(gPos).getGames().get(cPos).getGuestteam());


        ((TextView) ((MyCheckLinearLayout) sfLayout.getChildAt(0)).getChildAt(1)).setText(entity.getSpl());
        ((TextView) ((MyCheckLinearLayout) sfLayout.getChildAt(1)).getChildAt(1)).setText(entity.getPpl());
        ((TextView) ((MyCheckLinearLayout) sfLayout.getChildAt(2)).getChildAt(1)).setText(entity.getFpl());
        ((TextView) ((MyCheckLinearLayout) rqsfLayout.getChildAt(0)).getChildAt(1)).setText(entity.getRspl());
        ((TextView) ((MyCheckLinearLayout) rqsfLayout.getChildAt(1)).getChildAt(1)).setText(entity.getRfpl());
        ((TextView) ((MyCheckLinearLayout) rqsfLayout.getChildAt(2)).getChildAt(1)).setText(entity.getRppl());

        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(0)).getChildAt(1)).setText(entity.getBf_1_0());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(1)).getChildAt(1)).setText(entity.getBf_2_0());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(2)).getChildAt(1)).setText(entity.getBf_2_1());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(3)).getChildAt(1)).setText(entity.getBf_3_0());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(4)).getChildAt(1)).setText(entity.getBf_3_1());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(5)).getChildAt(1)).setText(entity.getBf_3_2());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(6)).getChildAt(1)).setText(entity.getBf_4_0());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(7)).getChildAt(1)).setText(entity.getBf_4_1());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(8)).getChildAt(1)).setText(entity.getBf_4_2());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(9)).getChildAt(1)).setText(entity.getBf_5_0());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(10)).getChildAt(1)).setText(entity.getBf_5_1());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(11)).getChildAt(1)).setText(entity.getBf_5_2());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(12)).getChildAt(1)).setText(entity.getBf_sqt());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(13)).getChildAt(1)).setText(entity.getBf_0_0());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(14)).getChildAt(1)).setText(entity.getBf_1_1());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(15)).getChildAt(1)).setText(entity.getBf_2_2());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(16)).getChildAt(1)).setText(entity.getBf_3_3());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(17)).getChildAt(1)).setText(entity.getBf_pqt());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(18)).getChildAt(1)).setText(entity.getBf_0_1());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(19)).getChildAt(1)).setText(entity.getBf_0_2());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(20)).getChildAt(1)).setText(entity.getBf_1_2());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(21)).getChildAt(1)).setText(entity.getBf_0_3());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(22)).getChildAt(1)).setText(entity.getBf_1_3());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(23)).getChildAt(1)).setText(entity.getBf_2_3());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(24)).getChildAt(1)).setText(entity.getBf_0_4());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(25)).getChildAt(1)).setText(entity.getBf_1_4());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(26)).getChildAt(1)).setText(entity.getBf_2_4());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(27)).getChildAt(1)).setText(entity.getBf_0_5());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(28)).getChildAt(1)).setText(entity.getBf_1_5());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(29)).getChildAt(1)).setText(entity.getBf_2_5());
        ((TextView) ((MyCheckLinearLayout) bfLayout.getChildAt(30)).getChildAt(1)).setText(entity.getBf_fqt());

        ((TextView) ((MyCheckLinearLayout) zjqLayout.getChildAt(0)).getChildAt(1)).setText(entity.getJqs_0());
        ((TextView) ((MyCheckLinearLayout) zjqLayout.getChildAt(1)).getChildAt(1)).setText(entity.getJqs_1());
        ((TextView) ((MyCheckLinearLayout) zjqLayout.getChildAt(2)).getChildAt(1)).setText(entity.getJqs_2());
        ((TextView) ((MyCheckLinearLayout) zjqLayout.getChildAt(3)).getChildAt(1)).setText(entity.getJqs_3());
        ((TextView) ((MyCheckLinearLayout) zjqLayout.getChildAt(4)).getChildAt(1)).setText(entity.getJqs_4());
        ((TextView) ((MyCheckLinearLayout) zjqLayout.getChildAt(5)).getChildAt(1)).setText(entity.getJqs_5());
        ((TextView) ((MyCheckLinearLayout) zjqLayout.getChildAt(6)).getChildAt(1)).setText(entity.getJqs_6());
        ((TextView) ((MyCheckLinearLayout) zjqLayout.getChildAt(7)).getChildAt(1)).setText(entity.getJqs_7());

        ((TextView) ((MyCheckLinearLayout) bcqLayout.getChildAt(0)).getChildAt(1)).setText(entity.getBqc_ss());
        ((TextView) ((MyCheckLinearLayout) bcqLayout.getChildAt(1)).getChildAt(1)).setText(entity.getBqc_sp());
        ((TextView) ((MyCheckLinearLayout) bcqLayout.getChildAt(2)).getChildAt(1)).setText(entity.getBqc_sf());
        ((TextView) ((MyCheckLinearLayout) bcqLayout.getChildAt(3)).getChildAt(1)).setText(entity.getBqc_ps());
        ((TextView) ((MyCheckLinearLayout) bcqLayout.getChildAt(4)).getChildAt(1)).setText(entity.getBqc_pf());
        ((TextView) ((MyCheckLinearLayout) bcqLayout.getChildAt(5)).getChildAt(1)).setText(entity.getBqc_pp());
        ((TextView) ((MyCheckLinearLayout) bcqLayout.getChildAt(6)).getChildAt(1)).setText(entity.getBqc_fs());
        ((TextView) ((MyCheckLinearLayout) bcqLayout.getChildAt(7)).getChildAt(1)).setText(entity.getBqc_fp());
        ((TextView) ((MyCheckLinearLayout) bcqLayout.getChildAt(8)).getChildAt(1)).setText(entity.getBqc_ff());

        String rqs = entity.getRqNum();
        if (rqs.contains("-")) {
            rqs = rqs.replace("-", "");
            rqLayout.setBackgroundResource(R.color.app_green_color_1db53a);
            rqTextView.setText("-" + AppUtil.sswrNum(Double.parseDouble(rqs), 0));
        } else if (rqs.contains("+")) {
            rqs = rqs.replace("+", "");
            rqLayout.setBackgroundResource(R.color.app_red_color_fa3243);
            rqTextView.setText("+" + AppUtil.sswrNum(Double.parseDouble(rqs), 0));
        }

        final List<MyCheckLinearLayout> allSelect = new ArrayList<>();
        for (int i = 0; i < sfLayout.getChildCount(); i++) {
            allSelect.add((MyCheckLinearLayout) sfLayout.getChildAt(i));
        }
        for (int i = 0; i < rqsfLayout.getChildCount(); i++) {
            allSelect.add((MyCheckLinearLayout) rqsfLayout.getChildAt(i));
        }
        for (int i = 0; i < bfLayout.getChildCount(); i++) {
            allSelect.add((MyCheckLinearLayout) bfLayout.getChildAt(i));
        }
        for (int i = 0; i < zjqLayout.getChildCount(); i++) {
            allSelect.add((MyCheckLinearLayout) zjqLayout.getChildAt(i));
        }
        for (int i = 0; i < bcqLayout.getChildCount(); i++) {
            allSelect.add((MyCheckLinearLayout) bcqLayout.getChildAt(i));
        }

        for (Integer key : childs.get(gPos).get(cPos).keySet()) {
            if (key > 0 && childs.get(gPos).get(cPos).get(key) == 1) {
                allSelect.get(key - 1).setChecked(false);
                dialoChangeViewState(allSelect.get(key - 1));
            }
        }


        final GridLayout layouts[] = {sfLayout, rqsfLayout, bfLayout, zjqLayout, bcqLayout};

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


        sfLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 0, 0);
            }
        });
        sfLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 0, 1);
            }
        });
        sfLayout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 0, 2);
            }
        });
        rqsfLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 1, 0);
            }
        });
        rqsfLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 1, 1);
            }
        });
        rqsfLayout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 1, 2);
            }
        });

        bfLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 0);
            }
        });
        bfLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 1);
            }
        });
        bfLayout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 2);
            }
        });
        bfLayout.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 3);
            }
        });
        bfLayout.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 4);
            }
        });
        bfLayout.getChildAt(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 5);
            }
        });
        bfLayout.getChildAt(6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 6);
            }
        });
        bfLayout.getChildAt(7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 7);
            }
        });
        bfLayout.getChildAt(8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 8);
            }
        });
        bfLayout.getChildAt(9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 9);
            }
        });
        bfLayout.getChildAt(10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 10);
            }
        });
        bfLayout.getChildAt(11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 11);
            }
        });
        bfLayout.getChildAt(12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 12);
            }
        });
        bfLayout.getChildAt(13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 13);
            }
        });
        bfLayout.getChildAt(14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 14);
            }
        });
        bfLayout.getChildAt(15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 15);
            }
        });
        bfLayout.getChildAt(16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 16);
            }
        });
        bfLayout.getChildAt(17).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 17);
            }
        });
        bfLayout.getChildAt(18).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 18);
            }
        });
        bfLayout.getChildAt(19).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 19);
            }
        });
        bfLayout.getChildAt(20).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 20);
            }
        });
        bfLayout.getChildAt(21).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 21);
            }
        });
        bfLayout.getChildAt(22).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 22);
            }
        });
        bfLayout.getChildAt(23).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 23);
            }
        });
        bfLayout.getChildAt(24).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 24);
            }
        });
        bfLayout.getChildAt(25).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 25);
            }
        });
        bfLayout.getChildAt(26).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 26);
            }
        });
        bfLayout.getChildAt(27).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 27);
            }
        });
        bfLayout.getChildAt(28).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 28);
            }
        });
        bfLayout.getChildAt(29).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 29);
            }
        });
        bfLayout.getChildAt(30).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 2, 30);
            }
        });

        zjqLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 0);
            }
        });
        zjqLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 1);
            }
        });
        zjqLayout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 2);
            }
        });
        zjqLayout.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 3);
            }
        });
        zjqLayout.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 4);
            }
        });
        zjqLayout.getChildAt(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 5);
            }
        });
        zjqLayout.getChildAt(6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 6);
            }
        });
        zjqLayout.getChildAt(7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 3, 7);
            }
        });

        bcqLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 4, 0);
            }
        });
        bcqLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 4, 1);
            }
        });
        bcqLayout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 4, 2);
            }
        });
        bcqLayout.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 4, 3);
            }
        });
        bcqLayout.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 4, 4);
            }
        });
        bcqLayout.getChildAt(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 4, 5);
            }
        });
        bcqLayout.getChildAt(6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 4, 6);
            }
        });
        bcqLayout.getChildAt(7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 4, 7);
            }
        });
        bcqLayout.getChildAt(8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayoutCheckState(layouts, 4, 8);
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
        JzAdapterUtil.clearSelected(childs, JzHhggElvAdapter.this);
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

        private TextView zdNameTextView, kdNameTextView, s_plTextview, p_plTextview, f_plTextview, rq_s_plTextview,
                rq_p_plTextview, rq_f_plTextview, selectedTextView, selectTextview, xqTextView, typeTextView,
                jzrqTextView, rqTextView, xxssfxTextView;
        private ImageView fxImageView;
        private LinearLayout qbxxLayout, fxLayout, sLayout, pLayout, fLayout, rq_sLayout, rq_pLayout, rq_fLayout,
                fxDetailLayout;
        private RelativeLayout rqLayout;
        private LinearLayout[] layouts;

        public ChildViewHolder(View view) {

            qbxxLayout = view.findViewById(R.id.jz_hhgg_elv_qbxx_layout);
            s_plTextview = view.findViewById(R.id.jz_hhgg_elv_frq_s_pl_text);
            p_plTextview = view.findViewById(R.id.jz_hhgg_elv_frq_p_pl_text);
            f_plTextview = view.findViewById(R.id.jz_hhgg_elv_frq_f_pl_text);
            rq_s_plTextview = view.findViewById(R.id.jz_hhgg_elv_rq_s_pl_text);
            rq_p_plTextview = view.findViewById(R.id.jz_hhgg_elv_rq_p_pl_text);
            rq_f_plTextview = view.findViewById(R.id.jz_hhgg_elv_rq_f_pl_text);
            fxImageView = view.findViewById(R.id.jz_hhgg_elv_child_fx_icon);
            rqLayout = view.findViewById(R.id.jz_hhgg_elv_child_rq_layout);
            xqTextView = view.findViewById(R.id.jz_hhgg_elv_child_xq_text);
            typeTextView = view.findViewById(R.id.jz_hhgg_elv_child_type_text);
            jzrqTextView = view.findViewById(R.id.jz_hhgg_elv_child_jzrq_text);
            rqTextView = view.findViewById(R.id.jz_hhgg_elv_child_rq_text);
            zdNameTextView = view.findViewById(R.id.jz_hhgg_elv_child_zd_name_text);
            kdNameTextView = view.findViewById(R.id.jz_hhgg_elv_child_kd_name_text);
            xxssfxTextView = view.findViewById(R.id.jz_hhgg_child_ssfx_text);
            fxLayout = view.findViewById(R.id.jz_hhgg_elv_child_fx_layout);
            sLayout = view.findViewById(R.id.jz_hhgg_elv_frq_s_layout);
            pLayout = view.findViewById(R.id.jz_hhgg_elv_frq_p_layout);
            fLayout = view.findViewById(R.id.jz_hhgg_elv_frq_f_layout);
            rq_sLayout = view.findViewById(R.id.jz_hhgg_elv_rq_s_layout);
            rq_pLayout = view.findViewById(R.id.jz_hhgg_elv_rq_p_layout);
            rq_fLayout = view.findViewById(R.id.jz_hhgg_elv_rq_f_layout);
            fxDetailLayout = view.findViewById(R.id.jz_hhgg_elv_fx_detail_layout);
            selectedTextView = view.findViewById(R.id.jz_hhgg_elv_qbxx_selected_size_text);
            selectTextview = view.findViewById(R.id.jz_hhgg_elv_qbxx_selected_text);
            layouts = new LinearLayout[]{sLayout, pLayout, fLayout, rq_sLayout, rq_pLayout, rq_fLayout};
        }
    }
}