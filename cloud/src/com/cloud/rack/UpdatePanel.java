package com.cloud.rack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cloud.util.DB_LOCAL;
import com.cloud.util.DB_RM_RDM_SID;
import com.cloud.util.DB_RM_TPS_ADB;

/**
 * SDH-LQ000
 * 326传输-接入设备架-02
 * @author Administrator
 *
 */
public class UpdatePanel {
	private static int cols = 0;
	private static int rows = 0;
	private static double height = 0;
	private static double width = 0;
	public static void getColsRows(String mapId) {
		String sql = "SELECT count(*), map_id FROM mapinfo AS a WHERE a.type = '\"插槽\"' AND map_id = ? AND a.x IS NOT NULL GROUP BY map_id, y;";
        Connection conn = DB_LOCAL.getConn();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mapId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String str = rs.getString(1);
				rows ++;
				if(Integer.parseInt(str) > cols) {
					cols = Integer.parseInt(str);
				}
			}
			System.out.println("rows:" + rows + ";cols:" + cols);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateSlotPosition(String[] arr) {
		String sql = "UPDATE t_rm_slot a SET a.POS_X = ?, a.POS_Y = ?, a.TP_WIDTH = ?, a.TP_HEIGHT = ? WHERE a.SLOT_ID = ?";
        Connection conn = DB_RM_RDM_SID.getConn();
        double pos_x = 10;
        double pos_y = 20;
        double width = (UpdatePanel.width-200)/cols;
        double height = (UpdatePanel.height-500)/rows;
		try {
			for(int i=0; i<rows; i++) {
				for(int j=0; j<cols; j++) {
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setDouble(1, pos_x + width*j/10);
					ps.setDouble(2, pos_y + height*i/10);
					ps.setDouble(3, width);
					ps.setDouble(4, height);
					ps.setString(5, arr[i*cols+j]);
					int rs = ps.executeUpdate();
					System.out.println(rs);
				}
			}
		} catch (SQLException e) {
			System.out.println("############未能填充满！！！#############");
			e.printStackTrace();
		}
	}
    
	public static void getParentComponentWH(String shelfId) {
		Connection conn = DB_RM_RDM_SID.getConn();
		String sql2 = "SELECT * FROM t_rm_shelf AS b WHERE b.SHELF_ID = ?;";
		try {
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			ps2.setString(1, shelfId);
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()) {
				width = rs2.getDouble("TP_WIDTH");
				height = rs2.getDouble("TP_HEIGHT");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		OldNewRelation onr = new OldNewRelation();
		String [] maps = onr.getMapIds();
		for (int i = 0; i < maps.length; i++) {
			//String[] arr = onr.getNewSlotId("SDH-LQ000");
			String[] arr = onr.getMisIdSlotIdRelation("326779");
			getColsRows("326779");
			getParentComponentWH(arr[999]);
			updateSlotPosition(arr);
		}
	}
}
