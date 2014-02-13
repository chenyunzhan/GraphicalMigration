package com.cloud.pipeline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cloud.util.DB_RM_RDM_SID_LU_H;

public class NewService {
	public List<PipeLine> getPipeLines() {
		String sql = "SELECT SUPPORT_FACILITY_ID, RES_CLASS_ID, SHARDING_ID, SUPPORT_FACILITY_NAME FROM t_rm_support_facility;";
		String sql1 = "SELECT d.TOPO_INSTANCE_ID FROM t_rm_topo_instance AS d WHERE d.RES_CLASS_ID = ? AND d.RESOURCE_ID = ? AND d.SHARDING_ID = ?;";
		Connection conn = DB_RM_RDM_SID_LU_H.getConn();
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		List<PipeLine> list = new ArrayList<PipeLine>();
		try {
			PipeLine p = new PipeLine();
			ps = conn.prepareStatement(sql);
			String SUPPORT_FACILITY_ID = null;
			String RES_CLASS_ID = null;
			String SHARDING_ID = null;
			String SUPPORT_FACILITY_NAME = null;
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				SUPPORT_FACILITY_ID = rs.getString("SUPPORT_FACILITY_ID");
				RES_CLASS_ID = rs.getString("RES_CLASS_ID");
				SHARDING_ID = rs.getString("SHARDING_ID");
				SUPPORT_FACILITY_NAME = rs.getString("SUPPORT_FACILITY_NAME");
			}
			ps1 = conn.prepareStatement(sql1);
			ps1.setString(1, RES_CLASS_ID);
			ps1.setString(2, SUPPORT_FACILITY_ID);
			ps1.setString(3, SHARDING_ID);
			String topoId = null;
			ResultSet rs1 = ps1.executeQuery();
			if(rs1.next()) {
				topoId = rs1.getString("TOPO_INSTANCE_ID");
			}
			p.setName(SUPPORT_FACILITY_NAME);
			p.setTopoId(topoId);
			list.add(p);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
			
	}
}

class PipeLine {
	String name = null;
	String topoId = null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTopoId() {
		return topoId;
	}
	public void setTopoId(String topoId) {
		this.topoId = topoId;
	}
	
}
