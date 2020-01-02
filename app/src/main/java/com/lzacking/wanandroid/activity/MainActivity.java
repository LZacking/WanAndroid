package com.lzacking.wanandroid.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzacking.wanandroid.R;
import com.lzacking.wanandroid.fragment.HomeFragment;
import com.lzacking.wanandroid.fragment.InvestFragment;
import com.lzacking.wanandroid.fragment.MeFragment;
import com.lzacking.wanandroid.fragment.MoreFragment;
import com.lzacking.wanandroid.util.UIUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.fl_main)
    FrameLayout flMain;
    @Bind(R.id.iv_main_home)
    ImageView ivMainHome;
    @Bind(R.id.tv_main_home)
    TextView tvMainHome;
    @Bind(R.id.ll_main_home)
    LinearLayout llMainHome;
    @Bind(R.id.iv_main_invest)
    ImageView ivMainInvest;
    @Bind(R.id.tv_main_invest)
    TextView tvMainInvest;
    @Bind(R.id.ll_main_invest)
    LinearLayout llMainInvest;
    @Bind(R.id.iv_main_me)
    ImageView ivMainMe;
    @Bind(R.id.tv_main_me)
    TextView tvMainMe;
    @Bind(R.id.ll_main_me)
    LinearLayout llMainMe;
    @Bind(R.id.iv_main_more)
    ImageView ivMainMore;
    @Bind(R.id.tv_main_more)
    TextView tvMainMore;
    @Bind(R.id.ll_main_more)
    LinearLayout llMainMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 默认显示首页
        setSelect(0);
    }

    @OnClick({R.id.ll_main_home, R.id.ll_main_invest, R.id.ll_main_me, R.id.ll_main_more})
    public void showTab(View view) {
        switch(view.getId()) {
            case R.id.ll_main_home: // 首页
                setSelect(0);
                break;
            case R.id.ll_main_invest: // 投资
                setSelect(1);
                break;
            case R.id.ll_main_me: // 我的投资
                setSelect(2);
                break;
            case R.id.ll_main_more: // 更多
                setSelect(3);
                break;
        }
    }

    private HomeFragment homeFragment;
    private InvestFragment investFragment;
    private MeFragment meFragment;
    private MoreFragment moreFragment;
    private FragmentTransaction transaction;

    private void setSelect(int i) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        // 隐藏所有Fragment的显示
        hideFragments();

        // 重置ImageView和TextView的显示状态
        resetTab();

        switch (i) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl_main, homeFragment);
                }
                transaction.show(homeFragment);
                ivMainHome.setImageResource(R.drawable.bottom02);
                tvMainHome.setTextColor(UIUtils.getColor(R.color.home_back_selected));
                break;

            case 1:
                if (investFragment == null) {
                    investFragment = new InvestFragment();
                    transaction.add(R.id.fl_main, investFragment);
                }
                transaction.show(investFragment);
                ivMainInvest.setImageResource(R.drawable.bottom04);
                tvMainInvest.setTextColor(UIUtils.getColor(R.color.home_back_selected));
                break;

            case 2:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.fl_main, meFragment);
                }
                transaction.show(meFragment);
                ivMainMe.setImageResource(R.drawable.bottom06);
                tvMainMe.setTextColor(UIUtils.getColor(R.color.home_back_selected));
                break;

            case 3:
                if (moreFragment == null) {
                    moreFragment = new MoreFragment();
                    transaction.add(R.id.fl_main, moreFragment);
                }
                transaction.show(moreFragment);
                ivMainMore.setImageResource(R.drawable.bottom08);
                tvMainMore.setTextColor(UIUtils.getColor(R.color.home_back_selected));
                break;

        }

        transaction.commit(); // 提交事务
    }

    private void hideFragments() {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }

        if (investFragment != null) {
            transaction.hide(investFragment);
        }

        if (meFragment != null) {
            transaction.hide(meFragment);
        }

        if (moreFragment != null) {
            transaction.hide(moreFragment);
        }
    }

    private void resetTab() {
        ivMainHome.setImageResource(R.drawable.bottom01);
        ivMainInvest.setImageResource(R.drawable.bottom03);
        ivMainMe.setImageResource(R.drawable.bottom05);
        ivMainMore.setImageResource(R.drawable.bottom07);

        tvMainHome.setTextColor(UIUtils.getColor(R.color.home_back_unselected));
        tvMainInvest.setTextColor(UIUtils.getColor(R.color.home_back_unselected));
        tvMainMe.setTextColor(UIUtils.getColor(R.color.home_back_unselected));
        tvMainMore.setTextColor(UIUtils.getColor(R.color.home_back_unselected));
    }

    private boolean flag = true;
    private static final int WHAT_RESET_BACK = 1;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what) {
                case WHAT_RESET_BACK:
                    flag = true;
                    break;
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && flag) {
            Toast.makeText(MainActivity.this, "再点击一次，退出当前应用", Toast.LENGTH_LONG).show();
            flag = false;
            handler.sendEmptyMessageDelayed(WHAT_RESET_BACK, 2000);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}
