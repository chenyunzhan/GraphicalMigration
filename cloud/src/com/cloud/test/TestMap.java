package com.cloud.test;

import java.util.HashMap;
import java.util.Map;

public class TestMap {
	public static void main(String[] args) {
		Map m = new HashMap();
		m.put("", "�½���");
		Object o = m.get("");
		System.out.println(o);
		
		
		Object a = null;
		System.out.println(a+"");
	}
}
