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

public class ShowDetailActivity extends AppCompatActivity {

    private ImageButton btn_img;
    private AlertDialog imageChooseDialog;
    private Gallery gallery;
    private ImageSwitcher imageSwitcher;
    //用于保存选中图片
    private int imagePosition;
    /*存放头像*/
    private int[] images = {R.drawable.image0, R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image10, R.drawable.image11, R.drawable.image12, R.drawable.image13, R.drawable.image14, R.drawable.image15, R.drawable.image16, R.drawable.image17, R.drawable.image18, R.drawable.image19, R.drawable.image20};

    /*输入栏*/
    private EditText edit_name;
    private EditText edit_phone;
    private EditText edit_email;
    private EditText edit_company;
    private EditText edit_ohterPhone;
    private EditText edit_remark;
    private EditText edit_position;
    private int imageId;
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
        initImageChooseDialog();
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
        btn_img.setOnClickListener(view -> {
            imageChooseDialog.show();
        });

//      是输入框可编辑，并替换成保存按钮
        btn_edit.setOnClickListener(view->{
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
            System.out.println(user._id);;
            user.name = edit_name.getText().toString();
            user.phone = edit_phone.getText().toString();
            user.email = edit_email.getText().toString();
            user.otherPhone = edit_ohterPhone.getText().toString();
            user.company = edit_company.getText().toString();
            user.remark = edit_remark.getText().toString();
            user.position = edit_position.getText().toString();
            user.imageId = images[imagePosition];

            boolean success = DBHelper.getInstance(ShowDetailActivity.this).update(user);
            if (success) {
                //成功后，锁定编辑框，数据库更改数据
                Toast.makeText(ShowDetailActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                initEditTextNotEnabled();
            } else {
                Toast.makeText(ShowDetailActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        });

        btn_return.setOnClickListener(view -> {
            setResult(1);
            finish();
        });

        btn_delete.setOnClickListener(view ->{
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

    }

    /**
     * 初始化图片弹窗
     */
    private void initImageChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择图像");
        //确定
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                btn_img.setImageResource(images[imagePosition]);
            }
        });

        /*渲染成view*/
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.images_switch, null);
        //从view 中获取gallery 和 imageSwitcher
        gallery = view.findViewById(R.id.img_gallery);
        imageSwitcher = view.findViewById(R.id.img_switcher);
        //将图片放入
        gallery.setAdapter(new ImageAdapter(this));
        gallery.setSelection(images.length / 2);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                imagePosition = i;
                imageSwitcher.setImageResource(images[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        imageSwitcher.setFactory(new MyViewFactory(this));

        builder.setView(view);
        imageChooseDialog = builder.create();
    }

    /**
     * 填充信息
     */
    private void initAllViewText() {
        btn_img.setImageResource((Integer) userMap.get("imageid"));
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
        btn_img = findViewById(R.id.btn_img);
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

    /**
     * ImageAdapter
     */
    class ImageAdapter extends BaseAdapter {
        private Context context;

        public ImageAdapter(Context context) {
            System.out.println(context);
            this.context = context;
        }

        @Override

        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(images[i]);
            imageView.setAdjustViewBounds(true);
            imageView.setLayoutParams(new Gallery.LayoutParams(80, 80));
            imageView.setPadding(15, 10, 15, 10);
            return imageView;
        }
    }
}
