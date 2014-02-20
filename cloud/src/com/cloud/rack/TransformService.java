package com.cloud.rack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cloud.util.DB_LOCAL;
import com.cloud.util.DB_OLD_FORMAL;
import com.cloud.util.DB_RM_RDM_SID;
import com.cloud.util.DB_RM_RDM_SID_LU_H;
import com.cloud.util.DB_RM_TPS_ADB;

/**
 * 高新区沁心园小区-S001GL
 * @author Administrator
 *
 */
public class TransformService {
	
	private double rackWidth = 6830;
	private double rackHeight = 15250;
	private Integer rackCols = 0;
	private Integer rackRows = 0;
	private double rackMaxX = 0;
	private double rackMaxY = 0;
	private double rackMinX = 0;
	private double rackMinY = 0;
	private double rackUnitWidth = 0;
	private double rackUnitHeight = 0;
	private List<ShelfModel> shelves = new ArrayList<ShelfModel>();
	
	
	public void transform(Integer topInstanceId, String rackName, Connection conn1, Connection conn2) {
		/**
		 * 
		
		 */

		//String sql1 = "select c.mis_id, c.geoloc.SDO_POINT.x x, c.geoloc.SDO_POINT.y y from e_mi_store a inner join e_shelf b on a.mis_id = b.shelf_id inner join e_mi_shelf_panel c on a.map_id = c.map_id where c.layer_name = '模块' and b.shelf_no = ? order by c.geoloc.SDO_POINT.Y, c.geoloc.SDO_POINT.X";
		String sqlx = "select mis_id, labelstr, min(column_value) minx, max(column_value) maxx from (select c.mis_id, c.labelstr, d.*, rownum rn from e_mi_store a, e_shelf b, e_mi_shelf_panel c, table(c.geoloc.SDO_ORDINATES) d where a.map_id = c.map_id and a.mis_id = b.shelf_id and b.shelf_no = ? and c.layer_name = '模块')  where mod(rn, 2) = 1 group by mis_id, labelstr";
		String sqly = "select mis_id, labelstr, min(column_value) miny, max(column_value) maxy from (select c.mis_id, c.labelstr, d.*, rownum rn from e_mi_store a, e_shelf b, e_mi_shelf_panel c, table(c.geoloc.SDO_ORDINATES) d where a.map_id = c.map_id and a.mis_id = b.shelf_id and b.shelf_no = ? and c.layer_name = '模块')  where mod(rn, 2) = 0 group by mis_id, labelstr";
		String sql2 = "select a.shelf_id, a.upper_resource_id from t_rm_shelf AS a where a.ATTRIBUTE2 = ?";
		
		try {
			PreparedStatement psx = conn1.prepareStatement(sqlx);
			PreparedStatement psy = conn1.prepareStatement(sqly);
			psx.setString(1, rackName);
			psy.setString(1, rackName);
			ResultSet rsx = psx.executeQuery();
			ResultSet rsy = psy.executeQuery();
			String misId = null;
			double minx = 0;
			double miny = 0;
			double maxx = 0;
			double maxy = 0;
			getRackCR(rackName, conn1);
			getRackMaxXY(rackName, conn1);
			while(rsx.next() && rsy.next()) {
				misId = rsx.getString("mis_id");
				minx = rsx.getDouble("minx");
				maxx = rsx.getDouble("maxx");
				miny = rsy.getDouble("miny");
				maxy = rsy.getDouble("maxy");
				
				PreparedStatement ps2 = conn2.prepareStatement(sql2);
				ps2.setString(1, misId);
				ResultSet rs2 = ps2.executeQuery();
				String resId = null;
				String rackId = null;
				Integer rs = 1;
				Integer cs = 1;
				Integer r = 1;
				Integer c = 1;
				if(rs2.next()) {
					ShelfModel sm = new ShelfModel();
					resId = rs2.getString("shelf_id");
					rackId = rs2.getString("upper_resource_id");
					if(rackId != null && rackWidth == 683 && rackHeight == 1525) {
						getRackWH(rackId, conn2);
					}
					rackUnitHeight = rackHeight/rackRows;
					rackUnitWidth = rackWidth/rackCols;
					/*
					while(((rackMaxX-rackMinX)/rackCols*cs) <= (rackMaxX-rackMinX) && minx >= ((rackMaxX-rackMinX)/rackCols*cs)) {
						cs ++;
					}
					while((rackMaxY/rackRows*rs) <= rackMaxY && miny >= (rackMaxY/rackRows*rs)) {
						rs++;
					}
					*/
					//cs = cs - 1;
					//rs = rs - 1;
					cs = (int) ((minx-rackMinX)/((rackMaxX-rackMinX)/rackCols));
					rs = (int) ((miny-rackMinY)/((rackMaxY-rackMinY)/rackRows));
					//cs = cs + 1;
					//rs = rs + 1;
					sm.setResId(resId);
					sm.setCol((int) ((maxx - minx)/rackUnitWidth));
					sm.setRow((int) ((maxy - miny)/rackUnitHeight));
					sm.setHeight(rackUnitHeight*r);
					sm.setWidth(rackWidth);
					sm.setPosY((-rackUnitHeight*(rs)+rackHeight)/10+60-rackUnitHeight/10);
					//sm.setPosX(rackUnitWidth*(cs-1)+10);
					sm.setPosX(20);
					shelves.add(sm);
					
					ps2.close();
					
				}
				
				
				
			}
			psx.close();
			psy.close();
			
			updateShelves(shelves, topInstanceId, conn2);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		
	}
	
	
	public void updateShelves(List<ShelfModel> shelves, Integer topInstanceId, Connection conn) {
		String sql = "UPDATE t_rm_shelf a SET a.POS_X = ?, a.POS_Y = ?, a.TP_WIDTH = ?, a.TP_HEIGHT = ? WHERE a.SHELF_ID = ?;";
		String sql1 = "SELECT count(*) FROM t_rm_slot where SHELF_ID = ?;";
		String sql2 = "SELECT SLOT_ID FROM t_rm_slot c WHERE c.SHELF_ID = ? ORDER BY c.SLOT_CODE;";
		String sql3 = "UPDATE t_rm_slot a SET a.POS_X = ?, a.POS_Y = ?, a.TP_WIDTH = ?, a.TP_HEIGHT = ? WHERE a.SLOT_ID = ?";
		//Connection conn = DB_RM_RDM_SID_LU_H.getConn();
		try {
			for (Iterator iterator = shelves.iterator(); iterator.hasNext();) {
					ShelfModel sm = (ShelfModel) iterator.next();
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setDouble(1, sm.getPosX());
					ps.setDouble(2, sm.getPosY());
					ps.setDouble(3, sm.getWidth());
					ps.setDouble(4, sm.getHeight());
					ps.setString(5, sm.getResId());
					ps.executeUpdate();
					
					
					PreparedStatement ps1 = conn.prepareStatement(sql1);
					ps1.setString(1, sm.getResId());
					ResultSet rs1 = ps1.executeQuery();
					int slot_count = 0;
					if(rs1.next()) {
						slot_count = rs1.getInt(1);
					}
					double uniteWSlot = (sm.getWidth()-100)/slot_count;
					
					PreparedStatement ps2 = conn.prepareStatement(sql2);
					ps2.setString(1, sm.getResId());
					ResultSet rs2 = ps2.executeQuery();
					List<SlotModel> slots = new ArrayList<SlotModel>();
					while(rs2.next()) {
						SlotModel slm = new SlotModel();
						String slotId = rs2.getString("SLOT_ID");
						slm.setSlotId(slotId);
						slm.setWidth(uniteWSlot);
						slm.setHeight(sm.getHeight());
						slots.add(slm);
					}
					
					int i = -1;
					for (Iterator iterator2 = slots.iterator(); iterator2
							.hasNext();) {
						i++;
						SlotModel slm = (SlotModel) iterator2.next();
						PreparedStatement ps3 = conn.prepareStatement(sql3);
						ps3.setDouble(1, i*uniteWSlot/10+30);
						ps3.setDouble(2, 25);
						ps3.setDouble(3, slm.getWidth());
						ps3.setDouble(4, slm.getHeight());
						ps3.setString(5, slm.getSlotId());
						ps3.executeUpdate();
					}
					
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public void getRackWH(String rackId, Connection conn) {
		//Connection conn = DB_RM_RDM_SID.getConn();
		//String sql2 = "SELECT * FROM t_rm_shelf AS b WHERE b.SHELF_ID = ?;";
		String sql3 = "select a.TP_HEIGHT, a.TP_WIDTH from t_rm_rack AS a WHERE a.RACK_ID = ?;";
		try {
			PreparedStatement ps2 = conn.prepareStatement(sql3);
			ps2.setString(1, rackId);
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()) {
				rackWidth = rs2.getString("TP_WIDTH") == null ? 683 : rs2.getDouble("TP_WIDTH");
				rackHeight = rs2.getString("TP_HEIGHT") == null ? 1525 : rs2.getDouble("TP_HEIGHT");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void getRackCR(String rackName, Connection conn) {
		String sql = "select count(*) from e_mi_store a inner join e_shelf b on a.mis_id = b.shelf_id inner join e_mi_shelf_panel c on a.map_id = c.map_id where c.layer_name = '模块' and b.shelf_no = ? group by c.geoloc.SDO_POINT.Y";
        //Connection conn = DB_OLD_FORMAL.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, rackName);
			rs = ps.executeQuery();
			while(rs.next()) {
				String str = rs.getString(1);
				rackRows ++;
				if(Integer.parseInt(str) > rackCols) {
					rackCols = Integer.parseInt(str);
				}
			}
			ps.close();
			System.out.println("rows:" + rackRows + ";cols:" + rackCols);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void getRackMaxXY(String rackName, Connection conn) {
		String sqlx = "select max(column_value) maxx, min(column_value) minx from (select c.mis_id, c.labelstr, d.*, rownum rn from e_mi_store a, e_shelf b, e_mi_shelf_panel c, table(c.geoloc.SDO_ORDINATES) d where a.map_id = c.map_id and a.mis_id = b.shelf_id and b.shelf_no = ? and c.layer_name = '模块')  where mod(rn, 2) = 1";
		String sqly = "select max(column_value) maxy, min(column_value) miny from (select c.mis_id, c.labelstr, d.*, rownum rn from e_mi_store a, e_shelf b, e_mi_shelf_panel c, table(c.geoloc.SDO_ORDINATES) d where a.map_id = c.map_id and a.mis_id = b.shelf_id and b.shelf_no = ? and c.layer_name = '模块')  where mod(rn, 2) = 0";
        //Connection conn = DB_OLD_FORMAL.getConn();
        PreparedStatement psx = null;
        PreparedStatement psy = null;
        ResultSet rsx = null;
        ResultSet rsy = null;
		try {
			psx = conn.prepareStatement(sqlx);
			psy = conn.prepareStatement(sqly);
			psx.setString(1, rackName);
			psy.setString(1, rackName);
			rsx = psx.executeQuery();
			rsy = psy.executeQuery();
			while(rsy.next()&& rsx.next()) {
				rackMaxX = rsx.getDouble("maxx");
				rackMaxY = rsy.getDouble("maxy");
				rackMinX = rsx.getDouble("minx");
				rackMinY = rsy.getDouble("miny");
			}
			psx.close();
			psy.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rsx.close();
				psx.close();
				rsy.close();
				psy.close();
				//conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		TransformService ts = new TransformService();
		//ts.transform(2149, "330局传输-IP设备架-07");
	}
}
