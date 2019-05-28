package com.lkkdesign.changlong.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lkkdesign.changlong.data.model.Tb_photometer;

import java.util.ArrayList;
import java.util.List;

public class PhotometerDao {

    private DBOpenHelper helper;// 创建DBOpenHelper对象
    private SQLiteDatabase db;// 创建SQLiteDatabase对象

    // 定义构造函数
    public PhotometerDao(Context context) {
        helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
    }


    /**
     * 添加光度计信息
     *
     * @param tb_photometer
     */
    public void add(Tb_photometer tb_photometer) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        db.execSQL(
                "insert into tb_photometer (_id, userId, wavelength, absorbance, tranatre, current, voltage, temperature, time) values (?,?,?,?,?,?,?,?,?)",
                new Object[]{tb_photometer.get_id(), tb_photometer.getUserId(), tb_photometer.getWavelength(), tb_photometer.getAbsorbance(),
                        tb_photometer.getTranatre(), tb_photometer.getCurrent(), tb_photometer.getVoltage(), tb_photometer.getTemperature(),
                        tb_photometer.getTime()});
    }

    /**
     * 更新光度计信息
     *
     * @param tb_photometer
     */
    public void update(Tb_photometer tb_photometer) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        db.execSQL("update tb_photometer set userId = ?, wavelength = ?, absorbance = ?, tranatre = ?, current = ?, voltage = ?, temperature = ?, time = ? where _id = ?",
                new Object[]{tb_photometer.getUserId(), tb_photometer.getWavelength(), tb_photometer.getAbsorbance(),
                        tb_photometer.getTranatre(), tb_photometer.getCurrent(), tb_photometer.getVoltage(), tb_photometer.getTemperature(),
                        tb_photometer.getTime(), tb_photometer.get_id()});
    }

    /**
     * 更新光度计信息
     *
     * @param tb_photometer
     */
    public void updateByClassic(Tb_photometer tb_photometer) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        db.execSQL("update tb_photometer set _id = ?, item = ?,name = ?,wavelength = ?,density = ?,tranatre = ?,absorbance = ?,time =?,mark= ? where userId = ?",
                new Object[]{tb_photometer.get_id(), tb_photometer.getWavelength(), tb_photometer.getAbsorbance(),
                        tb_photometer.getTranatre(), tb_photometer.getCurrent(), tb_photometer.getVoltage(), tb_photometer.getTemperature(),
                        tb_photometer.getTime(), tb_photometer.getUserId()});
    }

    /**
     * 查找光度计信息
     * 查询条件：光度计类别
     *
     * @param wavelength
     * @return
     */
    public List<Tb_photometer> findByWavelength(String wavelength) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        List<Tb_photometer> tb_photometer = new ArrayList<Tb_photometer>();// 创建集合对象
        Cursor cursor = db.rawQuery("select * from tb_photometer where wavelength = ?", new String[]{wavelength});// 根据编号查找光度计信息，并存储到Cursor类中
        // 遍历所有的光度计信息
        while (cursor.moveToNext()) {
            // 将遍历到的光度计信息添加到集合中
            tb_photometer.add(new Tb_photometer(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("userId")),
                    cursor.getString(cursor.getColumnIndex("wavelength")),
                    cursor.getString(cursor.getColumnIndex("absorbance")),
                    cursor.getString(cursor.getColumnIndex("tranatre")),
                    cursor.getString(cursor.getColumnIndex("current")),
                    cursor.getString(cursor.getColumnIndex("voltage")),
                    cursor.getString(cursor.getColumnIndex("temperature")),
                    cursor.getString(cursor.getColumnIndex("time"))));
        }
        return tb_photometer;// 返回集合
    }

    /**
     * 获取光度计信息
     *
     * @param start 起始位置
     * @param count 每页显示数量
     * @return
     */
    public List<Tb_photometer> getScrollData(int start, int count) {
        List<Tb_photometer> tb_photometer = new ArrayList<Tb_photometer>();// 创建集合对象
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        // 获取所有光度计信息
        Cursor cursor = db.rawQuery("select * from tb_photometer limit ?,?", new String[]{String.valueOf(start), String.valueOf(count)});
        // 遍历所有的光度计信息
        while (cursor.moveToNext()) {
            // 将遍历到的光度计信息添加到集合中
            tb_photometer.add(new Tb_photometer(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("userId")),
                    cursor.getString(cursor.getColumnIndex("wavelength")),
                    cursor.getString(cursor.getColumnIndex("absorbance")),
                    cursor.getString(cursor.getColumnIndex("tranatre")),
                    cursor.getString(cursor.getColumnIndex("current")),
                    cursor.getString(cursor.getColumnIndex("voltage")),
                    cursor.getString(cursor.getColumnIndex("temperature")),
                    cursor.getString(cursor.getColumnIndex("time"))));
        }
        return tb_photometer;// 返回集合
    }

    /**
     * 刪除光度计信息
     *
     * @param ids
     */
    public void detele(Integer... ids) {
        // 判断是否存在要删除的id
        if (ids.length > 0) {
            StringBuffer sb = new StringBuffer();// 创建StringBuffer对象
            // 遍历要删除的id集合
            for (int i = 0; i < ids.length; i++) {
                sb.append('?').append(',');// 将删除条件添加到StringBuffer对象中
            }
            sb.deleteCharAt(sb.length() - 1);// 去掉最后一个“,“字符
            db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
            // 执行删除收入信息操作
            db.execSQL("delete from tb_photometer where _id in (" + sb + ")", ids);
        }
    }

    /**
     * 刪除光度计信息
     *
     * @param itemNum
     */
    public void deteleByItemCode(String itemNum) {
//        if (ids.length > 0)// 判断是否存在要删除的id
//        {
//            StringBuffer sb = new StringBuffer();// 创建StringBuffer对象
//            for (int i = 0 ; i < ids.length ; i++)// 遍历要删除的id集合
//            {
//                sb.append('?').append(',');// 将删除条件添加到StringBuffer对象中
//            }
//            sb.deleteCharAt(sb.length() - 1);// 去掉最后一个“,“字符
//            db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
//            // 执行删除收入信息操作
//            //db.execSQL("delete from tb_product where _id in (" + sb + ")", ids);
//
//        }
        //删除SQL语句
        String sql = "delete from tb_photometer where itemNum = " + itemNum;

        //执行SQL语句
        db.execSQL(sql);
        //db.execSQL("delete from tb_product where itemNum = ?");
    }

    /**
     * 获取多个光度计信息
     *
     * @param id 起始位置
     * @return
     */
    public List<Tb_photometer> findMultipleInId(String id) {
        List<Tb_photometer> tb_photometer = new ArrayList<Tb_photometer>();// 创建集合对象
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        // 获取所有光度计信息
        Cursor cursor = db.rawQuery("select _id, userId, wavelength, absorbance, tranatre, current, voltage, temperature, time from tb_photometer where _id in (?)", new String[]{String.valueOf(id)});
        // 遍历所有的光度计信息
        while (cursor.moveToNext()) {
            // 将遍历到的光度计信息添加到集合中
            tb_photometer.add(new Tb_photometer(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("userId")),
                    cursor.getString(cursor.getColumnIndex("wavelength")),
                    cursor.getString(cursor.getColumnIndex("absorbance")),
                    cursor.getString(cursor.getColumnIndex("tranatre")),
                    cursor.getString(cursor.getColumnIndex("current")),
                    cursor.getString(cursor.getColumnIndex("voltage")),
                    cursor.getString(cursor.getColumnIndex("temperature")),
                    cursor.getString(cursor.getColumnIndex("time"))));
        }
        return tb_photometer;// 返回集合
    }

    /**
     * 获取总记录数
     *
     * @return
     */
    public int getCount() {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select count(_id) from tb_photometer", null);// 获取光度计信息的记录数
        // 判断Cursor中是否有数据
        if (cursor.moveToNext()) {
            return cursor.getInt(0);// 返回总记录数
        }
        return 0;// 如果没有数据，则返回0
    }

    /**
     * 获取收入最大编号
     *
     * @return
     */
    public int getMaxId() {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select max(_id) from tb_photometer", null);// 获取收入信息表中的最大编号
        while (cursor.moveToLast()) {// 访问Cursor中的最后一条数据
            return cursor.getInt(0);// 获取访问到的数据，即最大编号
        }
        return 0;// 如果没有数据，则返回0
    }

    /**
     * 批量更新光度计数量为100
     */
    public void updateByBatch() {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        for (int i = 1; i < 10; i++) {
            sb1.append("WHEN ").append(i).append(" THEN ").append(200).append(" ");
            sb2.append(i).append(",");
        }
        sb1.append("WHEN 10 THEN 200");
        sb2.append(10);
        String sql = "UPDATE tb_photometer SET itemSum = CASE itemNum " + sb1 +
                " END" +
                " WHERE itemNum IN (" + sb2 + ")";
        //执行SQL语句
        db.execSQL(sql);
    }

    /**
     * 查找光度计信息
     * 查询条件：用户id
     * @param userId
     * @return
     */
    public Tb_photometer findByUserID(String userId) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select * from tb_photometer where userId = ?",
                new String[]{userId});// 根据编号查找光度计信息，并存储到Cursor类中
        // 遍历查找到的光度计信息
        if (cursor.moveToNext()) {
            return new Tb_photometer(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("userId")),
                    cursor.getString(cursor.getColumnIndex("wavelength")),
                    cursor.getString(cursor.getColumnIndex("absorbance")),
                    cursor.getString(cursor.getColumnIndex("tranatre")),
                    cursor.getString(cursor.getColumnIndex("current")),
                    cursor.getString(cursor.getColumnIndex("voltage")),
                    cursor.getString(cursor.getColumnIndex("temperature")),
                    cursor.getString(cursor.getColumnIndex("time")));
        }
        return null;// 如果没有光度计信息，则返回null
    }

    /**
     * 查找光度计信息
     *
     * @param time
     * @return
     */
    public Tb_photometer findByItem(String time) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select * from tb_photometer where item = ?",
                new String[]{time});// 根据编号查找光度计信息，并存储到Cursor类中
        // 遍历查找到的光度计信息
        if (cursor.moveToNext()) {
            return new Tb_photometer(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("userId")),
                    cursor.getString(cursor.getColumnIndex("wavelength")),
                    cursor.getString(cursor.getColumnIndex("absorbance")),
                    cursor.getString(cursor.getColumnIndex("tranatre")),
                    cursor.getString(cursor.getColumnIndex("current")),
                    cursor.getString(cursor.getColumnIndex("voltage")),
                    cursor.getString(cursor.getColumnIndex("temperature")),
                    cursor.getString(cursor.getColumnIndex("time")));
        }
        return null;// 如果没有光度计信息，则返回null
    }

}
