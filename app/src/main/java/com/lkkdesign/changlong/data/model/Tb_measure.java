package com.lkkdesign.changlong.data.model;

public class Tb_measure {

    /**
     *
     * 此类根据客户第二版UI中的数据进行定义
     */
    private int _id;// integer primary key
    private String classic;// classic varchar(10) 测量类别：手动、自动、定时
    private String item;// item varchar(20) 曲线列表
    private String name;// name varchar(20) 曲线名称
    private int wavelength;//wavelength integer 波长
    private float density;// density float(9) 密度
    private float tranatre;// tranatre float(9) 透过率
    private float absorbance;// absorbance float(9) 吸光度
    private String userId;// userId varchar(10) 操作员ID
    private String type;// type varchar(20) COD（0-100 mg/L）
    private String result;// result varchar(20) 测量结果：C=15.000 mg/L
    private String temperature;//temperature varchar(10) 温度
    private String time;//time varchar(20) 时间
    private String mark;//mark varchar(200) 备注

    public Tb_measure(int _id, String classic, String item, String name, int wavelength, float density, float tranatre, float absorbance,
                      String userId, String type, String result, String temperature, String time, String mark) {
        this._id = _id;
        this.classic = classic;
        this.item = item;
        this.name = name;
        this.wavelength = wavelength;
        this.density = density;
        this.tranatre = tranatre;
        this.absorbance = absorbance;
        this.userId = userId;
        this.type = type;
        this.result = result;
        this.temperature = temperature;
        this.time = time;
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "Tb_measure{" +
                "_id=" + _id +
                ", classic='" + classic + '\'' +
                ", item='" + item + '\'' +
                ", name='" + name + '\'' +
                ", wavelength='" + wavelength + '\'' +
                ", density=" + density +
                ", tranatre=" + tranatre +
                ", absorbance=" + absorbance +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", result='" + result + '\'' +
                ", temperature='" + temperature + '\'' +
                ", time='" + time + '\'' +
                ", mark='" + mark + '\'' +
                '}';
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getClassic() {
        return classic;
    }

    public void setClassic(String classic) {
        this.classic = classic;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWavelength() {
        return wavelength;
    }

    public void setWavelength(int wavelength) {
        this.wavelength = wavelength;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getTranatre() {
        return tranatre;
    }

    public void setTranatre(float tranatre) {
        this.tranatre = tranatre;
    }

    public float getAbsorbance() {
        return absorbance;
    }

    public void setAbsorbance(float absorbance) {
        this.absorbance = absorbance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
