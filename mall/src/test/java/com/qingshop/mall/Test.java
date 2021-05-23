package com.qingshop.mall;

public class Test {

	public static void main(String[] args) {
		String str1 = "通话";
		String str2 = "重地";
		String v1 = "1.0";
		System.out.println(String.format("str1:%d | str2:%d", str1.hashCode(), str2.hashCode()));
		System.out.println(str1.equals(str2));
	}

	public static int halfSearch(int[] array, int searchKey) {
		int min, max, mid;
		min = 0;
		max = array.length;
		mid = (min + max) / 2;
		while (array[mid] != searchKey) {
			if (searchKey > array[mid]) {
				min = mid + 1;
			} else if (searchKey < array[mid]) {
				max = mid - 1;
			}
			if (max < min) {
				return -1;
			}
			mid = (min + max) / 2;
		}
		return mid;
	}
}
