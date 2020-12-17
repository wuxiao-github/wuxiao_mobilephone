package com.example.shopping.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.shopping.R;
import com.example.shopping.adapter.GoodPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class TabSecondFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private static final String TAG = "TabSecondFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private ArrayList<String> mTitleArray = new ArrayList<String>(); // 标题文字队列
    private TabLayout tab_title; // 定义一个标签布局对象
    private ViewPager vp_content; // 定义一个翻页视图对象

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据mContext布局文件fragment_tab_first.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_tab_second, container, false);
        // 根据碎片标签栏传来的参数拼接文本字符串


        Toolbar tl_head = mView.findViewById(R.id.tl_head);
        // 设置工具栏左边的导航图标
       tl_head.setNavigationIcon(R.drawable.ic_back);
        // 使用tl_head替换系统自带的ActionBar
       ((AppCompatActivity) getActivity()).setSupportActionBar(tl_head);
        mTitleArray.add("生涯");
        mTitleArray.add("藏品");
        mTitleArray.add("皮肤列表");

       initTabLayout(); // 初始化标签布局
        initTabViewPager(); // 初始化标签翻页




        return mView;
    }



    // 初始化标签布局
    @SuppressLint("ResourceType")
    private void initTabLayout() {
        // 从布局文件中获取名叫tab_title的标签布局
       tab_title = mView.findViewById(R.id.tab_title);

        // 给tab_title添加一个指定布局的标签
       tab_title.addTab(tab_title.newTab().setCustomView(R.drawable.item_toolbar1));
        TextView tv_toolbar1 = mView.findViewById(R.id.tv_toolbar1);
        tv_toolbar1.setText(mTitleArray.get(0));
        // 给tab_title添加一个指定布局的标签
        tab_title.addTab(tab_title.newTab().setCustomView(R.drawable.item_toolbar2));
        TextView tv_toolbar2 = mView.findViewById(R.id.tv_toolbar2);
        tv_toolbar2.setText(mTitleArray.get(1));

        // 给tab_title添加一个指定布局的标签
        tab_title.addTab(tab_title.newTab().setCustomView(R.drawable.item_toolbar3));
        TextView tv_toolbar3 = mView.findViewById(R.id.tv_toolbar3);
        tv_toolbar3.setText(mTitleArray.get(2));
        // 给tab_title添加标签选中监听器

       // tab_title.addTab(tab_title.newTab().setCustomView(R.drawable.item_toolbar2));
        //TextView tv_toolbar3 = mView.findViewById(R.id.tv_toolbar2);
       // tv_toolbar3.setText(mTitleArray.get(2));

        tab_title.addOnTabSelectedListener(this);

    }

    // 初始化标签翻页
    private void initTabViewPager() {
        // 从布局文件中获取名叫vp_content的翻页视图
        vp_content = mView.findViewById(R.id.vp_content);
        // 构建一个商品信息的翻页适配器
        GoodPagerAdapter adapter = new GoodPagerAdapter(
                ((AppCompatActivity) getActivity()). getSupportFragmentManager(), mTitleArray);
        // 给vp_content设置商品翻页适配器
        vp_content.setAdapter(adapter);
        // 给vp_content添加页面变更监听器
        vp_content.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // 选中tab_title指定位置的标签
                tab_title.getTabAt(position).select();
            }
        });
    }

    // 在标签被重复选中时触发
    public void onTabReselected(TabLayout.Tab tab) {


    }

    // 在标签选中时触发
    public void onTabSelected(TabLayout.Tab tab) {
        // 让vp_content显示指定位置的页面
         vp_content.setCurrentItem(tab.getPosition());
    }

    // 在标签取消选中时触发
    public void onTabUnselected(TabLayout.Tab tab) {
    }





}



