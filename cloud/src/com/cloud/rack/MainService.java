package com.cloud.rack;

import java.util.Iterator;
import java.util.List;

public class MainService {
	
	public static void main() {
		NewService ns = new NewService();
		TransformService ts = new TransformService();
		List<String> list = ns.getRacks();
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String temp = (String) iterator.next();
			ts.transform(1111, temp);
		}
	}
	
}
