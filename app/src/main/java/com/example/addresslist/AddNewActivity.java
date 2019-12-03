package com.example.addresslist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddNewActivity extends AppCompatActivity {
    private ImageButton btn_img;
    private Gallery gallery;
    private ImageSwitcher imageSwitcher;
    private AlertDialog imageChooseDialog;

    /*存放头像*/
    private int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image10, R.drawable.image11, R.drawable.image12, R.drawable.image13, R.drawable.image14, R.drawable.image15, R.drawable.image16, R.drawable.image17, R.drawable.image18, R.drawable.image19, R.drawable.image20};


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
        /*渲染成view*/
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.images_switch, null);
        gallery.setAdapter(new ImageAdapter(this));

        builder.setView(view);
        imageChooseDialog = builder.create();
    }

    private void setOnclickListener() {
        btn_img.setOnClickListener(view -> {
            imageChooseDialog.show();
        });

    }

    /*找到所有控件*/
    private void findAllView() {
        btn_img = findViewById(R.id.btn_img);
        gallery = findViewById(R.id.img_gallery);
        imageSwitcher = findViewById(R.id.img_switcher);
    }


    class ImageAdapter extends BaseAdapter {
        private Context context;

        public ImageAdapter(Context context) {
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
