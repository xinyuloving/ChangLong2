package com.lkkdesign.changlong.data.model;

public class Tb_data {

    /**
     * 此类根据客户第一版UI中的数据进行定义
     */
    private int _id;//id，自动增长
    private String classic ;//类别：手动、自动、定时
    private String item;//曲线列表
    private String name;//曲线名称
    private int wavelength;//波长
    private float density;//波长
    private float tranatre;//透过率
    private float absorbance;//吸光度
    private String time;//时间
    private String mark;//备注

    public Tb_data() {

    }

    public Tb_data(int _id, String classic, String item, String name, int wavelength, float density, float tranatre, float absorbance, String time, String mark) {
        this._id = _id;
        this.classic = classic;
        this.item = item;
        this.name = name;
        this.wavelength = wavelength;
        this.density = density;
        this.tranatre = tranatre;
        this.absorbance = absorbance;
        this.time = time;
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "Tb_data{" +
                "_id=" + _id +
                ", classic='" + classic + '\'' +
                ", item='" + item + '\'' +
                ", name='" + name + '\'' +
                ", wavelength=" + wavelength +
                ", density=" + density +
                ", tranatre=" + tranatre +
                ", absorbance=" + absorbance +
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
