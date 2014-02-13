package com.cloud.pipeline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cloud.util.DB_OLD_CITY;
import com.cloud.util.DB_RM_RDM_SID_LU_H;
import com.cloud.util.DB_RM_TPS_ADB;

/**
 * 高新区沁心园小区-S001GL
 * @author Administrator
 *
 */
public class TransformService {
	public static void getPoint(Integer topInstanceId, String ws_name) {
		String sql = "select b.geoloc.SDO_POINT.x X, b.geoloc.SDO_POINT.y Y , b.mis_id point_misid from mi_workspace a inner join map_well b on a.ws_id = b.ws_id where a.ws_name = '" + ws_name + "'";
		String update_sql = "UPDATE t_rm_topo_inst_point a SET  a.POS_X = ?, a.POS_Y = ? WHERE a.RES_ID = ? AND a.TOPO_INSTANCE_ID = "+ topInstanceId +";";
		String find_support_point = "SELECT b.SUPPORT_POINT_ID AS resId FROM T_RM_SUPPORT_POINT AS b where b.attribute2=?;";
		//淄博链接
		Connection conn = DB_OLD_CITY.getConn("jdbc:oracle:thin:@134.35.17.201:1521:zygl", "map", "map");
		Connection conn1 = DB_RM_TPS_ADB.getConn();
		Connection conn2 = DB_RM_RDM_SID_LU_H.getConn();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			double minx = 1000000.0;
			double maxy = 0.0;
			double miny = 1000000.0;
			double maxx = 0.0;
			//求出最小的x和最小的y
			while (rs.next()) {
				String X = rs.getString("X");
				String Y = rs.getString("Y");
				//System.out.println(X + " , " + Y);
				double a = (Double.parseDouble(X))*4000/639;
				double b = ((Double.parseDouble(Y))*4000/639);
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
			ResultSet rs1 = ps1.executeQuery();
			while (rs1.next()) {
				String X = rs1.getString("X");
				String Y = rs1.getString("Y");
				String misid = rs1.getString("point_misid");
				//System.out.println(misid + ":" + X + " , " + Y);
				double a = (Double.parseDouble(X))*4000/639-minx+71;
				double b = -((Double.parseDouble(Y))*4000/639-maxy) + 68;
				System.out.println(misid + ":" + a + " , " + b);
				
				PreparedStatement ps2 = conn1.prepareStatement(update_sql);
				PreparedStatement ps3 = conn2.prepareStatement(find_support_point);
				ps3.setString(1, misid);
				ResultSet rs3 = ps3.executeQuery();
				String resId = null;
				if(rs3.next()){
					resId = rs3.getString("resId");
				}
			
				ps2.setString(1, String.valueOf(a));
				ps2.setString(2, String.valueOf(b));
				ps2.setString(3, resId);
				int count = ps2.executeUpdate();
				/**
				 * 
				ps4.setString(1, resid);
				ResultSet rs4 = ps4.executeQuery();
				while (rs4.next()) {
					String PARENT_TOPO_CODE = rs4.getString("TOPO_CODE");
					ps3.setString(1, String.valueOf(a));
					ps3.setString(2, String.valueOf(b));
					ps3.setString(3, PARENT_TOPO_CODE);
					int num = ps3.executeUpdate();
				}
				 */
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		getPoint(2106, "鑫园小区管路");
	}
}
