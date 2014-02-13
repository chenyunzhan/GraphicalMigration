package com.cloud.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cloud.util.DB_LOCAL;

public class CoordinateTransformation {
	
	
	public static void getPoint() {
		String sql = "select wm_concat(e.COLUMN_VALUE) points, c.record_id, c.topo_name from t_mi_system_region c, table(c.geoloc.sdo_ordinates) e where c.topo_name like '%枣庄新城中兴环网2.5G/OF2.5G%'  group by c.record_id,c.topo_name";
		Connection conn = DB_LOCAL.getConn();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String temp = rs.getString("points");
				String topo_name = rs.getString("topo_name");
				System.out.println(topo_name.toString() + " : " + temp);
				String[] arr = temp.split(",");
				double a = 0,b = 0;
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < arr.length; i++) {
					a = (Double.parseDouble(arr[i]))*446/639+410;
					i++;
					b = -((Double.parseDouble(arr[i]))*446/639-2200);
					sb.append(a + ",").append(b + ",");
				}
				System.out.println(topo_name.toString() + " : " + sb.toString().substring(0,sb.length()-1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		getPoint();
	}
}
