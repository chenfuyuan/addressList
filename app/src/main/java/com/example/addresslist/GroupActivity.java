package com.example.addresslist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.addresslist.db.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {
    private ListView lv_company;
    private SimpleAdapter adapter;
    private GridView gv_buttom_menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        loadButtonMenu();
        findAllView();
        loadCompanyList();
        setListener();
    }

    private void findAllView() {

    }

    private void setListener() {
        lv_company.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(GroupActivity.this,GroupDetailActivity.class);
                HashMap map = (HashMap) adapterView.getItemAtPosition(i);
                String companyName = (String) map.get("company");
                System.out.println(companyName);
                //将公司名作为参数传递
                intent.putExtra("companyName", companyName);
                startActivity(intent);
            }
        });

    }

    private void loadCompanyList() {
        lv_company = findViewById(R.id.lv_company);
        List company_Data = DBHelper.getInstance(this).selectByCompanySize();
        int size= DBHelper.getInstance(this).selectNotCompanySize();
        Map map = new HashMap();
        map.put("company", "无公司信息");
        map.put("c_size",size);
        company_Data.add(map);
        adapter = new SimpleAdapter(this, company_Data,R.layout.company_item, new String[]{"company", "c_size"}, new int[]{R.id.tv_company,R.id.tv_csize});
        lv_company.setAdapter(adapter);

    }

    private void test() {
        List lists = DBHelper.getInstance(null).selectByCompanySize();
        for (int i = 0; i < lists.size(); i++) {
            Map map = (Map) lists.get(i);
            String company = (String) map.get("company");
            int c_size = (int) map.get("c_size");
            System.out.println("公司名: "+company +"人数:"+c_size);
        }
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
                    case 0:{
                        finish();
                        break;
                    }
                }
            }
        });


    }

    private void initButtomMenuMap(ArrayList data, HashMap map) {

        map = new HashMap();
        map.put("itemImage", R.drawable.quit);
        map.put("itemText", "返回");
        data.add(map);


    }
}

