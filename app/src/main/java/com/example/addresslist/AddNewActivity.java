package com.example.addresslist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddNewActivity extends AppCompatActivity {
    private ImageButton btn_img;

    private AlertDialog imageChooseDialog;
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
        builder.setView(view);
        imageChooseDialog = builder.create();
    }

    private void setOnclickListener() {
        btn_img.setOnClickListener(view->{
            imageChooseDialog.show();
        });

    }

    /*找到所有控件*/
    private void findAllView() {
        btn_img = findViewById(R.id.btn_img);

    }

}
