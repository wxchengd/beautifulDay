package travelsky.log.logcatch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class HXUtil_ratelimitcount {
	
	//写到一个文件，不拆分
	public static Map<String,LogEntity_ratelimitcount>  readFile_merge(File[] files){
		BufferedReader reader = null;
		Long start = System.currentTimeMillis();
		Map<String,LogEntity_ratelimitcount> map = new HashMap<String,LogEntity_ratelimitcount>();
		System.out.println("-开始读取数据-");
		try {
			LogEntity_ratelimitcount log;
			for(File file : files){
				if(file.getName().endsWith(".log.gz")){
					reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))));
				}else if(file.getName().endsWith(".log")){
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
				}else{
					System.out.println("-----文件格式错误----");
					return null;
				}
				
				int count =1;
				while(true){
					String line = reader.readLine();
					if(line == null){
						break;
					}
					if(line.contains("upstream-addr")){
						String appkey = line.substring(line.indexOf("app-key:\""), line.indexOf("\",office")).split(":\"")[1];
//						
						String status = line.substring(line.indexOf("status :\"")).replaceAll("\"", "").split(":")[1];
						if(map.get(appkey) != null){
							String ratelimitcount = line.substring(line.indexOf("current-connection-number:\""), line.indexOf("\",certificate-type")).split(":\"")[1];
							int currentcount = Integer.parseInt(ratelimitcount);
							map.get(appkey).setCount(map.get(appkey).getCount()<currentcount?currentcount:map.get(appkey).getCount());
							System.out.println(map.get(appkey).getCount()+":"+currentcount);
						}else{
							log = new LogEntity_ratelimitcount();
							log.setRequestDate(line.substring(0, 11));
							log.setAppkey(appkey);
							log.setCount(1);
							map.put(appkey,log);
						}
			}
				}
			}
			System.out.println("读取日志花费--"+(System.currentTimeMillis()-start));
			reader.close();
//			for(Entry<String, LogEntity_count> entry : map.entrySet()){
//				System.out.println(entry.getValue().toString());
//			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static void handLogs(Map<String,LogEntity_ratelimitcount> map,File file){
		Long start = System.currentTimeMillis();
		String fp = file.getName().substring(0,10);
		File ff = new File(file.getParent().concat("/").concat("ratelimitcount"),fp);
		createCSVFile(map,ff);
		System.out.println("读取数据完毕");
		Long end = System.currentTimeMillis();
		System.out.println("读取数据用时，用时"+(end-start)+"ms");
	
	}


	//cvs生成报表无法修改样式
	private static void createCSVFile(Map<String,LogEntity_ratelimitcount> map,File file){
	  try{
		LinkedHashMap<String,String> mapk = new LinkedHashMap<String,String>();
		mapk.put("1", "访问时间");
		mapk.put("2", "appkey名称");
		mapk.put("3", "当前最大并发数");
		 List<Map<String,String>> exportData = new ArrayList<Map<String,String>>();
		//调用DecimalFormat类  ,保留两位小数(.00)/取整五舍六入(0)
		 DecimalFormat df = new DecimalFormat("0"); 
		 for(String key : map.keySet()){
			 Map<String, String> row = new LinkedHashMap<String, String>();
			 row.put("1", map.get(key).getRequestDate());
			 row.put("2", map.get(key).getAppkey());
			 row.put("3", String.valueOf(map.get(key).getCount()));
			 exportData.add(row);
		 }
		 createCSVFile(exportData, mapk, file.getParent(), file.getName());
	  } catch (Exception e) {
		  e.printStackTrace();
	}
	}
	
	
	/**
	   * 生成为CVS文件 
	   * @param exportData
	   *       源数据List
	   * @param map
	   *       csv文件的列表头map
	   * @param outPutPath
	   *       文件路径
	   * @param fileName
	   *       文件名称
	   * @return
	   */
	  @SuppressWarnings("rawtypes")
	  public static File createCSVFile(List exportData, LinkedHashMap map, String outPutPath,String fileName) {
	    File csvFile = null;
	    BufferedWriter csvFileOutputStream = null;
	    try {
	      File file = new File(outPutPath);
	      if (!file.exists()) {
	        file.mkdir();
	      }
	      //定义文件名格式并创建
	      csvFile = new File(outPutPath+"/"+fileName+".csv");
//	      if(csvFile.exists()){
//	    	  
//	      }
	      System.out.println("csvFile：" + csvFile);
	      // UTF-8使正确读取分隔符"," 
	      csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "gbk"), 1024);
	      System.out.println("csvFileOutputStream：" + csvFileOutputStream);
	      // 写入文件头部 
	      for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {
	        java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
	        csvFileOutputStream.write("" + (String) propertyEntry.getValue() != null ? (String) propertyEntry.getValue() : "" + "");
	        if (propertyIterator.hasNext()) {
	          csvFileOutputStream.write(",");
	        }
	      }
	      csvFileOutputStream.newLine();//换行符
	      // 写入文件内容 
	      for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {
		        Object row = (Object) iterator.next();
		        for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {
		        	java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
		        	csvFileOutputStream.write((String) BeanUtils.getProperty(row,(String) propertyEntry.getKey()));
		        	if(propertyIterator.hasNext()) {
		        	  csvFileOutputStream.write(",");
		        	}
		        }
		        if (iterator.hasNext()) {
//		            csvFileOutputStream.newLine();
		            csvFileOutputStream.write("\n");//linux服务上用这个
		        }
	      }
//	      csvFileOutputStream.newLine();
//	      csvFileOutputStream.write("\n");
	      csvFileOutputStream.flush();
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      try {
	        csvFileOutputStream.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    }
	    return csvFile;
	  }
	  
}
