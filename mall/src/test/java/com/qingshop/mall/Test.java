package com.qingshop.mall;

public class Test {

	public static void main(String[] args) {
		String str1 = "通话";
		String str2 = "重地";
		System.out.println(String.format("str1:%d | str2:%d", str1.hashCode(), str2.hashCode()));
		System.out.println(str1.equals(str2));
	}

	public static int halfSearch(int[] arr, int target) {
		int min, mid, max;
		min = 0;
		max = arr.length - 1;
		mid = (min + max) / 2;
		while (arr[mid] != target) {
			if (arr[mid] > target) {
				min = mid + 1;
			}
			if (arr[mid] < target) {
				max = mid - 1;
			}
			mid = (mid + max) / 2;
		}
		return mid;
	}
}
