package travelsky.log.logcatch;
import java.io.File;
import java.io.FilenameFilter;

public class LogHandleMain {
	//private static final Logger logger = Logger.getLogger(LogHandleMain.class);
	//一周的日志
	public static void main(String[] args) {
		long data = System.currentTimeMillis();
		//Map<String,LogEntity> map = readFile();
		File file = new File("F:/opt/applog/");
		//该方法返回抽象路径名数组，表示在目录中此抽象路径名表示，满足指定过滤器的文件和目录。
		File[] files = file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".log.gz") || name.endsWith(".log");
			}
		});
		for(File f : files){
			HandLogThread hand = new HandLogThread(f);
			Thread thread = new Thread(hand); 
			thread.start();
		}
		//handLogs(map);
		System.out.println("总程序"+(System.currentTimeMillis()-data)+"ms");
	}
}
