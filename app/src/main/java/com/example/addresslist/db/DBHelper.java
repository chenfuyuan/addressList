package com.example.addresslist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.addresslist.pojo.User;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "addressList";

    public static final int VERSION = 1;

    private static DBHelper instance = null;

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;

    }

    /**
     * 单例模式 获取本类实例
     * @param context
     */
    private DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    private SQLiteDatabase database;

    private void openDatabase() {
        if (database == null) {
            database = this.getWritableDatabase();

        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //建表
        StringBuffer tableCreate = new StringBuffer();
        tableCreate.append("create table user ( _id integer primary key autoincrement,").append("name text,").append("email text,").append("company text,").append("otherPhone text,").append("remark text,").append("phone text,").append("imageid int )");

        sqLiteDatabase.execSQL(tableCreate.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table if exists user";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void save(User user) {
        openDatabase();
        ContentValues value = new ContentValues();
        //必须精确对应
        value.put("name", user.name);
        value.put("email",user.email);
        value.put("company", user.company);
        value.put("otherPhone", user.otherPhone);
        value.put("remark", user.remark);
        value.put("phone", user.phone);
        value.put("imageid", user.imageId);

        //保存到数据库
        database.insert("user", null, value);
    }
}
