package com.example.addresslist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.addresslist.pojo.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            map.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
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


    /**
     * 更新数据
     * @return
     */
    public boolean update(User user) {
        openDatabase();
        int update = 0;
            ContentValues value = new ContentValues();
            value.put("name", user.name);
            value.put("email", user.email);
            value.put("company", user.company);
            value.put("otherPhone", user.otherPhone);
            value.put("remark", user.remark);
            value.put("phone", user.phone);
            value.put("imageid", user.imageId);
            value.put("position", user.position);
            update = database.update("user", value, "_id = ?", new String[]{user._id + ""});
        return update!=0;
    }


    /**
     * 删除用户
     * @param id
     * @return
     */
    public boolean delete(int id) {
        openDatabase();
        int delete = 0;
        String whereClause= "_id = ?";
        String[] whereArgs= new String[]{id+""};
        delete = database.delete("user", whereClause, whereArgs);
        return delete != 0;
    }

    public List<Map> selectByNameOrPhone(String message) {
        openDatabase();
        String selection = "name = ? or phone = ?";
        String[] selectionArgs = new String[]{message, message};
        Cursor cursor = database.query("user", null, selection, selectionArgs, null, null, null);

        List<Map> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap map = new HashMap();
            map.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
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

    public List selectByCompany(){
        openDatabase();
        String[] columns = new String[]{"company","count(*) as c_size"};
        String groupBy = "company";
        Cursor cursor = database.query("user", columns, null, null, groupBy, null, null);

        ArrayList list = new ArrayList();
        while (cursor.moveToNext()) {
            HashMap map = new HashMap();
            map.put("company", cursor.getString(cursor.getColumnIndex("company")));
            map.put("c_size", cursor.getInt(cursor.getColumnIndex("c_size")));
            list.add(map);
        }
        return list;
    }
}
