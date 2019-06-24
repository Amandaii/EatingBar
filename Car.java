package cn.edu.swufe.eatingbar;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Car {
    private static String driver = "com.mysql.jdbc.Driver";// MySql驱动

//    private static String url = "jdbc:mysql://localhost:3306/map_designer_test_db";

    private static String user = "root";// 用户名

    private static String password = "Tyw123888";// 密码

    private static Connection getConn() {

        Connection connection = null;
        try {
            Class.forName(driver);// 动态加载类
            String ip = "rm-uf6y2ex107sy7lk94no.mysql.rds.aliyuncs.com";// 写成本机地址，不能写成localhost，同时手机和电脑连接的网络必须是同一个

            // 尝试建立到给定数据库URL的连接
            connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" +
                    "3306/shop", user, password);

        } catch (Exception e) {
            Log.e(TAG, "getConn: " + e.toString());
            e.printStackTrace();
        }

        return connection;
    }
    private static String TAG = "order";
    public static String addProduct(ProductItem productItem) {
        String ret = "";
        int m = 0;
        String name = productItem.getName();
        Log.i(TAG, "addProduct: name : "+ name);
        String price = productItem.getPrice();
        float p = Float.parseFloat(price);
        String intro = productItem.getIntro();
        byte[] photo = productItem.getPhoto();
        Log.i(TAG, "addProduct: " + name + price + intro + photo);
        boolean boo = name.length() > 0 && price.length() > 0 && intro.length() > 0 && photo != null ;
        try {
            Connection con = getConn();
            if (con != null) {

                if (boo) {
                    String sql = "insert into car(name,price,intro,photo,count, logname) values (?,?,?,?,?,?)";
                    PreparedStatement ps = con.prepareStatement(sql);

                    LoginState logn = new LoginState();
                    if (logn.getUsername() != null){
                    ps.setString(1, name);
                    ps.setFloat(2, p);
                    ps.setString(3, intro);
                    ps.setBinaryStream(4, new ByteArrayInputStream(photo), (int) photo.length);
                    ps.setInt(5, 1);
                    ps.setString(6,logn.getUsername());
                    m = ps.executeUpdate();
                    }else {
                        ret = "请先登录";
                    }

                    ps.close();

                    Log.i(TAG, "addProduct: photo.length=" + photo.length + " ret=" + m);

                    if (m > 0) {
                        ret = "加入购物车成功";
                    }
                } else {
                    ret = "";
                }
                con.close();
            }
            con.close();
        } catch (SQLException e) {
            Log.i(TAG, "addProduct: ERR" + e.toString());
            ret = "添加失败,请登录后添加";
        }
        return ret;
    }

    public static String deleteCar(int id){
        boolean boo = id > 0;
        try{
            Connection con = getConn();
            if (con != null){
                if (boo){
                    String sql = "delete from car where number = '"+id+"'";
                    PreparedStatement ps = con.prepareStatement(sql);
                    int m = ps.executeUpdate();
                    ps.close();

                    if (m > 0) {
                        return "移出购物车成功";
                    }
                }

            }
        }catch (SQLException e){
            Log.i(TAG, "deleteProduct:  ERR " + e.toString());
        }
        return "移出购物车失败";
    }

    public ArrayList getAll() {
        ArrayList<CarItem> items = new ArrayList<CarItem>();
        ResultSet resultSet = null;
        LoginState lo = new LoginState();
        String llo = lo.getUsername();
        try {
            Connection con = getConn();
            String condition = "select * from car where logname ='" + llo + "'";
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(condition);
                if (ps != null) {
                    resultSet = ps.executeQuery(condition);
                    while (resultSet.next()) {
                        CarItem item = new CarItem();

                        //rs->item
                        item.setNumber(resultSet.getInt("number"));
                        item.setPhoto(resultSet.getBytes("photo"));
                        item.setIntro(resultSet.getString("intro"));
                        item.setPrice(Float.parseFloat(resultSet.getString("price")));
                        item.setName(resultSet.getString("name"));

                        items.add(item);

                    }
                    con.close();
                    ps.close();
                    return items;
                }
            }
        } catch (SQLException e) {
            Log.i(TAG, "getAll: ERR: " + e.toString());
        }
        return items;
    }

}
