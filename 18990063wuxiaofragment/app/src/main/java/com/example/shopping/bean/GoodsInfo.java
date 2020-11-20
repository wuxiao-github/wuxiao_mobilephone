package com.example.shopping.bean;

import com.example.shopping.R;

import java.util.ArrayList;

public class GoodsInfo {
    public long rowid; // 行号
    public int sn; // 序号
    public String name; // 名称
    public String desc; // 描述
    public float price; // 价格
    public String thumb_path; // 小图的保存路径
    public String pic_path; // 大图的保存路径
    public int thumb; // 小图的资源编号
    public int pic; // 大图的资源编号

    public GoodsInfo() {
        rowid = 0L;
        sn = 0;
        name = "";
        desc = "";
        price = 0;
        thumb_path = "";
        pic_path = "";
        thumb = 0;
        pic = 0;
    }

    // 声明一个手机商品的名称数组
    private static String[] mNameArray = {
            "艾克", "艾希", "菲奥娜", "易", "烬", "卡特","薇恩","亚索"
    };
    // 声明一个手机商品的描述数组
    private static String[] mDescArray = {
            "源计划 ：自由 艾克",
            "源计划 ：联合 艾希",
            "源计划：火 菲奥娜",
            "源计划：林 易",
            "源计划：升华 烬",
            "源计划：雄心 卡特",
            "源计划：净化 薇恩",
            "源计划：风 亚索"
    };
    // 声明一个手机商品的价格数组
    private static float[] mPriceArray = {79,99, 79, 99, 79, 79,99,69};
    // 声明一个手机商品的小图数组
    private static int[] mThumbArray = {
            R.drawable.aike, R.drawable.hanbing, R.drawable.jianji,
            R.drawable.jiansheng, R.drawable.jin, R.drawable.kate,R.drawable.weien,R.drawable.yasuo
    };
    // 声明一个手机商品的大图数组
    private static int[] mPicArray = {
            R.drawable.aike, R.drawable.hanbing, R.drawable.jianji,
            R.drawable.jiansheng, R.drawable.jin, R.drawable.kate,R.drawable.weien,R.drawable.yasuo
    };

    // 获取默认的手机信息列表
    public static ArrayList<GoodsInfo> getDefaultList() {
        ArrayList<GoodsInfo> goodsList = new ArrayList<GoodsInfo>();
        for (int i = 0; i < mNameArray.length; i++) {
            GoodsInfo info = new GoodsInfo();
            info.name = mNameArray[i];
            info.desc = mDescArray[i];
            info.price = mPriceArray[i];
            info.thumb = mThumbArray[i];
            info.pic = mPicArray[i];
            goodsList.add(info);
        }
        return goodsList;
    }
}
