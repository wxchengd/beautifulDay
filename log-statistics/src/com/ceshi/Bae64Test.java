//package com.ceshi;
//
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//
//
//
//
//public class Bae64Test {
//	
//	public static String  Base64(String Logon,String password){
//		
//		password = new String(MD5.GetMD5Code(password));
//		System.out.println(password);
//		long time = System.currentTimeMillis() + 300000;
//		String logonName = Logon; 
//		String data = logonName + ":" + time + ":" + password + ":" + "capinfo-ecommunity-app-remember-me";
//		System.out.println(data);
//		String sign = MD5.GetMD5Code(data);
//		System.out.println(sign);
//		String base = logonName + ":" + time + ":" + sign;
//		System.out.println(base);
//		byte[] barr = base.getBytes();
////		sign = new String(Base64.encodeBase64(barr));
//		
//		return sign;
//	}
//}