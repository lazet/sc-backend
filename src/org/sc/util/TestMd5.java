package org.sc.util;

public class TestMd5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(EncryptUtil.encodeWithMD5("order{\"创建时间\":-1}"));
	}

}
