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

public class HXUtil_count {
	
	//写到一个文件，不拆分
	public static Map<String,LogEntity_count>  readFile_merge(File[] files){
		BufferedReader reader = null;
		Long start = System.currentTimeMillis();
		Map<String,LogEntity_count> map = new HashMap<String,LogEntity_count>();
		System.out.println("-开始读取数据-");
		try {
			LogEntity_count log;
			Map<String,Integer> maps;
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
						String apiEntry = line.substring(line.indexOf("api-entity:\""), line.indexOf("\",api-version")).split(":\"")[1];
						String user = line.substring(line.indexOf("user:\""), line.indexOf("\",current-delay-time")).split(":\"")[1];
//						String transactionId = line.substring(line.indexOf("transaction-id:\""), line.indexOf("\",nginx-ip")).split(":\"")[1];
//						String status = line.substring(line.indexOf("status :\"")).replaceAll("\"", "").split(":")[1];
						if(map.get(apiEntry) != null){
						    maps = map.get(apiEntry).getMap();
							if(maps.get(user) != null){
								maps.put(user, maps.get(user)+1);
								map.get(apiEntry).setMap(maps);
							}else{
								maps.put(user,1);
								map.get(apiEntry).setMap(maps);
							}
							map.get(apiEntry).setCount(map.get(apiEntry).getCount()+1);
						}else{
							log = new LogEntity_count();
							maps = new HashMap<String, Integer>();
							log.setRequestDate(line.substring(0, 11));
							log.setApiEntity(apiEntry);
//							log.setStatus(status);
//							log.setTransactionId(transactionId);
							maps.put(user, 1);
							log.setMap(maps);
							log.setCount(1);
							map.put(apiEntry,log);
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
	
	public static void handLogs(Map<String,LogEntity_count> map,File file){
		Long start = System.currentTimeMillis();
		String fp = file.getName().substring(0,10);
		File ff = new File(file.getParent().concat("/").concat("user_traffic"),fp);
		createCSVFile(map,ff);
		System.out.println("读取数据完毕");
		Long end = System.currentTimeMillis();
		System.out.println("读取数据用时，用时"+(end-start)+"ms");
	
	}

	
	//参见 https://www.cnblogs.com/biehongli/p/6497653.html
	private static void createEXCELByJxl(List<LogEntity> list,File file){
	 	 try {
	 		if(!file.getParentFile().exists()){
		 		file.mkdirs();
		 	}
	 		System.out.println(file.getParentFile().getName());
	 		//1:创建excel文件
	 		String fileName = file.getParent().concat("/").concat(file.getName()).concat(".xls");
	 		File f = new File(fileName);
	 		f.createNewFile();
	 		//2:创建工作簿
			WritableWorkbook workbook = Workbook.createWorkbook(f);
			//3:创建sheet,设置第二三四..个sheet，依次类推即可
			WritableSheet sheet = workbook.createSheet(file.getName(), 0);
			//4：设置titles
			String[] titles={"请求日期","接口名称","transaction","jbossIP","jboss请求用时","nginx请求用时"};
			//5:单元格
			Label label=null;
			 //6:给第一行设置列名
			for(int i=0;i<titles.length;i++){
				//x,y,第一行的列名
				label=new Label(i,0,titles[i]);
				//7：添加单元格
				sheet.addCell(label);
			  }
			for(int i = 0; i < list.size(); i++){
				label=new Label(0,i,list.get(i).getRequestDate());
				sheet.addCell(label);
				label=new Label(1,i,list.get(i).getInterfaceName());
				sheet.addCell(label);
				label=new Label(2,i,list.get(i).getTransactionId());
				sheet.addCell(label);
				label=new Label(3,i,list.get(i).getJbossAddr());
				sheet.addCell(label);
				label=new Label(4,i,list.get(i).getJbossElapsTime()==null?"无jboss请求":list.get(i).getJbossElapsTime());
				sheet.addCell(label);
				label=new Label(5,i,list.get(i).getNginxElapsTime()==null?"无nginx请求":list.get(i).getNginxElapsTime());
				sheet.addCell(label);
			}
			 workbook.write();
			 workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
		
		
	}
	
	//poi导出报表  超过65536会报异常  XSS替换HSS 
	//解决方法参见  https://blog.csdn.net/dufufd/article/details/69945942
	//数据多的话，会内存溢出
	private static void createEXCELfileByPoi(List<LogEntity> list,File file){
		 //HSSFWorkbook wb = new HSSFWorkbook(); 
		 //HSSFSheet sheet = wb.createSheet(file.getName()); 
		 //HSSFRow row = sheet.createRow(0); 
		 //HSSFCell cell = row.createCell(0);         //第一个单元格  
		 XSSFWorkbook wb = new XSSFWorkbook(); 
		 XSSFSheet sheet = wb.createSheet(file.getName()); 
		 XSSFRow row = sheet.createRow(0); 
		 XSSFCell cell = row.createCell(0); 
	     cell.setCellValue("请求日期"); 
	     cell = row.createCell(1); 
	     cell.setCellValue("接口名称"); 
	     cell = row.createCell(2);
	     cell.setCellValue("transaction"); 
	     cell = row.createCell(3);
	     cell.setCellValue("jbossIP"); 
	     cell = row.createCell(4);
	     cell.setCellValue("jboss请求用时"); 
	     cell = row.createCell(5);
	     cell.setCellValue("nginx请求用时"); 
	     for (int i = 0; i < list.size(); i++) {  
	            //创建行  
	            row = sheet.createRow(i+1);  
	            //创建单元格并且添加数据  
	            row.createCell(0).setCellValue(list.get(i).getRequestDate());  
	            row.createCell(1).setCellValue(list.get(i).getInterfaceName());  
	            row.createCell(2).setCellValue(list.get(i).getTransactionId());  
	            row.createCell(3).setCellValue(list.get(i).getJbossAddr());  
	            row.createCell(4).setCellValue(list.get(i).getJbossElapsTime()==null?"无jboss请求":list.get(i).getJbossElapsTime()); 
	            row.createCell(5).setCellValue(list.get(i).getNginxElapsTime()==null?"无nginx请求":list.get(i).getNginxElapsTime()); 
	     }  
	     try {  
	    	 	if(!file.getParentFile().exists()){
	    	 		file.mkdirs();
	    	 	}
	    	 	String fileName = file.getParent().concat("/").concat(file.getName()).concat(".xls");
	    	 	System.out.println(fileName);
	            FileOutputStream fout = new FileOutputStream(fileName);  
	            wb.write(fout);  
	            fout.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	}

	//cvs生成报表无法修改样式
	private static void createCSVFile(Map<String,LogEntity_count> map,File file){
	  try{
		LinkedHashMap<String,String> mapk = new LinkedHashMap<String,String>();
		mapk.put("1", "访问时间");
		mapk.put("2", "接口名称");
		mapk.put("3", "总访问次数");
		mapk.put("4", "用户名称");
		mapk.put("5", "用户访问次数");
		 List<Map<String,String>> exportData = new ArrayList<Map<String,String>>();
		//调用DecimalFormat类  ,保留两位小数(.00)/取整五舍六入(0)
		 DecimalFormat df = new DecimalFormat("0"); 
		 for(String key : map.keySet()){
			 for(String user : map.get(key).getMap().keySet()){
				 Map<String, String> row = new LinkedHashMap<String, String>();
				 row.put("1", map.get(key).getRequestDate());
				 row.put("2", map.get(key).getApiEntity());
				 row.put("3", String.valueOf(map.get(key).getCount()));
				 row.put("4", user);
				 row.put("5", map.get(key).getMap().get(user).toString());
				 exportData.add(row);
			 }
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
