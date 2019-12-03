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

public class AddNewActivity extends AppCompatActivity {
    private static final String TAG = "AddNewActivity";
    private ImageButton btn_img;
    private AlertDialog imageChooseDialog;
    private Gallery gallery;
    private ImageSwitcher imageSwitcher;
    //用于保存选中图片
    private int imagePosition;
    /*存放头像*/
    private int[] images = {R.drawable.image0,R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image10, R.drawable.image11, R.drawable.image12, R.drawable.image13, R.drawable.image14, R.drawable.image15, R.drawable.image16, R.drawable.image17, R.drawable.image18, R.drawable.image19, R.drawable.image20};

    /*输入栏*/
    private EditText edit_name;
    private EditText edit_phone;
    private EditText edit_email;
    private EditText edit_company;
    private EditText edit_ohterPhone;
    private EditText edit_remark;
    private int imageId;
//   按钮
    private Button btn_save;
    private Button btn_return;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnew);
        init();

        setOnclickListener();
    }

    private void init() {
        findAllView();
        initImageChooseDialog();
    }

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
        gallery.setSelection(images.length/2);
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

    private void setOnclickListener() {
        btn_img.setOnClickListener(view -> {
            imageChooseDialog.show();
        });

        btn_save.setOnClickListener(view ->{
            String name = edit_name.getText().toString();
            if (name.equals("")) {
                Toast.makeText(this, "姓名不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            User user = new User();
            user.phone = edit_phone.getText().toString();
            user.email = edit_email.getText().toString();
            user.otherPhone = edit_ohterPhone.getText().toString();
            user.company = edit_company.getText().toString();
            user.remark = edit_remark.getText().toString();
            imageId = images[imagePosition];
            Log.d(TAG, "执行save操作");
            DBHelper.getInstance(AddNewActivity.this).save(user);
            //save User
            //使用系统提供的工具类

        });

        btn_return.setOnClickListener(view ->{

        });

    }

    /*找到所有控件*/
    private void findAllView() {
        btn_img = findViewById(R.id.btn_img);
        edit_company = findViewById(R.id.edit_company);
        edit_email = findViewById(R.id.edit_email);
        edit_name = findViewById(R.id.edit_name);
        edit_ohterPhone = findViewById(R.id.edit_otherPhone);
        edit_phone = findViewById(R.id.edit_phone);
        edit_remark = findViewById(R.id.edit_remark);
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
            imageView.setPadding(15,10,15,10);
            return imageView;
        }
    }
}
