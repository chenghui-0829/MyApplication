package com.shrxc.sc.app.mine;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shrxc.sc.app.BaseFragment;
import com.shrxc.sc.app.LoginActivity;
import com.shrxc.sc.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.mine_fragment_qb_layout)
    LinearLayout qbLayout;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @OnClick({R.id.mine_fragment_qb_layout, R.id.mine_fragment_msg_image, R.id.mine_fragment_set_image})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.mine_fragment_qb_layout:
                startActivity(new Intent(getActivity(), QbActivity.class));
                break;
            case R.id.mine_fragment_msg_image:
                startActivity(new Intent(getActivity(), MsgActivity.class));
                break;
            case R.id.mine_fragment_set_image:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
        }
    }


    @Override
    protected void loadData() {
        System.out.println("MineFragment~~");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
