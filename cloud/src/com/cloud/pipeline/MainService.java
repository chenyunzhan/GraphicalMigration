package com.cloud.pipeline;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import com.cloud.util.DB_OLD_CITY;
import com.cloud.util.DB_OLD_FORMAL;
import com.cloud.util.DB_RM_RDM_SID_LU_H;
import com.cloud.util.DB_RM_TPS_ADB;

public class MainService {
	public static void main(String[] args) {
		//zibolianjie
		Connection conn = DB_OLD_CITY.getConn("jdbc:oracle:thin:@134.35.17.201:1521:zygl", "map", "map");
		Connection conn1 = DB_RM_TPS_ADB.getConn();
		Connection conn2 = DB_RM_RDM_SID_LU_H.getConn();
		NewService ns = new NewService();
		TransformService ts = new TransformService();
		List<PipeLine> list = ns.getPipeLines();
		System.out.println(list.toString());

		for (Iterator<PipeLine> iterator = list.iterator(); iterator.hasNext();) {
			PipeLine pl = (PipeLine) iterator.next();
			ts.getPoint(Integer.parseInt(pl.getTopoId()), pl.getName(),  conn,  conn1,  conn2);
		}
	}
}
