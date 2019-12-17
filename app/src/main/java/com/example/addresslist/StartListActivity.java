package com.example.addresslist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.addresslist.db.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class StartListActivity extends AppCompatActivity {

    private GridView gv_buttom_menu;
    private ListView lv_userList;
    private SimpleAdapter adapter;
    private EditText et_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadButtonMenu();
        loadUserList();    //加载用户列表
        findAllView();
        setListener();
    }

    private void findAllView() {
        et_search = findViewById(R.id.et_search);
    }

    private void setListener() {
//        文字改变时进行搜索
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String message = et_search.getText().toString();
                System.out.println(message);

                ArrayList users_data = (ArrayList) DBHelper.getInstance(StartListActivity.this).selectStartByNameOrPhone("%" + message + "%");
                adapter = new SimpleAdapter(StartListActivity.this,
                        users_data,
                        R.layout.list_item, new String[]{"name", "phone"},
                        new int[]{R.id.tv_showname, R.id.tv_showPhone});
                lv_userList.setAdapter(adapter);
            }
        });
    }

    /**
     * 加载用户信息
     */
    private void loadUserList() {
        lv_userList = findViewById(R.id.lv_userList);
        ArrayList users_data = DBHelper.getInstance(this).selectByStart();
        adapter = new SimpleAdapter(this, users_data, R.layout.list_item, new String[]{"name", "phone"}, new int[]{R.id.tv_showname, R.id.tv_showPhone});
        lv_userList.setAdapter(adapter);
        lv_userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap map = (HashMap) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(StartListActivity.this,ShowDetailActivity.class);
                intent.putExtra("userMap", map);
//              当requestCode为3时，请求转向DetailActivity这个页面
                startActivityForResult(intent, 3);
            }
        });
    }

    /**
     * 加载按钮控件
     */
    private void loadButtonMenu() {
        gv_buttom_menu = this.findViewById(R.id.gv_button_menu);
        gv_buttom_menu.setNumColumns(1);    //设置列数
        gv_buttom_menu.setGravity(Gravity.CENTER);
        gv_buttom_menu.setVerticalSpacing(10);
        gv_buttom_menu.setHorizontalSpacing(10);
        ArrayList data = new ArrayList();
        HashMap map = new HashMap();

        initButtomMenuMap(data, map);


        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item_menu, new String[]{"itemImage", "itemText"}, new int[]{R.id.item_image, R.id.item_text});

        gv_buttom_menu.setAdapter(adapter);
        gv_buttom_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: {
                        finish();
                        break;
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 0 || requestCode == 3) {
            if (resultCode == 1) {
                //增加成功，刷新页面
                ArrayList users_data = DBHelper.getInstance(this).getUserList();
                adapter = new SimpleAdapter(this,
                        users_data,
                        R.layout.list_item, new String[]{ "name", "phone"},
                        new int[]{R.id.tv_showname, R.id.tv_showPhone});
                lv_userList.setAdapter(adapter);
            } else if (resultCode == 2) {
                //增加失败，不刷新页面
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initButtomMenuMap(ArrayList data, HashMap map) {

        map = new HashMap();
        map.put("itemImage", R.drawable.quit);
        map.put("itemText", "返回");
        data.add(map);


    }
}
