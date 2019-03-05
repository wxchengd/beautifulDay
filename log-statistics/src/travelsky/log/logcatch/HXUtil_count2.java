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

public class HXUtil_count2 {
	
	//写到一个文件，不拆分
	public static List<LogEntity_count>  readFile_merge(File[] files){
		BufferedReader reader = null;
		Long start = System.currentTimeMillis();
		List<LogEntity_count> list = new ArrayList<LogEntity_count>();
		System.out.println("-开始读取数据-");
		try {
			String fp = files[0].getName().substring(0,10);
			File ff = new File(files[0].getParent().concat("/").concat("data_comparison"),fp);
			LinkedHashMap<String,String> mapk = new LinkedHashMap<String,String>();
			mapk.put("1", "transaction-id:");
			mapk.put("2", "status");
			File csvFile = null;
		    BufferedWriter csvFileOutputStream = null;
	        File File = new File(ff.getParent());
	        if (!File.exists()) {
	          File.mkdir();
	        }
	        //定义文件名格式并创建
	        csvFile = new File(ff.getParent()+"/"+ff.getName()+".csv");
	        System.out.println("csvFile：" + csvFile);
	     // UTF-8使正确读取分隔符"," 
		      csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "gbk"), 1024);
		      System.out.println("csvFileOutputStream：" + csvFileOutputStream);
		      // 写入文件头部 
		      for (Iterator propertyIterator = mapk.entrySet().iterator(); propertyIterator.hasNext();) {
		        java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
		        csvFileOutputStream.write("" + (String) propertyEntry.getValue() != null ? (String) propertyEntry.getValue() : "" + "");
		        if (propertyIterator.hasNext()) {
		          csvFileOutputStream.write(",");
		        }
		      }
		      csvFileOutputStream.newLine();//换行符
	        
//			LogEntity_count log;
//			Map<String,Integer> maps;
		    int  count = 1 ;
			for(File file : files){
				if(file.getName().endsWith(".log.gz")){
					reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))));
				}else if(file.getName().endsWith(".log")){
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
				}else{
					System.out.println("-----文件格式错误----");
					return null;
				}
				
				while(true){
					String line = reader.readLine();
					if(line == null){
						break;
					}
					if(line.contains("upstream-addr")){
						String apiEntry = line.substring(line.indexOf("api-entity:\""), line.indexOf("\",api-version")).split(":\"")[1];
						if(apiEntry.equals("travelsky.air.fare.DomesticFlightShopping")){
							String user = line.substring(line.indexOf("user:\""), line.indexOf("\",current-delay-time")).split(":\"")[1];
							String transactionId = line.substring(line.indexOf("transaction-id:\""), line.indexOf("\",nginx-ip")).split(":\"")[1];
							String status = line.substring(line.indexOf("status :\"")).replaceAll("\"", "").split(":")[1];
//							log = new LogEntity_count();
//							log.setRequestDate(line.substring(0, 11));
//							log.setApiEntity(apiEntry);
//							log.setStatus(status);
//							log.setTransactionId(transactionId);
//							list.add(log);
				        	csvFileOutputStream.write(transactionId);
				        	csvFileOutputStream.write(",");
				        	csvFileOutputStream.write(status);
				        	csvFileOutputStream.write("\n");//linux服务上用这个
							System.out.println(count++);
						}
					}
				}
			}
			System.out.println("读取日志花费--"+(System.currentTimeMillis()-start));
			reader.close();
			csvFileOutputStream.flush();
			csvFileOutputStream.close();
//			for(Entry<String, LogEntity_count> entry : map.entrySet()){
//				System.out.println(entry.getValue().toString());
//			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void handLogs(List<LogEntity_count> list,File file){
		Long start = System.currentTimeMillis();
		String fp = file.getName().substring(0,10);
		File ff = new File(file.getParent().concat("/").concat(fp),fp);
		createCSVFile(list,ff);
		System.out.println("读取数据完毕");
		Long end = System.currentTimeMillis();
		System.out.println("读取数据用时，用时"+(end-start)+"ms");
	
	}

	
	//参见 https://www.cnblogs.com/biehongli/p/6497653.html
	
	//poi导出报表  超过65536会报异常  XSS替换HSS 
	//解决方法参见  https://blog.csdn.net/dufufd/article/details/69945942
	//数据多的话，会内存溢出

	//cvs生成报表无法修改样式
	private static void createCSVFile(List<LogEntity_count> list,File file){
	  try{
		LinkedHashMap<String,String> mapk = new LinkedHashMap<String,String>();
		mapk.put("1", "访问时间");
		mapk.put("2", "接口名称");
		mapk.put("3", "transaction-id:");
		mapk.put("4", "用户名称");
		mapk.put("5", "status");
		 List<Map<String,String>> exportData = new ArrayList<Map<String,String>>();
		//调用DecimalFormat类  ,保留两位小数(.00)/取整五舍六入(0)
		 DecimalFormat df = new DecimalFormat("0"); 
		 for(LogEntity_count entity : list){
			 Map<String, String> row = new LinkedHashMap<String, String>();
			 row.put("1", entity.getRequestDate());
			 row.put("2", entity.getApiEntity());
			 row.put("3", entity.getTransactionId());
			 row.put("4", entity.getUser());
			 row.put("5", entity.getStatus());
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
	      csvFileOutputStream.write(",");
	      csvFileOutputStream.write("该接口调用jboss总次数为:" + exportData.size() );
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
	  
	  /**
	   * 下载文件
	   * @param response
	   * @param csvFilePath
	   *       文件路径
	   * @param fileName
	   *       文件名称
	   * @throws IOException
	   */
	  public static void exportFile(HttpServletResponse response, String csvFilePath, String fileName)
	                                                  throws IOException {
	    response.setContentType("application/csv;charset=UTF-8");
	    response.setHeader("Content-Disposition",
	      "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
	 
	    InputStream in = null;
	    try {
	      in = new FileInputStream(csvFilePath);
	      int len = 0;
	      byte[] buffer = new byte[1024];
	      response.setCharacterEncoding("UTF-8");
	      OutputStream out = response.getOutputStream();
	      while ((len = in.read(buffer)) > 0) {
	        out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
	        out.write(buffer, 0, len);
	      }
	    } catch (FileNotFoundException e) {
	      System.out.println(e);
	    } finally {
	      if (in != null) {
	        try {
	          in.close();
	        } catch (Exception e) {
	          throw new RuntimeException(e);
	        }
	      }
	    }
	  }	  
}
