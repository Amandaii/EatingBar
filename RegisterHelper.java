package cn.edu.swufe.eatingbar;

import java.io.Serializable;

public class RegisterHelper implements Serializable {

    private String phone;
    private String realname;
    private String address;
    private String logname;
    private String password;
    public RegisterHelper() {
        super();
        // TODO Auto-generated constructor stub
    }



    public RegisterHelper(String logname, String password, String address, String phone, String realname) {
        super();
        this.logname = logname;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.realname = realname;
    }
    public String getLogname() {
        return logname;
    }
    public void setLogname(String logname) {
        this.logname = logname;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getRealname(){
        return realname;
    }
    public void setRealname(String realname){
        this.realname = realname;
    }

    @Override
    public String toString() {
        return "User [logname=" + logname + ", password="
                + password + ", address=" + address + ", phone=" + phone +", realname=" + realname + "]";
    }

}
