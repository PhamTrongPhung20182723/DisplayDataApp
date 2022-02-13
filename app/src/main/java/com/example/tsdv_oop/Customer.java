package com.example.tsdv_oop;

public class Customer {
    String name;
    String ID;
    String CurClass;
    String Email;
    String ExpClass;
    String Subject;
    String Telephone;

    public String getCurClass() {
        return CurClass;
    }

    public void setCurClass(String curClass) {
        CurClass = curClass;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getExpClass() {
        return ExpClass;
    }

    public void setExpClass(String expClass) {
        ExpClass = expClass;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getSchoolClass() {
        return SchoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        SchoolClass = schoolClass;
    }

    String SchoolClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return ID;
    }

    public void setId(String id) {
        this.ID = id;
    }
}
