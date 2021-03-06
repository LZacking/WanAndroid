package com.lzacking.wanandroid.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzacking.wanandroid.R;
import com.lzacking.wanandroid.util.UIUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MeFragment extends Fragment {
    @Bind(R.id.iv_title_back)
    ImageView ivTitleBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_title_setting)
    ImageView ivTitleSetting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // View view = View.inflate(getActivity(), R.layout.fragment_me, null);
        View view = UIUtils.getView(R.layout.fragment_me);
        ButterKnife.bind(this, view);
        // 初始化title
        initTitle();
        return view;
    }

    private void initTitle() {
        ivTitleBack.setVisibility(View.GONE);
        tvTitle.setText("我的投资");
        ivTitleSetting.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
