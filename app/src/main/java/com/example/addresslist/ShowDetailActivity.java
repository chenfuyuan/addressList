package com.example.addresslist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.addresslist.db.DBHelper;
import com.example.addresslist.pojo.User;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*展示详情*/
public class ShowDetailActivity extends AppCompatActivity {
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
    private Button btn_start;
    private Button btn_removeStrat;
    private Map userMap;
    private User user;

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
            user = new User();
            user._id = (int) userMap.get("id");
            System.out.println(user._id);
            user.name = edit_name.getText().toString();
            user.phone = edit_phone.getText().toString();
            user.email = edit_email.getText().toString();
            user.otherPhone = edit_ohterPhone.getText().toString();
            user.company = edit_company.getText().toString();
            user.remark = edit_remark.getText().toString();
            user.position = edit_position.getText().toString();
            user.start = (String) userMap.get("start");
            if (haveError()) {
                return;
            }

            //判断电话号码是否存在
            User user1 = DBHelper.getInstance(this).selectByPhone(user.phone);

            if (user1 != null && user1._id!=user._id) {
                Toast.makeText(this, "电话号码已存在", Toast.LENGTH_LONG).show();
                return;
            }

            boolean success = DBHelper.getInstance(ShowDetailActivity.this).update(user);
            if (success) {
                //成功后，锁定编辑框，数据库更改数据
                Toast.makeText(ShowDetailActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                initEditTextNotEnabled();
            } else {
                Toast.makeText(ShowDetailActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
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
            boolean success = DBHelper.getInstance(ShowDetailActivity.this).delete(id);
            if (success) {
                Toast.makeText(ShowDetailActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                setResult(1);
                finish();
            } else {
                Toast.makeText(ShowDetailActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        });

        btn_start.setOnClickListener(view->{
            DBHelper.getInstance(this).startUser((Integer) userMap.get("id"));
            btn_start.setVisibility(View.GONE);
            btn_removeStrat.setVisibility(View.VISIBLE);
        });

        btn_removeStrat.setOnClickListener(view->{
            DBHelper.getInstance(this).moveStartUser((Integer) userMap.get("id"));
            btn_start.setVisibility(View.VISIBLE);
            btn_removeStrat.setVisibility(View.GONE);
        });

    }
    //检测输入的是否正确
    private boolean haveError() {
        if (user.name.equals("")) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_LONG).show();
            return true;
        }
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        //验证手机号
        if (user.phone.equals("")) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_LONG).show();
            return true;
        }
        //验证手机号格式
        p = Pattern.compile("^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$"); // 验证手机号
        m = p.matcher(user.phone);
        b = m.matches();
        if (!b) {
            Toast.makeText(this, "电话号码格式错误", Toast.LENGTH_LONG).show();
            return true;
        }

        //验证邮箱
        if(!user.email.equals("")){
            p = Pattern.compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"); //验证邮箱
            m = p.matcher(user.email);
            b = m.matches();
            if (!b) {
                Toast.makeText(this, "email格式错误", Toast.LENGTH_LONG).show();
                return true;
            }
        }

        return false;
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

        boolean isStart = false;
        String start = (String) userMap.get("start");
        System.out.println("start="+start);
        isStart = start.equals("1");
        System.out.println("isStart="+isStart);
        if (isStart) {
            btn_removeStrat.setVisibility(View.VISIBLE);
            btn_start.setVisibility(View.GONE);
        } else {
            btn_removeStrat.setVisibility(View.GONE);
            btn_start.setVisibility(View.VISIBLE);
        }
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
        btn_start = findViewById(R.id.btn_start);
        btn_removeStrat = findViewById(R.id.btn_removestart);
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

