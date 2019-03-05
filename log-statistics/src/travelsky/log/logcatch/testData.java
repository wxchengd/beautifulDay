package travelsky.log.logcatch;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class testData {

	 public static void main(String[] args) {
		    List exportData = new ArrayList<Map>();
		    Map row1 = new LinkedHashMap<String, String>();
		    row1.put("1", "11");
		    row1.put("2", "12");
		    row1.put("3", "13");
		    row1.put("4", "14");
		    exportData.add(row1);
		    row1 = new LinkedHashMap<String, String>();
		    row1.put("1", "21");
		    row1.put("2", "22");
		    row1.put("3", "23");
		    row1.put("4", "24");
		    exportData.add(row1);
		    row1 = new LinkedHashMap<String, String>();
		    row1.put("1", "31");
		    row1.put("2", "32");
		    row1.put("3", "33");
		    row1.put("4", "34");
		    exportData.add(row1);
		    LinkedHashMap map = new LinkedHashMap();
		    map.put("1", "第一列");
		    map.put("2", "第二列");
		    map.put("3", "第三列");
		    map.put("4", "第四列");
		 
		    String path = "F:/opt/applog/";
		    String fileName = "文件导出";
		    File file = HXUtil_old.createCSVFile(exportData, map, path, fileName,exportData.size());
		    String fileName2 = file.getName();
		    System.out.println("文件名称：" + fileName2);
//		    HXUtil.exportFile(response, filePath + fileName, fileName);//下载生成的CSV文件
		  }
	
	
}
