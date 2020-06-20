package com.foam.MyUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyUtil {

    static {
        //1.加载驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static final String url = "jdbc:mysql://localhost:3306/xdz1903";
    private static final String user = "root";
    private static final String password = "883215";
    public static Connection getConn(){
        //2.创建连接
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static void cloceAll(ResultSet rs, PreparedStatement pstmt, Connection conn){
        //释放资源
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(pstmt!=null){
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static Boolean ChangeDate(String sql, List<Object> list){
        int line=0;
        Connection conn = getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            if(list!=null){
                for (int i = 0; i < list.size() ; i++) {
                    pstmt.setObject(i+1, list.get(i));
                }
            }
            line = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            cloceAll(rs, pstmt, conn);
        }
        return line>0? true:false;
    }
    public static <T> List<T> upDate(String sql, List<Object> list, Class<T> cls){
        Connection conn = getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<T> date = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sql);
            if(list != null){
                for (int i = 0; i <list.size() ; i++) {
                    pstmt.setObject(i+1,list.get(i) );
                }
            }
            rs = pstmt.executeQuery();
            ResultSetMetaData rsd = rs.getMetaData();
            while (rs.next()){
                T m = cls.newInstance();
                for (int i = 0; i <rsd.getColumnCount() ; i++) {
                    String Column_Name = rsd.getColumnName(i+1);
                    Object value = rs.getObject(Column_Name);
                    Field field = cls.getDeclaredField(Column_Name);
                    field.setAccessible(true);
                    field.set(m,value);
                }
                date.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }finally {
            cloceAll(rs, pstmt, conn);
        }
        return date;
    }
}
