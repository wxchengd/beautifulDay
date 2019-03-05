package travelsky.log.logcatch;

import java.text.DecimalFormat;

public class test {
	
	
	public static void main(String[] args) {
		String ss="11.425";
		System.out.println("-------------");
		System.out.println(0.92);
		System.out.println(Double.parseDouble(ss)*100);
		//调用DecimalFormat类  ,保留两位小数
		DecimalFormat df = new DecimalFormat("0"); 
		System.out.println(df.format(Double.parseDouble(ss)*100));;
		System.out.println((int)(Double.parseDouble(ss)*100));;
		
		//Java 生成指定长度的String
		StringBuffer sb = new StringBuffer();
		System.out.println(sb.length());
		for(int i=0;i<5;i++){
		   sb.append("a");
		}
		System.out.println(sb.toString());
		System.out.println(sb.length());
		
		String str = "2019/01/10 00:00:02 [06/Nov/2018:11:17:26 +0800] client_ip:172.22.55.171,status:200,request:POST /jbossecho/v1.0 HTTP/1.1,sat_transaction_id:XXXXXSAT862018110611172500003616,total_elapse_time:0.856,upstream_status:200,jboss_address:10.221.196.24:8180,jboss_elapse_time:0";
//		String id = str.substring(str.indexOf("sat_transaction_id:"),str.indexOf(",total_elapse_time")).split(":")[1];
	    String service_name = str.substring(str.indexOf("request:POST /"), str.indexOf(" HTTP/1.1")).split(" /")[1];
		System.out.println(service_name);
		System.out.println(str.substring(str.indexOf("jboss_elapse_time")).split(":")[1]);
	}

}
