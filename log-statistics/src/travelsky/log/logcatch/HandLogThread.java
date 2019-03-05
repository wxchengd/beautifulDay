package travelsky.log.logcatch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

public class HandLogThread implements Runnable{
	private File file;
	
	private File[] files;
	private List<LogEntity> lists ;
	

	public HandLogThread(List<LogEntity> lists,File file){
		super();
		this.file = file;
		this.lists = lists;
	}
	public HandLogThread(File file){
		super();
		this.file = file;
	}
	
	public HandLogThread(File[] files){
		super();
		this.files = files;
	}
	
	public void handle(){
		Long start = System.currentTimeMillis();
		if(files.length>0){
			List<LogEntity> lists = new ArrayList<LogEntity>();
			List<LogEntity> list = new ArrayList<LogEntity>();
			int count =1 ;
			String fp = files[0].getName().substring(0,files[0].getName().indexOf("-access"));
			for(File File : files){
				list = HXUtil.readFile_merge(File);
				for(LogEntity entry :list){
					lists.add(entry);
				}
//				createCSVFile(list,files[0].getParent().concat("/").concat(fp),fp+"_"+(count++));
//				HandLogThread hand = new HandLogThread(list,files[0]);
//				Thread thread = new Thread(hand); 
//				thread.start();
//				String fp = files[0].getName().substring(0,files[0].getName().indexOf("-access"));
//				list.clear();
//				for(Map.Entry<String,LogEntity> entry:map.entrySet()){
//					maps.put(entry.getKey(), entry.getValue());
//				}
//				maps.putAll(map);
//				System.out.println(map.size());
//				for(Map.Entry<String,LogEntity> entry:map.entrySet()){  
//					  list.add(entry.getValue());
//					  set.add(entry.getValue().getInterfaceName().
//							  replace("/v1.0", "").replace("/v2.0", ""));
//				}
			}
			
			createCSVFile(lists,files[0].getParent().concat("/").concat(fp),fp);
//			HandLogThread hand = new HandLogThread(lists,files[0]);
//			Thread thread = new Thread(hand); 
//			thread.start();
//			lists.clear();
//			Map<String,List<LogEntity>> mapp = new LinkedHashMap<String,List<LogEntity>>();
//			for(String rr:set){
//				List<LogEntity> t = new ArrayList<LogEntity>();
//				for(LogEntity l : list){
//					if(l.getInterfaceName().contains(rr)){
//						t.add(l);
//					}
//				}
//				mapp.put(rr, t);
//			}
//			list.clear();
//			set.clear();
////			System.out.println(mapp.size());
//			System.out.println("===================================================");
//			String fp = files[0].getName().substring(0,files[0].getName().indexOf("-access"));
//			for(String key : mapp.keySet()){
//				String key0 =key;
//				if(key.contains("/")){
//					key0 = key.replace("/", "").replace("?", "");
//				}
//				File ff = new File(files[0].getParent().concat("/").concat(fp),key0);
//				createCSVFile(mapp.get(key),files[0].getParent().concat("/").concat(fp),key0);
//			}
//			createCSVFile(lists,files[0].getParent().concat("/").concat(fp),fp);
			System.out.println("读取数据完毕");
			Long end = System.currentTimeMillis();
			System.out.println("读取数据用时，用时"+(end-start)+"ms");
		}else{
			System.out.println("执行失败");
		}
	}
	
	@Override
	public void run() {
//		Map<String,LogEntity> map = HXUtil.readFile(file);
		//System.out.println(file.getName());
		//System.out.println(map.size());
//		HXUtil.handLogs(map, file);
		String fp = file.getName().substring(0,file.getName().indexOf("-access"));
		createCSVFile(lists,file.getParent().concat("/").concat(fp),fp);
	}
	
