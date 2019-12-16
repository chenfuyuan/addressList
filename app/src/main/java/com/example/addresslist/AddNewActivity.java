package com.example.addresslist;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.addresslist.db.DBHelper;
import com.example.addresslist.pojo.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewActivity extends AppCompatActivity {
    private static final String TAG = "AddNewActivity";
    /*输入栏*/
    private EditText edit_name;
    private EditText edit_phone;
    private EditText edit_email;
    private EditText edit_company;
    private EditText edit_ohterPhone;
    private EditText edit_remark;
    private EditText edit_position;

//   按钮
    private Button btn_save;
    private Button btn_return;

    //用于暂存user
    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnew);
        //初始化
        init();

        setOnclickListener();
    }

    private void init() {
        findAllView();
    }


    private void setOnclickListener() {

        btn_save.setOnClickListener(view ->{
            user = new User();
            user.name = edit_name.getText().toString();
            user.phone = edit_phone.getText().toString();
            user.email = edit_email.getText().toString();
            user.otherPhone = edit_ohterPhone.getText().toString();
            user.company = edit_company.getText().toString();
            user.remark = edit_remark.getText().toString();
            user.position = edit_position.getText().toString();
            if (haveError()) {
                return;
            }

            //判断电话号码是否存在
            User user1 = DBHelper.getInstance(this).selectByPhone(user.phone);
            if (user1 != null) {
                Toast.makeText(this, "电话号码已存在", Toast.LENGTH_LONG).show();
                return;
            }
            Log.d(TAG, "执行save操作");
            long success = DBHelper.getInstance(AddNewActivity.this).save(user);
            if (success != -1) {
                Toast.makeText(AddNewActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
//             resultCode为1时代表增加用户成功，返回主页面
                setResult(1);
                finish();
            } else {
                Toast.makeText(AddNewActivity.this, "添加失败！请重新操作", Toast.LENGTH_SHORT).show();

//             resultCode为1时代表增加用户成功，返回主页面
                setResult(2);
                finish();

            }
            //save User
            //使用系统提供的工具类

        });

        btn_return.setOnClickListener(view ->{
            setResult(2);
            finish();
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



    /*找到所有控件*/
    private void findAllView() {

        edit_company = findViewById(R.id.edit_company);
        edit_email = findViewById(R.id.edit_email);
        edit_name = findViewById(R.id.edit_name);
        edit_ohterPhone = findViewById(R.id.edit_otherPhone);
        edit_phone = findViewById(R.id.edit_phone);
        edit_remark = findViewById(R.id.edit_remark);
        edit_position = findViewById(R.id.edit_position);
        btn_save = findViewById(R.id.btn_save);
        btn_return = findViewById(R.id.btn_return);
    }

    class MyViewFactory implements ViewSwitcher.ViewFactory {
        private Context context;

        public MyViewFactory(Context context) {
            this.context = context;
        }

        @Override

        public View makeView() {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(90,90));
            return imageView;
        }
    }

}
