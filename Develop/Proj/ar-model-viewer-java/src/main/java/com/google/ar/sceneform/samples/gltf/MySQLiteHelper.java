package com.google.ar.sceneform.samples.gltf;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * 安卓数据库调用
 * 1.建立SQLiteOpenHelper类，然后将参数填好
 * */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static  final String CREATE_USER_TABLE = "create table user(id integer primary key autoincrement,username text,password text)";

    /**
     * 构造方法，你可以联想到创建了一个数据，类似于MySQL的建库语句
     *用于创建数据库，并配置数据库信息
     * */
    public MySQLiteHelper(Context context) {
        //构造方法的四个参数：1.上下文 2.数据库名字 3.数据库工厂直接null就可以，4.数据库版本
        super(context, "user.db", null, 1);
    }

    /**
     * 用于建表或者执行sql语句，但是没有返回值因此不推荐调用增删改查sql语句，只推荐用来建表
     * SQLiteDatabase--->SQLite数据库（Java类）
     * */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //调用执行sql语句
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);

    }

    /**
     * 用于做数据库升级的方法，如果数据库的表或者表结构表以及数据关系都发生改变则需要在这里升级
     * */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}