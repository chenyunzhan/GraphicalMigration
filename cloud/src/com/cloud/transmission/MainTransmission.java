package com.cloud.transmission;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import com.cloud.util.DB_OLD_FORMAL;
import com.cloud.util.DB_RM_RDM_SID_LU_H;
import com.cloud.util.DB_RM_TPS_ADB;

public class MainTransmission {
	public static void main(String[] args) {
		Connection conn = DB_OLD_FORMAL.getConn();
		Connection conn1 = DB_RM_TPS_ADB.getConn();
		Connection conn2 = DB_RM_RDM_SID_LU_H.getConn();
		List<String> list = BatchTransmission.batchUpdateTransmission();
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String temp = (String) iterator.next();
			TransPoint.getPoint(Integer.parseInt(temp.split("#")[0]), temp.split("#")[1], conn, conn1, conn2);
		}
	}
}
