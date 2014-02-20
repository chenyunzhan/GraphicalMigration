package com.cloud.transmission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cloud.util.DB_OLD_FORMAL;
import com.cloud.util.DB_RM_TPS_ADB;

/**
 * 滕州本地网西北环四2.5G/OF2.5G
 * @author Administrator
 *
 */
public class TransPoint {
	public static void getPoint(Integer topInstanceId, String topoName, Connection conn, Connection conn1, Connection conn2) {
		String sql = "select a.geoloc.SDO_POINT.x X, a.geoloc.SDO_POINT.y Y , a.point_misid, a.topo_name from t_mi_system_points a where a.topo_name = ?";
		//String sql = "select a.geoloc.SDO_POINT.x X, a.geoloc.SDO_POINT.y Y , a.point_misid, a.topo_name from t_mi_system_points a where a.topo_name = '" + topoName + "'";
		String update_sql = "UPDATE t_rm_topo_inst_point a SET  a.POS_X = ?, a.POS_Y = ? WHERE a.RES_ID = ? AND a.TOPO_INSTANCE_ID = " + topInstanceId + ";";
		String query_parent_sql = "SELECT * FROM  t_rm_topo_inst_point AS a WHERE a.TOPO_INSTANCE_ID = "+ topInstanceId +" AND a.RES_ID = ?;";
		String update_child_sql = "UPDATE t_rm_topo_inst_point a SET  a.POS_X = ?, a.POS_Y = ? WHERE a.PARENT_TOPO_CODE = ? AND a.TOPO_INSTANCE_ID = "+ topInstanceId +";";
		String find_physical_device = "SELECT b.PHYSICAL_DEVICE_ID AS resId FROM t_rm_physical_device AS b WHERE b.ATTRIBUTE2 = ?";
		//Connection conn = DB_OLD_FORMAL.getConn();
		//Connection conn1 = DB_RM_TPS_ADB.getConn();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, topoName);
			ResultSet rs = ps.executeQuery();
			double minx = 100000.0;
			double maxy = 0.0;
			double miny = 100000.0;
			double maxx = 0.0;
			//求出最小的x和最小的y
			while (rs.next()) {
				String X = rs.getString("X");
				String Y = rs.getString("Y");
				//System.out.println(X + " , " + Y);
				double a = (Double.parseDouble(X))*1000/639;
				double b = ((Double.parseDouble(Y))*1000/639);
				System.out.println(a + " , " + b);
				if(a < minx) {
					minx = a;
				}
				if(b > maxy) {
					maxy = b;
				}
				if(a > maxx) {
					maxx = a;
				}
				if(b < miny) {
					miny = b;
				}
			}
			System.out.println("#########################");
			System.out.println("minx:" + minx);
			System.out.println("maxy:" + maxy);
			System.out.println("maxx:" + maxx);
			System.out.println("miny:" + miny);
			System.out.println("#########################");
			
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ps1.setString(1, topoName);
			ResultSet rs1 = ps1.executeQuery();
			while (rs1.next()) {
				String X = rs1.getString("X");
				String Y = rs1.getString("Y");
				String misid = rs1.getString("point_misid");
				//System.out.println(misid + ":" + X + " , " + Y);
				double a = (Double.parseDouble(X))*1000/639-minx+71;
				double b = -((Double.parseDouble(Y))*1000/639-maxy) + 68;
				System.out.println(misid + ":" + a + " , " + b);
				
				PreparedStatement ps2 = conn1.prepareStatement(update_sql);
				PreparedStatement ps3 = conn1.prepareStatement(update_child_sql);
				PreparedStatement ps4 = conn1.prepareStatement(query_parent_sql);
				PreparedStatement ps5 = conn2.prepareStatement(find_physical_device);
				ps5.setString(1, misid);
				String resId = null;
				ResultSet rs5 = ps5.executeQuery();
				if(rs5.next()) {
					resId = rs5.getString("resId");
				}
				/*
				 * 
				if(misid.equals("26467")) {
					resId = "1386317584446";
				}else if(misid.equals("27053")) {
					resId = "1386317444519";
				}else if(misid.equals("27051")) {
					resId = "1386317551216";
				}else if(misid.equals("27057")) {
					resId = "1386317881718";
				}else if(misid.equals("27015")) {
					resId = "1386317633947";
				}else if(misid.equals("27055")) {
					resId = "1386317753686";
				}
				 */
				ps2.setString(1, String.valueOf(a));
				ps2.setString(2, String.valueOf(b));
				ps2.setString(3, resId);
				int count = ps2.executeUpdate();
				
				ps4.setString(1, resId);
				ResultSet rs4 = ps4.executeQuery();
				while (rs4.next()) {
					String PARENT_TOPO_CODE = rs4.getString("TOPO_CODE");
					ps3.setString(1, String.valueOf(a));
					ps3.setString(2, String.valueOf(b));
					ps3.setString(3, PARENT_TOPO_CODE);
					int num = ps3.executeUpdate();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
