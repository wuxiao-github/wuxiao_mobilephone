package com.example.shopping.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.shopping.R;
import com.example.shopping.adapter.RecyclerStaggeredAdapter;
import com.example.shopping.bean.GoodsInfo;
import com.example.shopping.widget.SpacesItemDecoration;

public class LayoutFirstFragment  extends Fragment {

    private static final String TAG = "LayoutFirstFragment ";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文

        mView = inflater.inflate(R.layout.activity_layout_first, container, false);
        // 根据碎片标签栏传来的参数拼接文本字符串
        initRecyclerStaggered(mContext,mView); // 初始化瀑布流布局的循环视图
         return mView;
    }

    // 初始化瀑布流布局的循环视图
    private void initRecyclerStaggered(Context mContext,View mView) {
        // 从布局文件中获取名叫rv_staggered的循环视图
        RecyclerView rv_staggered = mView.findViewById(R.id.rv_staggered);
        // 创建一个垂直方向的瀑布流布局管理器
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                3, RecyclerView.VERTICAL);
        // 设置循环视图的布局管理器
        rv_staggered.setLayoutManager(manager);
        // 构建一个服装列表的瀑布流适配器
        RecyclerStaggeredAdapter adapter = new RecyclerStaggeredAdapter(mContext, GoodsInfo.getDefaultStag());
        // 设置瀑布流列表的点击监听器
        adapter.setOnItemClickListener(adapter);
        // 设置瀑布流列表的长按监听器
        adapter.setOnItemLongClickListener(adapter);
        // 给rv_staggered设置服装瀑布流适配器
        rv_staggered.setAdapter(adapter);
        // 设置rv_staggered的默认动画效果
        rv_staggered.setItemAnimator(new DefaultItemAnimator());
        // 给rv_staggered添加列表项之间的空白装饰
        rv_staggered.addItemDecoration(new SpacesItemDecoration(3));
    }
}