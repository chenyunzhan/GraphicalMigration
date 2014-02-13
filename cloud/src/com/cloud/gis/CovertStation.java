package com.cloud.gis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cloud.util.DB_GIS;
import com.cloud.util.DB_RM_RDM_SID_LU_H;

public class CovertStation {
	public void covert() {

		Connection conn1 = DB_RM_RDM_SID_LU_H.getConn();
		Connection conn2 = DB_GIS.getConn();
		/**
		 */
		String[] sqls = new String[1];
		/*
		 * 
		sqls[0] = "SELECT LOCATION_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_LOCATION AS a WHERE a.RES_CLASS_ID = 108;";
		sqls[1] = "SELECT LOCATION_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_LOCATION AS a WHERE a.RES_CLASS_ID = 112;";
		sqls[2] = "SELECT BUILDING_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_BUILDING AS a WHERE a.RES_CLASS_ID = 114;";
		sqls[3] = "SELECT SUPPORT_POINT_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_SUPPORT_POINT AS a WHERE a.RES_CLASS_ID = 211;";
		sqls[4] = "SELECT SUPPORT_POINT_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_SUPPORT_POINT AS a WHERE a.RES_CLASS_ID = 212;";
		sqls[5] = "SELECT SUPPORT_POINT_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_SUPPORT_POINT AS a WHERE a.RES_CLASS_ID = 213;";
		sqls[6] = "SELECT SUPPORT_POINT_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_SUPPORT_POINT AS a WHERE a.RES_CLASS_ID = 214;";
		sqls[7] = "SELECT SUPPORT_POINT_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_SUPPORT_POINT AS a WHERE a.RES_CLASS_ID = 215;";
		sqls[8] = "SELECT JOINT_BOX_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_JOINT_BOX AS a WHERE a.RES_CLASS_ID = 271;";
		sqls[9] = "SELECT JOINT_BOX_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_JOINT_BOX AS a WHERE a.RES_CLASS_ID = 272;";
		sqls[10] = "SELECT a.PHYSICAL_DEVICE_ID AS RES_ID, a.RES_CLASS_ID, a.SHARDING_ID, MAINTENANCE_AREA_ID, a.LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_PHYSICAL_DEVICE AS a INNER JOIN T_RM_PHYSICAL_CONNECT_DEVICE AS b ON a.PHYSICAL_DEVICE_ID = b.PHYSICAL_DEVICE_ID WHERE a.RES_CLASS_ID = 291;";
		sqls[11] = "SELECT a.PHYSICAL_DEVICE_ID AS RES_ID, a.RES_CLASS_ID, a.SHARDING_ID, MAINTENANCE_AREA_ID, a.LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_PHYSICAL_DEVICE AS a INNER JOIN T_RM_PHYSICAL_CONNECT_DEVICE AS b ON a.PHYSICAL_DEVICE_ID = b.PHYSICAL_DEVICE_ID WHERE a.RES_CLASS_ID = 292;";
		sqls[12] = "SELECT a.PHYSICAL_DEVICE_ID AS RES_ID, a.RES_CLASS_ID, a.SHARDING_ID, MAINTENANCE_AREA_ID, a.LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_PHYSICAL_DEVICE AS a INNER JOIN T_RM_PHYSICAL_CONNECT_DEVICE AS b ON a.PHYSICAL_DEVICE_ID = b.PHYSICAL_DEVICE_ID WHERE a.RES_CLASS_ID = 293;";
		sqls[13] = "SELECT a.PHYSICAL_DEVICE_ID AS RES_ID, a.RES_CLASS_ID, a.SHARDING_ID, MAINTENANCE_AREA_ID, a.LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_PHYSICAL_DEVICE AS a INNER JOIN T_RM_PHYSICAL_CONNECT_DEVICE AS b ON a.PHYSICAL_DEVICE_ID = b.PHYSICAL_DEVICE_ID WHERE a.RES_CLASS_ID = 294;";
		sqls[14] = "SELECT a.PHYSICAL_DEVICE_ID AS RES_ID, a.RES_CLASS_ID, a.SHARDING_ID, MAINTENANCE_AREA_ID, a.LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_PHYSICAL_DEVICE AS a INNER JOIN T_RM_PHYSICAL_CONNECT_DEVICE AS b ON a.PHYSICAL_DEVICE_ID = b.PHYSICAL_DEVICE_ID WHERE a.RES_CLASS_ID = 295;";
		sqls[15] = "SELECT a.PHYSICAL_DEVICE_ID AS RES_ID, a.RES_CLASS_ID, a.SHARDING_ID, MAINTENANCE_AREA_ID, a.LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_PHYSICAL_DEVICE AS a INNER JOIN T_RM_PHYSICAL_CONNECT_DEVICE AS b ON a.PHYSICAL_DEVICE_ID = b.PHYSICAL_DEVICE_ID WHERE a.RES_CLASS_ID = 296;";
		 */

		for (int i = 0; i < sqls.length; i++) {

			String sql1 = "select * from cloud_temp_gis_point_test a where a.attribute1 BETWEEN 115 AND 125 AND a.attribute2 BETWEEN 34 AND 39";
			//String sql1 = sqls[i];
			System.out.println(sql1);
			String sql2 = "insert into T_RM_GIS_RESMAP (OBJECTID, GIS_RESMAP_ID, RES_ID, RES_CLASS_ID, RES_SHARDING_ID, MAINTENANCE_AREA_ID,MAINTENANCE_AREA_RES_CLASS_ID, LOCAL_NETWORK_ID, SHAPE) values ((select nvl(MAX(objectid),0) + 1 from T_RM_GIS_RESMAP),(select nvl(MAX(objectid),0) + 1 from T_RM_GIS_RESMAP), ? , ? , ? , ? , 102 , ? , SDE.ST_POINT(?,?,4326))";
			String resId = null;
			try {
				PreparedStatement ps1 = conn2.prepareStatement(sql1);
				ResultSet rs1 = ps1.executeQuery();
				while (rs1.next()) {
					resId = rs1.getString("RES_ID");
					String resClassId = rs1.getString("RES_CLASS_ID");
					String shardingId = rs1.getString("SHARDING_ID");
					String maintenanceAreaId = rs1
							.getString("MAINTENANCE_AREA_ID");
					String localNetworkId = rs1.getString("LOCAL_NETWORK_ID");
					String longitude = rs1.getString("ATTRIBUTE1");
					if (longitude == null) {
						longitude = "0";
					}
					String latitude = rs1.getString("ATTRIBUTE2");
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
				System.out.println("###############Խ����" + resId + "#################");
			}
		}
	}

	public static void main(String[] args) {
		CovertStation cs = new CovertStation();
		cs.covert();
	}
}





/*



SELECT LOCATION_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_LOCATION AS a WHERE a.RES_CLASS_ID = 112 AND a.LONGITUDE BETWEEN 115 AND 125 AND a.LATITUDE BETWEEN 34 AND 39;
SELECT BUILDING_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_BUILDING AS a WHERE a.RES_CLASS_ID = 114 AND a.LONGITUDE BETWEEN 115 AND 125 AND a.LATITUDE BETWEEN 34 AND 39;
SELECT SUPPORT_POINT_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_SUPPORT_POINT AS a WHERE a.RES_CLASS_ID = 211 AND a.LONGITUDE BETWEEN 115 AND 125 AND a.LATITUDE BETWEEN 34 AND 39;
SELECT SUPPORT_POINT_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_SUPPORT_POINT AS a WHERE a.RES_CLASS_ID = 212 AND a.LONGITUDE BETWEEN 115 AND 125 AND a.LATITUDE BETWEEN 34 AND 39;
SELECT JOINT_BOX_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_JOINT_BOX AS a WHERE a.RES_CLASS_ID = 272 AND a.LONGITUDE BETWEEN 115 AND 125 AND a.LATITUDE BETWEEN 34 AND 39;
SELECT a.PHYSICAL_DEVICE_ID AS RES_ID, a.RES_CLASS_ID, a.SHARDING_ID, MAINTENANCE_AREA_ID, a.LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_PHYSICAL_DEVICE AS a INNER JOIN T_RM_PHYSICAL_CONNECT_DEVICE AS b ON a.PHYSICAL_DEVICE_ID = b.PHYSICAL_DEVICE_ID WHERE a.RES_CLASS_ID = 296 AND a.LONGITUDE BETWEEN 115 AND 125 AND a.LATITUDE BETWEEN 34 AND 39;









*/