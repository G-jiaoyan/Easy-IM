package com.example.g_jiaoyan.qq_hx.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//$保存用户好友的数据库
public class ContactsOpenHelper extends SQLiteOpenHelper {
    private ContactsOpenHelper(Context context,String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //创建数据库
    public ContactsOpenHelper(Context context){
        this(context,"contacts.db",null,1);
    }
    //建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table t_contact (_id integer primary key , username varchar(20) , contact varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
