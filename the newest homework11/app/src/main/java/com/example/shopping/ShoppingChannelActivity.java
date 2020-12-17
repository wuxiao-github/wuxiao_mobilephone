package com.example.shopping;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTabHost;

import com.example.shopping.adapter.GoodsAdapter;
import com.example.shopping.adapter.GoodsAdapter.addCartListener;
import com.example.shopping.bean.CartInfo;
import com.example.shopping.bean.GoodInfo;
import com.example.shopping.database.CartDBHelper;
import com.example.shopping.database.GoodsDBHelper;
import com.example.shopping.util.DateUtil;
import com.example.shopping.util.FileUtil;
import com.example.shopping.util.MenuUtil;
import com.example.shopping.util.SharedUtil;

import java.util.ArrayList;

public class ShoppingChannelActivity extends AppCompatActivity implements View.OnClickListener, addCartListener{
    private static final String TAG = "ShoppingChannel";
    private TextView tv_count;
    private GridView gv_channel; // 声明一个网格视图对象
    private int mCount; // 购物车中的商品数量
    private GoodsDBHelper mGoodsHelper; // 声明一个商品数据库的帮助器对象
    private CartDBHelper mCartHelper; // 声明一个购物车数据库的帮助器对象
    private SearchView.SearchAutoComplete sac_key; // 声明一个搜索自动完成的编辑框对象
    private String[] hintArray = {"iphone", "iphone8", "iphone8 plus", "iphone7", "iphone7 plus"};
    private FragmentTabHost tabHost; // 声明一个碎片标签栏对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_channel);

        // 隐藏标题栏
        getSupportActionBar().hide();

