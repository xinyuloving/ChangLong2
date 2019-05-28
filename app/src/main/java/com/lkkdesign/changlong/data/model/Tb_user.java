package com.lkkdesign.changlong.data.model;

public class Tb_user {

    private int _id;//用户id
    private String name;//用户姓名
    private String password;//用户密码
    private String jobNo;//用户工号
    private String company;//用户所属单位
    private String contact;//联系方式
    private String address;//地址
    private String time;//时间

    public Tb_user() {
    }

    public Tb_user(int _id, String name,String password, String jobNo, String company, String contact, String address,String time) {
        this._id = _id;
        this.name = name;
        this.password = password;
        this.jobNo = jobNo;
        this.company = company;
        this.contact = contact;
        this.address = address;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Tb_user{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", jobNo='" + jobNo + '\'' +
                ", company='" + company + '\'' +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
