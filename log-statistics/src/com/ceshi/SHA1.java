package com.ceshi;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class SHA1 {
	
	
 private static String getSha1(String str){
	 
	 if(str==null||str.length()==0){
         return null;
     }
     char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
             'a','b','c','d','e','f'};
     try {
         MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
         mdTemp.update(str.getBytes("UTF-8"));

         byte[] md = mdTemp.digest();
         int j = md.length;
         char buf[] = new char[j*2];
         int k = 0;
         for (int i = 0; i < j; i++) {
             byte byte0 = md[i];
             buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
             buf[k++] = hexDigits[byte0 & 0xf];      
         }
         return new String(buf);
     } catch (Exception e) {
         // TODO: handle exception
         return null;
     }
 }

 public static void main(String[] args) {
	 	
//	   MD5
	   String str = "abc";
	   String a=DigestUtils.md5Hex(str);
	   System.out.println(a);
//	   SHA1
	   String str1 = "abc";
	   String a1=DigestUtils.shaHex(str1);
	   System.out.println(a1);
//	   BASE64
	   //加密
	   String str2= "abc"; // abc为要加密的字符串
	   byte[] b = Base64.encodeBase64(str2.getBytes(), false);
	   System.out.println("---------------");
	   System.out.println(new String(b));
	   System.out.println("---------------");
	   //解密
	   String str3 = "YWJj"; // YWJj为要解密的字符串
	   byte[] b1 = Base64.decodeBase64(b);
	   System.out.println(new String(b1));
	   System.out.println(new String(b1).length());
	   System.out.println("---------------");
}
 

}
