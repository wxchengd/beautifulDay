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

public class HandLogThread_count implements Runnable{
	private File file;
	
	private File[] files;
	

	public HandLogThread_count(File file){
		super();
		this.file = file;
	}
	
	public HandLogThread_count(File[] files){
		super();
		this.files = files;
	}
	
	public void handle(){
		Long start = System.currentTimeMillis();
		if(files.length>0){
			//接口的用户访问量统计
			Map<String,LogEntity_count> map = HXUtil_count.readFile_merge(files);
			HXUtil_count.handLogs(map, files[0]);
			
			//接口调用transactionid及状态统计
//		    HXUtil_count2.readFile_merge(files);
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
//		String fp = file.getName().substring(0,file.getName().indexOf("-access"));
	}
	

}
