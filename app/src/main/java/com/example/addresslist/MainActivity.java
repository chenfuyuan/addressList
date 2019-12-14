package com.example.addresslist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.addresslist.db.DBHelper;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private GridView gv_buttom_menu;
    private ListView lv_userList;
    private SimpleAdapter adapter;
    private LinearLayout ll_search;
    private EditText et_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadUserList();    //加载用户列表
        findAllView();
        setListener();
    }

    private void findAllView() {
        ll_search = findViewById(R.id.ll_search);
        et_search = findViewById(R.id.et_search);
    }

    private void setListener() {
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (keyEvent.getAction()) {
                    case KeyEvent.ACTION_UP:             //键盘松开
                        String message = et_search.getText().toString();
                        System.out.println(message);

                        ArrayList users_data = (ArrayList) DBHelper.getInstance(MainActivity.this).selectByNameOrPhone(message);
                        adapter = new SimpleAdapter(MainActivity.this,
                                users_data,
                                R.layout.list_item, new String[]{"imageid", "name", "phone"},
                                new int[]{R.id.user_image,R.id.tv_showname, R.id.tv_showPhone});
                        lv_userList.setAdapter(adapter);
                    case KeyEvent.ACTION_DOWN:          //键盘按下
                        break;
                }
                return false;

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_MENU) {
            if (gv_buttom_menu == null) {
                loadButtonMenu();
            }
            //设置可见
            if (gv_buttom_menu.getVisibility() == View.GONE) {
                gv_buttom_menu.setVisibility(View.VISIBLE);
            } else {
                gv_buttom_menu.setVisibility(View.GONE);
            }
        }
        return true;
    }

    /**
     * 加载用户信息
     */
    private void loadUserList() {
        lv_userList = findViewById(R.id.lv_userList);
        ArrayList users_data = DBHelper.getInstance(this).getUserList();
        adapter = new SimpleAdapter(this, users_data, R.layout.list_item, new String[]{"imageid", "name", "phone"}, new int[]{R.id.user_image,R.id.tv_showname, R.id.tv_showPhone});
        lv_userList.setAdapter(adapter);
        lv_userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap map = (HashMap) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(MainActivity.this,ShowDetailActivity.class);
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
        gv_buttom_menu.setNumColumns(4);    //设置列数
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
                        Intent intent = new Intent(MainActivity.this,AddNewActivity.class);
                        //0代表请求跳转到添加页面
                        startActivityForResult(intent,0);
                        break;
                    }
                    case 1:{
                        if (ll_search.getVisibility() == View.GONE) {
                            ll_search.setVisibility(View.VISIBLE);
                        } else {
                            ll_search.setVisibility(View.GONE);
                        }
                        break;
                    }

                    case 2:{
                        Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                        startActivityForResult(intent,0);
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
                        R.layout.list_item, new String[]{"imageid", "name", "phone"},
                        new int[]{R.id.user_image,R.id.tv_showname, R.id.tv_showPhone});
                lv_userList.setAdapter(adapter);
            } else if (resultCode == 2) {
                //增加失败，不刷新页面
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initButtomMenuMap(ArrayList data, HashMap map) {
        map.put("itemImage", R.drawable.add);
        map.put("itemText", "增加");
        data.add(map);

        map = new HashMap();
        map.put("itemImage", R.drawable.search);
        map.put("itemText", "查询");
        data.add(map);

        map = new HashMap();
        map.put("itemImage", R.drawable.group);
        map.put("itemText", "分组");
        data.add(map);

        map = new HashMap();
        map.put("itemImage", R.drawable.quit);
        map.put("itemText", "退出");
        data.add(map);


    }
}