	private static void createCSVFile(List<LogEntity> list,String parent, String child){
		  try{
			LinkedHashMap<String,String> mapk = new LinkedHashMap<String,String>();
			mapk.put("1", "请求日期");
			mapk.put("2", "接口名称");
			mapk.put("3", "transaction");
			mapk.put("4", "jbossIP");
			mapk.put("5", "jboss请求用时");
			mapk.put("6", "nginx请求用时");
			mapk.put("7", "nginx和jboss耗时时差");
			 List<Map<String,String>> exportData = new ArrayList<Map<String,String>>();
			//调用DecimalFormat类  ,保留两位小数(.00)/取整五舍六入(0)
			 DecimalFormat df = new DecimalFormat("0"); 
			 int count =0;
			 System.out.println("======================"+list.size());
			 File csvFile = null;
		    BufferedWriter csvFileOutputStream = null;
		    	File file = new File(parent);
		    	if (!file.exists()) {
		    		file.mkdir();
		    	}
		    	csvFile = new File(parent+"/"+child+".csv");
		    //定义文件名格式并创建
//				      if(csvFile.exists()){
//				    	  
//				      }
		      System.out.println("csvFile：" + csvFile);
		      // UTF-8使正确读取分隔符"," 
		      csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "gbk"), 3064);
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
			 for(int i =0;i<list.size();i++){
				 if(list.get(i).getNginxElapsTime()!=null && list.get(i).getJbossElapsTime()!=null ){
					 Map<String, String> row = new LinkedHashMap<String, String>();
					 row.put("1", list.get(i).getRequestDate());
					 row.put("2", list.get(i).getInterfaceName());
					 row.put("3", list.get(i).getTransactionId());
					 row.put("4", list.get(i).getJbossAddr());
					 if(list.get(i).getJbossElapsTime().equals("-")){
//						 continue;
						 row.put("5", "");
						 row.put("6", df.format(Double.parseDouble(list.get(i).getNginxElapsTime())*1000));
						 row.put("7", "");
					 }else if(list.get(i).getJbossElapsTime().isEmpty() || list.get(i).getJbossElapsTime().contains(";")){
						 row.put("5", "");
						 row.put("6", df.format(Double.parseDouble(list.get(i).getNginxElapsTime())*1000));
						 row.put("7", "");
					 }
					 else{
						 try{
//							 System.out.println(list.get(i).getJbossElapsTime());
							 row.put("5", df.format((Double.parseDouble(list.get(i).getJbossElapsTime())*1000)));
							 row.put("6", df.format(Double.parseDouble(list.get(i).getNginxElapsTime())*1000));
							 row.put("7", df.format(Double.parseDouble(list.get(i).getNginxElapsTime())*1000 - Double.parseDouble(list.get(i).getJbossElapsTime())*1000));
						 }catch (Exception e) {
							e.printStackTrace();
						}
					 }
//					 exportData.add(row);
//					 list.remove(i);
				      // 写入文件内容 
				        for (Iterator propertyIterator = mapk.entrySet().iterator(); propertyIterator.hasNext();) {
				        	java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
				        	csvFileOutputStream.write((String) BeanUtils.getProperty((Object)row,(String) propertyEntry.getKey()));
				        	if(propertyIterator.hasNext()) {
				        	  csvFileOutputStream.write(",");
				        	}
				        }
				        csvFileOutputStream.newLine();//换行符
				            //csvFileOutputStream.write("\n");//linux服务上用这个
					      }
//					      csvFileOutputStream.newLine();
//					      csvFileOutputStream.write("\n");
					    
			 }
			 csvFileOutputStream.flush();
			 csvFileOutputStream.close();
			 System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
//			 createCSVFile(exportData, mapk, parent, child,list.size());
		  } catch (Exception e) {
			  e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	  public static File createCSVFile(List exportData, LinkedHashMap map, String outPutPath,String fileName,int total) {
	    File csvFile = null;
	    BufferedWriter csvFileOutputStream = null;
	    try {
	    	File file = new File(outPutPath);
	    	if (!file.exists()) {
	    		file.mkdir();
	    	}
	    	csvFile = new File(outPutPath+"/"+fileName+".csv");
	    //定义文件名格式并创建
//	      if(csvFile.exists()){
//	    	  
//	      }
	      System.out.println("csvFile：" + csvFile);
	      // UTF-8使正确读取分隔符"," 
	      csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "gbk"), 2048);
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
    	  csvFileOutputStream.write(",");
    	  csvFileOutputStream.write("该接口调用nginx总次数为:" + total );
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
