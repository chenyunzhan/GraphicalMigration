package com.cloud.gis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cloud.util.DB_GIS;

public class CovertSengment {
	public void covert() {

		Connection conn2 = DB_GIS.getConn();

			//String sql1 = "SELECT LOCATION_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_LOCATION AS a WHERE a.RES_CLASS_ID = 108;";
			String sql1 = "select a.route_seq_num, " +
				       "a.cable_segment_name, " +
				       "a.cable_segment_id RES_ID, " +
				       "a.res_class_id, " +
				       "a.sharding_id, " +
				       "a.c_a_resource_id,       a.c_a_resource_res_class_id,       a.c_a_resource_sharding_id,       a.c_z_resource_id,       a.c_z_resource_res_class_id,       a.c_z_resource_sharding_id, " + 
				       "a.maintenance_area_id, " +
				       "a.local_network_id, " +
				       "a.record_version, " +
				       "b.shape.minx          A_POS_X, " +
				       "b.shape.miny          A_POS_Y, " +
				       "c.shape.minx          Z_POS_X, " +
				       "c.shape.miny          Z_POS_Y " +
				  "from CLOUD_TEMP_CABLE_SEGMENT a " +
				  "inner join T_RM_GIS_RESMAP b " +
				    "on a.a_resource_id = b.res_id " +
				  "inner join T_RM_GIS_RESMAP c " +
				    "on a.z_resource_id = c.res_id " +
				  "where a.cable_segment_id = ? " +
				 "order by a.route_seq_num";
			String sql3 = "select max(a.route_seq_num) maxseq, cable_segment_id from CLOUD_TEMP_CABLE_SEGMENT a where a.z_resource_id != 0 group by a.cable_segment_id";
			try {
				PreparedStatement ps3 = conn2.prepareStatement(sql3);
				ResultSet rs3 = ps3.executeQuery();
				while(rs3.next()) {
					String maxSeq = rs3.getString("maxseq");
					String cable_segment_id = rs3.getString("cable_segment_id");
					PreparedStatement ps1 = conn2.prepareStatement(sql1);
					ps1.setString(1, cable_segment_id);
					ResultSet rs1 = ps1.executeQuery();
					String str1 = null;
					StringBuffer line = new StringBuffer();
					String temp1 = null;
					String temp2 = null;
					while (rs1.next()) {
						String resId = rs1.getString("RES_ID");
						String resClassId = rs1.getString("RES_CLASS_ID");
						String shardingId = rs1.getString("SHARDING_ID");
						String maintenanceAreaId = rs1.getString("MAINTENANCE_AREA_ID");
						String localNetworkId = rs1.getString("LOCAL_NETWORK_ID");
						String c_a_resource_id = rs1.getString("c_a_resource_id");
						String c_a_resource_res_class_id = rs1.getString("c_a_resource_res_class_id");
						String c_a_resource_sharding_id = rs1.getString("c_a_resource_sharding_id");
						String c_z_resource_id = rs1.getString("c_z_resource_id");
						String c_z_resource_res_class_id = rs1.getString("c_z_resource_res_class_id");
						String c_z_resource_sharding_id = rs1.getString("c_z_resource_sharding_id");
						String cable_segment_name = rs1.getString("cable_segment_name");
						if(!resId.equals(str1)) {
							str1 = resId;
							line.append(rs1.getString("A_POS_X") + " ");
							line.append(rs1.getString("A_POS_Y") + ",");
							line.append(rs1.getString("Z_POS_X") + " ");
							line.append(rs1.getString("Z_POS_Y") + ",");
							temp1 = rs1.getString("Z_POS_X") + " ";
							temp2 = rs1.getString("Z_POS_Y") + ",";
						} else {
							//line.append(temp1);
							//line.append(temp2);
							line.append(rs1.getString("Z_POS_X") + " ");
							line.append(rs1.getString("Z_POS_Y") + ",");
							temp1 = rs1.getString("Z_POS_X") + " ";
							temp2 = rs1.getString("Z_POS_Y") + ",";
						}
						
						if(maxSeq.equals(rs1.getString("route_seq_num"))) {
							String sql2 = "insert into T_RM_GIS_OPT_CABLE_SEG_ROUTE  (OBJECTID,   GIS_RESMAP_ID,   object_label,   RES_ID,   RES_CLASS_ID,   RES_SHARDING_ID,   start_point_res_id,   start_point_res_class_id,   start_point_sharding_id,   end_point_res_id,   end_point_res_class_id,   end_point_sharding_id,   MAINTENANCE_AREA_ID,   MAINTENANCE_AREA_RES_CLASS_ID,   LOCAL_NETWORK_ID,   SHAPE)values  ((select nvl(MAX(objectid), 0) + 1 from T_RM_GIS_RESMAP),   (select nvl(MAX(objectid), 0) + 1 from T_RM_GIS_RESMAP), ?,  ?,   ?,   ?,   ?,   ?,   ?,   ?,   ?,   ?,   ?,   102,   ?,   SDE.ST_LINEFROMTEXT('LINESTRING("+ line.toString().substring(0, line.toString().length()-1) +")', 4326))";
							PreparedStatement ps2 = conn2.prepareStatement(sql2);
							ps2.setString(1, cable_segment_name);
							ps2.setString(2, resId);
							ps2.setString(3, resClassId);
							ps2.setString(4, shardingId);
							ps2.setString(5, c_a_resource_id);
							ps2.setString(6, c_a_resource_res_class_id);
							ps2.setString(7, c_a_resource_sharding_id);
							ps2.setString(8, c_z_resource_id);
							ps2.setString(9, c_z_resource_res_class_id);
							ps2.setString(10, c_z_resource_sharding_id);
							ps2.setString(11, maintenanceAreaId);
							ps2.setString(12, localNetworkId);
							//ps2.setString(13, line.toString().substring(0, line.toString().length()-1));
							int count = ps2.executeUpdate();
						}
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	public static void main(String[] args) {
		CovertSengment cs = new CovertSengment();
		cs.covert();
	}
}
