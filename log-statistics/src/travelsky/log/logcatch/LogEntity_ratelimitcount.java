package travelsky.log.logcatch;

import java.io.Serializable;
import java.util.Map;
/**
 * 内容摘要: 日志实体类
 * 创建日期: 2018-6-7 上午10:24:25
 * 作       者 : 
 * 版本信息 : 3.0
 */
public class LogEntity_ratelimitcount implements Serializable{

	private static final long serialVersionUID = 8581367688481298815L;
	private String requestDate;//请求日期
	private String appkey;//appkey
	private int count;  //当前最大限流并发数
	private String transactionId;//tid
	private String user;//用户名称
	
	public static final long getSerialversionuid() {
		return serialVersionUID;
	}

	public final String getRequestDate() {
		return requestDate;
	}

	public final void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public final String getAppkey() {
		return appkey;
	}

	public final void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public final int getCount() {
		return count;
	}

	public final void setCount(int count) {
		this.count = count;
	}

	public final String getTransactionId() {
		return transactionId;
	}

	public final void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public final String getUser() {
		return user;
	}

	public final void setUser(String user) {
		this.user = user;
	}
	
	
	
	
	
	

}
