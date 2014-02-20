package com.cloud.cable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cloud.util.DB_OLD_FORMAL;
import com.cloud.util.DB_RM_RDM_SID_LU_H;
import com.cloud.util.DB_RM_TPS_ADB;

/**
 * 高新区沁心园小区-S001GL
 * @author Administrator
 *
 */
public class CableTransPoint {
	public static void getPoint(Integer topInstanceId, String cableName, Connection conn, Connection conn1, Connection conn2) {
		String sql = "select c.geoloc.SDO_POINT.x X, c.geoloc.SDO_POINT.y Y , c.point_misid, c.object_type from F_mi_store a inner join f_fiber_system b on a.mis_id=b.fsystem_id inner join f_mi_system_points c on a.map_id = c.map_id where b.fsystem_no = ?";
		String update_sql = "UPDATE t_rm_topo_inst_point a SET  a.POS_X = ?, a.POS_Y = ? WHERE a.RES_ID = ? AND a.TOPO_INSTANCE_ID = "+ topInstanceId +";";
		//在新系统里查询该点的资源名称
		String find_res_id1 = "SELECT b.LOCATION_ID AS RES_ID FROM t_rm_location AS b WHERE b.ATTRIBUTE2 = ?;";
		String find_res_id2 = "SELECT b.JOINT_BOX_ID AS RES_ID FROM T_RM_JOINT_BOX AS b WHERE b.ATTRIBUTE2 = ?;";
		String find_res_id3 = "SELECT b.PHYSICAL_DEVICE_ID AS RES_ID FROM t_rm_physical_device AS b WHERE b.ATTRIBUTE2 = ?;";
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, cableName);
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
				double a = (Double.parseDouble(X))*800/639;
				double b = ((Double.parseDouble(Y))*800/639);
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
			ps1.setString(1, cableName);
			ResultSet rs1 = ps1.executeQuery();
			while (rs1.next()) {
				String X = rs1.getString("X");
				String Y = rs1.getString("Y");
				String misid = rs1.getString("point_misid");
				String type = rs1.getString("object_type");
				//System.out.println(misid + ":" + X + " , " + Y);
				double a = (Double.parseDouble(X))*800/639-minx+71;
				double b = -((Double.parseDouble(Y))*800/639-maxy) + 68;
				System.out.println(misid + ":" + a + " , " + b);
				
				String resId = null;
				PreparedStatement ps2 = conn1.prepareStatement(update_sql);
				String find_res_id = null;
				if(type.equals("机房")) {
					find_res_id = find_res_id1;
				}else if(type.equals("光分歧接头")) {
					find_res_id = find_res_id2;
				}else {
					find_res_id = find_res_id3;
				}
				PreparedStatement ps3 = conn2.prepareStatement(find_res_id);
				ps3.setString(1, misid);
				ResultSet rs3 = ps3.executeQuery();
				if(rs3.next()) {
					resId = rs3.getString("RES_ID");
				}
				ps2.setString(1, String.valueOf(a));
				ps2.setString(2, String.valueOf(b));
				ps2.setString(3, resId);
				int count = ps2.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		//getPoint(12345);
	}
}
