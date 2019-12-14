package com.example.addresslist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.addresslist.db.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {
    private ListView lv_company;
    private SimpleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        loadCompanyList();
    }

    private void loadCompanyList() {
        lv_company = findViewById(R.id.lv_company);
        List company_Data = DBHelper.getInstance(this).selectByCompany();
        adapter = new SimpleAdapter(this, company_Data,R.layout.company_item, new String[]{"company", "c_size"}, new int[]{R.id.tv_company,R.id.tv_csize});
        lv_company.setAdapter(adapter);
    }

    private void test() {
        List lists = DBHelper.getInstance(null).selectByCompany();
        for (int i = 0; i < lists.size(); i++) {
            Map map = (Map) lists.get(i);
            String company = (String) map.get("company");
            int c_size = (int) map.get("c_size");
            System.out.println("公司名: "+company +"人数:"+c_size);
        }
    }
}
