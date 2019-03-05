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

public class HandLogThread_ratelimitcount implements Runnable{
	private File file;
	
	private File[] files;
	

	public HandLogThread_ratelimitcount(File file){
		super();
		this.file = file;
	}
	
	public HandLogThread_ratelimitcount(File[] files){
		super();
		this.files = files;
	}
	
	public void handle(){
		Long start = System.currentTimeMillis();
		if(files.length>0){
			//最大并发数统计
			Map<String,LogEntity_ratelimitcount> map = HXUtil_ratelimitcount.readFile_merge(files);
			HXUtil_ratelimitcount.handLogs(map, files[0]);
			
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
