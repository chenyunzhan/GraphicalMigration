package com.cloud.rack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cloud.util.DB_LOCAL;
import com.cloud.util.DB_RM_RDM_SID;

public class OldNewRelation {
	public String[] getNewSlotId(String physicalDeviceName) {
		Connection conn = DB_RM_RDM_SID.getConn();
		String sql = "SELECT c.SHELF_ID, d.SLOT_ID FROM t_rm_phy_device_hardware_rel a INNER JOIN  t_rm_physical_device b ON a.PHYSICAL_DEVICE_ID = b.PHYSICAL_DEVICE_ID " +
																											" INNER JOIN t_rm_shelf c ON a.HARDWARE_ID = c.SHELF_ID " +
																											" INNER JOIN t_rm_slot d ON c.SHELF_ID = d.SHELF_ID WHERE d.DELETED_FLAG = 0 AND b.PHYSICAL_DEVICE_NAME = ? ;";
		String arr[] = new String[1000];
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, physicalDeviceName);
			ResultSet rs = ps.executeQuery();
			int index = 0;
			while(rs.next()) {
				String slotId = rs.getString("SLOT_ID");
				String shelfId = rs.getString("SHELF_ID");
				arr[index] = slotId;
				arr[999] = shelfId;
				index++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arr;
	}
	
	
	public String[] getMapIds() {
		Connection conn = DB_LOCAL.getConn();
		String sql = "SELECT a.map_id, a.shelf_name FROM mapinfo AS a GROUP BY map_id;";
		String arr[] = new String[1000];
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int index = 0;
			while(rs.next()) {
				String mapId = rs.getString("map_id");
				String shelfName = rs.getString("shelf_name");
				arr[index] = mapId + "#" + shelfName;
				index++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arr;
	}
	
	public String[] getMisIdSlotIdRelation(String mapId) {
		Connection conn1 = DB_RM_RDM_SID.getConn();
		Connection conn = DB_LOCAL.getConn();
		String sql = "select mis_id from mapinfo where map_id = ?";
		String sql1 = "SELECT SLOT_ID FROM t_rm_slot AS a WHERE a.mis_id = ?;";
		String arr[] = new String[1000];
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mapId);
			ResultSet rs = ps.executeQuery();
			int index = 0;
			while(rs.next()) {
				String misId = rs.getString("mis_id");
				PreparedStatement ps1 = conn1.prepareStatement(sql1);
				ps1.setString(1, misId);
				ResultSet rs1 = ps1.executeQuery();
				if(rs1.next()) {
					arr[index] = rs1.getString("SLOT_ID");
				}
				index++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arr;
	}
}
