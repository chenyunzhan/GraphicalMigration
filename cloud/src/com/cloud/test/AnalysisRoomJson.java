package com.cloud.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class AnalysisRoomJson {
	public static void main(String[] args) {
		getRoomJson();
	}
	
	
	public static List<String> getRoomJson() {
		File file_mif = new File("E:\\mycode\\cloud\\src\\ROOM.json");

		BufferedReader br_mif = null;
		List<String> list = new ArrayList<String>();

		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			br_mif = new BufferedReader(new FileReader(file_mif));
			String tempString_mif = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString_mif = br_mif.readLine()) != null) {
				System.out.println(tempString_mif);
				if(!tempString_mif.equals("##############################################################################################")){
					list.add(tempString_mif);
				}
			}
			
		} catch (Exception e) {

		}
		return list;
	}
}
