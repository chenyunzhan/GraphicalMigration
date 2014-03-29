package com.cloud.gis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.cloud.util.DB_GIS;

public class OptCableSegRoute {
	public void covert() {

		Connection conn2 = DB_GIS.getConn();

			//String sql1 = "SELECT LOCATION_ID AS RES_ID, RES_CLASS_ID, SHARDING_ID, MAINTENANCE_AREA_ID, LOCAL_NETWORK_ID, LONGITUDE, LATITUDE FROM T_RM_LOCATION AS a WHERE a.RES_CLASS_ID = 108;";
			//查询出该光缆中所有的点
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
				  "from cloud_temp_opt_cable_seg_route a " +
				  "inner join T_RM_GIS_RESMAP b " +
				    "on a.a_resource_id = b.res_id " +
				  "inner join T_RM_GIS_RESMAP c " +
				    "on a.z_resource_id = c.res_id " +
				  "where a.cable_segment_id = ? " +
				 "order by a.route_seq_num";
			//获取所有点中序列id最大的
			//String sql3 = "select max(a.route_seq_num) maxseq, cable_segment_id  from cloud_temp_opt_cable_seg_route a where a.z_resource_id != 0 group by a.cable_segment_id";
			String sql3 = "select max(a.route_seq_num) maxseq, cable_segment_id  from cloud_temp_opt_cable_seg_route a where a.cable_segment_id =200648911  and a.z_resource_id != 0 group by a.cable_segment_id";
			String sql4 = "select seq_rm_gis_opt_cable_seg_route.nextval objectId from dual";
			String sql5 = "select count(*) from T_RM_GIS_OPT_CABLE_SEG_ROUTE where res_id = ?";
			String cable_segment_id = null;
			try {
				
				PreparedStatement ps3 = conn2.prepareStatement(sql3);
				ResultSet rs3 = ps3.executeQuery();
				while(rs3.next()) {
					
					String objectId = null;
					PreparedStatement ps4 = conn2.prepareStatement(sql4);
					ResultSet rs4 = ps4.executeQuery();
					while(rs4.next()) {
						objectId = rs4.getString("objectId");
					}		
					
					String maxSeq = rs3.getString("maxseq");
					cable_segment_id = rs3.getString("cable_segment_id");
					PreparedStatement ps5 = conn2.prepareStatement(sql5);
					ps5.setString(1, cable_segment_id);
					ResultSet rs5 = ps5.executeQuery();
					if(rs5.next() && rs5.getInt(1) > 0) {
						ps4.close();
						ps5.close();
						continue;
					}
					
					PreparedStatement ps1 = conn2.prepareStatement(sql1);
					ps1.setString(1, cable_segment_id);
					ResultSet rs1 = ps1.executeQuery();
					String str1 = null;
					StringBuffer line = new StringBuffer();
					int count = 0;
					
					
					String tempLine = null;
					
					
					
					String resId = null;
					String resClassId = null;
					String shardingId = null;
					String maintenanceAreaId = null;
					String localNetworkId = null;
					String c_a_resource_id = null;
					String c_a_resource_res_class_id = null;
					String c_a_resource_sharding_id = null;
					String c_z_resource_id = null;
					String c_z_resource_res_class_id = null;
					String c_z_resource_sharding_id = null;
					String cable_segment_name = null;
					while (rs1.next()) {
						resId = rs1.getString("RES_ID");
						if(!resId.equals(str1)) {
							str1 = resId;
							line.append(rs1.getString("A_POS_X") + " ");
							line.append(rs1.getString("A_POS_Y") + ",");
							line.append(rs1.getString("Z_POS_X") + " ");
							line.append(rs1.getString("Z_POS_Y") + ",");
						} else {
							line.append(rs1.getString("Z_POS_X") + " ");
							line.append(rs1.getString("Z_POS_Y") + ",");
						}
					
						if(maxSeq.equals(rs1.getString("route_seq_num"))){
							
							tempLine = line.toString().substring(0, line.toString().length()-1);
							Set<String> posSet = new HashSet<String>();
							String[] strs = tempLine.split(",");
							for(int i=0; i<strs.length; i++) {
								posSet.add(strs[i]);
							}
							count = posSet.size();
						}
						
						if(cable_segment_name  == null) {
							
							resId = rs1.getString("RES_ID");
							resClassId = rs1.getString("RES_CLASS_ID");
							shardingId = rs1.getString("SHARDING_ID");
							maintenanceAreaId = rs1.getString("MAINTENANCE_AREA_ID");
							localNetworkId = rs1.getString("LOCAL_NETWORK_ID");
							c_a_resource_id = rs1.getString("c_a_resource_id");
							c_a_resource_res_class_id = rs1.getString("c_a_resource_res_class_id");
							c_a_resource_sharding_id = rs1.getString("c_a_resource_sharding_id");
							c_z_resource_id = rs1.getString("c_z_resource_id");
							c_z_resource_res_class_id = rs1.getString("c_z_resource_res_class_id");
							c_z_resource_sharding_id = rs1.getString("c_z_resource_sharding_id");
							cable_segment_name = rs1.getString("cable_segment_name");
							
						}
						ps4.close();
					}
					if( count > 1) {
						String sql2 = "insert into T_RM_GIS_OPT_CABLE_SEG_ROUTE  (OBJECTID,   GIS_RESMAP_ID,   object_label,   RES_ID,   RES_CLASS_ID,   RES_SHARDING_ID,   start_point_res_id,   start_point_res_class_id,   start_point_sharding_id,   end_point_res_id,   end_point_res_class_id,   end_point_sharding_id,   MAINTENANCE_AREA_ID,   MAINTENANCE_AREA_RES_CLASS_ID,   LOCAL_NETWORK_ID,   SHAPE)values  (? , ?, ?,  ?,   ?,   ?,   ?,   ?,   ?,   ?,   ?,   ?,   ?,   102,   ?,   SDE.ST_LINEFROMTEXT('LINESTRING("+ tempLine +")', 4326))";
						PreparedStatement ps2 = conn2.prepareStatement(sql2);
						ps2.setString(1, objectId);
						ps2.setString(2, objectId);
						ps2.setString(3, cable_segment_name);
						ps2.setString(4, resId);
						ps2.setString(5, resClassId);
						ps2.setString(6, shardingId);
						ps2.setString(7, c_a_resource_id);
						ps2.setString(8, c_a_resource_res_class_id);
						ps2.setString(9, c_a_resource_sharding_id);
						ps2.setString(10, c_z_resource_id);
						ps2.setString(11, c_z_resource_res_class_id);
						ps2.setString(12, c_z_resource_sharding_id);
						ps2.setString(13, maintenanceAreaId);
						ps2.setString(14, localNetworkId);
						System.out.print("1objectId="+objectId+";2objectId="+objectId+
								";3cable_segment_name="+cable_segment_name+
								";4resId="+resId+";5resClassId="+resClassId+
								";6shardingId="+shardingId+";7c_a_resource_id="+c_a_resource_id
								+";8c_a_resource_res_class_id="+c_a_resource_res_class_id+";9c_a_resource_sharding_id="+c_a_resource_sharding_id
								+";10c_z_resource_id="+c_z_resource_id+";11c_z_resource_res_class_id="+c_z_resource_res_class_id+
								";12c_z_resource_sharding_id"+c_z_resource_sharding_id+
								";13maintenanceAreaId="+maintenanceAreaId+";localNetworkId="+localNetworkId);
						//ps2.setString(13, line.toString().substring(0, line.toString().length()-1));
						int c = ps2.executeUpdate();
						ps2.close();
					}
					ps1.close();
					
				}
				
				ps3.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("##############################"+ cable_segment_id +"##################################");
				e.printStackTrace();
				
			}
		}

	public static void main(String[] args) {
		OptCableSegRoute cs = new OptCableSegRoute();
		cs.covert();
	}
}
