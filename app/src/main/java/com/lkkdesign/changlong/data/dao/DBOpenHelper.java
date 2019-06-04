package com.lkkdesign.changlong.data.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;// 定义数据库版本号
    //    private static final String DBNAME = "blessings.db";// 定义数据库名
    private static final String DBNAME = "curve.db";// 定义数据库名

    public DBOpenHelper(Context context) {// 定义构造函数

        super(context, DBNAME, null, VERSION);// 重写基类的构造函数
    }

    @Override
    public void onCreate(SQLiteDatabase db) {// 创建数据库

        db.execSQL("create table tb_data (_id integer primary key,classic varchar(10),item varchar(20),name varchar(20),"
                + "wavelength integer,density float(9),tranatre float(9),absorbance float(9),time varchar(20),mark varchar(200),measure_name varchar(100)," +
                "entity_name varchar(100),sampling_time varchar(20),sampler varchar(10),inspector varchar(10))");// 创建曲线信息表（根据第1次UI提供的数据确定表结构）


        db.execSQL("create table tb_measure (_id integer primary key,classic varchar(10),style varchar(10),item varchar(20),name varchar(20),"
                + "wavelength integer,density float(9),tranatre float(9),absorbance float(9),userId varchar(10),type varchar(20)," +
                "result varchar(20),temperature varchar(10),time varchar(20),mark varchar(200),measure_name varchar(100)," +
                "entity_name varchar(100),sampling_time varchar(20),sampler varchar(10),inspector varchar(10))");// 创建测量结果表（根据第二次UI提供的数据确定表结构）

        db.execSQL("create table tb_photometer (_id integer primary key,userId varchar(10),wavelength varchar(10),absorbance varchar(10),tranatre varchar(10),"
                + "current varchar(10),voltage varchar(10),temperature varchar(10),time varchar(20))");//保存光度计数据
        db.execSQL("create table tb_user (_id integer primary key,name varchar(20),password varchar(20),jobNo varchar(10),company varchar(20),"
                + "contact String(20),address varchar(100),time varchar(20))");//用户表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)// 覆写基类的onUpgrade方法，以便数据库版本更新
    {
    }
}
