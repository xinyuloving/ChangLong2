package com.lkkdesign.changlong.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lkkdesign.changlong.data.model.Tb_user;

import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private DBOpenHelper helper;// 创建DBOpenHelper对象
    private SQLiteDatabase db;// 创建SQLiteDatabase对象

    // 定义构造函数
    public UserDao(Context context) {
        helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
    }


    /**
     * 添加用户信息
     *
     * @param tb_user
     */
    public void add(Tb_user tb_user) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        db.execSQL(
                "insert into tb_user (_id,name,password,jobNo,company,contact,address,time) values (?,?,?,?,?,?,?,?)",
                new Object[]{tb_user.get_id(), tb_user.getName(), tb_user.getPassword(), tb_user.getJobNo(), tb_user.getCompany(), tb_user.getContact(), tb_user.getAddress(), tb_user.getTime()});
    }

    /**
     * 更新用户信息
     *
     * @param tb_user
     */
    public void update(Tb_user tb_user) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        db.execSQL("update tb_user set name = ?,password = ?,jobNo = ?,company = ?,contact = ?,address = ?,time = ? where _id = ?",
                new Object[]{tb_user.getName(),tb_user.getPassword(), tb_user.getJobNo(), tb_user.getCompany(), tb_user.getContact(), tb_user.getAddress(), tb_user.getTime(), tb_user.get_id()});
    }

    /**
     * 更新用户信息
     *
     * @param tb_user
     */
    public void updateByJobNo(Tb_user tb_user) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        db.execSQL("update tb_user set _id = ?, name = ?,password = ?, company = ?,contact = ?,address = ?,time=? where jobNo = ?",
                new Object[]{tb_user.get_id(), tb_user.getName(),tb_user.getPassword(), tb_user.getCompany(), tb_user.getContact(), tb_user.getAddress(), tb_user.getTime(), tb_user.getJobNo()});
    }

    /**
     * 查找用户信息
     * 查询条件：用户工号
     *
     * @param jobNo
     * @return
     */
    public Tb_user findByJobNo(String jobNo) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select * from tb_user where jobNo = ?", new String[]{jobNo});// 根据编号查找用户信息，并存储到Cursor类中
        // 遍历所有的用户信息
        while (cursor.moveToNext()) {
            // 将遍历到的用户信息添加到集合中
            return new Tb_user(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("jobNo")),
                    cursor.getString(cursor.getColumnIndex("company")),
                    cursor.getString(cursor.getColumnIndex("contact")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("time")));
        }
        return null;// 返回集合
    }
    /**
     * 查找用户信息
     * 查询条件：用户名称
     *
     * @param strName
     * @return
     */
    public Tb_user findByName(String strName) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select * from tb_user where name = ?", new String[]{strName});// 根据编号查找用户信息，并存储到Cursor类中
        // 遍历所有的用户信息
        while (cursor.moveToNext()) {
            // 将遍历到的用户信息添加到集合中
            return new Tb_user(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("jobNo")),
                    cursor.getString(cursor.getColumnIndex("company")),
                    cursor.getString(cursor.getColumnIndex("contact")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("time")));
        }
        return null;// 返回集合
    }

 /**
     * 查找用户信息
     * 查询条件：用户工号 ， 用户密码
     *
     * @param name
     * @param password
     * @return
     */
    public int findByNameAndPassword(String name,String password) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select count(*) from tb_user where name = ? and password = ?", new String[]{name,password});// 根据编号查找用户信息，并存储到Cursor类中
        // 遍历所有的用户信息
        // 判断Cursor中是否有数据
        if (cursor.moveToNext()) {
            return cursor.getInt(0);// 返回总记录数
        }
        return 0;// 如果没有数据，则返回0
