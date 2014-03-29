package com.cloud.transmission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cloud.util.DB_RM_RDM_SID;
import com.cloud.util.DB_RM_TPS_ADB;

public class BatchTransmission {
	public static List<String> batchUpdateTransmission() {
		List<String> list = new ArrayList<String>();
		//String sql = "SELECT * FROM T_RM_SERVICE_PATH WHERE service_path_name IN ('���ݻ�ΪOSN7500������','��ׯ�����ϱ���SDH10G-2','���������ϻ�һ��ΪSDH2.5G','�ŵ갢��ASON������','����ASON�м���','���ݱ�������-���ڻ�Ϊ2.5G/OF2.5G','�ŵ���SDH2.5G-1');";
		String sql = "SELECT * FROM T_RM_SERVICE_PATH WHERE service_path_name IN ('ʡ�����Ļ�SDH2.5G-1','ʡ���󱱻�SDH10G/L-1','�ŵ���Ѷ��վ��SDH 622M-20(U298)','��Դ��Ѷ��վ��SDH622M-07(U328)','���ݱ�������-���ڻ�Ϊ2.5G/OF2.5G','����м̻�MSTP10G-2','�ŵ���SDH2.5G-1','206�ִ������GF155/622-03B(KZ)/OF155M-1','ʡ�����Ļ�SDH10G-1','���ݻ�ΪOSN7500������','���������ϻ�һ��ΪSDH2.5G');";
		String sql2 = "SELECT * FROM t_rm_topo_instance AS d WHERE d.RES_CLASS_ID = ? AND d.RESOURCE_ID = ? AND d.SHARDING_ID = ?;";
		Connection conn1 = DB_RM_TPS_ADB.getConn();
		Connection conn = DB_RM_RDM_SID.getConn();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			PreparedStatement ps1 = conn1.prepareStatement(sql2);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String resId = rs.getString("SERVICE_PATH_ID");
				String resClassId = rs.getString("RES_CLASS_ID");
				String shardingId = rs.getString("SHARDING_ID");
				String cableName = rs.getString("SERVICE_PATH_NAME");
				ps1.setString(1, resClassId);
				ps1.setString(2, resId);
				ps1.setString(3, shardingId);
				ResultSet rs1 = ps1.executeQuery();
				while(rs1.next()) {
					Integer topInstanceId = rs1.getInt("TOPO_INSTANCE_ID");
					list.add(topInstanceId + "#" + cableName);
					System.out.println(topInstanceId);
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
