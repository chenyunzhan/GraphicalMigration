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
			// System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			br_mif = new BufferedReader(new FileReader(file_mif));
			String tempString_mif = null;
			int line = 1;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
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
