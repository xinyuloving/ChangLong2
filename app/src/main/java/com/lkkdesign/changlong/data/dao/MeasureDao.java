package com.lkkdesign.changlong.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lkkdesign.changlong.data.model.Tb_measure;

import java.util.ArrayList;
import java.util.List;

public class MeasureDao {

    private DBOpenHelper helper;// 创建DBOpenHelper对象
    private SQLiteDatabase db;// 创建SQLiteDatabase对象

    // 定义构造函数
    public MeasureDao(Context context) {
        helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
    }


    /**
     * 添加测量结果信息
     *
     * @param tb_measure
     */
    public void add(Tb_measure tb_measure) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        db.execSQL(
                "insert into tb_measure (_id, classic, item, name, wavelength, density, tranatre, absorbance, userId, type, result, temperature, time, mark,measure_name,entity_name,sampling_time,sampler,inspector) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new Object[]{tb_measure.get_id(), tb_measure.getClassic(), tb_measure.getItem(), tb_measure.getName(),
                        tb_measure.getWavelength(), tb_measure.getDensity(), tb_measure.getTranatre(), tb_measure.getAbsorbance(),
                        tb_measure.getUserId(), tb_measure.getType(), tb_measure.getResult(), tb_measure.getTemperature(),
                        tb_measure.getTime(), tb_measure.getMark(),tb_measure.getMeasure_name(),tb_measure.getEntity_name(),tb_measure.getSampling_time(),
                        tb_measure.getSampler(),tb_measure.getInspector()});
    }

    /**
     * 更新测量结果信息
     *
     * @param tb_measure
     */
    public void update(Tb_measure tb_measure) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        db.execSQL("update tb_measure set classic = ?, item = ?, name = ?, wavelength = ?, density = ?, tranatre = ?, absorbance = ?, userId = ?, type = ?, result = ?, temperature = ?, time = ?, mark = ? where _id = ?",
                new Object[]{tb_measure.getClassic(), tb_measure.getItem(), tb_measure.getName(),
                        tb_measure.getWavelength(), tb_measure.getDensity(), tb_measure.getTranatre(),
                        tb_measure.getAbsorbance(), tb_measure.getUserId(), tb_measure.getType(),
                        tb_measure.getResult(), tb_measure.getTemperature(), tb_measure.getTime(),
                        tb_measure.getMark(), tb_measure.get_id()});
    }

    /**
     * 更新测量结果信息
     *
     * @param tb_measure
     */
    public void updateByClassic(Tb_measure tb_measure) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        db.execSQL("update tb_measure set _id = ?, item = ?, name = ?, wavelength = ?, density = ?, tranatre = ?, absorbance = ?, userId = ?, type = ?, result = ?, temperature = ?, time = ?, mark = ? where classic = ?",
                new Object[]{tb_measure.get_id(), tb_measure.getItem(), tb_measure.getName(), tb_measure.getWavelength(),
                        tb_measure.getDensity(), tb_measure.getTranatre(), tb_measure.getAbsorbance(), tb_measure.getUserId(),
                        tb_measure.getType(), tb_measure.getResult(), tb_measure.getTemperature(), tb_measure.getTime(),
                        tb_measure.getMark(), tb_measure.getClassic()});
    }

    /**
     * 查找测量结果信息
     * 查询条件：测量类别
     *
     * @param classic
     * @return
     */
    public List<Tb_measure> findByClassic(String classic) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        List<Tb_measure> tb_measure = new ArrayList<Tb_measure>();// 创建集合对象
        Cursor cursor = db.rawQuery("select * from tb_measure where classic = ?", new String[]{classic});// 根据编号查找曲线信息，并存储到Cursor类中
        // 遍历所有的曲线信息
        while (cursor.moveToNext()) {
            // 将遍历到的曲线信息添加到集合中
            tb_measure.add(new Tb_measure(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("classic")),
                    cursor.getString(cursor.getColumnIndex("item")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getInt(cursor.getColumnIndex("wavelength")),
                    cursor.getFloat(cursor.getColumnIndex("density")),
                    cursor.getFloat(cursor.getColumnIndex("tranatre")),
                    cursor.getFloat(cursor.getColumnIndex("absorbance")),
                    cursor.getString(cursor.getColumnIndex("userId")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("result")),
                    cursor.getString(cursor.getColumnIndex("temperature")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("mark")),
                    cursor.getString(cursor.getColumnIndex("measure_name")),
                    cursor.getString(cursor.getColumnIndex("entity_name")),
                    cursor.getString(cursor.getColumnIndex("sampling_time")),
                    cursor.getString(cursor.getColumnIndex("sampler")),
                    cursor.getString(cursor.getColumnIndex("inspector"))));



        }
        return tb_measure;// 返回集合
    }

    public List<Tb_measure> findBySearchCondition(String searchContent,String searchCondition) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        List<Tb_measure> tb_measure = new ArrayList<Tb_measure>();// 创建集合对象
        Cursor cursor = db.rawQuery("select * from tb_measure ",null);// 根据编号查找曲线信息，并存储到Cursor类中
        // 遍历所有的曲线信息
        while (cursor.moveToNext()) {
            // 将遍历到的曲线信息添加到集合中
            tb_measure.add(new Tb_measure(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("classic")),
                    cursor.getString(cursor.getColumnIndex("item")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getInt(cursor.getColumnIndex("wavelength")),
                    cursor.getFloat(cursor.getColumnIndex("density")),
                    cursor.getFloat(cursor.getColumnIndex("tranatre")),
                    cursor.getFloat(cursor.getColumnIndex("absorbance")),
                    cursor.getString(cursor.getColumnIndex("userId")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("result")),
                    cursor.getString(cursor.getColumnIndex("temperature")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("mark")),
                    cursor.getString(cursor.getColumnIndex("measure_name")),
                    cursor.getString(cursor.getColumnIndex("entity_name")),
                    cursor.getString(cursor.getColumnIndex("sampling_time")),
                    cursor.getString(cursor.getColumnIndex("sampler")),
                    cursor.getString(cursor.getColumnIndex("inspector"))));



        }
        return tb_measure;// 返回集合
    }

    /**
     * 获取测量结果信息
     *
     * @param start 起始位置
     * @param count 每页显示数量
     * @return
     */
    public List<Tb_measure> getScrollData(int start, int count) {
        List<Tb_measure> tb_measure = new ArrayList<Tb_measure>();// 创建集合对象
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        // 获取所有曲线信息
        Cursor cursor = db.rawQuery("select * from tb_measure limit ?,?", new String[]{String.valueOf(start), String.valueOf(count)});
        // 遍历所有的曲线信息
        while (cursor.moveToNext()) {
            // 将遍历到的曲线信息添加到集合中
            tb_measure.add(new Tb_measure(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("classic")),
                    cursor.getString(cursor.getColumnIndex("item")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getInt(cursor.getColumnIndex("wavelength")),
                    cursor.getFloat(cursor.getColumnIndex("density")),
                    cursor.getFloat(cursor.getColumnIndex("tranatre")),
                    cursor.getFloat(cursor.getColumnIndex("absorbance")),
                    cursor.getString(cursor.getColumnIndex("userId")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("result")),
                    cursor.getString(cursor.getColumnIndex("temperature")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("mark")),
                    cursor.getString(cursor.getColumnIndex("measure_name")),
                    cursor.getString(cursor.getColumnIndex("entity_name")),
                    cursor.getString(cursor.getColumnIndex("sampling_time")),
                    cursor.getString(cursor.getColumnIndex("sampler")),
                    cursor.getString(cursor.getColumnIndex("inspector"))));
        }
        return tb_measure;// 返回集合
    }

    /**
     * 刪除曲线信息
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
            db.execSQL("delete from tb_measure where _id in (" + sb + ")", ids);
        }
    }

    /**
     * 刪除曲线信息
     *
     * @param item
     */
    public void deteleByItem(String item) {
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
        String sql = "delete from tb_measure where item = \'" + item + "\'";

        //执行SQL语句
        db.execSQL(sql);
        //db.execSQL("delete from tb_product where itemNum = ?");
    }

    /**
     * 获取多个曲线信息
     *
     * @param id 起始位置
     * @return
     */
    public List<Tb_measure> findMultipleInId(String id) {
        List<Tb_measure> tb_measure = new ArrayList<Tb_measure>();// 创建集合对象
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        // 获取所有曲线信息
        Cursor cursor = db.rawQuery("select _id ,classic ,item ,name ,wavelength ,density ,tranatre ,absorbance ,time ,mark from tb_measure where _id in (?)", new String[]{String.valueOf(id)});
        // 遍历所有的曲线信息
        while (cursor.moveToNext()) {
            // 将遍历到的曲线信息添加到集合中
            tb_measure.add(new Tb_measure(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("classic")),
                    cursor.getString(cursor.getColumnIndex("item")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getInt(cursor.getColumnIndex("wavelength")),
                    cursor.getFloat(cursor.getColumnIndex("density")),
                    cursor.getFloat(cursor.getColumnIndex("tranatre")),
                    cursor.getFloat(cursor.getColumnIndex("absorbance")),
                    cursor.getString(cursor.getColumnIndex("userId")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("result")),
                    cursor.getString(cursor.getColumnIndex("temperature")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("mark")),
                    cursor.getString(cursor.getColumnIndex("measure_name")),
                    cursor.getString(cursor.getColumnIndex("entity_name")),
                    cursor.getString(cursor.getColumnIndex("sampling_time")),
                    cursor.getString(cursor.getColumnIndex("sampler")),
                    cursor.getString(cursor.getColumnIndex("inspector"))));
        }
        return tb_measure;// 返回集合
    }

    /**
     * 获取总记录数
     *
     * @return
     */
    public int getCount() {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select count(_id) from tb_measure", null);// 获取曲线信息的记录数
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
        Cursor cursor = db.rawQuery("select max(_id) from tb_measure", null);// 获取收入信息表中的最大编号
        while (cursor.moveToLast()) {// 访问Cursor中的最后一条数据
            return cursor.getInt(0);// 获取访问到的数据，即最大编号
        }
        return 0;// 如果没有数据，则返回0
    }

    /**
     * 批量更新曲线数量为100
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
        String sql = "UPDATE tb_measure SET itemSum = CASE itemNum " + sb1 +
                " END" +
                " WHERE itemNum IN (" + sb2 + ")";
        //执行SQL语句
        db.execSQL(sql);
    }

    /**
     * 查找曲线信息
     *
     * @param time
     * @return
     */
    public Tb_measure findByTime(String time) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select * from tb_measure where time = ?",
                new String[]{time});// 根据编号查找曲线信息，并存储到Cursor类中
        // 遍历查找到的曲线信息
        if (cursor.moveToNext()) {
            return new Tb_measure(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("classic")),
                    cursor.getString(cursor.getColumnIndex("item")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getInt(cursor.getColumnIndex("wavelength")),
                    cursor.getFloat(cursor.getColumnIndex("density")),
                    cursor.getFloat(cursor.getColumnIndex("tranatre")),
                    cursor.getFloat(cursor.getColumnIndex("absorbance")),
                    cursor.getString(cursor.getColumnIndex("userId")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("result")),
                    cursor.getString(cursor.getColumnIndex("temperature")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("mark")),
                    cursor.getString(cursor.getColumnIndex("measure_name")),
                    cursor.getString(cursor.getColumnIndex("entity_name")),
                    cursor.getString(cursor.getColumnIndex("sampling_time")),
                    cursor.getString(cursor.getColumnIndex("sampler")),
                    cursor.getString(cursor.getColumnIndex("inspector")));
        }
        return null;// 如果没有曲线信息，则返回null
    }

    /**
     * 查找曲线信息
     *
     * @param item
     * @return
     */
    public Tb_measure findByItem(String item) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select * from tb_measure where item = ?",
                new String[]{item});// 根据编号查找曲线信息，并存储到Cursor类中
        // 遍历查找到的曲线信息
        if (cursor.moveToNext()) {
            return new Tb_measure(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("classic")),
                    cursor.getString(cursor.getColumnIndex("item")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getInt(cursor.getColumnIndex("wavelength")),
                    cursor.getFloat(cursor.getColumnIndex("density")),
                    cursor.getFloat(cursor.getColumnIndex("tranatre")),
                    cursor.getFloat(cursor.getColumnIndex("absorbance")),
                    cursor.getString(cursor.getColumnIndex("userId")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("result")),
                    cursor.getString(cursor.getColumnIndex("temperature")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("mark")),
                    cursor.getString(cursor.getColumnIndex("measure_name")),
                    cursor.getString(cursor.getColumnIndex("entity_name")),
                    cursor.getString(cursor.getColumnIndex("sampling_time")),
                    cursor.getString(cursor.getColumnIndex("sampler")),
                    cursor.getString(cursor.getColumnIndex("inspector")));
        }
        return null;// 如果没有曲线信息，则返回null
    }

}
