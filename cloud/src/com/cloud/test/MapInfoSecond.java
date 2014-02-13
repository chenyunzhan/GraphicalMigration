package com.cloud.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapInfoSecond {
	public static void analyzeMapInfo() {
		File file_mif = new File("E:\\mycode\\cloud\\src\\E_MI_SHELF_PANEL.MIF");
		File file_mid = new File("E:\\mycode\\cloud\\src\\E_MI_SHELF_PANEL.MID");
		
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
            	m.put("a"+line, tempString_mid.split(",")[2]);
            	line++;
            }
            line = 1;
            while ((tempString_mif = br_mif.readLine()) != null) {
                // 显示行号
            	if(tempString_mif.trim().equals("5")) {
            		tempString_mif = br_mif.readLine();
            		tempString_mif = (Double.parseDouble(tempString_mif.split(" ")[0])+3300) + "," + -(Double.parseDouble(tempString_mif.split(" ")[1])-1800);
            		System.out.println(tempString_mif);
            		tempString_mif = br_mif.readLine();
            		tempString_mif = (Double.parseDouble(tempString_mif.split(" ")[0])+3300) + "," + -(Double.parseDouble(tempString_mif.split(" ")[1])-1800);
            		System.out.println(tempString_mif);
            		tempString_mif = br_mif.readLine();
            		tempString_mif = (Double.parseDouble(tempString_mif.split(" ")[0])+3300) + "," + -(Double.parseDouble(tempString_mif.split(" ")[1])-1800);
            		System.out.println(tempString_mif);
            		tempString_mif = br_mif.readLine();
            		tempString_mif = (Double.parseDouble(tempString_mif.split(" ")[0])+3300) + "," + -(Double.parseDouble(tempString_mif.split(" ")[1])-1800);
            		System.out.println(tempString_mif);
            		System.out.println("##############################################");
            		//机架、机框
            		//m.put("b"+line, "POS_X:" + (Double.parseDouble(tempString_mif.split(" ")[0])+400) + ",POS_Y:" + (Double.parseDouble(tempString_mif.split(" ")[1])+4000));
            		//m.put("b"+line, "POS_X:" + (Double.parseDouble(tempString_mif.split(" ")[0])) + ",POS_Y:" + (Double.parseDouble(tempString_mif.split(" ")[1])));
            		//m.put("b"+line, (Double.parseDouble(tempString_mif.split(" ")[0])) + "," + (Double.parseDouble(tempString_mif.split(" ")[1])));
            		line ++;
            	} else if(tempString_mif.trim().equals("Pline 5")){
            		m.put("b"+line, null);
            		line ++;
            	}
            	
            }
            br_mid.close();
            br_mif.close();
            
            for(int i=1; i<m.size()/2+1; i++) {
            	//System.out.println(m.get("a"+i) + "," + m.get("b"+i));
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
