package com.cloud.room;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.cloud.util.DB_LOCAL;

public class AnalysisMapInfo {
	public static void analyzeMapInfo() {
		File file_mif = new File("E:\\mycode\\cloud\\src\\com\\cloud\\room\\MI_SHELF_PLANFORM.MIF");
		File file_mid = new File("E:\\mycode\\cloud\\src\\com\\cloud\\room\\MI_SHELF_PLANFORM.MID");
		
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
            	//System.out.println(tempString_mid);
            	//m.put("a"+line,tempString_mid.split(",")[3] + "类型:" + tempString_mid.split(",")[2]);
            	m.put("a"+line, tempString_mid.split(",")[3] + "," + tempString_mid.split(",")[6]+ "," + tempString_mid.split(",")[8]+ "," + tempString_mid.split(",")[1]+ "," + tempString_mid.split(",")[2]);
            	line++;
            }
            line = 1;
            /**
             * 
            while ((tempString_mif = br_mif.readLine()) != null) {
            	if(tempString_mif.trim().equals("1")) {
            		System.out.println("####################################");
            		System.out.println(br_mif.readLine());
            		System.out.println(br_mif.readLine());
            		System.out.println(br_mif.readLine());
            	}
            }
             */
            while ((tempString_mif = br_mif.readLine()) != null) {
                // 显示行号
            	if(tempString_mif.length() > 6 && tempString_mif.substring(0, 6).equals("Region")) {
            		/**
            		 * 
            		 */
            		br_mif.readLine();
            		String minxy = br_mif.readLine();
            		br_mif.readLine();
            		String maxxy = br_mif.readLine();
            		m.put("b"+line, (Double.parseDouble(minxy.split(" ")[0])) + "," + (Double.parseDouble(minxy.split(" ")[1])) + "," + (Double.parseDouble(maxxy.split(" ")[0])) + ',' + (Double.parseDouble(maxxy.split(" ")[1])));
            		line ++;
            		//tempString_mif = br_mif.readLine();
            		//tempString_mif = br_mif.readLine();
            		//System.out.println(tempString_mif);
            		//机架、机框
            		//m.put("b"+line, "POS_X:" + (Double.parseDouble(tempString_mif.split(" ")[0])+400) + ",POS_Y:" + (Double.parseDouble(tempString_mif.split(" ")[1])+4000));
            		//m.put("b"+line, "POS_X:" + (Double.parseDouble(tempString_mif.split(" ")[0])) + ",POS_Y:" + (Double.parseDouble(tempString_mif.split(" ")[1])));
            	} else if(tempString_mif.length() > 5 && tempString_mif.substring(0, 5).equals("Pline")){
            		m.put("b"+line, null);
            		line ++;
            	} else if(tempString_mif.length() > 4 && tempString_mif.substring(0, 4).equals("Line")) {
            		//String temp  = tempString_mif.substring(5, tempString_mif.length());
            		//m.put("b"+line, Double.parseDouble(temp.split(" ")[0]) + "," + Double.parseDouble(temp.split(" ")[1]) + "," + Double.parseDouble(temp.split(" ")[2]) + "," + Double.parseDouble(temp.split(" ")[3]) +"");
            		m.put("b"+line, null);
            		line ++;
            	}
            }
            br_mid.close();
            br_mif.close();
            
            
            for(int i=1; i<m.size()/2+1; i++) {
            	System.out.println(m.get("a"+i) + "," + m.get("b"+i));
            }
            
            /**
             * 
             */
            String sql = "INSERT INTO room_mapinfo VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            Connection conn = DB_LOCAL.getConn();
            for(int i=1; i<m.size()/2+1; i++) {
        		try {
        			PreparedStatement ps = conn.prepareStatement(sql);
        			ps.setString(1, m.get("a"+i).split(",")[0]);
        			ps.setString(2, m.get("a"+i).split(",")[1]);
        			ps.setString(3, m.get("a"+i).split(",")[2]);
        			ps.setString(4, m.get("a"+i).split(",")[4]);
        			ps.setString(5, m.get("a"+i).split(",")[3]);
        			if(m.get("b"+i) != null){
        				ps.setString(6, m.get("b"+i).split(",")[0]);
        				ps.setString(7, m.get("b"+i).split(",")[1]);
        				ps.setString(8, m.get("b"+i).split(",")[2]);
        				ps.setString(9, m.get("b"+i).split(",")[3]);
        			} else {
        				ps.setString(6, null);
        				ps.setString(7, null);
        				ps.setString(8, null);
        				ps.setString(9, null);
        			}
        			
        			int rs = ps.executeUpdate();
        			//System.out.println(rs);
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
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
