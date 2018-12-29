package com.example.g_jiaoyan.qq_hx.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.g_jiaoyan.qq_hx.dao.ContactsOpenHelper;

import java.util.ArrayList;
import java.util.List;
//$保存用户好友数据库的工具类
public class DBUtils {
    private static ContactsOpenHelper sContactsOpenHelper;
    private static boolean inited = false;

    //在application中初始化
    public static void init(Context context){
        sContactsOpenHelper = new ContactsOpenHelper(context);
        inited = true;
    }

    //保存当前用户好友到数据库
    public static void saveContacts(String username,List<String> contacts){
        if(!inited){
            throw  new RuntimeException("还未初始化DBU,请先初始化再使用");
        }

        SQLiteDatabase database = sContactsOpenHelper.getWritableDatabase();
        //开始事务 事务 一系列操作 要么全成功 要么全失败 只要失败 之前操作都失败
        database.beginTransaction();
        //清空表中相关数据 重新存入数据
        database.execSQL("delete from t_contact where username = '"+username+"'");
        ContentValues values = new ContentValues();
        values.put("username",username);
        for(int i = 0; i<contacts.size();i++){
            values.put("contact",contacts.get(i));
            database.insert("t_contact",null,values);
        }
        //提交事务
        database.setTransactionSuccessful();
        //释放事务资源 解除事务
        database.endTransaction();
        database.close();
    }

    //获取用户好友
    public static List<String> getContacts(String username){
        List<String> contactsList = new ArrayList<>();

        SQLiteDatabase readableDatabase = sContactsOpenHelper.getReadableDatabase();
        //orderBy  contact asc(可) 表示以contact正序排序    xxx desc 表示以xxx倒序排序
        //查找数据库中当前用户对应的字段
        Cursor cursor = readableDatabase.query("t_contact", new String[]{"contact"},
                "username=?", new String[]{username}, null, null, "contact");
        //保存所有字段到用户好友集合
        while(cursor.moveToNext()){
            String contact = cursor.getString(0);
            contactsList.add(contact);
        }
        cursor.close();//必须关闭 否则容易导致内存泄漏
        return contactsList;
    }

}
