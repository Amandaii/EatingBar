package cn.edu.swufe.eatingbar;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DBUtils {

    private static String TAG= "DBUtils";
    private static String driver = "com.mysql.jdbc.Driver";// MySql驱动

//    private static String url = "jdbc:mysql://localhost:3306/map_designer_test_db";

    private static String user = "root";// 用户名

    private static String password = "Tyw123888";// 密码

    public static String handleString(String s)
    {   try{ byte bb[]=s.getBytes("iso-8859-1");
        s=new String(bb);
    }
    catch(Exception ee){}
        return s;
    }

    private static Connection getConn(){

        Connection connection = null;
        try{
            Class.forName(driver);// 动态加载类
            String ip = "rm-uf6y2ex107sy7lk94no.mysql.rds.aliyuncs.com";// 写成本机地址，不能写成localhost，同时手机和电脑连接的网络必须是同一个

            // 尝试建立到给定数据库URL的连接
            connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" +
                    "3306/shop", user, password);

        }catch (Exception e){
            Log.e(TAG, "getConn: " + e.toString() );
            e.printStackTrace();
        }

        return connection;
    }

    public static String login(String logname,String password) {
        String login_str = "";
        boolean boo = (logname.length() > 0)&&(password.length() > 0);
        try {
            Connection con = getConn();
            String condition = "select * from user where logname = '" + logname +
                    "' and password ='" + password + "'";
            if (con!=null){
                PreparedStatement ps = con.prepareStatement(condition);
                if (ps != null) {
                    ResultSet rs = ps.executeQuery(condition);
                    boolean m = rs.next();
                    if (m == true) {
                        rs.first();
                        String type = rs.getString("type");
                        Log.i(TAG, "login: type" + type);
                        if (type.equals("1")){
                            con.close();
                            ps.close();
                            login_str = "用户登录成功";
                            Log.i(TAG, "login:  " + login_str);
                            return  login_str;
                        }else if (type.equals("2")){
                            con.close();
                            ps.close();
                            login_str = "管理员登录成功";
                            Log.i(TAG, "login:  " + login_str);
                            return  login_str;
                        }

                    }
                    else {
                        login_str = "您输入的用户名不存在，或密码不般配";
                        return login_str;
                    }
                }
                else {
                    login_str = "请输入用户名和密码";
                    return login_str;
                }
            }
            return login_str;

        } catch (SQLException e) {
            Log.i(TAG, "login: ERR"+ e.toString());
        }
        return login_str;
    }

    public static String register(RegisterHelper registerHelper) {
        String register_str = "";
        String logname = registerHelper.getLogname();
        String password = registerHelper.getPassword();
        String address = registerHelper.getAddress();
        String realname = registerHelper.getRealname();
        String phone = registerHelper.getPhone();
        boolean isLD = true;
        for (int i = 0; i < logname.length(); i++) {
            char c = logname.charAt(i);
            if (!((c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A') || (c <= '9' && c >= '0')))
                isLD = false;
        }
        boolean boo = logname.length() > 0 && password.length() > 0 && address.length() > 0 && isLD;
        try {
            Connection con = getConn();
            String insertCondition = "INSERT INTO user VALUES (?,?,?,?,?,?)";
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(insertCondition);
                if (ps != null) {
                    //Boolean rs = ps.execute(insertCondition);
                    if (boo) {
                        ps.setString(1, handleString(logname));
                        ps.setString(2, handleString(password));
                        ps.setString(3, handleString(phone));
                        ps.setString(4, address);
                        ps.setString(5, realname);
                        ps.setString(6, "1");
                        int m = ps.executeUpdate();
                        if (m!=0){
                            register_str = "注册成功";
                            return register_str;
                        }
                    }
                    else {
                        register_str = "信息填写不完整，或名字中有非法字符";
                        return register_str;
                    }
                    con.close();
                    return "";
                }
                return "";
            }
        } catch (SQLException e) {
            register_str = "该会员名已被占用，请您更换名字";
            return register_str;
        }
        return "";
    }



    public static HashMap<String, Object> getInfoByName(String name){

        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = getConn();

        try {
            // mysql简单的查询语句。这里是根据MD_CHARGER表的NAME字段来查询某条记录
            String sql = "select * from user";
//            String sql = "select * from MD_CHARGER";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    // 设置上面的sql语句中的？的值为name
//                    ps.setString(1, name);
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        int count = rs.getMetaData().getColumnCount();
                        Log.e("DBUtils","列总数：" + count);
                        while (rs.next()){
                            // 注意：下标是从1开始的
                            for (int i = 1;i <= count;i++){
                                String field = rs.getMetaData().getColumnName(i);
                                map.put(field, rs.getString(field));
                            }
                        }
                        connection.close();
                        ps.close();
                        return  map;
                    }else {
                        return null;
                    }
                }else {
                    return  null;
                }
            }else {
                return  null;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());
            return null;
        }

    }

}