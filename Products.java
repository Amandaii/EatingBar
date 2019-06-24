package cn.edu.swufe.eatingbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Products {
    private static String TAG = "Products";
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

    public static String addProduct(ProductItem productItem) {
        String ret = "";
        int number = productItem.getNumber();
        String name = productItem.getName();
        String price = productItem.getPrice();
        String intro = productItem.getIntro();
        float p = Float.parseFloat(price);
        byte[] photo = productItem.getPhoto();
        String type = productItem.getType();
        Log.i(TAG, "addProduct: " + name + price + intro + type + photo);
        boolean boo = name.length() > 0 && price.length() > 0 && intro.length() > 0 && photo != null && type.length() > 0;
        try {
            Connection con = getConn();
            if (con != null) {

                if (boo) {
                    String sql = "insert into product(name,price,intro,photo,type) values (?,?,?,?,?)";
                    PreparedStatement ps = con.prepareStatement(sql);

                    ps.setString(1, name);
                    ps.setFloat(2, p);
                    ps.setString(3, intro);
                    ps.setBinaryStream(4, new ByteArrayInputStream(photo), (int) photo.length);
                    ps.setString(5, type);
                    int m = ps.executeUpdate();
                    ps.close();

                    Log.i(TAG, "addProduct: photo.length=" + photo.length + " ret=" + m);

                    if (m > 0) {
                        ret = "添加商品成功";
                    }
                } else {
                    ret = "信息填写不完整，请完善信息";
                }
                con.close();
            }
            con.close();
        } catch (SQLException e) {
            Log.i(TAG, "addProduct: ERR" + e.toString());
            ret = "添加失败";
        }
        return ret;
    }

    public static String deleteProduct(int id){
        boolean boo = id > 0;
        try{
            Connection con = getConn();
            if (con != null){
                if (boo){
                    String sql = "delete from product where number = '"+id+"'";
                    PreparedStatement ps = con.prepareStatement(sql);
                    int m = ps.executeUpdate();
                    ps.close();

                    if (m > 0) {
                        return "删除成功";
                    }
                }

            }
        }catch (SQLException e){
            Log.i(TAG, "deleteProduct:  ERR " + e.toString());
        }
        return "删除失败";
    }

    public static String updateProduct(ProductItem productItem) {
        String ret = "";
        int number = productItem.getNumber();
        String name = productItem.getName();
        String price = productItem.getPrice();
        String intro = productItem.getIntro();
        float p = Float.parseFloat(price);
        byte[] photo = productItem.getPhoto();
        String type = productItem.getType();
        Log.i(TAG, "addProduct: " + name + price + intro + type + photo);
        boolean boo = name.length() > 0 && price.length() > 0 && intro.length() > 0 && photo != null && type.length() > 0;
        try {
            Connection con = getConn();
            if (con != null) {

                if (boo) {
                    String sql = "update product set name = ?, price = ?, intro = ?, photo = ?, type = ? where number = ?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, name);
                    ps.setFloat(2, p);
                    ps.setString(3, intro);
                    ps.setBinaryStream(4, new ByteArrayInputStream(photo), (int) photo.length);
                    ps.setString(5, type);
                    ps.setInt(6,number);
                    int m = ps.executeUpdate();
                    ps.close();

                    Log.i(TAG, "addProduct: photo.length=" + photo.length +""+ name + " ret=" + m);

                    if (m > 0) {
                        ret = "修改商品成功";
                    }
                } else {
                    ret = "信息填写不完整，请完善信息";
                }
                con.close();
            }
            con.close();
        } catch (SQLException e) {
            Log.i(TAG, "addProduct: ERR" + e.toString());
            ret = "修改失败";
        }
        return ret;
    }

    public ArrayList<Bitmap> getDrawable() {
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        //查询数据库
        try {
            Connection con = getConn();
            String condition = "select photo from product";
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(condition);
                if (ps != null) {
                    ResultSet rs = ps.executeQuery(condition);
                    boolean m = rs.next();
                    if (m == true) {
                        rs.first();
                        byte[] b = rs.getBytes("photo");
                        Log.i(TAG, "login: type" + b);
                        //将获取的数据转换成drawable
                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, null);
                        con.close();
                        ps.close();
                    }
                }
            }
        } catch (SQLException e) {
            Log.i(TAG, "getDrawable: ERR: " + e.toString());
        }
        return bitmaps;
    }

    public ArrayList getAll() {
        ArrayList<ProductItem> items = new ArrayList<ProductItem>();
        ResultSet resultSet = null;
        try {
            Connection con = getConn();
            String condition = "select * from product";
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(condition);
                if (ps != null) {
                    resultSet = ps.executeQuery(condition);
                    while (resultSet.next()) {
                        ProductItem item = new ProductItem();

                        //rs->item
                        item.setNumber(resultSet.getInt("number"));
                        item.setPhoto(resultSet.getBytes("photo"));
                        item.setIntro(resultSet.getString("intro"));
                        item.setPrice(resultSet.getString("price"));
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

    public static ProductItem getInfoByName(int id) {

        ArrayList<ProductItem> items = new ArrayList<ProductItem>();
        ProductItem item = null;
        // 根据数据库名称，建立连接

        try {
            Connection con = getConn();
            String sql = "select * from product where number ='" + id +"'";
            // mysql简单的查询语句。这里是根据MD_CHARGER表的NAME字段来查询某条记录
//            String sql = "select * from MD_CHARGER";
            if (con != null) {// connection不为null表示与数据库建立了连接
                PreparedStatement ps = con.prepareStatement(sql);
                if (ps != null) {
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            item = new ProductItem();

                            //rs->item
                            item.setNumber(rs.getInt("number"));
                            item.setPhoto(rs.getBytes("photo"));
                            item.setIntro(rs.getString("intro"));
                            item.setPrice(rs.getString("price"));
                            item.setName(rs.getString("name"));
                            Log.i(TAG, "getInfoByName:  name : " + rs.getString("name"));

                            items.add(item);
                        }
                    }
                    con.close();
                    ps.close();
                    return item;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, "异常：" + e.getMessage());
        }
        return item;
    }
}