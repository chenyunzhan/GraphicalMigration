package com.cloud.transmission;

import java.util.Iterator;
import java.util.List;

public class MainTransmission {
	public static void main(String[] args) {
		List<String> list = BatchTransmission.batchUpdateTransmission();
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String temp = (String) iterator.next();
			TransPoint.getPoint(Integer.parseInt(temp.split("#")[0]), temp.split("#")[1]);
		}
	}
}
