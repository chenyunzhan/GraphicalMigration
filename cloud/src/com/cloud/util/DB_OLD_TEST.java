package com.cloud.util;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB_OLD_TEST {
	public static Connection getConn () {
		try {  
            //Class.forName("com.mysql.jdbc.Driver");  
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //System.out.println(DBUtil.class.getResource("/"));
            URL path = DB_OLD_TEST.class.getResource("/");
            Connection conn = null;
            if(path.toString().contains("vcap")) {
            	conn = DriverManager.getConnection("jdbc:mysql://10.4.3.233:3306/d079c75dcb6774864a29fb39f682e0bb0", "ul2C8QGJ0msUJ", "pfOE1ki60EExS");
            }else {
            	//conn = DriverManager.getConnection("jdbc:mysql://10.249.5.35:8066/RM_RDM_SID?&useUnicode=true&characterEncoding=utf-8", "yySID", "adminYY");  
            	//conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/myweb?&useUnicode=true&characterEncoding=utf-8", "root", "");  
            	conn = DriverManager.getConnection("jdbc:oracle:thin:@10.72.67.137:1521:zygl", "zygl", "zygl");  
            }
            if(null != conn) {  
                return conn;  
            } 
        } catch (SQLException e) {  
            e.printStackTrace();  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        }  
        return null;
	}
	
	public static void close(Connection conn) {  
        if(conn != null) {  
            try {  
                conn.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
      
    public static void close(PreparedStatement ps) {  
        if(ps != null) {  
            try {  
                ps.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
      
    public static void close(ResultSet rs) {  
        if(rs != null) {  
            try {  
                rs.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
	
}
