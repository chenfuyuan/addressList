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
     *
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
        tableCreate.append("create table user ( _id integer primary key autoincrement,").append("name text,").append("email text,").append("company text,").append("otherPhone text,").append("remark text,").append("phone text,").append("start text,").append("position text);");

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
        value.put("email", user.email);
        value.put("company", user.company);
        value.put("otherPhone", user.otherPhone);
        value.put("remark", user.remark);
        value.put("phone", user.phone);
        value.put("position", user.position);
        value.put("start", "0");
        System.out.println("1."+value.get("start"));
        //保存到数据库
        return database.insert("user", null, value);
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    public ArrayList getUserList() {
        openDatabase();
        Cursor cursor = database.query("user", null, null, null, null, null, null);

        ArrayList list = new ArrayList();
        while (cursor.moveToNext()) {
            HashMap map = new HashMap();
            map.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
            map.put("company", cursor.getString(cursor.getColumnIndex("company")));
            map.put("email", cursor.getString(cursor.getColumnIndex("email")));
            map.put("otherPhone", cursor.getString(cursor.getColumnIndex("otherPhone")));
            map.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
            map.put("position", cursor.getString(cursor.getColumnIndex("position")));
            map.put("start", cursor.getString(cursor.getColumnIndex("start")));
            list.add(map);
        }
        return list;
    }


    /**
     * 更新数据
     *
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
        value.put("position", user.position);
        value.put("start", user.start);
        update = database.update("user", value, "_id = ?", new String[]{user._id + ""});
        return update != 0;
    }


    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    public boolean delete(int id) {
        openDatabase();
        int delete = 0;
        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{id + ""};
        delete = database.delete("user", whereClause, whereArgs);
        return delete != 0;
    }

    public List<Map> selectByNameOrPhone(String message, String company) {
        openDatabase();
        String selection = "(name like ? or phone like ?)";
        String[] selectionArgs = new String[]{message, message};
        if (company != null) {
            selection += "and company = ?";
            selectionArgs = new String[]{message, message, company};
        }
        Cursor cursor = database.query("user", null, selection, selectionArgs, null, null, null);

        List<Map> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap map = new HashMap();
            map.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
            map.put("company", cursor.getString(cursor.getColumnIndex("company")));
            map.put("email", cursor.getString(cursor.getColumnIndex("email")));
            map.put("otherPhone", cursor.getString(cursor.getColumnIndex("otherPhone")));
            map.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
            map.put("position", cursor.getString(cursor.getColumnIndex("position")));
            map.put("start", cursor.getString(cursor.getColumnIndex("start")));
            list.add(map);
        }

        return list;

    }

    public List selectByCompanySize() {
        openDatabase();
        String[] columns = new String[]{"company", "count(*) as c_size"};
        String selection = "company is not null and company!=''";
        String groupBy = "company";
        Cursor cursor = database.query("user", columns, selection, null, groupBy, null, null);
        ArrayList list = new ArrayList();
        while (cursor.moveToNext()) {
            HashMap map = new HashMap();
            map.put("company", cursor.getString(cursor.getColumnIndex("company")));
            map.put("c_size", cursor.getInt(cursor.getColumnIndex("c_size")));
            list.add(map);
        }
        return list;
    }

    public int selectNotCompanySize() {
        openDatabase();
        String[] columns = new String[]{"count(*) as c_size"};
        String selection = "company is null or company=''";
        Cursor cursor = database.query("user", columns, selection, null, null, null, null);
        cursor.moveToNext();
        int size = cursor.getInt(cursor.getColumnIndex("c_size"));
        return size;

    }

    public List selectByComapany(String name) {
        openDatabase();
        String selection = "company = ?";
        String[] selectionArgs = new String[]{name};
        Cursor cursor = database.query("user", null, selection, selectionArgs, null, null, null);
        ArrayList list = new ArrayList();
        while (cursor.moveToNext()) {
            HashMap map = new HashMap();
            map.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
            map.put("company", cursor.getString(cursor.getColumnIndex("company")));
            map.put("email", cursor.getString(cursor.getColumnIndex("email")));
            map.put("otherPhone", cursor.getString(cursor.getColumnIndex("otherPhone")));
            map.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
            map.put("position", cursor.getString(cursor.getColumnIndex("position")));
            map.put("start", cursor.getString(cursor.getColumnIndex("start")));
            list.add(map);
        }
        return list;
    }

    public List selectNotCompany() {
        openDatabase();
        String selection = "company is null or company =''";
        Cursor cursor = database.query("user", null, selection, null, null, null, null);
        ArrayList list = new ArrayList();
        while (cursor.moveToNext()) {
            HashMap map = new HashMap();
            map.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
            map.put("company", cursor.getString(cursor.getColumnIndex("company")));
            map.put("email", cursor.getString(cursor.getColumnIndex("email")));
            map.put("otherPhone", cursor.getString(cursor.getColumnIndex("otherPhone")));
            map.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
            map.put("position", cursor.getString(cursor.getColumnIndex("position")));
            map.put("start", cursor.getString(cursor.getColumnIndex("start")));
            list.add(map);
        }
        return list;
    }

    public User selectByPhone(String phone) {
        openDatabase();
        String selection = "phone = ?";
        String[] selectionArgs = new String[]{phone};
        Cursor cursor = database.query("user", null, selection, selectionArgs, null, null, null);
        User user = null;
        if (cursor.moveToNext()) {
            user = new User();
            user._id = cursor.getInt(cursor.getColumnIndex("_id"));
            user.name = cursor.getString(cursor.getColumnIndex("name"));
            user.phone = cursor.getString(cursor.getColumnIndex("phone"));
            user.company = cursor.getString(cursor.getColumnIndex("company"));
            user.email = cursor.getString(cursor.getColumnIndex("email"));
            user.otherPhone = cursor.getString(cursor.getColumnIndex("otherPhone"));
            user.remark = cursor.getString(cursor.getColumnIndex("remark"));
            user.position = cursor.getString(cursor.getColumnIndex("position"));
            user.start = cursor.getString(cursor.getColumnIndex("start"));
        }
        return user;
    }

    public boolean startUser(int id) {
        openDatabase();
        int update = 0;
        ContentValues value = new ContentValues();
        value.put("start","1");
        update = database.update("user", value, "_id = ?", new String[]{id + ""});
        return update != 0;
    }

    public boolean moveStartUser(int id) {
        openDatabase();
        int update = 0;
        ContentValues value = new ContentValues();
        value.put("start","0");
        update = database.update("user", value, "_id = ?", new String[]{id + ""});
        return update != 0;
    }

    public ArrayList selectByStart() {
        openDatabase();
        String selection = "start = 1";
        Cursor cursor = database.query("user", null, selection, null, null, null, null);

        ArrayList list = new ArrayList();
        while (cursor.moveToNext()) {
            HashMap map = new HashMap();
            map.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
            map.put("company", cursor.getString(cursor.getColumnIndex("company")));
            map.put("email", cursor.getString(cursor.getColumnIndex("email")));
            map.put("otherPhone", cursor.getString(cursor.getColumnIndex("otherPhone")));
            map.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
            map.put("position", cursor.getString(cursor.getColumnIndex("position")));
            map.put("start", cursor.getString(cursor.getColumnIndex("start")));
            list.add(map);
        }
        return list;
    }

    public List<Map> selectStartByNameOrPhone(String message) {
        openDatabase();
        String selection = "(name like ? or phone like ?)and start = 1";
        String[] selectionArgs = new String[]{message, message};
        Cursor cursor = database.query("user", null, selection, selectionArgs, null, null, null);

        List<Map> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap map = new HashMap();
            map.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
            map.put("company", cursor.getString(cursor.getColumnIndex("company")));
            map.put("email", cursor.getString(cursor.getColumnIndex("email")));
            map.put("otherPhone", cursor.getString(cursor.getColumnIndex("otherPhone")));
            map.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
            map.put("position", cursor.getString(cursor.getColumnIndex("position")));
            map.put("start", cursor.getString(cursor.getColumnIndex("start")));
            list.add(map);
        }

        return list;

    }
}
