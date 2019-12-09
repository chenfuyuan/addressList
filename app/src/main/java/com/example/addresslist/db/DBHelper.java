package com.example.addresslist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.addresslist.pojo.User;

import java.util.ArrayList;
import java.util.HashMap;

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
        tableCreate.append("create table user ( _id integer primary key autoincrement,").append("name text,").append("email text,").append("company text,").append("otherPhone text,").append("remark text,").append("phone text,").append("position text,").append("imageid int )");

        sqLiteDatabase.execSQL(tableCreate.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table if exists user";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public long save(User user) {
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
        value.put("position",user.position);
        //保存到数据库
        return database.insert("user", null, value);
    }

    /**
     * 获取用户列表
     * @return
     */
    public ArrayList getUserList() {
        openDatabase();
        Cursor cursor = database.query("user", null, null, null, null, null, null);

        ArrayList list = new ArrayList();
        while (cursor.moveToNext()) {
            HashMap map = new HashMap();
            map.put("imageid",cursor.getInt(cursor.getColumnIndex("imageid")));
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
            map.put("company", cursor.getString(cursor.getColumnIndex("company")));
            map.put("email", cursor.getString(cursor.getColumnIndex("email")));
            map.put("otherPhone", cursor.getString(cursor.getColumnIndex("otherPhone")));
            map.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
            map.put("position", cursor.getString(cursor.getColumnIndex("position")));
            list.add(map);
        }
        return list;
    }


}
