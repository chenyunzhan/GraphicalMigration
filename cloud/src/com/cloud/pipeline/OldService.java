package com.cloud.pipeline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.cloud.util.DB_OLD_CITY;
import com.cloud.util.DB_OLD_FORMAL;

public class OldService {
	public static void main(String[] args) {
		Connection conn1 = DB_OLD_FORMAL.getConn();
		Connection conn2 = null;
		Integer count = 0;
		Map<String, String> map = new HashMap<String,String>();
		map.put("", "");
		String sql1 = "select city_id, ws_id from mi_workspace a where a.pipe_long_id is not null";
		try {
			PreparedStatement ps1 = conn1.prepareStatement(sql1);
			ResultSet rs1 = ps1.executeQuery();
			while(rs1.next()) {
				Integer cityId = rs1.getInt("city_id");
				Integer wsId = rs1.getInt("ws_id");
				String sql2 = "select b.layer_name from MI_WS_LAYER b where b.ws_id = ?";
				PreparedStatement ps2 = conn1.prepareStatement(sql2);
				ps2.setInt(1, wsId);
				ResultSet rs2 = ps2.executeQuery();
				String url = null;
				String username = null;
				String password = null;
				if(cityId == 1) {
					url = "jdbc:oracle:thin:@134.33.9.83:1521:map";
					username = "map";
					password = "map";
				}else if(cityId == 2) {
					url = "jdbc:oracle:thin:@134.34.64.43:1522:qdhx";
					username = "map";
					password = "map";
				}else if(cityId == 3) {
					url = "jdbc:oracle:thin:@134.37.17.203:1522:zygl";
					username = "map";
					password = "map";
				}else if(cityId == 4) {
					url = "jdbc:oracle:thin:@134.38.17.203:1522:zygl";
					username = "map";
					password = "map";
				}else if(cityId == 5) {
					url = "jdbc:oracle:thin:@134.35.17.201:1521:zygl";
					username = "map";
					password = "map";
				}else if(cityId == 6) {
					url = "jdbc:oracle:thin:@134.49.9.75:1521:ibss";
					username = "map";
					password = "map";
				}else if(cityId == 7) {
					url = "jdbc:oracle:thin:@134.39.17.203:1522:zygl";
					username = "map";
					password = "map";
				}else if(cityId == 8) {
					url = "jdbc:oracle:thin:@134.40.17.203:1522:zygl";
					username = "map";
					password = "map";
				}else if(cityId == 9) {
					url = "jdbc:oracle:thin:@134.36.139.246:1522:zygl";
					username = "map";
					password = "map";
				}else if(cityId == 10) {
					url = "jdbc:oracle:thin:@134.41.17.203:1522:zygl";
					username = "map";
					password = "map";
				}else if(cityId == 11) {
					url = "jdbc:oracle:thin:@134.34.64.111:1521:qdhx";
					username = "map";
					password = "map";
				}else if(cityId == 12) {
					url = "jdbc:oracle:thin:@134.42.17.203:1522:zygl";
					username = "map";
					password = "map";
				}else if(cityId == 13) {
					url = "jdbc:oracle:thin:@134.48.17.1:1522:map";
					username = "map";
					password = "map";
				}else if(cityId == 14) {
					url = "jdbc:oracle:thin:@134.43.17.203:1522:zygl";
					username = "map";
					password = "map";
				}else if(cityId == 15) {
					url = "jdbc:oracle:thin:@134.44.17.204:1522:zygl";
					username = "map";
					password = "map";
				}else if(cityId == 16) {
					url = "jdbc:oracle:thin:@134.46.17.203:1522:zygl";
					username = "map";
					password = "map";
				}else if(cityId == 17) {
					url = "jdbc:oracle:thin:@134.32.21.3:1521:zygl";
					username = "map";
					password = "map";
					continue;
				}
				System.out.println("cityid:" + cityId + "   wsId" + wsId);
				conn2 = DB_OLD_CITY.getConn(url, username, password);
				while(rs2.next()) {
					String layerName = rs2.getString("layer_name");
					String sql3 = null;
					if(layerName.startsWith("MI_")) {
						sql3 = "select count(*) from "+ layerName +" b where b.map_id = ?";
						continue;
					}else {
						sql3 = "select count(*) from "+ layerName +" b where b.ws_id = ?";
					}
					PreparedStatement ps3 = conn2.prepareCall(sql3);
					//ps3.setString(1, layerName);
					//System.out.println("layerName:" + layerName + "   cityid:" + cityId + "   wsId" + wsId);
					ps3.setInt(1, wsId);
					ResultSet rs3 = ps3.executeQuery();
					if(rs3.next()) {
						if(rs3.getInt(1) > 0) {
							count ++;
							break;
						}
					}
					rs3.close();
					ps3.close();
				}
				
				rs2.close();
				ps2.close();
			}
			rs1.close();
			ps1.close();
			System.out.println(count);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(conn2 != null) {
					conn2.close();
				}
				if(conn1 != null) {
					conn1.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