        TextView tv_title = findViewById(R.id.tv_title);
        tv_count = findViewById(R.id.tv_count);
        // 从布局视图中获取名叫gv_channel的网格视图
        gv_channel = findViewById(R.id.gv_channel);
        findViewById(R.id.iv_cart).setOnClickListener(this);
        tv_title.setText("武潇的商城");



    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_cart) { // 点击了购物车图标
            // 跳转到购物车页面
            Intent intent = new Intent(this, ShoppingCartProActivity.class);
            startActivity(intent);
        }
    }

    // 把指定编号的商品添加到购物车
    public void addToCart(long goods_id) {
        mCount++;
        tv_count.setText("" + mCount);
        // 把购物车中的商品数量写入共享参数
        SharedUtil.getIntance(this).writeShared("count", "" + mCount);
        // 根据商品编号查询购物车数据库中的商品记录
        CartInfo info = mCartHelper.queryByGoodsId(goods_id);
        if (info != null) { // 购物车已存在该商品记录
            info.count++; // 该商品的数量加一
            info.update_time = DateUtil.getNowDateTime("");
            // 更新购物车数据库中的商品记录信息
            mCartHelper.update(info);
        } else { // 购物车不存在该商品记录
            info = new CartInfo();
            info.goods_id = goods_id;
            info.count = 1;
            info.update_time = DateUtil.getNowDateTime("");
            // 往购物车数据库中添加一条新的商品记录
            mCartHelper.insert(info);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获取共享参数保存的购物车中的商品数量
        mCount = Integer.parseInt(SharedUtil.getIntance(this).readShared("count", "0"));
        tv_count.setText("" + mCount);
        // 获取商品数据库的帮助器对象
        mGoodsHelper = GoodsDBHelper.getInstance(this, 1);
        // 打开商品数据库的读连接
        mGoodsHelper.openReadLink();
        // 获取购物车数据库的帮助器对象
        mCartHelper = CartDBHelper.getInstance(this, 1);
        // 打开购物车数据库的写连接
        mCartHelper.openWriteLink();

        // 获取共享参数保存的是否首次打开参数
        mFirst = SharedUtil.getIntance(this).readShared("first", "true");
        // 模拟从网络下载商品图片
        downloadGoods();
        // 把是否首次打开写入共享参数
        SharedUtil.getIntance(this).writeShared("first", "false");

        // 展示商品列表
        showGoods();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 关闭商品数据库的数据库连接
        mGoodsHelper.closeLink();
        // 关闭购物车数据库的数据库连接
        mCartHelper.closeLink();
    }

    private void showGoods() {
        // 查询商品数据库中的所有商品记录
        ArrayList<GoodInfo> goodsArray = mGoodsHelper.query("1=1");
        // 构建商场中商品网格的适配器对象
        GoodsAdapter adapter = new GoodsAdapter(this, goodsArray, this);
        // 给gv_channel设置商品网格适配器
        gv_channel.setAdapter(adapter);
        // 给gv_channel设置网格项点击监听器
        gv_channel.setOnItemClickListener(adapter);
    }

    private String mFirst = "true"; // 是否首次打开

    //模拟网络数据，初始化数据库中的商品信息
    private void downloadGoods() {
        // 获取共享参数保存的是否首次打开参数
        mFirst = SharedUtil.getIntance(this).readShared("first", "true");
        // 获取当前App的私有存储路径
        String path = MainApplication.getInstance().getExternalFilesDir(
                Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        if (mFirst.equals("true")) { // 如果是首次打开
            ArrayList<GoodInfo> goodsList = GoodInfo.getDefaultList();
            for (int i = 0; i < goodsList.size(); i++) {
                GoodInfo info = goodsList.get(i);
                // 往商品数据库插入一条该商品的记录
                long rowid = mGoodsHelper.insert(info);
                info.rowid = rowid;
                // 往全局内存写入商品小图
                Bitmap thumb = BitmapFactory.decodeResource(getResources(), info.thumb);
                MainApplication.getInstance().mIconMap.put(rowid, thumb);
                String thumb_path = path + rowid + "_s.jpg";
                FileUtil.saveImage(thumb_path, thumb);
                info.thumb_path = thumb_path;
                // 往SD卡保存商品大图
                Bitmap pic = BitmapFactory.decodeResource(getResources(), info.pic);
                String pic_path = path + rowid + ".jpg";
                FileUtil.saveImage(pic_path, pic);
                pic.recycle();
                info.pic_path = pic_path;
                // 更新商品数据库中该商品记录的图片路径
                mGoodsHelper.update(info);
            }
        } else { // 不是首次打开
            // 查询商品数据库中所有商品记录
            ArrayList<GoodInfo> goodsArray = mGoodsHelper.query("1=1");
            for (int i = 0; i < goodsArray.size(); i++) {
                GoodInfo info = goodsArray.get(i);
                // 从指定路径读取图片文件的位图数据
                Bitmap thumb = BitmapFactory.decodeFile(info.thumb_path);
                // 把该位图对象保存到应用实例的全局变量中
                MainApplication.getInstance().mIconMap.put(info.rowid, thumb);
            }
        }
        // 把是否首次打开写入共享参数
        SharedUtil.getIntance(this).writeShared("first", "false");
    }





    private TabHost.TabSpec getTabView(int textId, int imgId) {
        // 根据资源编号获得字符串对象
        String text = getResources().getString(textId);
        // 根据资源编号获得图形对象
        Drawable drawable = getResources().getDrawable(imgId);
        // 设置图形的四周边界。这里必须设置图片大小，否则无法显示图标
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        // 根据布局文件item_tabbar.xml生成标签按钮对象
        View item_tabbar = getLayoutInflater().inflate(R.layout.item_tabbar, null);
        TextView tv_item = item_tabbar.findViewById(R.id.tv_item_tabbar);
        tv_item.setText(text);
        // 在文字上方显示标签的图标
        tv_item.setCompoundDrawables(null, drawable, null, null);
        // 生成并返回该标签按钮对应的标签规格
        return tabHost.newTabSpec(text).setIndicator(item_tabbar);
    }
















    @SuppressLint("RestrictedApi")
    private void initSearchView(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        // 从菜单项中获取搜索框对象
        SearchView searchView = (SearchView) menuItem.getActionView();
        // 设置搜索框默认自动缩小为图标
        searchView.setIconifiedByDefault(getIntent().getBooleanExtra("collapse", true));
        // 设置是否显示搜索按钮。搜索按钮只显示一个箭头图标，Android暂不支持显示文本。
        // 查看Android源码，搜索按钮用的控件是ImageView，所以只能显示图标不能显示文字。
        searchView.setSubmitButtonEnabled(true);
        // 从系统服务中获取搜索管理器
        SearchManager sm = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // 创建搜索结果页面的组件名称对象
        ComponentName cn = new ComponentName(this, SearchResultActvity.class);
        // 从结果页面注册的activity节点获取相关搜索信息，即searchable.xml定义的搜索控件
        SearchableInfo info = sm.getSearchableInfo(cn);
        if (info == null) {
            Log.d(TAG, "Fail to get SearchResultActvity.");
            return;
        }
        // 设置搜索框的可搜索信息
        searchView.setSearchableInfo(info);
        // 从搜索框中获取名叫search_src_text的自动完成编辑框
        sac_key = searchView.findViewById(R.id.search_src_text);
        // 设置自动完成编辑框的文本颜色
        sac_key.setTextColor(Color.BLACK);
        // 设置自动完成编辑框的提示文本颜色
        sac_key.setHintTextColor(Color.BLACK);
        // 给搜索框设置文本变化监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 搜索关键词完成输入
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 搜索关键词发生变化
            public boolean onQueryTextChange(String newText) {
                dosearch(newText);
                return true;
            }
        });
        Bundle bundle = new Bundle(); // 创建一个新包裹
        bundle.putString("hi", "hello"); // 往包裹中存放名叫hi的字符串
        // 设置搜索框的额外搜索数据
        searchView.setAppSearchData(bundle);
    }

    private void dosearch(String text) {
        if (text.indexOf("i") == 0) {
            // 根据提示词数组构建一个数组适配器
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.search_list_auto, hintArray);
            // 设置自动完成编辑框的数组适配器
            sac_key.setAdapter(adapter);
            // 给自动完成编辑框设置列表项的点击监听器
            sac_key.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                // 一旦点击关键词匹配列表中的某一项，就触发点击监听器的onItemClick方法
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sac_key.setText(((TextView) view).getText());
                }
            });
        }
    }
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        // 显示菜单项左侧的图标
        MenuUtil.setOverflowIconVisible(featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 从menu_search.xml中构建菜单界面布局
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // 初始化搜索框
        initSearchView(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) { // 点击了工具栏左边的返回箭头
            finish();
        } else if (id == R.id.menu_refresh) { // 点击了刷新图标
            Toast.makeText(this,"当前刷新时间: " + DateUtil.getNowDateTime("yyyy-MM-dd HH:mm:ss"),Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.menu_about) { // 点击了关于菜单项
            Toast.makeText(this, "这个是工具栏的演示demo", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.menu_quit) { // 点击了退出菜单项
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}