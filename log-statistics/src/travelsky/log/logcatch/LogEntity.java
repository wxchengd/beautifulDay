package travelsky.log.logcatch;

import java.io.Serializable;
/**
 * 内容摘要: 日志实体类
 * 创建日期: 2018-6-7 上午10:24:25
 * 作       者 : 
 * 版本信息 : 3.0
 */
public class LogEntity implements Serializable{

	private static final long serialVersionUID = 8581367688481298815L;
	private String requestDate;//请求日期
	private String clientIp;//客户端IP
	private String interfaceName;//接口名称
	private String transactionId;//唯一的id
	private String jbossAddr;//jboss ip地址
	private String jbossElapsTime;//jboss 调用花费的时间
	private String nginxElapsTime;//nginx调用花费的时间
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getJbossAddr() {
		return jbossAddr;
	}
	public void setJbossAddr(String jbossAddr) {
		this.jbossAddr = jbossAddr;
	}
	public String getJbossElapsTime() {
		return jbossElapsTime;
	}
	public void setJbossElapsTime(String jbossElapsTime) {
		this.jbossElapsTime = jbossElapsTime;
	}
	public String getNginxElapsTime() {
		return nginxElapsTime;
	}
	public void setNginxElapsTime(String nginxElapsTime) {
		this.nginxElapsTime = nginxElapsTime;
	}
	
	
	
	

}
