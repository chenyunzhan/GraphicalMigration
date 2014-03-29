package com.cloud.test;

public class TestArray {
	public static void main(String[] args) {
		int[][] arr = new int[100][100];
		int count1 = 0;
		int count2 = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length; j++) {
				count1 ++;
				System.out.print(i+","+j+"  ");
				if(count1>600){
					break;
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length; j++) {  
				count2 ++;
				System.out.print(j+","+i+"  ");
				if(count2>600){
					break;
				}
			}
			System.out.println();
		}
	}
}
