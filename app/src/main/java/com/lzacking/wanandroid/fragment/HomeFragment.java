package com.lzacking.wanandroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.lzacking.wanandroid.R;
import com.lzacking.wanandroid.bean.Image;
import com.lzacking.wanandroid.bean.Index;
import com.lzacking.wanandroid.bean.Product;
import com.lzacking.wanandroid.ui.RoundProgress;
import com.lzacking.wanandroid.util.AppNetConfig;
import com.lzacking.wanandroid.util.UIUtils;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    @Bind(R.id.iv_title_back)
    ImageView ivTitleBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_title_setting)
    ImageView ivTitleSetting;
    @Bind(R.id.banner)
    Banner mBanner;
    @Bind(R.id.tv_home_product)
    TextView mTvHomeProduct;
    @Bind(R.id.tv_home_yearrate)
    TextView mTvHomeYearrate;
    @Bind(R.id.roundPro_home)
    RoundProgress mRoundProHome;

    private int currentProress;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mRoundProHome.setMax(100);
            for (int i = 0; i < currentProress; i++) {
                mRoundProHome.setProgress(i + 1);
                SystemClock.sleep(20);
                mRoundProHome.postInvalidate();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        View view = UIUtils.getView(R.layout.fragment_home);
        ButterKnife.bind(this, view);
        // 初始化title
        initTitle();
        // 初始化数据
        initData();
        return view;
    }

    private void initTitle() {
        ivTitleBack.setVisibility(View.GONE);
        tvTitle.setText("首页");
        ivTitleSetting.setVisibility(View.GONE);
    }

    private Index index;

    private void initData() {
        index = new Index();
        AsyncHttpClient client = new AsyncHttpClient();
        // 访问的URL
        String url = AppNetConfig.INDEX;
        client.post(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String content) {
                // 解析json数据：GSON / FASTJSON
                JSONObject jsonObject = JSON.parseObject(content);

                // 解析json对象数据（只是解析proInfo的数据）
                String proInfo = jsonObject.getString("proInfo");
                // 将数据存入product中
                Product product = JSON.parseObject(proInfo, Product.class);

                // 解析json数组数据（只是解析imageArr数据）
                String imageArr = jsonObject.getString("imageArr");
                // 将数据存入Image对象中
                List<Image> images = jsonObject.parseArray(imageArr, Image.class);

                index.product = product;
                index.images = images;

                // 更新页面数据
                mTvHomeProduct.setText(product.name);
                Log.i("info", "onSuccess: " + product.name);
                mTvHomeYearrate.setText(product.yearRate + "%");

                // 更新数据中的进度值
                currentProress = Integer.parseInt(index.product.progress);
                new Thread(mRunnable).start();

                // 设置banner样式
                mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
                // 设置图片加载器
                mBanner.setImageLoader(new GlideImageLoader());
                // 设置图片地址构成的集合
                ArrayList<String> imagesUrl = new ArrayList<String>(index.images.size());
                for (int i = 0; i < index.images.size(); i++) {
                    imagesUrl.add(index.images.get(i).IMAURL);
                }
                mBanner.setImages(imagesUrl);
                // 设置banner动画效果
                mBanner.setBannerAnimation(Transformer.DepthPage);
                // 设置标题集合(当banner样式有显示title时)
                String[] titles = new String[]{"分享砍学费", "人脉总动员", "想不到你是这样的app", "购物节，爱不单行"};
                mBanner.setBannerTitles(Arrays.asList(titles));
                // 设置自动轮播，默认为true
                mBanner.isAutoPlay(true);
                // 设置轮播时间
                mBanner.setDelayTime(2000);
                // 设置指示器位置（当banner模式中有指示器时）
                mBanner.setIndicatorGravity(BannerConfig.CENTER);
                // banner设置方法全部调用完毕时最后调用
                mBanner.start();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(UIUtils.getContext(), "联网获取数据失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Picasso.with(context).load((String) path).into(imageView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
