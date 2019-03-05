package travelsky.log.logcatch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class TestRead {
	
	
	public static void main(String[] args) {    
        try {   
        	
        	//对比两个csv中的数据：
        	File csv = new File("F:/opt/applog/data_comparison/travelsky.air.fare.DomesticFlightShopping.20190117.CallTime.SATIBE.csv");  // CSV文件路径
        	File csv1 = new File("F:/opt/applog/data_comparison/2019-01-17.csv");  // CSV文件路径
        	BufferedReader reader = new BufferedReader(new FileReader(csv));//换成你的文件名   
            reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉   
            String line = null;
            Map<String,Integer> map = new HashMap<String,Integer>();
            while((line=reader.readLine())!=null){    
                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分   
//                String last = item[item.length-1];
                String tid = item[1];//这就是你要的数据了   
                //int value = Integer.parseInt(last);//如果是数值，可以转化为数值   
//                System.out.println(tid); 
                map.put(tid, 1);
//                map.remove(tid);
            }    
            reader.close();
            System.out.println("开始读取第二个");
            Map<String,Integer> map1 = new HashMap<String,Integer>();
            BufferedReader reader1 = new BufferedReader(new FileReader(csv1));//换成你的文件名   
            reader1.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉   
            String line1 = null;
            while((line1=reader1.readLine())!=null){
                String item[] = line1.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分   
                String tid = item[0];//这就是你要的数据了   
                if(map.get(tid) != null){
                	map.remove(tid);
                }else{
                	map1.put(tid, 0);
//                	System.out.println(tid);
                }
                //int value = Integer.parseInt(last);//如果是数值，可以转化为数值   
            }   
              reader1.close();
              BufferedWriter csvFileOutputStream = null;
              File csvFile1 = new File("F:/opt/applog/data_comparison/JbossData.csv");
              File csvFile = new File("F:/opt/applog/data_comparison/nginxData.csv");
              csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "gbk"), 1024);
		      System.out.println("csvFileOutputStream：" + csvFileOutputStream);
		      csvFileOutputStream.write("tid");
		      csvFileOutputStream.newLine();//换行符
		      for(String tid :map1.keySet()){
		    	  csvFileOutputStream.write(tid);
		    	  csvFileOutputStream.write("\n");//linux服务上用这个
		      }
        	  csvFileOutputStream.flush();
  			  csvFileOutputStream.close();
  			 for(String tid :map.keySet()){
  				 System.out.println("jboss多出的数据:"+tid);
		      }
  			 System.out.println(map.size());
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
    }    

}
