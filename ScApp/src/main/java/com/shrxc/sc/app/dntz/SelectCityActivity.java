package com.shrxc.sc.app.dntz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.utils.select.city.HanziToPinyin;
import com.shrxc.sc.app.utils.select.city.PinyinComparator;
import com.shrxc.sc.app.utils.select.city.SideBar;
import com.shrxc.sc.app.utils.select.city.SortAdapter;
import com.shrxc.sc.app.utils.select.city.SortModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectCityActivity extends AppCompatActivity {

    private ListView sortListView, searchListView;
    private SideBar sideBar;
    private SortAdapter adapter;
    private EditText mClearEditText;
    private RelativeLayout searchLayout;
    private TextView noCityTextView, cityTextView;
    private ImageView backImageView;
    private String hot_city[] = {"北京", "成都", "重庆", "广州", "杭州", "南京", "上海", "深圳", "苏州", "天津", "武汉", "西安"};
    /**
     * 汉字转换成拼音的类
     */
    private HanziToPinyin characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city_activity);
        initViews();
        initEvent();
    }

    private void initEvent() {

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    return;
                }
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
                position = position - 1;
                Intent intent = new Intent();
                intent.putExtra("select", ((SortModel) adapter.getItem(position)).getName());
                setResult(0x888, intent);
                finish();
            }
        });
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                intent.putExtra("select", ((SortModel) adapter.getItem(position)).getName());
                setResult(0x888, intent);
                finish();
            }
        });
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {
        // 实例化汉字转拼音类
        characterParser = HanziToPinyin.getInstance();
        pinyinComparator = new PinyinComparator();
        sideBar = findViewById(R.id.select_city_activity_sidebar);
        searchLayout = findViewById(R.id.select_city_activity_search_layout);
        searchListView = findViewById(R.id.select_city_activity_search_list);
        noCityTextView = findViewById(R.id.select_city_activity_no_text);
        cityTextView = findViewById(R.id.select_city_activity_city_text);
        backImageView = findViewById(R.id.select_city_back_icon);

        Intent intent = getIntent();
        cityTextView.setText(intent.getStringExtra("city"));


        sortListView = findViewById(R.id.select_city_activity_city_list);
        View headerView = getLayoutInflater().inflate(
                R.layout.select_city_list_header_layout, null);
        initHotCity(headerView);
        sortListView.addHeaderView(headerView);
        SourceDateList = filledData(getResources().getStringArray(R.array.cities));

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);
        searchListView.setAdapter(adapter);

        mClearEditText = findViewById(R.id.select_city_activity_search_edit);

        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initHotCity(View v) {

        LinearLayout hot1Layout = v.findViewById(R.id.select_city_activity_hot_1_layout);
        LinearLayout hot2Layout = v.findViewById(R.id.select_city_activity_hot_2_layout);
        LinearLayout hot3Layout = v.findViewById(R.id.select_city_activity_hot_3_layout);

        hot1Layout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHotCity(hot_city[0]);
            }
        });
        hot1Layout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHotCity(hot_city[1]);
            }
        });
        hot1Layout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHotCity(hot_city[2]);
            }
        });
        hot1Layout.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHotCity(hot_city[3]);
            }
        });
        hot2Layout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHotCity(hot_city[4]);
            }
        });
        hot2Layout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHotCity(hot_city[5]);
            }
        });
        hot2Layout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHotCity(hot_city[6]);
            }
        });
        hot2Layout.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHotCity(hot_city[7]);
            }
        });
        hot3Layout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHotCity(hot_city[8]);
            }
        });
        hot3Layout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHotCity(hot_city[9]);
            }
        });
        hot3Layout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHotCity(hot_city[10]);
            }
        });
        hot3Layout.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHotCity(hot_city[11]);
            }
        });
    }

    private void selectHotCity(String city) {
        Intent intent = new Intent();
        intent.putExtra("select", city);
        setResult(0x888, intent);
        finish();
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            // 汉字转换成拼音
            String pinyin = characterParser.getPinYin(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {


        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getPinYin(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }


        if (filterStr.length() == 0) {
            searchLayout.setVisibility(View.GONE);
        } else {
            searchLayout.setVisibility(View.VISIBLE);
            if (filterDateList.size() == 0) {
                noCityTextView.setVisibility(View.VISIBLE);
            } else {
                noCityTextView.setVisibility(View.GONE);
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }
}
