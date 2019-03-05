//package com.ceshi;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Date;
//
//
//import com.feinno.util.DateUtil;
///*
// * MD5 算法
//*/
//public class MD5{
//    
//    // 全局数组
//    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
//            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
//
//    public MD5() {
//    }
//
//    // 返回形式为数字跟字符串
//    private static String byteToArrayString(byte bByte) {
//        int iRet = bByte;
//        // System.out.println("iRet="+iRet);
//        if (iRet < 0) {
//            iRet += 256;
//        }
//        int iD1 = iRet / 16;
//        int iD2 = iRet % 16;
//        return strDigits[iD1] + strDigits[iD2];
//    }
//
//    // 返回形式只为数字
//    private static String byteToNum(byte bByte) {
//        int iRet = bByte;
//        System.out.println("iRet1=" + iRet);
//        if (iRet < 0) {
//            iRet += 256;
//        }
//        return String.valueOf(iRet);
//    }
//
//    // 转换字节数组为16进制字串
//    private static String byteToString(byte[] bByte) {
//        StringBuffer sBuffer = new StringBuffer();
//        for (int i = 0; i < bByte.length; i++) {
//            sBuffer.append(byteToArrayString(bByte[i]));
//        }
//        return sBuffer.toString();
//    }
//
//    public static String GetMD5Code(String strObj) {
//        String resultString = null;
//        try {
//            resultString = new String(strObj);
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            // md.digest() 该函数返回值为存放哈希值结果的byte数组
//            resultString = byteToString(md.digest(strObj.getBytes()));
//        } catch (NoSuchAlgorithmException ex) {
//            ex.printStackTrace();
//        }
//        return resultString;
//    }
//    /**
//     * 根据号码和当前时间生成MD5码
//     * @param mibileno
//     * @return
//     */
//    public static String GetMD5Token(Long mobileno){
//    	String Mdmobile= DateUtil.formatDate(new Date(), "yyyy MM dd HH mm ss")+mobileno;
//    			
//    	return GetMD5Code(Mdmobile);
//    	
//    }
//    
//    
//    /**
//     * 根据username和当前时间生成MD5码
//     * @param mibileno
//     * @return
//     */
//    public static String GetMD5SessionId(String username){
//    	String Username= DateUtil.formatDate(new Date(), "yyyy MM dd HH mm ss")+username;
//    			
//    	return GetMD5Code(Username);
//    }
//    
//
//    public static void main(String[] args) {
//    	MD5 getMD5 = new MD5();
//        System.out.println(getMD5.GetMD5Token(15110263099L));
//        Long a=0L;
//       System.out.println( a.toString());
//        
////       UserInfoDao dao =new UserInfoDao();
////          dao.setRedisToken(15110263099L, "1234");   
////          System.out.println(dao.getRedisToken(15110263099L));
////      System.out.println( dao.delRedisToken(15110263099L));
////      System.out.println(dao.getRedisToken(15110263099L));
//      
//    }
//    //ff6945b4ae1709d319124740a4ee62da
//    //777125b977ddc0317d0533782d3c27b5
//   // e85620f7b3c53d91b5193cc2f110b7fc
//}