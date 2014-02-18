package com.cloud.rack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cloud.util.DB_RM_RDM_SID_LU_H;


public class NewService {
	public List<String> getRacks() {
		//String sql = "select rack_name FROM t_rm_rack a where a.DELETED_FLAG = 0 limit 0, 1000";
		String sql = "select rack_name FROM t_rm_rack a where a.DELETED_FLAG = 0 and a.rack_name = '经十路三楼联通综合机房/SB11_06'";
		java.sql.Connection conn = DB_RM_RDM_SID_LU_H.getConn();
		List<String> list = new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String rackName = rs.getString("rack_name");
				list.add(rackName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
