package com.cloud.gis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.cloud.util.DB_GIS;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class SupportingSegment {
	public void covert() {

		Connection conn2 = DB_GIS.getConn();

			//String sql1 = "SELECT LOCATION_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_LOCATION AS a WHERE a.RES_CLASS_ID = 108;";
			String sql1 = "select "+
				       "a.res_id, "+
				       "a.res_class_id, "+
				       "a.sharding_id, "+
				       "a.local_network_id, "+
				       "a.maintenance_area_id, "+
				       "a.record_version, "+
				       "a.a_resource_id, " +
				       "a.a_resource_res_class_id, "+
				       "a.a_resource_sharding_id, "+
				       "a.z_resource_id, "+
				       "a.z_resource_res_class_id, "+
				       "a.z_resource_sharding_id, "+
				       "a.support_segment_name, " + 
				       "b.shape.minx                A_POS_X, "+
				       "b.shape.miny                A_POS_Y, "+
				       "c.shape.minx                Z_POS_X, "+
				       "c.shape.miny                Z_POS_Y "+
				 "from CLOUD_TEMP_SUPPORT_SEGMENT a "+
				 "inner join T_RM_GIS_RESMAP b "+
				 "   on a.a_resource_id = b.res_id "+
				 "inner join T_RM_GIS_RESMAP c "+
				 "   on a.z_resource_id = c.res_id "+
				 "where a.res_id = ? and a.support_segment_name is not null";
			String sql3 = " select a.res_id, a.support_segment_name from cloud_temp_support_segment a where a.a_resource_id != 0 and a.z_resource_id != 0 and a.support_segment_name is not null ";
			String sql4 = " select seq_rm_gis_supporting_segment.nextval objectId from dual";
			String sql5 = "select count(*) from t_rm_gis_supporting_segment where RES_ID = ?";
			String res_id = null;
			try {
				
				PreparedStatement ps3 = conn2.prepareStatement(sql3);
				ResultSet rs3 = ps3.executeQuery();
				while(rs3.next()) {
					String objectId = null;
					PreparedStatement ps4 = conn2.prepareStatement(sql4);
					ResultSet rs4 = ps4.executeQuery();
					if(rs4.next()) {
						objectId = rs4.getString("objectId");
					}
					res_id = rs3.getString("res_id");
					PreparedStatement ps5 = conn2.prepareStatement(sql5);
					ps5.setString(1, res_id);
					ResultSet rs5 = ps5.executeQuery();
					if(rs5.next() && rs5.getInt(1) > 0) {
						ps4.close();
						ps5.close();
						continue;
					}
					PreparedStatement ps1 = conn2.prepareStatement(sql1);
					ps1.setString(1, res_id);
					ResultSet rs1 = ps1.executeQuery();
					String str1 = null;
					StringBuffer line = new StringBuffer();
					String resId = null;
					String resClassId = null;
					String shardingId = null;
					String maintenanceAreaId = null;
					String localNetworkId = null;
					String a_resource_id = null;
					String a_resource_res_class_id = null; 
					String a_resource_sharding_id = null;
					String z_resource_id = null;
					String z_resource_res_class_id = null;
					String z_resource_sharding_id = null;
					String support_segment_name = null;
					String tempLine = null;
					int count = 0;
					while (rs1.next()) {
						//count++;
						if(support_segment_name == null){
							resId = rs1.getString("RES_ID");
							resClassId = rs1.getString("RES_CLASS_ID");
							shardingId = rs1.getString("SHARDING_ID");
							maintenanceAreaId = rs1.getString("MAINTENANCE_AREA_ID");
							localNetworkId = rs1.getString("LOCAL_NETWORK_ID");
							a_resource_id = rs1.getString("a_resource_id");
							a_resource_res_class_id = rs1.getString("a_resource_res_class_id");
							a_resource_sharding_id = rs1.getString("a_resource_sharding_id");
							z_resource_id = rs1.getString("z_resource_id");
							z_resource_res_class_id = rs1.getString("z_resource_res_class_id");
							z_resource_sharding_id = rs1.getString("z_resource_sharding_id");
							support_segment_name = rs1.getString("support_segment_name");
						}
						if(!resId.equals(str1)) {
							str1 = resId;
							line.append(rs1.getDouble("A_POS_X")+Math.random()*0.0001 + " ");
							line.append(rs1.getDouble("A_POS_Y") +Math.random()*0.0001 + ",");
							line.append(rs1.getDouble("Z_POS_X") +Math.random()*0.0001 + " ");
							line.append(rs1.getDouble("Z_POS_Y") +Math.random()*0.0001 + ",");
						} else {
							//line.append(temp1);
							//line.append(temp2);
							line.append(rs1.getDouble("Z_POS_X") +Math.random()*0.0001 + " ");
							line.append(rs1.getDouble("Z_POS_Y") +Math.random()*0.0001 + ",");
							
						}
						//tempLine = "117.20605344 36.90048917,117.20605306 36.90043317";
						tempLine = line.toString().substring(0, line.toString().length()-1);
						ArrayList<String> posSet = new ArrayList<String>();
						String[] strs = tempLine.split(",");
						for(int i=0; i<strs.length; i++) {
							posSet.add(strs[i]);
						}
						count = posSet.size();
						
						
						/**
						Set<String> posSet = new HashSet<String>();
						String[] strs = tempLine.split(",");
						for(int i=0; i<strs.length; i++) {
							posSet.add(strs[i]);
						}
						count = posSet.size();
						*/
					}
					
					
					if( count > 1) {
						String sql2 = "insert into t_rm_gis_supporting_segment  (OBJECTID,   GIS_RESMAP_ID,   object_label,   RES_ID,   RES_CLASS_ID,   RES_SHARDING_ID,   start_point_res_id,   start_point_res_class_id,   start_point_sharding_id,   end_point_res_id,   end_point_res_class_id,   end_point_sharding_id,   MAINTENANCE_AREA_ID,   MAINTENANCE_AREA_RES_CLASS_ID,   LOCAL_NETWORK_ID,   SHAPE)values  (? , ?, ?,  ?,   ?,   ?,   ?,   ?,   ?,   ?,   ?,   ?,   ?,   102,   ?,   SDE.ST_LINEFROMTEXT('LINESTRING("+ tempLine +")', 4326))";
						System.out.println(sql2);
						PreparedStatement ps2 = conn2.prepareStatement(sql2);
						ps2.setString(1, objectId);
						ps2.setString(2, objectId);
						ps2.setString(3, support_segment_name);
						ps2.setString(4, resId);
						ps2.setString(5, resClassId);
						ps2.setString(6, shardingId);
						ps2.setString(7, a_resource_id);
						ps2.setString(8, a_resource_res_class_id);
						ps2.setString(9, a_resource_sharding_id);
						ps2.setString(10, z_resource_id);
						ps2.setString(11, z_resource_res_class_id);
						ps2.setString(12, z_resource_sharding_id);
						ps2.setString(13, maintenanceAreaId);
						ps2.setString(14, localNetworkId);
						//ps2.setString(13, line.toString().substring(0, line.toString().length()-1));
						int c = ps2.executeUpdate();
						ps2.close();
					}
					ps1.close();
					ps5.close();
					ps4.close();
				}
				ps3.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("##############################"+ res_id +"##################################");
				e.printStackTrace();
				
			}
		}

	public static void main(String[] args) {
		SupportingSegment cs = new SupportingSegment();
		cs.covert();
	}
}
