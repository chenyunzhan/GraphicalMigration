package com.cloud.rack;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import com.cloud.util.DB_OLD_FORMAL;
import com.cloud.util.DB_RM_RDM_SID_LU_H;

public class MainService {
	
	public static void main(String args[]) {
		NewService ns = new NewService();
		List<String> list = ns.getRacks();
		Connection conn1 = DB_OLD_FORMAL.getConn();
		Connection conn2 = DB_RM_RDM_SID_LU_H.getConn();
		System.out.println(list.toString());
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String temp = (String) iterator.next();
			TransformService ts = new TransformService();
			ts.transform(1111, temp, conn1, conn2);
		}
	}
	
}
