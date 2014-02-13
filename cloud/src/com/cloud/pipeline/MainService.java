package com.cloud.pipeline;

import java.util.Iterator;
import java.util.List;

public class MainService {
	public static void main(String[] args) {
		NewService ns = new NewService();
		TransformService ts = new TransformService();
		List<PipeLine> list = ns.getPipeLines();
		for (Iterator<PipeLine> iterator = list.iterator(); iterator.hasNext();) {
			PipeLine pl = (PipeLine) iterator.next();
			ts.getPoint(Integer.parseInt(pl.getTopoId()), pl.getName());
		}
	}
}
