package com.cloud.cable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cloud.util.DB_RM_RDM_SID;
import com.cloud.util.DB_RM_RDM_SID_LU_H;
import com.cloud.util.DB_RM_TPS_ADB;

public class BatchCable {
	public static List<String> batchUpdateTransmission() {
		List<String> list = new ArrayList<String>();
		String sql = "SELECT * FROM t_rm_cable AS a WHERE a.DELETED_FLAG=0 AND a.RES_CLASS_ID=252 AND a.SHARDING_ID = 123702 LIMIT 0, 101;";
		String sql2 = "SELECT * FROM t_rm_topo_instance AS d WHERE d.RES_CLASS_ID = ? AND d.RESOURCE_ID = ? AND d.SHARDING_ID = ?;";
		Connection conn1 = DB_RM_TPS_ADB.getConn();
		Connection conn = DB_RM_RDM_SID_LU_H.getConn();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			PreparedStatement ps1 = conn1.prepareStatement(sql2);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String resId = rs.getString("CABLE_ID");
				String resClassId = rs.getString("RES_CLASS_ID");
				String shardingId = rs.getString("SHARDING_ID");
				String cableName = rs.getString("CABLE_NAME");
				ps1.setString(1, resClassId);
				ps1.setString(2, resId);
				ps1.setString(3, shardingId);
				ResultSet rs1 = ps1.executeQuery();
				while(rs1.next()) {
					Integer topInstanceId = rs1.getInt("TOPO_INSTANCE_ID");
					list.add(topInstanceId + "#" + cableName);
					System.out.println(topInstanceId + "#" + cableName);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	public static void main(String[] args) {
		batchUpdateTransmission();
	}
}
