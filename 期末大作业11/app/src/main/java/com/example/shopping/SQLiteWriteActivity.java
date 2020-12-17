package com.example.shopping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopping.bean.UserInfo;
import com.example.shopping.database.UserDBHelper;
import com.example.shopping.util.DateUtil;



public class SQLiteWriteActivity extends AppCompatActivity implements View.OnClickListener {

    private UserDBHelper mHelper; // 声明一个用户数据库帮助器的对象
    private EditText et_name;
    private EditText et_number;
    private EditText et_pwd;
    protected Context mContext; // 声明一个上下文对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_write);

        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);
        et_pwd = findViewById(R.id.et_pwd);
        findViewById(R.id.btn_save).setOnClickListener(this);


    }



    @Override
    protected void onStart() {
        super.onStart();
        // 获得数据库帮助器的实例
        mHelper = UserDBHelper.getInstance(this, 2);
        // 打开数据库帮助器的写连接
        mHelper.openWriteLink();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 关闭数据库连接
      //  mHelper.closeLink();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            String name = et_name.getText().toString();
            String number = et_number.getText().toString();
            String pwd = et_pwd.getText().toString();

            if (TextUtils.isEmpty(name)) {
                showToast("请先填写用户名");
                return;
            } else if (TextUtils.isEmpty(number)) {
                showToast("请先填写手机号");
                return;
            } else if (TextUtils.isEmpty(pwd)) {
                showToast("请先填写密码");
                return;
            }
            // 以下声明一个用户信息对象，并填写它的各字段值
            UserInfo info = new UserInfo();
            info.name = name;
            info.phonenumber= number;
            info.phonepwd = pwd;
            info.update_time = DateUtil.getNowDateTime("yyyy-MM-dd HH:mm:ss");

            // 执行数据库帮助器的插入操作
            mHelper.insert(info);
            showToast("注册成功");

            //Intent intent = new Intent(this, SQLiteReadActivity.class);
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
        }
    }

    private void showToast(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }
}
