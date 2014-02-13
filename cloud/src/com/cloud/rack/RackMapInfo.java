package com.cloud.rack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.cloud.util.DB_LOCAL;
import com.cloud.util.DB_OLD_TEST;

public class RackMapInfo {
	public static void analyzeMapInfo() {
		File file_mif = new File("E:\\mycode\\cloud\\src\\com\\cloud\\rack\\E_MI_EQU_PANEL4.MIF");
		File file_mid = new File("E:\\mycode\\cloud\\src\\com\\cloud\\rack\\E_MI_EQU_PANEL4.MID");
		
		Map<String,String> m = new HashMap<String, String>();
		
		BufferedReader br_mif = null;
		BufferedReader br_mid = null;
		
		try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            br_mif = new BufferedReader(new FileReader(file_mif));
            br_mid = new BufferedReader(new FileReader(file_mid));
            String tempString_mif = null;
            String tempString_mid = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString_mid = br_mid.readLine()) != null) {
            	//System.out.print(tempString_mid.split(",")[3]);
            	//m.put("a"+line,tempString_mid.split(",")[3] + "类型:" + tempString_mid.split(",")[2]);
            	m.put("a"+line, tempString_mid.split(",")[2] + "," + tempString_mid.split(",")[3] + "," + tempString_mid.split(",")[1]);
            	line++;
            }
            line = 1;
            while ((tempString_mif = br_mif.readLine()) != null) {
                // 显示行号
            	if(tempString_mif.trim().equals("5")) {
            		tempString_mif = br_mif.readLine();
            		//tempString_mif = br_mif.readLine();
            		//tempString_mif = br_mif.readLine();
            		//System.out.println(tempString_mif);
            		//机架、机框
            		//m.put("b"+line, "POS_X:" + (Double.parseDouble(tempString_mif.split(" ")[0])+400) + ",POS_Y:" + (Double.parseDouble(tempString_mif.split(" ")[1])+4000));
            		//m.put("b"+line, "POS_X:" + (Double.parseDouble(tempString_mif.split(" ")[0])) + ",POS_Y:" + (Double.parseDouble(tempString_mif.split(" ")[1])));
            		m.put("b"+line, (Double.parseDouble(tempString_mif.split(" ")[0])) + "," + (Double.parseDouble(tempString_mif.split(" ")[1])));
            		line ++;
            	} else if(tempString_mif.trim().equals("Pline 5")){
            		m.put("b"+line, null);
            		line ++;
            	}
            }
            br_mid.close();
            br_mif.close();
            
            
            for(int i=1; i<m.size()/2+1; i++) {
            	System.out.println(m.get("a"+i) + "," + m.get("b"+i));
            }
            String sql1 = "select distinct b.map_id, d.shelf_name from e_mi_store a inner join e_mi_equ_panel b on a.map_id = b.map_id " +
                    " inner join e_equip_frame c on a.mis_id = c.frame_id " + 
                    " inner join (select n.mis_id, x.shelf_name from e_mi_store m inner join e_mi_shelf_panel n on m.map_id = n.map_id " +
                    " inner join e_shelf x on m.mis_id = x.shelf_id where n.layer_name ='模块') d on c.frame_id = d.mis_id " +
                    " where b.map_id = ?";
            Connection conn1 = DB_OLD_TEST.getConn();
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            
            String sql = "INSERT INTO mapinfo VALUES (null, ?, ?, ?, ?, ?, ?);";
            Connection conn = DB_LOCAL.getConn();
            System.out.println(m.size()/2+1);
            for(int i=1; i<m.size()/2+1; i++) {
            	System.out.println(i);
            	if(!(m.get("a"+i).split(",")[2].equals("326768") || m.get("a"+i).split(",")[2].equals("326779")) ) {
    				continue;
    			}
        		try {
        			//conn1 = DB_OLD_TEST.getConn();
        			ps1 = conn1.prepareStatement(sql1);
        			ps1.setString(1, m.get("a"+i).split(",")[2]);
        			rs1 = ps1.executeQuery();
        			String shelfName = null;
        			if(rs1.next()) {
        				shelfName = rs1.getString("shelf_name");
        			}
        			PreparedStatement ps = conn.prepareStatement(sql);
        			ps.setString(1, m.get("a"+i).split(",")[0]);
        			ps.setString(4, m.get("a"+i).split(",")[1]);
        			ps.setString(5, shelfName);
        			ps.setString(6, m.get("a"+i).split(",")[2]);
        			if(m.get("b"+i) != null){
        				ps.setString(2, m.get("b"+i).split(",")[0]);
        				ps.setString(3, m.get("b"+i).split(",")[1]);
        			} else {
        				ps.setString(2, null);
        				ps.setString(3, null);
        			}
        			
        			int rs = ps.executeUpdate();
        			//System.out.println(rs);
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} finally {
        			try {
        				rs1.close();
						ps1.close();
						//conn1.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br_mif != null) {
                try {
                    br_mif.close();
                } catch (IOException e1) {
                }
            }
        }
	}
	
	                                     
	public static void main(String[] args) {
		analyzeMapInfo();
	}
}
