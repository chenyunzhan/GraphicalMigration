package com.cloud.room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cloud.util.DB_LOCAL;
import com.cloud.util.DB_OLD_FORMAL;
import com.cloud.util.DB_RM_RDM_SID_LU_H;
import com.cloud.util.DB_RM_TPS_ADB;

/**
 * @author Administrator
 *
 */
public class BatchRoom {
	public void batchUpdateRoom(Integer mapId) {
		String sql1 = "SELECT * FROM room_mapinfo WHERE minx IS NOT NULL and map_id = " + mapId;
		String sql2 = "UPDATE t_rm_physical_device SET POS_X = ?, POS_Y = ? WHERE ATTRIBUTE2 = ?;";
		String sql3 = "UPDATE t_rm_rack SET POS_X = ?, POS_Y = ? WHERE ATTRIBUTE2 = ?;";
		String sql4 = "select c.room_name map_name from e_mi_store a inner join mi_shelf_planform b on a.map_id=b.map_id inner join s_room c on a.mis_id=c.room_id where a.map_id = ?";
		String sql5 = "SELECT a.LOCATION_ID AS RES_ID, a.RES_CLASS_ID, a.SHARDING_ID from t_rm_location AS a WHERE a.LOCATION_NAME = ?;";
		String sql6 = "SELECT a.TOPO_INSTANCE_ID FROM t_rm_topo_instance AS a WHERE a.RES_CLASS_ID = ? AND a.RESOURCE_ID = ? AND a.SHARDING_ID = ?;";
		String sql7 = "SELECT MAX(TOPO_INST_POINT_ID)+1 AS maxId FROM t_rm_topo_inst_point";
		String sql8 = "INSERT t_rm_topo_inst_point(TOPO_INST_POINT_ID, TOPO_INSTANCE_ID, TOPOLOGY_SPEC_LAYER_ID, DEFAULT_STYLE_ATTR_VALUE, AUTO_REFRESH_FLAG, NAME, TOPO_CODE, TIP, POS_X, POS_Y, POS_Z, HEIGHT, WIDTH, LENGTH, POINTS, GROUP_EXPANDED_FLAG, PARENT_TOPO_CODE, RES_OBJECT_TYPE_ENUM_ID, RES_CLASS_REL_ID, RES_CLASS_ID, RES_SHARDING_ID, RES_ID, RES_EXPAND_INFO, MAPPING_TOPO_INSTANCE_ID, CREATED_BY, LAST_UPDATED_BY) values (?, ?, 562, '', 0, ?, UUID(), '', ?, ?, NULL, ?, ?, NULL, NULL, 0, '', 44102, -1, 0, 0, 0, 'content.type:vector|vector.shape:rectangle', 0, 0, 0);";
		Connection conn1 = DB_LOCAL.getConn();
		Connection conn2 = DB_RM_RDM_SID_LU_H.getConn();
		Connection conn3 = DB_OLD_FORMAL.getConn();
		Connection conn4 = DB_RM_TPS_ADB.getConn();
		
		try {
			PreparedStatement ps = conn1.prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();
			double minx = 100000.0;
			double maxy = 0.0;
			double miny = 100000.0;
			double maxx = 0.0;
			//求出最小的x和最小的y
			while (rs.next()) {
				String X = rs.getString("minx");
				String Y = rs.getString("miny");
				//System.out.println(X + " , " + Y);
				double a = (Double.parseDouble(X))*639/639;
				double b = ((Double.parseDouble(Y))*639/639);
				System.out.println(a + " , " + b);
				if(a < minx) {
					minx = a;
				}
				if(b > maxy) {
					maxy = b;
				}
				if(a > maxx) {
					maxx = a;
				}
				if(b < miny) {
					miny = b;
				}
			}
			System.out.println("#########################");
			System.out.println("minx:" + minx);
			System.out.println("maxy:" + maxy);
			System.out.println("maxx:" + maxx);
			System.out.println("miny:" + miny);
			System.out.println("#########################");
			PreparedStatement ps1 = conn1.prepareStatement(sql1);
			ResultSet rs1 = ps1.executeQuery();
			
			
			//批量更新存量数据的坐标点。
			PreparedStatement ps2 = null;
			PreparedStatement ps3 = null;
			ps2 = conn2.prepareStatement(sql2);
			ps3 = conn2.prepareStatement(sql3);
			conn2.setAutoCommit(false);
			//遍历所有的矩形
			while (rs1.next()) {
				int misId = rs1.getInt("mis_id");
				//String mapId = rs1.getString("map_id");
				String name = rs1.getString("name");
				double height = 0;
				double width = 0;
				double X = Double.parseDouble(rs1.getString("minx"))*639/639 - minx + 400;
				double Y = -(Double.parseDouble(rs1.getString("miny"))*639/639 - maxy) + 800;
				height = rs1.getDouble("maxy") - rs1.getDouble("miny");
				width = rs1.getDouble("maxx") - rs1.getDouble("minx");
				
				X = X/2.5;
				Y = Y/4;
				height = height/4;
				width = width/2.5;
				
				if(misId==0 || misId < 0) {
					PreparedStatement ps4 = conn3.prepareStatement(sql4);
					ps4.setString(1, String.valueOf(mapId));
					ResultSet rs4 = ps4.executeQuery();
					String mapName = null;
					if(rs4.next()) {
						mapName = rs4.getString("map_name");
					}
					PreparedStatement ps5 = conn2.prepareStatement(sql5);
					ps5.setString(1, mapName);
					ResultSet rs5 = ps5.executeQuery();
					String resId = null;
					String resClassId = null;
					String shardingId = null;
					if(rs5.next()) {
						resId = rs5.getString("RES_ID");
						resClassId = rs5.getString("RES_CLASS_ID");
						shardingId = rs5.getString("SHARDING_ID");
					}
					PreparedStatement ps6 = conn4.prepareStatement(sql6);
					ps6.setString(1, resClassId);
					ps6.setString(2, resId);
					ps6.setString(3, shardingId);
					ResultSet rs6 = ps6.executeQuery();
					String TOPO_INSTANCE_ID = null;
					if(rs6.next()) {
						TOPO_INSTANCE_ID = rs6.getString("TOPO_INSTANCE_ID");
					}
					PreparedStatement ps7 = conn4.prepareStatement(sql7);
					ResultSet rs7 = ps7.executeQuery();
					String maxId = null;
					if(rs7.next()) {
						maxId = rs7.getString("maxId");
					}
					PreparedStatement ps8 = conn4.prepareStatement(sql8);
					ps8.setString(1, maxId);
					ps8.setString(2, TOPO_INSTANCE_ID);
					ps8.setString(3, "");
					ps8.setDouble(4, X);
					ps8.setDouble(5, Y-height);
					if((rs1.getString("layer_id").equals("\"3\""))) {
						ps8.setDouble(6, 1);
						ps8.setDouble(7, 1);
					}else {
						ps8.setDouble(6, height);
						ps8.setDouble(7, width);
					}
					System.out.println(mapName);
					if(TOPO_INSTANCE_ID != null) {
						ps8.executeUpdate();
					}
					
					ps8.close();
					rs7.close();
					ps7.close();
					rs6.close();
					ps6.close();
					rs5.close();
					ps5.close();
					rs4.close();
					ps4.close();
				} else {
					ps2.setString(1, String.valueOf(X));
					ps2.setString(2, String.valueOf(Y-height));
					ps2.setString(3, String.valueOf(misId));
					ps2.addBatch();
					
					ps3.setString(1, String.valueOf(X));
					ps3.setString(2, String.valueOf(Y-height));
					ps3.setString(3, String.valueOf(misId));
					ps3.addBatch();
				}
				
			}
			
			int[] num2 = ps2.executeBatch();
			int[] num3 = ps3.executeBatch();
			conn2.commit();  
            conn2.setAutoCommit(true);  
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public List<Integer> getRoomList() {
		List<Integer> list = new ArrayList<Integer>();
		String sql = "SELECT DISTINCT map_id FROM room_mapinfo;";
		Connection conn = DB_LOCAL.getConn();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				list.add(rs.getInt("map_id"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	public static void main(String[] args) {
		BatchRoom br = new BatchRoom();
		/**
		 * 
		br.batchUpdateRoom(2208);
		 */
		List<Integer> list = br.getRoomList();
		for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
			Integer mapId = (Integer) iterator.next();
			br.batchUpdateRoom(mapId);
		}
	}
	
}
