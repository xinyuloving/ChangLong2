package com.lkkdesign.changlong.data.model;

public class Tb_photometer {

    /**
     * 光度计
     * 此类根据客户第二版UI中的数据进行定义
     */
    private int _id;// integer primary key 自增id
    private String userId;//userId varchar(10) 操作员ID
    private String wavelength;//wavelength varchar(10) 波长
    private String absorbance;//absorbance varchar(10) 吸光度
    private String tranatre;//tranatre varchar(10) 透过率
    private String current;//current varchar(10) 电流
    private String voltage;//voltage varchar(10) 电压
    private String temperature;//temperature varchar(10) 温度
    private String time;//time varchar(20) 时间

    public Tb_photometer(int _id, String userId, String wavelength, String absorbance, String tranatre, String current,
                         String voltage, String temperature, String time) {
        this._id = _id;
        this.userId = userId;
        this.wavelength = wavelength;
        this.absorbance = absorbance;
        this.tranatre = tranatre;
        this.current = current;
        this.voltage = voltage;
        this.temperature = temperature;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Tb_photometer{" +
                "_id=" + _id +
                ", userId='" + userId + '\'' +
                ", wavelength='" + wavelength + '\'' +
                ", absorbance='" + absorbance + '\'' +
                ", tranatre='" + tranatre + '\'' +
                ", current='" + current + '\'' +
                ", voltage='" + voltage + '\'' +
                ", temperature='" + temperature + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWavelength() {
        return wavelength;
    }

    public void setWavelength(String wavelength) {
        this.wavelength = wavelength;
    }

    public String getAbsorbance() {
        return absorbance;
    }

    public void setAbsorbance(String absorbance) {
        this.absorbance = absorbance;
    }

    public String getTranatre() {
        return tranatre;
    }

    public void setTranatre(String tranatre) {
        this.tranatre = tranatre;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
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
}
