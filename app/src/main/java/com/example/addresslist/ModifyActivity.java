package com.example.addresslist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.addresslist.db.DBHelper;
import com.example.addresslist.pojo.User;

import java.util.HashMap;
import java.util.Map;

public class ModifyActivity extends AppCompatActivity {


    private AlertDialog imageChooseDialog;
    private Gallery gallery;
    private ImageSwitcher imageSwitcher;
    //用于保存选中图片
    private int imagePosition;


    /*输入栏*/
    private EditText edit_name;
    private EditText edit_phone;
    private EditText edit_email;
    private EditText edit_company;
    private EditText edit_ohterPhone;
    private EditText edit_remark;
    private EditText edit_position;
    //   按钮
    private Button btn_edit;
    private Button btn_return;
    private Button btn_delete;
    private Button btn_save;

    private Map userMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail);

        Intent intent = getIntent();
        userMap = (HashMap) intent.getSerializableExtra("userMap");
        findAllView();
        initAllViewText();
        initEditTextNotEnabled();
        setOnclickListener();
    }


    private void initEditTextNotEnabled() {
        edit_name.setEnabled(false);
        edit_phone.setEnabled(false);
        edit_company.setEnabled(false);
        edit_ohterPhone.setEnabled(false);
        edit_email.setEnabled(false);
        edit_remark.setEnabled(false);
        edit_position.setEnabled(false);
    }

    /*设置点击监听器*/
    private void setOnclickListener() {

//      是输入框可编辑，并替换成保存按钮
        btn_edit.setOnClickListener(view -> {
            edit_name.setEnabled(true);
            edit_phone.setEnabled(true);
            edit_company.setEnabled(true);
            edit_position.setEnabled(true);
            edit_ohterPhone.setEnabled(true);
            edit_remark.setEnabled(true);
            edit_email.setEnabled(true);
            btn_edit.setVisibility(View.GONE);
            btn_save.setVisibility(View.VISIBLE);
        });


        /*设置保存按钮点击事件*/
        btn_save.setOnClickListener(view -> {
            String name = edit_name.getText().toString();
            if (name.equals("")) {
                Toast.makeText(this, "姓名不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            User user = new User();
            user._id = (int) userMap.get("id");
            System.out.println(user._id);
            ;
            user.name = edit_name.getText().toString();
            user.phone = edit_phone.getText().toString();
            user.email = edit_email.getText().toString();
            user.otherPhone = edit_ohterPhone.getText().toString();
            user.company = edit_company.getText().toString();
            user.remark = edit_remark.getText().toString();
            user.position = edit_position.getText().toString();

            boolean success = DBHelper.getInstance(ModifyActivity.this).update(user);
            if (success) {
                //成功后，锁定编辑框，数据库更改数据
                Toast.makeText(ModifyActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                initEditTextNotEnabled();
            } else {
                Toast.makeText(ModifyActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
            btn_save.setVisibility(View.GONE);
            btn_edit.setVisibility(View.VISIBLE);
        });

        btn_return.setOnClickListener(view -> {
            setResult(1);
            finish();
        });

        btn_delete.setOnClickListener(view -> {
            int id = (int) userMap.get("id");
            boolean success = DBHelper.getInstance(ModifyActivity.this).delete(id);
            if (success) {
                Toast.makeText(ModifyActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                setResult(1);
                finish();
            } else {
                Toast.makeText(ModifyActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * 填充信息
     */
    private void initAllViewText() {

        edit_name.setText((String) userMap.get("name"));
        edit_phone.setText((String) userMap.get("phone"));
        edit_company.setText((String) userMap.get("company"));
        edit_email.setText((String) userMap.get("email"));
        edit_ohterPhone.setText((String) userMap.get("otherPhone"));
        edit_remark.setText((String) userMap.get("remark"));
        edit_position.setText((String) userMap.get("position"));
    }

    /**
     * 找寻所有控件
     */
    private void findAllView() {
        /*找到所有控件*/

        edit_company = findViewById(R.id.edit_company);
        edit_email = findViewById(R.id.edit_email);
        edit_name = findViewById(R.id.edit_name);
        edit_ohterPhone = findViewById(R.id.edit_otherPhone);
        edit_phone = findViewById(R.id.edit_phone);
        edit_remark = findViewById(R.id.edit_remark);
        edit_position = findViewById(R.id.edit_position);
        btn_edit = findViewById(R.id.btn_edit);
        btn_return = findViewById(R.id.btn_return);
        btn_delete = findViewById(R.id.btn_delete);
        btn_save = findViewById(R.id.btn_save);
    }

    /**
     * 自定义图片选择窗体
     */
    class MyViewFactory implements ViewSwitcher.ViewFactory {
        private Context context;

        public MyViewFactory(Context context) {
            this.context = context;
        }

        @Override

        public View makeView() {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(90, 90));
            return imageView;
        }
    }
}
