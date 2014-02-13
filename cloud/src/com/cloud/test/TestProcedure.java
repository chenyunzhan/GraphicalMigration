package com.cloud.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.cloud.util.DB_LOCAL;

public class TestProcedure {
	public static void executeProcedureOut () {
		Connection conn = DB_LOCAL.getConn();
		String sql = "{CALL simpleproc(?)};";
		try {
			CallableStatement cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.INTEGER);
			cs.execute();
			int temp = cs.getInt(1);
			System.out.println(temp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void executeProcedureIn () {
		Connection conn = DB_LOCAL.getConn();
		String sql = "{CALL proc_article_findById(?)};";
		try {
			CallableStatement cs = conn.prepareCall(sql);
			cs.setInt(1, 1);
			ResultSet rs = cs.executeQuery();
			while(rs.next()) {
				System.out.println(rs.getInt(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		executeProcedureOut();
		//executeProcedureIn();
	}
}
