package com.cloud.gis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.cloud.util.DB_GIS;
import com.cloud.util.DB_RM_RDM_SID_LU_H;

public class GisUtil {
	public void covert() {

		Connection conn1 = DB_RM_RDM_SID_LU_H.getConn();
		Connection conn2 = DB_GIS.getConn();
		
		Set set=new HashSet();
		
		
		String sql6 = "select a.a_resource_id from cloud_temp_cable_segment a";
		String sql7 = "select a.z_resource_id from cloud_temp_cable_segment a";
		String sql8 = "select a.a_resource_id from CLOUD_TEMP_SUPPORT_SEGMENT a";
		String sql9 = "select a.z_resource_id from CLOUD_TEMP_SUPPORT_SEGMENT a";
		
		try {
			PreparedStatement ps6 = conn2.prepareStatement(sql6);
			PreparedStatement ps7 = conn2.prepareStatement(sql7);
			PreparedStatement ps8 = conn2.prepareStatement(sql8);
			PreparedStatement ps9 = conn2.prepareStatement(sql9);
			ResultSet rs6 = ps6.executeQuery();
			while(rs6.next()) {
				set.add(rs6.getString("a_resource_id"));
			}
			ResultSet rs7 = ps7.executeQuery();
			while(rs7.next()) {
				set.add(rs7.getString("z_resource_id"));
			}
			ResultSet rs8 = ps8.executeQuery();
			while(rs8.next()) {
				set.add(rs8.getString("a_resource_id"));
			}
			ResultSet rs9 = ps9.executeQuery();
			while(rs9.next()) {
				set.add(rs9.getString("z_resource_id"));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/**
		 * 
		 */

		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			String str = (String) iterator.next();
			
			String sql1 = "SELECT SUPPORT_POINT_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_SUPPORT_POINT AS a WHERE a.SUPPORT_POINT_ID = "+ str +";";
			//String sql1 = "SELECT LOCATION_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_LOCATION AS a WHERE a.RES_CLASS_ID = 108;";
			String sql2 = "insert into T_RM_GIS_RESMAP (OBJECTID, GIS_RESMAP_ID, RES_ID, RES_CLASS_ID, RES_SHARDING_ID, MAINTENANCE_AREA_ID,MAINTENANCE_AREA_RES_CLASS_ID, LOCAL_NETWORK_ID, SHAPE) values ((select nvl(MAX(objectid),0) + 1 from T_RM_GIS_RESMAP),(select nvl(MAX(objectid),0) + 1 from T_RM_GIS_RESMAP), ? , ? , ? , ? , 102 , ? , SDE.ST_POINT(?,?,4326))";
			try {
				PreparedStatement ps1 = conn1.prepareStatement(sql1);
				ResultSet rs1 = ps1.executeQuery();
				while (rs1.next()) {
					String resId = rs1.getString("RES_ID");
					String resClassId = rs1.getString("RES_CLASS_ID");
					String shardingId = rs1.getString("SHARDING_ID");
					String maintenanceAreaId = rs1
							.getString("MAINTENANCE_AREA_ID");
					String localNetworkId = rs1.getString("LOCAL_NETWORK_ID");
					String longitude = rs1.getString("LONGITUDE");
					if (longitude == null) {
						longitude = "0";
					}
					String latitude = rs1.getString("LATITUDE");
					if (latitude == null) {
						latitude = "0";
					}
					PreparedStatement ps2 = conn2.prepareStatement(sql2);
					ps2.setString(1, resId);
					ps2.setString(2, resClassId);
					ps2.setString(3, shardingId);
					ps2.setString(4, maintenanceAreaId);
					ps2.setString(5, localNetworkId);
					ps2.setString(6, longitude);
					ps2.setString(7, latitude);
					int count = ps2.executeUpdate();
					ps2.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		GisUtil cs = new GisUtil();
		cs.covert();
	}
}
