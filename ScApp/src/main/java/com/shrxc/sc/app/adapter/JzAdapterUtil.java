package com.shrxc.sc.app.adapter;

import android.content.Context;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.JzChildGameEntity;
import com.shrxc.sc.app.bean.JzGroupGameEntity;
import com.shrxc.sc.app.bean.SelectOrder;
import com.shrxc.sc.app.utils.AppUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by CH on 2018/9/7.
 */

public class JzAdapterUtil {


    /**
     * 清空选择
     */
    public static void clearSelected(Map<Integer, Map<Integer, Map<Integer, Integer>>> childs, BaseExpandableListAdapter adapter) {

        for (Integer key : childs.keySet()) {
            Map<Integer, Map<Integer, Integer>> gMap = childs.get(key);
            for (Integer ckey : gMap.keySet()) {
                Map<Integer, Integer> cMap = gMap.get(ckey);
                for (Integer index : cMap.keySet()) {
                    if (cMap.get(index) == 1) {
                        cMap.put(index, 0);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }


    public static Map<Integer, Map<Integer, Map<Integer, Integer>>> initChilds(int groupCount, int childCount, int selectCount) {

        Map<Integer, Map<Integer, Map<Integer, Integer>>> childs = new HashMap<>();
        for (int i = 0; i < groupCount; i++) {
            Map<Integer, Map<Integer, Integer>> cPos = new HashMap<>();
            for (int j = 0; j < childCount; j++) {
                Map<Integer, Integer> selects = new HashMap<>();
                for (int k = 0; k < selectCount; k++) {
                    selects.put(k, 0);
                }
                cPos.put(j, selects);
            }
            childs.put(i, cPos);
        }
        return childs;
    }

    public static void initViewState(Context context, int state, LinearLayout layout, int index, int type) {

        TextView[] textViews = getLayoutChild(layout, index, type);
        if (state == 0) {
            if (type == 0 || type == 2 || type == 3) {
                layout.setBackgroundResource(R.drawable.app_gray_stroke_color);
            } else {
                layout.setBackgroundResource(R.color.app_white_color_ffffff);
            }
            for (int i = 0; i < textViews.length; i++) {
                if (i == 0) {
                    textViews[i].setTextColor(context.getResources().getColor(R.color.app_text_color_333333));
                } else {
                    textViews[i].setTextColor(context.getResources().getColor(R.color.app_text_color_999999));
                }
            }
        } else {
            layout.setBackgroundResource(R.color.app_red_color_fa3243);
            for (int i = 0; i < textViews.length; i++) {
                textViews[i].setTextColor(context.getResources().getColor(R.color.app_white_color_ffffff));
            }
        }
    }

    public static void changeViewSet(Context context, BaseExpandableListAdapter adapter, int gPos, int cPos, int index,
                                     Map<Integer, Map<Integer, Map<Integer, Integer>>> childs, List<JzGroupGameEntity> games) {

        if (games != null) {
            List<JzChildGameEntity> list = JzAdapterUtil.getSelectList(childs, games);
            if (list.size() == 8) {
                int isSelect = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (games.get(gPos).getGames().get(cPos).getPlanid().equals(list.get(i).getPlanid())) {
                        isSelect = 1;
                        break;
                    }
                }
                if (isSelect == 0) {
                    Toast.makeText(context, "最多选择8场比赛", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        int state = childs.get(gPos).get(cPos).get(index);
        if (state == 0) {
            childs.get(gPos).get(cPos).put(index, 1);
        } else {
            childs.get(gPos).get(cPos).put(index, 0);
        }
        adapter.notifyDataSetChanged();
    }

    private static TextView[] getLayoutChild(LinearLayout layout, int index, int type) {
        TextView[] textViews = null;
        if (type == 0) {
            if (index == 0) {
                textViews = new TextView[3];
                LinearLayout childLayout = (LinearLayout) layout.getChildAt(0);
                textViews[0] = (TextView) childLayout.getChildAt(1);
                textViews[1] = (TextView) childLayout.getChildAt(0);
                textViews[2] = (TextView) layout.getChildAt(1);
            } else if (index == 1) {
                textViews = new TextView[2];
                textViews[0] = (TextView) layout.getChildAt(0);
                textViews[1] = (TextView) layout.getChildAt(1);
            } else if (index == 2) {
                textViews = new TextView[3];
                LinearLayout childLayout = (LinearLayout) layout.getChildAt(0);
                textViews[0] = (TextView) childLayout.getChildAt(0);
                textViews[1] = (TextView) childLayout.getChildAt(1);
                textViews[2] = (TextView) layout.getChildAt(1);
            }
        } else if (type == 1 || type == 3) {
            int length = layout.getChildCount();
            textViews = new TextView[length];
            for (int i = 0; i < length; i++) {
                textViews[i] = (TextView) layout.getChildAt(i);
            }
        } else if (type == 2) {
            if (index == 0) {
                textViews = new TextView[3];
                LinearLayout childLayout = (LinearLayout) layout.getChildAt(0);
                textViews[0] = (TextView) childLayout.getChildAt(1);
                textViews[1] = (TextView) childLayout.getChildAt(0);
                textViews[2] = (TextView) layout.getChildAt(1);
            } else if (index == 1) {
                LinearLayout childLayout = (LinearLayout) layout.getChildAt(0);
                int length = childLayout.getChildCount() + 1;
                textViews = new TextView[length];
                for (int i = 0; i < childLayout.getChildCount(); i++) {
                    textViews[i] = (TextView) childLayout.getChildAt(i);
                }
                textViews[length - 1] = (TextView) layout.getChildAt(1);
            }
        }
        return textViews;
    }


    public static List<JzChildGameEntity> getSelectList(Map<Integer, Map<Integer, Map<Integer, Integer>>> childs, List<JzGroupGameEntity> games) {
        List<JzChildGameEntity> orderList = new ArrayList<>();
        List<JzChildGameEntity> selectedList = new ArrayList<>();
        Map<String, List<JzChildGameEntity>> selectMap = new ConcurrentHashMap<>();
        for (Integer key : childs.keySet()) {
            Map<Integer, Map<Integer, Integer>> gMap = childs.get(key);
            for (Integer ckey : gMap.keySet()) {
                Map<Integer, Integer> cMap = gMap.get(ckey);
                for (Integer index : cMap.keySet()) {
                    if (cMap.get(index) == 1) {
                        JzChildGameEntity entity = games.get(key).getGames().get(ckey);
                        List<Integer> selects = entity.getSelectedList();
                        if (selects == null) {
                            selects = new ArrayList<>();
                        }
                        selects.add(index);
                        removeDuplicate(selects);
                        entity.setSelectedList(selects);
                        selectedList.add(entity);
                        String pid = games.get(key).getGames().get(ckey).getPlanid();
                        selectMap.put(pid, new ArrayList<JzChildGameEntity>());
                    }
                }
            }
        }

        for (Map.Entry<String, List<JzChildGameEntity>> selected : selectMap.entrySet()) {
            String pid = selected.getKey();
            for (int i = 0; i < selectedList.size(); i++) {
                if (selectedList.get(i).getPlanid() == pid) {
                    List<JzChildGameEntity> select = selected.getValue();
                    JzChildGameEntity entity = selectedList.get(i);
                    select.add(entity);
                    selectMap.put(pid, select);
                }
            }
        }
        for (Map.Entry<String, List<JzChildGameEntity>> selected : selectMap.entrySet()) {
            JzChildGameEntity entity = selected.getValue().get(0);
            orderList.add(entity);
        }
        return orderList;
    }


    public static SelectOrder getUpOrderType(int type, int ggfs, JzChildGameEntity entity) {

        SelectOrder select = new SelectOrder();

        switch (ggfs) {
            case 0:
                switch (type) {
                    case 1://胜
                        select.setSpf("h");
                        select.setPl(entity.getSpl());
                        break;
                    case 2://平
                        select.setSpf("d");
                        select.setPl(entity.getPpl());
                        break;
                    case 3://负
                        select.setSpf("a");
                        select.setPl(entity.getFpl());
                        break;
                }
                break;
            case 1:
                switch (type) {
                    case 1:
                        select.setRspf("h");
                        select.setRangqiu(entity.getRangqiu());
                        select.setPl(entity.getRspl());
                        break;
                    case 2:
                        select.setRangqiu(entity.getRangqiu());
                        select.setRspf("d");
                        select.setPl(entity.getRppl());
                        break;
                    case 3:
                        select.setRangqiu(entity.getRangqiu());
                        select.setRspf("a");
                        select.setPl(entity.getRfpl());
                        break;
                }
                break;
            case 2:
                switch (type) {
                    case 1:
                        select.setSpf("h");
                        select.setPl(entity.getSpl());
                        break;
                    case 2:
                        select.setSpf("d");
                        select.setPl(entity.getPpl());
                        break;
                    case 3:
                        select.setSpf("a");
                        select.setPl(entity.getFpl());
                        break;
                    case 4:
                        select.setRspf("h");
                        select.setRangqiu(entity.getRangqiu());
                        select.setPl(entity.getRspl());
                        break;
                    case 5:
                        select.setRangqiu(entity.getRangqiu());
                        select.setRspf("d");
                        select.setPl(entity.getRppl());
                        break;
                    case 6:
                        select.setRangqiu(entity.getRangqiu());
                        select.setRspf("a");
                        select.setPl(entity.getRfpl());
                        break;
                    case 7:
                        select.setBf("s0100");
                        select.setPl(entity.getTzTypeEntity().getBf_1_0());
                        break;
                    case 8:
                        select.setBf("s0200");
                        select.setPl(entity.getTzTypeEntity().getBf_2_0());
                        break;
                    case 9:
                        select.setBf("s0201");
                        select.setPl(entity.getTzTypeEntity().getBf_2_1());
                        break;
                    case 10:
                        select.setBf("s0300");
                        select.setPl(entity.getTzTypeEntity().getBf_3_0());
                        break;
                    case 11:
                        select.setBf("s0301");
                        select.setPl(entity.getTzTypeEntity().getBf_3_1());
                        break;
                    case 12:
                        select.setBf("s0302");
                        select.setPl(entity.getTzTypeEntity().getBf_3_2());
                        break;
                    case 13:
                        select.setBf("s0400");
                        select.setPl(entity.getTzTypeEntity().getBf_4_0());
                        break;
                    case 14:
                        select.setBf("s0401");
                        select.setPl(entity.getTzTypeEntity().getBf_4_1());
                        break;
                    case 15:
                        select.setBf("s0402");
                        select.setPl(entity.getTzTypeEntity().getBf_4_2());
                        break;
                    case 16:
                        select.setBf("s0500");
                        select.setPl(entity.getTzTypeEntity().getBf_5_0());
                        break;
                    case 17:
                        select.setBf("s0501");
                        select.setPl(entity.getTzTypeEntity().getBf_5_1());
                        break;
                    case 18:
                        select.setBf("s0502");
                        select.setPl(entity.getTzTypeEntity().getBf_5_2());
                        break;
                    case 19:
                        select.setBf("s_1_h");
                        select.setPl(entity.getTzTypeEntity().getBf_sqt());
                        break;
                    case 20:
                        select.setBf("s0000");
                        select.setPl(entity.getTzTypeEntity().getBf_0_0());
                        break;
                    case 21:
                        select.setBf("s0101");
                        select.setPl(entity.getTzTypeEntity().getBf_1_1());
                        break;
                    case 22:
                        select.setBf("s0202");
                        select.setPl(entity.getTzTypeEntity().getBf_2_2());
                        break;
                    case 23:
                        select.setBf("s0303");
                        select.setPl(entity.getTzTypeEntity().getBf_3_3());
                        break;
                    case 24:
                        select.setBf("s_1_d");
                        select.setPl(entity.getTzTypeEntity().getBf_pqt());
                        break;
                    case 25:
                        select.setBf("s0001");
                        select.setPl(entity.getTzTypeEntity().getBf_0_1());
                        break;
                    case 26:
                        select.setBf("s0002");
                        select.setPl(entity.getTzTypeEntity().getBf_0_2());
                        break;
                    case 27:
                        select.setBf("s0102");
                        select.setPl(entity.getTzTypeEntity().getBf_1_2());
                        break;
                    case 28:
                        select.setBf("s0003");
                        select.setPl(entity.getTzTypeEntity().getBf_0_3());
                        break;
                    case 29:
                        select.setBf("s0103");
                        select.setPl(entity.getTzTypeEntity().getBf_1_3());
                        break;
                    case 30:
                        select.setBf("s0203");
                        select.setPl(entity.getTzTypeEntity().getBf_2_3());
                        break;
                    case 31:
                        select.setBf("s0004");
                        select.setPl(entity.getTzTypeEntity().getBf_0_4());
                        break;
                    case 32:
                        select.setBf("s0104");
                        select.setPl(entity.getTzTypeEntity().getBf_1_4());
                        break;
                    case 33:
                        select.setBf("s0204");
                        select.setPl(entity.getTzTypeEntity().getBf_2_4());
                        break;
                    case 34:
                        select.setBf("s0005");
                        select.setPl(entity.getTzTypeEntity().getBf_0_5());
                        break;
                    case 35:
                        select.setBf("s0105");
                        select.setPl(entity.getTzTypeEntity().getBf_1_5());
                        break;
                    case 36:
                        select.setBf("s0205");
                        select.setPl(entity.getTzTypeEntity().getBf_2_5());
                        break;
                    case 37:
                        select.setBf("s_1_a");
                        select.setPl(entity.getTzTypeEntity().getBf_fqt());
                        break;
                    case 38:
                        select.setJq("s0");
                        select.setPl(entity.getTzTypeEntity().getJqs_0());
                        break;
                    case 39:
                        select.setJq("s1");
                        select.setPl(entity.getTzTypeEntity().getJqs_1());
                        break;
                    case 40:
                        select.setJq("s2");
                        select.setPl(entity.getTzTypeEntity().getJqs_2());
                        break;
                    case 41:
                        select.setJq("s3");
                        select.setPl(entity.getTzTypeEntity().getJqs_3());
                        break;
                    case 42:
                        select.setJq("s4");
                        select.setPl(entity.getTzTypeEntity().getJqs_4());
                        break;
                    case 43:
                        select.setJq("s5");
                        select.setPl(entity.getTzTypeEntity().getJqs_5());
                        break;
                    case 44:
                        select.setJq("s6");
                        select.setPl(entity.getTzTypeEntity().getJqs_6());
                        break;
                    case 45:
                        select.setJq("s7");
                        select.setPl(entity.getTzTypeEntity().getJqs_7());
                        break;
                    case 46:
                        select.setBqc("hh");
                        select.setPl(entity.getTzTypeEntity().getBqc_ss());
                        break;
                    case 47:
                        select.setBqc("hd");
                        select.setPl(entity.getTzTypeEntity().getBqc_sp());
                        break;
                    case 48:
                        select.setBqc("ha");
                        select.setPl(entity.getTzTypeEntity().getBqc_sf());
                        break;
                    case 49:
                        select.setBqc("dh");
                        select.setPl(entity.getTzTypeEntity().getBqc_ps());
                        break;
                    case 50:
                        select.setBqc("dd");
                        select.setPl(entity.getTzTypeEntity().getBqc_pp());
                        break;
                    case 51:
                        select.setBqc("da");
                        select.setPl(entity.getTzTypeEntity().getBqc_pf());
                        break;
                    case 52:
                        select.setBqc("ah");
                        select.setPl(entity.getTzTypeEntity().getBqc_fs());
                        break;
                    case 53:
                        select.setBqc("ad");
                        select.setPl(entity.getTzTypeEntity().getBqc_fp());
                        break;
                    case 54:
                        select.setBqc("aa");
                        select.setPl(entity.getTzTypeEntity().getBqc_ff());
                        break;
                }
                break;
            case 3:
                switch (type) {
                    case 1:
                        select.setBf("s0100");
                        select.setPl(entity.getTzTypeEntity().getBf_1_0());
                        break;
                    case 2:
                        select.setBf("s0200");
                        select.setPl(entity.getTzTypeEntity().getBf_2_0());
                        break;
                    case 3:
                        select.setBf("s0201");
                        select.setPl(entity.getTzTypeEntity().getBf_2_1());
                        break;
                    case 4:
                        select.setBf("s0300");
                        select.setPl(entity.getTzTypeEntity().getBf_3_0());
                        break;
                    case 5:
                        select.setBf("s0301");
                        select.setPl(entity.getTzTypeEntity().getBf_3_1());
                        break;
                    case 6:
                        select.setBf("s0302");
                        select.setPl(entity.getTzTypeEntity().getBf_3_2());
                        break;
                    case 7:
                        select.setBf("s0400");
                        select.setPl(entity.getTzTypeEntity().getBf_4_0());
                        break;
                    case 8:
                        select.setBf("s0401");
                        select.setPl(entity.getTzTypeEntity().getBf_4_1());
                        break;
                    case 9:
                        select.setBf("s0402");
                        select.setPl(entity.getTzTypeEntity().getBf_4_2());
                        break;
                    case 10:
                        select.setBf("s0500");
                        select.setPl(entity.getTzTypeEntity().getBf_5_0());
                        break;
                    case 11:
                        select.setBf("s0501");
                        select.setPl(entity.getTzTypeEntity().getBf_5_1());
                        break;
                    case 12:
                        select.setBf("s0502");
                        select.setPl(entity.getTzTypeEntity().getBf_5_2());
                        break;
                    case 13:
                        select.setBf("s_1_h");
                        select.setPl(entity.getTzTypeEntity().getBf_sqt());
                        break;
                    case 14:
                        select.setBf("s0000");
                        select.setPl(entity.getTzTypeEntity().getBf_0_0());
                        break;
                    case 15:
                        select.setBf("s0101");
                        select.setPl(entity.getTzTypeEntity().getBf_1_1());
                        break;
                    case 16:
                        select.setBf("s0202");
                        select.setPl(entity.getTzTypeEntity().getBf_2_2());
                        break;
                    case 17:
                        select.setBf("s0303");
                        select.setPl(entity.getTzTypeEntity().getBf_3_3());
                        break;
                    case 18:
                        select.setBf("s_1_d");
                        select.setPl(entity.getTzTypeEntity().getBf_pqt());
                        break;
                    case 19:
                        select.setBf("s0001");
                        select.setPl(entity.getTzTypeEntity().getBf_0_1());
                        break;
                    case 20:
                        select.setBf("s0002");
                        select.setPl(entity.getTzTypeEntity().getBf_0_2());
                        break;
                    case 21:
                        select.setBf("s0102");
                        select.setPl(entity.getTzTypeEntity().getBf_1_2());
                        break;
                    case 22:
                        select.setBf("s0003");
                        select.setPl(entity.getTzTypeEntity().getBf_0_3());
                        break;
                    case 23:
                        select.setBf("s0103");
                        select.setPl(entity.getTzTypeEntity().getBf_1_3());
                        break;
                    case 24:
                        select.setBf("s0203");
                        select.setPl(entity.getTzTypeEntity().getBf_2_3());
                        break;
                    case 25:
                        select.setBf("s0004");
                        select.setPl(entity.getTzTypeEntity().getBf_0_4());
                        break;
                    case 26:
                        select.setBf("s0104");
                        select.setPl(entity.getTzTypeEntity().getBf_1_4());
                        break;
                    case 27:
                        select.setBf("s0204");
                        select.setPl(entity.getTzTypeEntity().getBf_2_4());
                        break;
                    case 28:
                        select.setBf("s0005");
                        select.setPl(entity.getTzTypeEntity().getBf_0_5());
                        break;
                    case 29:
                        select.setBf("s0105");
                        select.setPl(entity.getTzTypeEntity().getBf_1_5());
                        break;
                    case 30:
                        select.setBf("s0205");
                        select.setPl(entity.getTzTypeEntity().getBf_2_5());
                        break;
                    case 31:
                        select.setBf("s_1_a");
                        select.setPl(entity.getTzTypeEntity().getBf_fqt());
                        break;
                }
                break;
            case 4:
                switch (type) {
                    case 1:
                        select.setJq("s0");
                        select.setPl(entity.getTzTypeEntity().getJqs_0());
                        break;
                    case 2:
                        select.setJq("s1");
                        select.setPl(entity.getTzTypeEntity().getJqs_1());
                        break;
                    case 3:
                        select.setJq("s2");
                        select.setPl(entity.getTzTypeEntity().getJqs_2());
                        break;
                    case 4:
                        select.setJq("s3");
                        select.setPl(entity.getTzTypeEntity().getJqs_3());
                        break;
                    case 5:
                        select.setJq("s4");
                        select.setPl(entity.getTzTypeEntity().getJqs_4());
                        break;
                    case 6:
                        select.setJq("s5");
                        select.setPl(entity.getTzTypeEntity().getJqs_5());
                        break;
                    case 7:
                        select.setJq("s6");
                        select.setPl(entity.getTzTypeEntity().getJqs_6());
                        break;
                    case 8:
                        select.setJq("s7");
                        select.setPl(entity.getTzTypeEntity().getJqs_7());
                        break;
                }
                break;
            case 5:
                switch (type) {
                    case 1:
                        select.setBqc("hh");
                        select.setPl(entity.getTzTypeEntity().getBqc_ss());
                        break;
                    case 2:
                        select.setBqc("hd");
                        select.setPl(entity.getTzTypeEntity().getBqc_sp());
                        break;
                    case 3:
                        select.setBqc("ha");
                        select.setPl(entity.getTzTypeEntity().getBqc_sf());
                        break;
                    case 4:
                        select.setBqc("dh");
                        select.setPl(entity.getTzTypeEntity().getBqc_ps());
                        break;
                    case 5:
                        select.setBqc("dd");
                        select.setPl(entity.getTzTypeEntity().getBqc_pp());
                        break;
                    case 6:
                        select.setBqc("da");
                        select.setPl(entity.getTzTypeEntity().getBqc_pf());
                        break;
                    case 7:
                        select.setBqc("ah");
                        select.setPl(entity.getTzTypeEntity().getBqc_fs());
                        break;
                    case 8:
                        select.setBqc("ad");
                        select.setPl(entity.getTzTypeEntity().getBqc_fp());
                        break;
                    case 9:
                        select.setBqc("aa");
                        select.setPl(entity.getTzTypeEntity().getBqc_ff());
                        break;
                }
                break;
            case 6:
                switch (type) {
                    case 1:
                        select.setSpf("h");
                        select.setPl(entity.getSpl());
                        break;
                    case 2:
                        select.setSpf("d");
                        select.setPl(entity.getPpl());
                        break;
                    case 3:
                        select.setSpf("a");
                        select.setPl(entity.getFpl());
                        break;
                    case 4:
                        select.setBf("s0100");
                        select.setPl(entity.getTzTypeEntity().getBf_1_0());
                        break;
                    case 5:
                        select.setBf("s0200");
                        select.setPl(entity.getTzTypeEntity().getBf_2_0());
                        break;
                    case 6:
                        select.setBf("s0201");
                        select.setPl(entity.getTzTypeEntity().getBf_2_1());
                        break;
                    case 7:
                        select.setBf("s0300");
                        select.setPl(entity.getTzTypeEntity().getBf_3_0());
                        break;
                    case 8:
                        select.setBf("s0301");
                        select.setPl(entity.getTzTypeEntity().getBf_3_1());
                        break;
                    case 9:
                        select.setBf("s0302");
                        select.setPl(entity.getTzTypeEntity().getBf_3_2());
                        break;
                    case 10:
                        select.setBf("s0400");
                        select.setPl(entity.getTzTypeEntity().getBf_4_0());
                        break;
                    case 11:
                        select.setBf("s0401");
                        select.setPl(entity.getTzTypeEntity().getBf_4_1());
                        break;
                    case 12:
                        select.setBf("s0402");
                        select.setPl(entity.getTzTypeEntity().getBf_4_2());
                        break;
                    case 13:
                        select.setBf("s0500");
                        select.setPl(entity.getTzTypeEntity().getBf_5_0());
                        break;
                    case 14:
                        select.setBf("s0501");
                        select.setPl(entity.getTzTypeEntity().getBf_5_1());
                        break;
                    case 15:
                        select.setBf("s0502");
                        select.setPl(entity.getTzTypeEntity().getBf_5_2());
                        break;
                    case 16:
                        select.setBf("s_1_h");
                        select.setPl(entity.getTzTypeEntity().getBf_sqt());
                        break;
                    case 17:
                        select.setBf("s0000");
                        select.setPl(entity.getTzTypeEntity().getBf_0_0());
                        break;
                    case 18:
                        select.setBf("s0101");
                        select.setPl(entity.getTzTypeEntity().getBf_1_1());
                        break;
                    case 19:
                        select.setBf("s0202");
                        select.setPl(entity.getTzTypeEntity().getBf_2_2());
                        break;
                    case 20:
                        select.setBf("s0303");
                        select.setPl(entity.getTzTypeEntity().getBf_3_3());
                        break;
                    case 21:
                        select.setBf("s_1_d");
                        select.setPl(entity.getTzTypeEntity().getBf_pqt());
                        break;
                    case 22:
                        select.setBf("s0001");
                        select.setPl(entity.getTzTypeEntity().getBf_0_1());
                        break;
                    case 23:
                        select.setBf("s0002");
                        select.setPl(entity.getTzTypeEntity().getBf_0_2());
                        break;
                    case 24:
                        select.setBf("s0102");
                        select.setPl(entity.getTzTypeEntity().getBf_1_2());
                        break;
                    case 25:
                        select.setBf("s0003");
                        select.setPl(entity.getTzTypeEntity().getBf_0_3());
                        break;
                    case 26:
                        select.setBf("s0103");
                        select.setPl(entity.getTzTypeEntity().getBf_1_3());
                        break;
                    case 27:
                        select.setBf("s0203");
                        select.setPl(entity.getTzTypeEntity().getBf_2_3());
                        break;
                    case 28:
                        select.setBf("s0004");
                        select.setPl(entity.getTzTypeEntity().getBf_0_4());
                        break;
                    case 29:
                        select.setBf("s0104");
                        select.setPl(entity.getTzTypeEntity().getBf_1_4());
                        break;
                    case 30:
                        select.setBf("s0204");
                        select.setPl(entity.getTzTypeEntity().getBf_2_4());
                        break;
                    case 31:
                        select.setBf("s0005");
                        select.setPl(entity.getTzTypeEntity().getBf_0_5());
                        break;
                    case 32:
                        select.setBf("s0105");
                        select.setPl(entity.getTzTypeEntity().getBf_1_5());
                        break;
                    case 33:
                        select.setBf("s0205");
                        select.setPl(entity.getTzTypeEntity().getBf_2_5());
                        break;
                    case 34:
                        select.setBf("s_1_a");
                        select.setPl(entity.getTzTypeEntity().getBf_fqt());
                        break;
                    case 35:
                        select.setJq("s0");
                        select.setPl(entity.getTzTypeEntity().getJqs_0());
                        break;
                    case 36:
                        select.setJq("s1");
                        select.setPl(entity.getTzTypeEntity().getJqs_1());
                        break;
                    case 37:
                        select.setJq("s2");
                        select.setPl(entity.getTzTypeEntity().getJqs_2());
                        break;
                    case 38:
                        select.setJq("s3");
                        select.setPl(entity.getTzTypeEntity().getJqs_3());
                        break;
                    case 39:
                        select.setJq("s4");
                        select.setPl(entity.getTzTypeEntity().getJqs_4());
                        break;
                    case 40:
                        select.setJq("s5");
                        select.setPl(entity.getTzTypeEntity().getJqs_5());
                        break;
                    case 41:
                        select.setJq("s6");
                        select.setPl(entity.getTzTypeEntity().getJqs_6());
                        break;
                    case 42:
                        select.setJq("s7");
                        select.setPl(entity.getTzTypeEntity().getJqs_7());
                        break;
                    case 43:
                        select.setBqc("hh");
                        select.setPl(entity.getTzTypeEntity().getBqc_ss());
                        break;
                    case 44:
                        select.setBqc("hd");
                        select.setPl(entity.getTzTypeEntity().getBqc_sp());
                        break;
                    case 45:
                        select.setBqc("ha");
                        select.setPl(entity.getTzTypeEntity().getBqc_sf());
                        break;
                    case 46:
                        select.setBqc("dh");
                        select.setPl(entity.getTzTypeEntity().getBqc_ps());
                        break;
                    case 47:
                        select.setBqc("dd");
                        select.setPl(entity.getTzTypeEntity().getBqc_pp());
                        break;
                    case 48:
                        select.setBqc("da");
                        select.setPl(entity.getTzTypeEntity().getBqc_pf());
                        break;
                    case 49:
                        select.setBqc("ah");
                        select.setPl(entity.getTzTypeEntity().getBqc_fs());
                        break;
                    case 50:
                        select.setBqc("ad");
                        select.setPl(entity.getTzTypeEntity().getBqc_fp());
                        break;
                    case 51:
                        select.setBqc("aa");
                        select.setPl(entity.getTzTypeEntity().getBqc_ff());
                        break;
                }
                break;
        }
        return select;
    }


    public static String getGgfs(int ggfs) {

        String ggfsStr = "";
        switch (ggfs) {
            case 0:
                ggfsStr = "胜平负";
                break;
            case 1:
                ggfsStr = "让球胜平负";
                break;
            case 2:
                ggfsStr = "混合过关";
                break;
            case 3:
                ggfsStr = "比分";
                break;
            case 4:
                ggfsStr = "进球数";
                break;
            case 5:
                ggfsStr = "半全场";
                break;
            case 6:
                ggfsStr = "单关投注";
                break;
        }
        return ggfsStr;
    }

    public static String getGgfsString(int ggfs) {

        String ggfsStr = "";
        switch (ggfs) {
            case 6:
                ggfsStr = "胜平负";
                break;
            case 40:
                ggfsStr = "让球胜平负";
                break;
            case 10:
                ggfsStr = "混合过关";
                break;
            case 7:
                ggfsStr = "比分";
                break;
            case 8:
                ggfsStr = "进球数";
                break;
            case 9:
                ggfsStr = "半全场";
                break;
            case 39:
                ggfsStr = "单关投注";
                break;
        }
        return ggfsStr;
    }


    public static String[] getSelectType(int type, int ggfs, JzChildGameEntity entity) {

        String[] typeStr = new String[2];
        String select = "";
        switch (ggfs) {
            case 0:
                switch (type) {
                    case 1:
                        typeStr[0] = "胜";
                        typeStr[1] = entity.getSpl();
                        break;
                    case 2:
                        typeStr[0] = "平";
                        typeStr[1] = entity.getPpl();
                        break;
                    case 3:
                        typeStr[0] = "负";
                        typeStr[1] = entity.getFpl();
                        break;
                }
                break;
            case 1:
                String rqs = entity.getRangqiu();
                if (rqs.contains("-")) {
                    rqs = rqs.replace("-", "");
                    select = "主-" + AppUtil.sswrNum(Double.parseDouble(rqs), 0) + "  ";
                } else if (rqs.contains("+")) {
                    rqs = rqs.replace("+", "");
                    select = "主+" + AppUtil.sswrNum(Double.parseDouble(rqs), 0) + "  ";
                }
                switch (type) {
                    case 1:
                        typeStr[0] = select + "胜";
                        typeStr[1] = entity.getRspl();
                        break;
                    case 2:
                        typeStr[0] = select + "平";
                        typeStr[1] = entity.getRppl();
                        break;
                    case 3:
                        typeStr[0] = select + "负";
                        typeStr[1] = entity.getRfpl();
                        break;
                }
                break;
            case 2:
                String rq = entity.getRangqiu();
                if (rq.contains("-")) {
                    rq = rq.replace("-", "");
                    select = "主-" + AppUtil.sswrNum(Double.parseDouble(rq), 0) + "  ";
                } else if (rq.contains("+")) {
                    rq = rq.replace("+", "");
                    select = "主+" + AppUtil.sswrNum(Double.parseDouble(rq), 0) + "  ";
                }
                switch (type) {
                    case 1:
                        typeStr[0] = "胜";
                        typeStr[1] = entity.getSpl();
                        break;
                    case 2:
                        typeStr[0] = "平";
                        typeStr[1] = entity.getPpl();
                        break;
                    case 3:
                        typeStr[0] = "负";
                        typeStr[1] = entity.getFpl();
                        break;
                    case 4:
                        typeStr[0] = select + "胜";
                        typeStr[1] = entity.getRspl();
                        break;
                    case 5:
                        typeStr[0] = select + "平";
                        typeStr[1] = entity.getRppl();
                        break;
                    case 6:
                        typeStr[0] = select + "负";
                        typeStr[1] = entity.getRfpl();
                        break;
                    case 7:
                        typeStr[0] = "1:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_0();
                        break;
                    case 8:
                        typeStr[0] = "2:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_0();
                        break;
                    case 9:
                        typeStr[0] = "2:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_1();
                        break;
                    case 10:
                        typeStr[0] = "3:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_3_0();
                        break;
                    case 11:
                        typeStr[0] = "3:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_3_1();
                        break;
                    case 12:
                        typeStr[0] = "3:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_3_2();
                        break;
                    case 13:
                        typeStr[0] = "4:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_4_0();
                        break;
                    case 14:
                        typeStr[0] = "4:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_4_1();
                        break;
                    case 15:
                        typeStr[0] = "4:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_4_2();
                        break;
                    case 16:
                        typeStr[0] = "5:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_5_0();
                        break;
                    case 17:
                        typeStr[0] = "5:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_5_1();
                        break;
                    case 18:
                        typeStr[0] = "5:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_5_2();
                        break;
                    case 19:
                        typeStr[0] = "胜其它";
                        typeStr[1] = entity.getTzTypeEntity().getBf_sqt();
                        break;
                    case 20:
                        typeStr[0] = "0:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_0();
                        break;
                    case 21:
                        typeStr[0] = "1:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_1();
                        break;
                    case 22:
                        typeStr[0] = "2:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_2();
                        break;
                    case 23:
                        typeStr[0] = "3:3";
                        typeStr[1] = entity.getTzTypeEntity().getBf_3_3();
                        break;
                    case 24:
                        typeStr[0] = "平其它";
                        typeStr[1] = entity.getTzTypeEntity().getBf_pqt();
                        break;
                    case 25:
                        typeStr[0] = "0:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_1();
                        break;
                    case 26:
                        typeStr[0] = "0:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_2();
                        break;
                    case 27:
                        typeStr[0] = "1:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_2();
                        break;
                    case 28:
                        typeStr[0] = "0:3";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_3();
                        break;
                    case 29:
                        typeStr[0] = "1:3";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_3();
                        break;
                    case 30:
                        typeStr[0] = "2:3";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_3();
                        break;
                    case 31:
                        typeStr[0] = "0:4";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_4();
                        break;
                    case 32:
                        typeStr[0] = "1:4";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_4();
                        break;
                    case 33:
                        typeStr[0] = "2:4";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_4();
                        break;
                    case 34:
                        typeStr[0] = "0:5";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_5();
                        break;
                    case 35:
                        typeStr[0] = "1:5";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_5();
                        break;
                    case 36:
                        typeStr[0] = "2:5";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_5();
                        break;
                    case 37:
                        typeStr[0] = "负其它";
                        typeStr[1] = entity.getTzTypeEntity().getBf_fqt();
                        break;
                    case 38:
                        typeStr[0] = "0";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_0();
                        break;
                    case 39:
                        typeStr[0] = "1";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_1();
                        break;
                    case 40:
                        typeStr[0] = "2";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_2();
                        break;
                    case 41:
                        typeStr[0] = "3";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_3();
                        break;
                    case 42:
                        typeStr[0] = "4";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_4();
                        break;
                    case 43:
                        typeStr[0] = "5";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_5();
                        break;
                    case 44:
                        typeStr[0] = "6";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_6();
                        break;
                    case 45:
                        typeStr[0] = "7+";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_7();
                        break;
                    case 46:
                        typeStr[0] = "胜胜";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_ss();
                        break;
                    case 47:
                        typeStr[0] = "胜平";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_sp();
                        break;
                    case 48:
                        typeStr[0] = "胜负";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_sf();
                        break;
                    case 49:
                        typeStr[0] = "平胜";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_ps();
                        break;
                    case 50:
                        typeStr[0] = "平平";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_pp();
                        break;
                    case 51:
                        typeStr[0] = "平负";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_pf();
                        break;
                    case 52:
                        typeStr[0] = "负胜";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_fs();
                        break;
                    case 53:
                        typeStr[0] = "负平";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_fp();
                        break;
                    case 54:
                        typeStr[0] = "负负";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_ff();
                        break;
                }
                break;
            case 3:
                switch (type) {
                    case 1:
                        typeStr[0] = "1:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_0();
                        break;
                    case 2:
                        typeStr[0] = "2:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_0();
                        break;
                    case 3:
                        typeStr[0] = "2:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_1();
                        break;
                    case 4:
                        typeStr[0] = "3:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_3_0();
                        break;
                    case 5:
                        typeStr[0] = "3:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_3_1();
                        break;
                    case 6:
                        typeStr[0] = "3:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_3_2();
                        break;
                    case 7:
                        typeStr[0] = "4:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_4_0();
                        break;
                    case 8:
                        typeStr[0] = "4:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_4_1();
                        break;
                    case 9:
                        typeStr[0] = "4:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_4_2();
                        break;
                    case 10:
                        typeStr[0] = "5:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_5_0();
                        break;
                    case 11:
                        typeStr[0] = "5:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_5_1();
                        break;
                    case 12:
                        typeStr[0] = "5:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_5_2();
                        break;
                    case 13:
                        typeStr[0] = "胜其它";
                        typeStr[1] = entity.getTzTypeEntity().getBf_sqt();
                        break;
                    case 14:
                        typeStr[0] = "0:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_0();
                        break;
                    case 15:
                        typeStr[0] = "1:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_1();
                        break;
                    case 16:
                        typeStr[0] = "2:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_2();
                        break;
                    case 17:
                        typeStr[0] = "3:3";
                        typeStr[1] = entity.getTzTypeEntity().getBf_3_3();
                        break;
                    case 18:
                        typeStr[0] = "平其它";
                        typeStr[1] = entity.getTzTypeEntity().getBf_pqt();
                        break;
                    case 19:
                        typeStr[0] = "0:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_1();
                        break;
                    case 20:
                        typeStr[0] = "0:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_2();
                        break;
                    case 21:
                        typeStr[0] = "1:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_2();
                        break;
                    case 22:
                        typeStr[0] = "0:3";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_3();
                        break;
                    case 23:
                        typeStr[0] = "1:3";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_3();
                        break;
                    case 24:
                        typeStr[0] = "2:3";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_3();
                        break;
                    case 25:
                        typeStr[0] = "0:4";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_4();
                        break;
                    case 26:
                        typeStr[0] = "1:4";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_4();
                        break;
                    case 27:
                        typeStr[0] = "2:4";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_4();
                        break;
                    case 28:
                        typeStr[0] = "0:5";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_5();
                        break;
                    case 29:
                        typeStr[0] = "1:5";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_5();
                        break;
                    case 30:
                        typeStr[0] = "2:5";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_5();
                        break;
                    case 31:
                        typeStr[0] = "负其它";
                        typeStr[1] = entity.getTzTypeEntity().getBf_fqt();
                        break;
                }
                break;
            case 4:
                switch (type) {
                    case 1:
                        typeStr[0] = "0";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_0();
                        break;
                    case 2:
                        typeStr[0] = "1";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_1();
                        break;
                    case 3:
                        typeStr[0] = "2";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_2();
                        break;
                    case 4:
                        typeStr[0] = "3";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_3();
                        break;
                    case 5:
                        typeStr[0] = "4";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_4();
                        break;
                    case 6:
                        typeStr[0] = "5";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_5();
                        break;
                    case 7:
                        typeStr[0] = "6";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_6();
                        break;
                    case 8:
                        typeStr[0] = "7+";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_7();
                        break;
                }
                break;
            case 5:
                switch (type) {
                    case 1:
                        typeStr[0] = "胜胜";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_ss();
                        break;
                    case 2:
                        typeStr[0] = "胜平";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_sp();
                        break;
                    case 3:
                        typeStr[0] = "胜负";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_sf();
                        break;
                    case 4:
                        typeStr[0] = "平胜";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_ps();
                        break;
                    case 5:
                        typeStr[0] = "平平";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_pp();
                        break;
                    case 6:
                        typeStr[0] = "平负";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_pf();
                        break;
                    case 7:
                        typeStr[0] = "负胜";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_fs();
                        break;
                    case 8:
                        typeStr[0] = "负平";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_fp();
                        break;
                    case 9:
                        typeStr[0] = "负负";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_ff();
                        break;
                }
                break;
            case 6:
                String dgrq = entity.getRangqiu();
                if (dgrq.contains("-")) {
                    dgrq = dgrq.replace("-", "");
                    select = "主-" + AppUtil.sswrNum(Double.parseDouble(dgrq), 0) + "  ";
                } else if (dgrq.contains("+")) {
                    dgrq = dgrq.replace("+", "");
                    select = "主+" + AppUtil.sswrNum(Double.parseDouble(dgrq), 0) + "  ";
                }
                switch (type) {
                    case 1:
                        typeStr[0] = "胜";
                        typeStr[1] = entity.getSpl();
                        break;
                    case 2:
                        typeStr[0] = "平";
                        typeStr[1] = entity.getPpl();
                        break;
                    case 3:
                        typeStr[0] = "负";
                        typeStr[1] = entity.getFpl();
                        break;
                    case 4:
                        typeStr[0] = select + "胜";
                        typeStr[1] = entity.getRspl();
                        break;
                    case 5:
                        typeStr[0] = select + "平";
                        typeStr[1] = entity.getRppl();
                        break;
                    case 6:
                        typeStr[0] = select + "负";
                        typeStr[1] = entity.getRfpl();
                        break;
                    case 7:
                        typeStr[0] = "1:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_0();
                        break;
                    case 8:
                        typeStr[0] = "2:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_0();
                        break;
                    case 9:
                        typeStr[0] = "2:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_1();
                        break;
                    case 10:
                        typeStr[0] = "3:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_3_0();
                        break;
                    case 11:
                        typeStr[0] = "3:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_3_1();
                        break;
                    case 12:
                        typeStr[0] = "3:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_3_2();
                        break;
                    case 13:
                        typeStr[0] = "4:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_4_0();
                        break;
                    case 14:
                        typeStr[0] = "4:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_4_1();
                        break;
                    case 15:
                        typeStr[0] = "4:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_4_2();
                        break;
                    case 16:
                        typeStr[0] = "5:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_5_0();
                        break;
                    case 17:
                        typeStr[0] = "5:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_5_1();
                        break;
                    case 18:
                        typeStr[0] = "5:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_5_2();
                        break;
                    case 19:
                        typeStr[0] = "胜其它";
                        typeStr[1] = entity.getTzTypeEntity().getBf_sqt();
                        break;
                    case 20:
                        typeStr[0] = "0:0";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_0();
                        break;
                    case 21:
                        typeStr[0] = "1:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_1();
                        break;
                    case 22:
                        typeStr[0] = "2:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_2();
                        break;
                    case 23:
                        typeStr[0] = "3:3";
                        typeStr[1] = entity.getTzTypeEntity().getBf_3_3();
                        break;
                    case 24:
                        typeStr[0] = "平其它";
                        typeStr[1] = entity.getTzTypeEntity().getBf_pqt();
                        break;
                    case 25:
                        typeStr[0] = "0:1";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_1();
                        break;
                    case 26:
                        typeStr[0] = "0:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_2();
                        break;
                    case 27:
                        typeStr[0] = "1:2";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_2();
                        break;
                    case 28:
                        typeStr[0] = "0:3";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_3();
                        break;
                    case 29:
                        typeStr[0] = "1:3";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_3();
                        break;
                    case 30:
                        typeStr[0] = "2:3";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_3();
                        break;
                    case 31:
                        typeStr[0] = "0:4";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_4();
                        break;
                    case 32:
                        typeStr[0] = "1:4";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_4();
                        break;
                    case 33:
                        typeStr[0] = "2:4";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_4();
                        break;
                    case 34:
                        typeStr[0] = "0:5";
                        typeStr[1] = entity.getTzTypeEntity().getBf_0_5();
                        break;
                    case 35:
                        typeStr[0] = "1:5";
                        typeStr[1] = entity.getTzTypeEntity().getBf_1_5();
                        break;
                    case 36:
                        typeStr[0] = "2:5";
                        typeStr[1] = entity.getTzTypeEntity().getBf_2_5();
                        break;
                    case 37:
                        typeStr[0] = "负其它";
                        typeStr[1] = entity.getTzTypeEntity().getBf_fqt();
                        break;
                    case 38:
                        typeStr[0] = "0";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_0();
                        break;
                    case 39:
                        typeStr[0] = "1";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_1();
                        break;
                    case 40:
                        typeStr[0] = "2";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_2();
                        break;
                    case 41:
                        typeStr[0] = "3";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_3();
                        break;
                    case 42:
                        typeStr[0] = "4";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_4();
                        break;
                    case 43:
                        typeStr[0] = "5";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_5();
                        break;
                    case 44:
                        typeStr[0] = "6";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_6();
                        break;
                    case 45:
                        typeStr[0] = "7+";
                        typeStr[1] = entity.getTzTypeEntity().getJqs_7();
                        break;
                    case 46:
                        typeStr[0] = "胜胜";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_ss();
                        break;
                    case 47:
                        typeStr[0] = "胜平";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_sp();
                        break;
                    case 48:
                        typeStr[0] = "胜负";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_sf();
                        break;
                    case 49:
                        typeStr[0] = "平胜";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_ps();
                        break;
                    case 50:
                        typeStr[0] = "平平";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_pp();
                        break;
                    case 51:
                        typeStr[0] = "平负";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_pf();
                        break;
                    case 52:
                        typeStr[0] = "负胜";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_fs();
                        break;
                    case 53:
                        typeStr[0] = "负平";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_fp();
                        break;
                    case 54:
                        typeStr[0] = "负负";
                        typeStr[1] = entity.getTzTypeEntity().getBqc_ff();
                        break;
                }
                break;
        }
        return typeStr;
    }

    private static void removeDuplicate(List<Integer> list) {
        LinkedHashSet<Integer> set = new LinkedHashSet<Integer>(list.size());
        set.addAll(list);
        list.clear();
        list.addAll(set);
        Collections.sort(list);
    }

    /**
     * 获取注数
     *
     * @param count   比赛场数
     * @param selects 每场比赛的选项数
     * @param qs      过关方式
     * @return
     */
    public static int getBetCountWithGamesCount(int count, int[] selects, int qs) {

        int result = 0;
        if (qs < 2 || qs > count || count < 2) {
            return 0;
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < qs; i++) {
            list.add(1);
        }
        for (int i = qs; i < count; i++) {
            list.add(0);
        }
        result += getItemBetCountWithArray(list, selects);
        while (true) {
            if (shouldBreakFromCirculationWithArray(list, qs)) {
                break;
            } else {
                int i;
                for (i = 0; i < list.size() - 1; i++) {
                    if (list.get(i) == 1 && list.get(i + 1) == 0) {
                        int a = list.get(i);
                        int b = list.get(i + 1);
                        list.set(i, b);
                        list.set(i + 1, a);
                        break;
                    }
                }
                for (int j = 0; i < i - 1; j++) {
                    for (int k = 0; k < i - 1 - j; k++) {
                        if (list.get(k) < list.get(k + 1)) {
                            int a = list.get(k);
                            int b = list.get(k + 1);
                            list.set(k, b);
                            list.set(k + 1, a);
                        }
                    }
                }
                result += getItemBetCountWithArray(list, selects);
            }
        }

        return result;
    }

    private static int getItemBetCountWithArray(List<Integer> list, int[] selects) {

        int count = 1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == 1) {
                count *= selects[i];
            }
        }
        return count;
    }

    private static boolean shouldBreakFromCirculationWithArray(List<Integer> list, int qs) {

        boolean shouldbreak = true;
        for (int i = 0; i < list.size() - qs; i++) {
            if (list.get(i) == 1) {
                shouldbreak = false;
                break;
            }
        }
        return shouldbreak;
    }

}
