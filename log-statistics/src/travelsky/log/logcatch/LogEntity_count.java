package travelsky.log.logcatch;

import java.io.Serializable;
import java.util.Map;
/**
 * 内容摘要: 日志实体类
 * 创建日期: 2018-6-7 上午10:24:25
 * 作       者 : 
 * 版本信息 : 3.0
 */
public class LogEntity_count implements Serializable{

	private static final long serialVersionUID = 8581367688481298815L;
	private String requestDate;//请求日期
	private String apiEntity;//接口名称
	private Map<String,Integer> map; //存放用户名称和用户调用该接口次数
	private int count;
	private String transactionId;//tid
	private String status;//状态码
	private String user;//用户名称
	public final int getCount() {
		return count;
	}
	public final void setCount(int count) {
		this.count = count;
	}
	public final String getUser() {
		return user;
	}
	public final void setUser(String user) {
		this.user = user;
	}
	public final String getTransactionId() {
		return transactionId;
	}
	public final void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public final String getStatus() {
		return status;
	}
	public final void setStatus(String status) {
		this.status = status;
	}
	public final String getRequestDate() {
		return requestDate;
	}
	public final void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public final String getApiEntity() {
		return apiEntity;
	}
	public final void setApiEntity(String apiEntity) {
		this.apiEntity = apiEntity;
	}
	public final Map<String, Integer> getMap() {
		return map;
	}
	public final void setMap(Map<String, Integer> map) {
		this.map = map;
	}
	public static final long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "接口访问量： [访问时间：" + requestDate + ", 接口：" + apiEntity +", 总访问次数："+count+
				", 用户及用户调用次数：" + map.entrySet().toString()+  "]";
	}
	
	
	
	
	

}
