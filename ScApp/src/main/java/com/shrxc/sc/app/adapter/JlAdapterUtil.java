package com.shrxc.sc.app.adapter;

import android.content.Context;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.bean.JlChildEntity;
import com.shrxc.sc.app.bean.JlGroupEntity;
import com.shrxc.sc.app.bean.SelectOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by CH on 2018/10/25.
 */

public class JlAdapterUtil {

    /**
     * 初始化选中状态
     *
     * @param groupCount  父项数
     * @param childCount  子项数
     * @param selectCount 选择的个数
     * @return
     */
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
            if (type == 0 || type == 1) {
                layout.setBackgroundResource(R.drawable.app_gray_stroke_color);
            } else {
                layout.setBackgroundResource(R.color.app_white_color_ffffff);
            }
            for (int i = 0; i < textViews.length; i++) {
                if (i == 0) {
                    textViews[i].setTextColor(context.getResources().getColor(R.color.app_text_color_333333));
                } else {
                    if (textViews.length == 3) {
                        if (i == 1) {
                            if (textViews[i].getText().toString().contains("-")) {
                                textViews[i].setTextColor(context.getResources().getColor(R.color.app_green_color_1db53a));
                            } else {
                                textViews[i].setTextColor(context.getResources().getColor(R.color.app_red_color_fa3243));
                            }
                        } else {
                            textViews[i].setTextColor(context.getResources().getColor(R.color.app_text_color_999999));
                        }
                    } else {
                        textViews[i].setTextColor(context.getResources().getColor(R.color.app_text_color_999999));
                    }
                }
            }
        } else {
            layout.setBackgroundResource(R.color.app_red_color_fa3243);
            for (int i = 0; i < textViews.length; i++) {
                textViews[i].setTextColor(context.getResources().getColor(R.color.app_white_color_ffffff));
            }
        }
    }

    private static TextView[] getLayoutChild(LinearLayout layout, int index, int type) {
        TextView[] textViews = null;
        if (type == 0 || type == 2) {
            int length = layout.getChildCount();
            textViews = new TextView[length];
            for (int i = 0; i < length; i++) {
                textViews[i] = (TextView) layout.getChildAt(i);
            }
        } else if (type == 1) {
            if (index == 0) {
                int length = layout.getChildCount();
                textViews = new TextView[length];
                for (int i = 0; i < length; i++) {
                    textViews[i] = (TextView) layout.getChildAt(i);
                }
            } else if (index == 1) {
                LinearLayout childLayout = (LinearLayout) layout.getChildAt(0);
                textViews = new TextView[3];
                textViews[0] = (TextView) childLayout.getChildAt(0);
                textViews[1] = (TextView) childLayout.getChildAt(1);
                textViews[2] = (TextView) layout.getChildAt(1);
            }
        }
        return textViews;
    }

    public static void changeViewSet(Context context, BaseExpandableListAdapter adapter, int gPos, int cPos, int index,
                                     Map<Integer, Map<Integer, Map<Integer, Integer>>> childs, List<JlGroupEntity> games) {
        if (games != null) {
            List<JlChildEntity> list = JlAdapterUtil.getSelectList(childs, games);
            if (list.size() == 8) {
                int isSelect = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (games.get(gPos).getList().get(cPos).getPlanid().equals(list.get(i).getPlanid())) {
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


    public static List<JlChildEntity> getSelectList(Map<Integer, Map<Integer, Map<Integer, Integer>>> childs, List<JlGroupEntity> games) {
        List<JlChildEntity> orderList = new ArrayList<>();
        List<JlChildEntity> selectedList = new ArrayList<>();
        Map<String, List<JlChildEntity>> selectMap = new ConcurrentHashMap<>();
        for (Integer key : childs.keySet()) {
            Map<Integer, Map<Integer, Integer>> gMap = childs.get(key);
            for (Integer ckey : gMap.keySet()) {
                Map<Integer, Integer> cMap = gMap.get(ckey);
                for (Integer index : cMap.keySet()) {
                    if (cMap.get(index) == 1) {
                        JlChildEntity entity = games.get(key).getList().get(ckey);
                        List<Integer> selects = entity.getSelected();
                        if (selects == null) {
                            selects = new ArrayList<>();
                        }
                        selects.add(index);
                        removeDuplicate(selects);
                        entity.setSelected(selects);
                        selectedList.add(entity);
                        String pid = games.get(key).getList().get(ckey).getPlanid();
                        selectMap.put(pid, new ArrayList<JlChildEntity>());
                    }
                }
            }
        }

        for (Map.Entry<String, List<JlChildEntity>> selected : selectMap.entrySet()) {
            String pid = selected.getKey();
            for (int i = 0; i < selectedList.size(); i++) {
                if (selectedList.get(i).getPlanid() == pid) {
                    List<JlChildEntity> select = selected.getValue();
                    JlChildEntity entity = selectedList.get(i);
                    select.add(entity);
                    selectMap.put(pid, select);
                }
            }
        }
        for (Map.Entry<String, List<JlChildEntity>> selected : selectMap.entrySet()) {
            JlChildEntity entity = selected.getValue().get(0);
            orderList.add(entity);
        }
        return orderList;
    }

    private static void removeDuplicate(List<Integer> list) {
        LinkedHashSet<Integer> set = new LinkedHashSet<Integer>(list.size());
        set.addAll(list);
        list.clear();
        list.addAll(set);
        Collections.sort(list);
    }

    public static String getGgfs(int ggfs) {

        String ggfsStr = "";
        switch (ggfs) {
            case 0:
                ggfsStr = "胜负";
                break;
            case 1:
                ggfsStr = "让分胜负";
                break;
            case 2:
                ggfsStr = "胜分差";
                break;
            case 3:
                ggfsStr = "大小分";
                break;
            case 4:
                ggfsStr = "混合过关";
                break;
            case 5:
                ggfsStr = "单关投注";
                break;
        }
        return ggfsStr;
    }

    public static String[] getSelectType(int type, int ggfs, JlChildEntity entity) {

        String[] typeStr = new String[2];
        String select = "";
        switch (ggfs) {
            case 0:
                switch (type) {
                    case 1:
                        typeStr[0] = "负";
                        typeStr[1] = entity.getZfpl();
                        break;
                    case 2:
                        typeStr[0] = "胜";
                        typeStr[1] = entity.getZspl();
                        break;
                }
                break;
            case 1:
//                String rqs = entity.getRangfen();
//
//                if (rqs.contains("-")) {
//                    rqs = rqs.replace("-", "");
//                    select = "主" + rqs + "  ";
//                } else if (rqs.contains("+")) {
//                    rqs = rqs.replace("+", "");
//                    select = "主+" + rqs + "  ";
//                }
                switch (type) {
                    case 1:
                        typeStr[0] = "负";
                        typeStr[1] = entity.getRzfpl();
                        break;
                    case 2:
                        typeStr[0] = "胜";
                        typeStr[1] = entity.getRzspl();
                        break;
                }
                break;
            case 2:
                switch (type) {
                    case 1:
                        typeStr[0] = "客胜1-5  ";
                        typeStr[1] = entity.getKS1_5();
                        break;
                    case 2:
                        typeStr[0] = "客胜6-10  ";
                        typeStr[1] = entity.getKS6_10();
                        break;
                    case 3:
                        typeStr[0] = "客胜11-15  ";
                        typeStr[1] = entity.getKS11_15();
                        break;
                    case 4:
                        typeStr[0] = "客胜16-20  ";
                        typeStr[1] = entity.getKS16_20();
                        break;
                    case 5:
                        typeStr[0] = "客胜21-25  ";
                        typeStr[1] = entity.getKS21_25();
                        break;
                    case 6:
                        typeStr[0] = "客胜26  ";
                        typeStr[1] = entity.getKS26();
                        break;
                    case 7:
                        typeStr[0] = "主胜1-5  ";
                        typeStr[1] = entity.getZS1_5();
                        break;
                    case 8:
                        typeStr[0] = "主胜6-10  ";
                        typeStr[1] = entity.getZS6_10();
                        break;
                    case 9:
                        typeStr[0] = "主胜11-15  ";
                        typeStr[1] = entity.getZS11_15();
                        break;
                    case 10:
                        typeStr[0] = "主胜16-20  ";
                        typeStr[1] = entity.getZS16_20();
                        break;
                    case 11:
                        typeStr[0] = "主胜21-25  ";
                        typeStr[1] = entity.getZS21_25();
                        break;
                    case 12:
                        typeStr[0] = "主胜26  ";
                        typeStr[1] = entity.getZS26();
                        break;
                }
                break;
            case 3:
                switch (type) {
                    case 1:
                        typeStr[0] = "大于" + entity.getZongfen();
                        typeStr[1] = entity.getMaxpl();
                        break;
                    case 2:
                        typeStr[0] = "小于" + entity.getZongfen();
                        typeStr[1] = entity.getMinpl();
                        break;
                }
                break;
            case 4:
                switch (type) {
                    case 1:
                        typeStr[0] = "让分负";
                        typeStr[1] = entity.getRzfpl();
                        break;
                    case 2:
                        typeStr[0] = "让分胜";
                        typeStr[1] = entity.getRzspl();
                        break;
                    case 3:
                        typeStr[0] = "大于" + entity.getZongfen();
                        typeStr[1] = entity.getMaxpl();
                        break;
                    case 4:
                        typeStr[0] = "小于" + entity.getZongfen();
                        typeStr[1] = entity.getMinpl();
                        break;
                    case 5:
                        typeStr[0] = "客胜1-5  ";
                        typeStr[1] = entity.getKS1_5();
                        break;
                    case 6:
                        typeStr[0] = "客胜6-10  ";
                        typeStr[1] = entity.getKS6_10();
                        break;
                    case 7:
                        typeStr[0] = "客胜11-15  ";
                        typeStr[1] = entity.getKS11_15();
                        break;
                    case 8:
                        typeStr[0] = "客胜16-20  ";
                        typeStr[1] = entity.getKS16_20();
                        break;
                    case 9:
                        typeStr[0] = "客胜21-25  ";
                        typeStr[1] = entity.getKS21_25();
                        break;
                    case 10:
                        typeStr[0] = "客胜26  ";
                        typeStr[1] = entity.getKS26();
                        break;
                    case 11:
                        typeStr[0] = "主胜1-5  ";
                        typeStr[1] = entity.getZS1_5();
                        break;
                    case 12:
                        typeStr[0] = "主胜6-10  ";
                        typeStr[1] = entity.getZS6_10();
                        break;
                    case 13:
                        typeStr[0] = "主胜11-15  ";
                        typeStr[1] = entity.getZS11_15();
                        break;
                    case 14:
                        typeStr[0] = "主胜16-20  ";
                        typeStr[1] = entity.getZS16_20();
                        break;
                    case 15:
                        typeStr[0] = "主胜21-25  ";
                        typeStr[1] = entity.getZS21_25();
                        break;
                    case 16:
                        typeStr[0] = "主胜26  ";
                        typeStr[1] = entity.getZS26();
                        break;
                    case 17:
                        typeStr[0] = "非让分负";
                        typeStr[1] = entity.getZfpl();
                        break;
                    case 18:
                        typeStr[0] = "非让分胜";
                        typeStr[1] = entity.getZspl();
                        break;
                }
                break;
            case 5:
                switch (type) {
                    case 1:
                        typeStr[0] = "客胜1-5  ";
                        typeStr[1] = entity.getKS1_5();
                        break;
                    case 2:
                        typeStr[0] = "客胜6-10  ";
                        typeStr[1] = entity.getKS6_10();
                        break;
                    case 3:
                        typeStr[0] = "客胜11-15  ";
                        typeStr[1] = entity.getKS11_15();
                        break;
                    case 4:
                        typeStr[0] = "客胜16-20  ";
                        typeStr[1] = entity.getKS16_20();
                        break;
                    case 5:
                        typeStr[0] = "客胜21-25  ";
                        typeStr[1] = entity.getKS21_25();
                        break;
                    case 6:
                        typeStr[0] = "客胜26  ";
                        typeStr[1] = entity.getKS26();
                        break;
                    case 7:
                        typeStr[0] = "主胜1-5  ";
                        typeStr[1] = entity.getZS1_5();
                        break;
                    case 8:
                        typeStr[0] = "主胜6-10  ";
                        typeStr[1] = entity.getZS6_10();
                        break;
                    case 9:
                        typeStr[0] = "主胜11-15  ";
                        typeStr[1] = entity.getZS11_15();
                        break;
                    case 10:
                        typeStr[0] = "主胜16-20  ";
                        typeStr[1] = entity.getZS16_20();
                        break;
                    case 11:
                        typeStr[0] = "主胜21-25  ";
                        typeStr[1] = entity.getZS21_25();
                        break;
                    case 12:
                        typeStr[0] = "主胜26  ";
                        typeStr[1] = entity.getZS26();
                        break;
                }
                break;
        }
        return typeStr;
    }

    public static SelectOrder getUpOrderType(int type, int ggfs, JlChildEntity entity) {

        System.out.println(type + "-------------" + ggfs + "------" + entity.getRzfpl());

        SelectOrder select = new SelectOrder();
        switch (ggfs) {
            case 0:
                switch (type) {
                    case 1:
                        select.setSf("ZF");
                        select.setPl(entity.getZspl());
                        break;
                    case 2:
                        select.setSf("ZS");
                        select.setPl(entity.getZfpl());
                        break;
                }
                break;
            case 1:
                switch (type) {
                    case 1:
                        select.setRsf("ZF");
                        select.setRangqiu(entity.getRangfen());
                        select.setPl(entity.getRzfpl());
                        break;
                    case 2:
                        select.setRangqiu(entity.getRangfen());
                        select.setRsf("ZS");
                        select.setPl(entity.getRzspl());
                        break;
                }
                break;
            case 2:
                switch (type) {
                    case 1:
                        select.setSfc("KS1_5");
                        select.setPl(entity.getKS1_5());
                        break;
                    case 2:
                        select.setSfc("KS6_10");
                        select.setPl(entity.getKS6_10());
                        break;
                    case 3:
                        select.setSfc("KS11_15");
                        select.setPl(entity.getKS11_15());
                        break;
                    case 4:
                        select.setSfc("KS16_20");
                        select.setPl(entity.getKS16_20());
                        break;
                    case 5:
                        select.setSfc("KS21_25");
                        select.setPl(entity.getKS21_25());
                        break;
                    case 6:
                        select.setSfc("KS26");
                        select.setPl(entity.getKS26());
                        break;
                    case 7:
                        select.setSfc("ZS1_5");
                        select.setPl(entity.getZS1_5());
                        break;
                    case 8:
                        select.setSfc("ZS6_10");
                        select.setPl(entity.getZS6_10());
                        break;
                    case 9:
                        select.setSfc("ZS11_15");
                        select.setPl(entity.getZS11_15());
                        break;
                    case 10:
                        select.setSfc("ZS16_20");
                        select.setPl(entity.getZS16_20());
                        break;
                    case 11:
                        select.setSfc("ZS21_25");
                        select.setPl(entity.getZS21_25());
                        break;
                    case 12:
                        select.setSfc("ZS26");
                        select.setPl(entity.getZS26());
                        break;
                }
                break;
            case 3:
                switch (type) {
                    case 1:
                        select.setDxf("Max");
                        select.setPl(entity.getMaxpl());
                        break;
                    case 2:
                        select.setDxf("Min");
                        select.setPl(entity.getMinpl());
                        break;
                }
                break;
            case 4:
                switch (type) {
                    case 1:
                        select.setRsf("ZF");
                        select.setRangqiu(entity.getRangfen());
                        select.setPl(entity.getRzfpl());
                        break;
                    case 2:
                        select.setRangqiu(entity.getRangfen());
                        select.setRsf("ZS");
                        select.setPl(entity.getRzspl());
                        break;
                    case 3:
                        select.setDxf("Max");
                        select.setPl(entity.getMaxpl());
                        break;
                    case 4:
                        select.setDxf("Min");
                        select.setPl(entity.getMinpl());
                        break;
                    case 5:
                        select.setSfc("KS1_5");
                        select.setPl(entity.getKS1_5());
                        break;
                    case 6:
                        select.setSfc("KS6_10");
                        select.setPl(entity.getKS6_10());
                        break;
                    case 7:
                        select.setSfc("KS11_15");
                        select.setPl(entity.getKS11_15());
                        break;
                    case 8:
                        select.setSfc("KS16_20");
                        select.setPl(entity.getKS16_20());
                        break;
                    case 9:
                        select.setSfc("KS21_25");
                        select.setPl(entity.getKS21_25());
                        break;
                    case 10:
                        select.setSfc("KS26");
                        select.setPl(entity.getKS26());
                        break;
                    case 11:
                        select.setSfc("ZS1_5");
                        select.setPl(entity.getZS1_5());
                        break;
                    case 12:
                        select.setSfc("ZS6_10");
                        select.setPl(entity.getZS6_10());
                        break;
                    case 13:
                        select.setSfc("ZS11_15");
                        select.setPl(entity.getZS11_15());
                        break;
                    case 14:
                        select.setSfc("ZS16_20");
                        select.setPl(entity.getZS16_20());
                        break;
                    case 15:
                        select.setSfc("ZS21_25");
                        select.setPl(entity.getZS21_25());
                        break;
                    case 16:
                        select.setSfc("ZS26");
                        select.setPl(entity.getZS26());
                        break;
                    case 17:
                        select.setSf("ZF");
                        select.setPl(entity.getZspl());
                        break;
                    case 18:
                        select.setSf("ZS");
                        select.setPl(entity.getZfpl());
                        break;
                }
                break;
            case 5:
                switch (type) {
                    case 1:
                        select.setSfc("KS1_5");
                        select.setPl(entity.getKS1_5());
                        break;
                    case 2:
                        select.setSfc("KS6_10");
                        select.setPl(entity.getKS6_10());
                        break;
                    case 3:
                        select.setSfc("KS11_15");
                        select.setPl(entity.getKS11_15());
                        break;
                    case 4:
                        select.setSfc("KS16_20");
                        select.setPl(entity.getKS16_20());
                        break;
                    case 5:
                        select.setSfc("KS21_25");
                        select.setPl(entity.getKS21_25());
                        break;
                    case 6:
                        select.setSfc("KS26");
                        select.setPl(entity.getKS26());
                        break;
                    case 7:
                        select.setSfc("ZS1_5");
                        select.setPl(entity.getZS1_5());
                        break;
                    case 8:
                        select.setSfc("ZS6_10");
                        select.setPl(entity.getZS6_10());
                        break;
                    case 9:
                        select.setSfc("ZS11_15");
                        select.setPl(entity.getZS11_15());
                        break;
                    case 10:
                        select.setSfc("ZS16_20");
                        select.setPl(entity.getZS16_20());
                        break;
                    case 11:
                        select.setSfc("ZS21_25");
                        select.setPl(entity.getZS21_25());
                        break;
                    case 12:
                        select.setSfc("ZS26");
                        select.setPl(entity.getZS26());
                        break;
                }
                break;
        }
        return select;
    }
}