//        int intCount = cursor.getCount();
//        System.out.println("UserDao->intCount="+intCount);
//        while (cursor.moveToNext()) {
//            // 将遍历到的用户信息添加到集合中
//            return new Tb_user(cursor.getInt(cursor.getColumnIndex("_id")),
//                    cursor.getString(cursor.getColumnIndex("name")),
//                    cursor.getString(cursor.getColumnIndex("password")),
//                    cursor.getString(cursor.getColumnIndex("jobNo")),
//                    cursor.getString(cursor.getColumnIndex("company")),
//                    cursor.getString(cursor.getColumnIndex("contact")),
//                    cursor.getString(cursor.getColumnIndex("address")),
//                    cursor.getString(cursor.getColumnIndex("time")));
//        }
//        return null;// 返回集合
//        return intCount;
    }

    /**
     * 获取用户信息
     *
     * @param start 起始位置
     * @param count 每页显示数量
     * @return
     */
    public List<Tb_user> getScrollData(int start, int count) {
        List<Tb_user> tb_user = new ArrayList<Tb_user>();// 创建集合对象
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        // 获取所有用户信息
        Cursor cursor = db.rawQuery("select * from tb_user limit ?,?", new String[]{String.valueOf(start), String.valueOf(count)});
        // 遍历所有的用户信息
        while (cursor.moveToNext()) {
            // 将遍历到的用户信息添加到集合中
            tb_user.add(new Tb_user(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("jobNo")),
                    cursor.getString(cursor.getColumnIndex("company")),
                    cursor.getString(cursor.getColumnIndex("contact")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("time"))));
        }
        return tb_user;// 返回集合
    }

    /**
     * 刪除用户信息
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
            db.execSQL("delete from tb_user where _id in (" + sb + ")", ids);
        }
    }

    /**
     * 刪除用户信息
     *
     * @param jobNo
     */
    public void deteleByJobNo(String jobNo) {
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
        String sql = "delete from tb_user where jobNo = \'" + jobNo + "\'";

        //执行SQL语句
        db.execSQL(sql);
        //db.execSQL("delete from tb_product where itemNum = ?");
    }

    /**
     * 获取多个用户信息
     *
     * @param id 起始位置
     * @return
     */
    public List<Tb_user> findMultipleInId(String id) {
        List<Tb_user> tb_user = new ArrayList<Tb_user>();// 创建集合对象
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        // 获取所有用户信息
        Cursor cursor = db.rawQuery("select _id,name,jobNo,company,contact,address from tb_user where _id in (?)", new String[]{String.valueOf(id)});
        // 遍历所有的用户信息
        while (cursor.moveToNext()) {
            // 将遍历到的用户信息添加到集合中
            tb_user.add(new Tb_user(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("jobNo")),
                    cursor.getString(cursor.getColumnIndex("company")),
                    cursor.getString(cursor.getColumnIndex("contact")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("time"))));
        }
        return tb_user;// 返回集合
    }

    /**
     * 获取总记录数
     *
     * @return
     */
    public int getCount() {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select count(_id) from tb_user", null);// 获取用户信息的记录数
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
        Cursor cursor = db.rawQuery("select max(_id) from tb_user", null);// 获取收入信息表中的最大编号
        while (cursor.moveToLast()) {// 访问Cursor中的最后一条数据
            return cursor.getInt(0);// 获取访问到的数据，即最大编号
        }
        return 0;// 如果没有数据，则返回0
    }

    /**
     * 批量更新用户数量为100
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
        String sql = "UPDATE tb_user SET itemSum = CASE itemNum " + sb1 +
                " END" +
                " WHERE itemNum IN (" + sb2 + ")";
        //执行SQL语句
        db.execSQL(sql);
    }

    /**
     * 查找用户信息
     *
     * @param time
     * @return
     */
    public Tb_user findByTime(String time) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select * from tb_user where time = ?",
                new String[]{time});// 根据编号查找用户信息，并存储到Cursor类中
        // 遍历查找到的用户信息
        if (cursor.moveToNext()) {
            return new Tb_user(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("jobNo")),
                    cursor.getString(cursor.getColumnIndex("company")),
                    cursor.getString(cursor.getColumnIndex("contact")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("time")));
        }
        return null;// 如果没有用户信息，则返回null
    }

    /**
     * 查找用户信息
     *
     * @param id
     * @return
     */
    public Tb_user findByID(String id) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select * from tb_user where _id = ?",
                new String[]{id});// 根据编号查找用户信息，并存储到Cursor类中
        // 遍历查找到的用户信息
        if (cursor.moveToNext()) {
            return new Tb_user(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("jobNo")),
                    cursor.getString(cursor.getColumnIndex("company")),
                    cursor.getString(cursor.getColumnIndex("contact")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("time")));
        }
        return null;// 如果没有用户信息，则返回null
    }

//    /**
//     * 查询表中福签数量最多的对应柜号
//     * 先找出签署最多的用户编号，然后返回一个随机数值
//     * SELECT max(itemNum) FROM tb_user where itemSum=(SELECT max(itemSum) FROM tb_user);
//     * SELECT itemNum FROM tb_user where itemSum=(SELECT max(itemSum) FROM tb_user);
//     * SELECT itemNum FROM tb_user where itemSum=(SELECT max(itemSum) FROM tb_user) ORDER BY RANDOM() LIMIT 1 ;
//     */
//    public int getMaxNumBySum() {
//        db = helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT itemNum FROM tb_user where itemSum=(SELECT max(itemSum) FROM tb_user) ORDER BY RANDOM() LIMIT 1 ;", null);
//        //遍历查找到的用户信息
//        while (cursor.moveToLast()) {// 访问Cursor中的最后一条数据
//            return cursor.getInt(0);// 获取访问到的数据，即最大编号
//        }
//        return 0;
//    }


}
