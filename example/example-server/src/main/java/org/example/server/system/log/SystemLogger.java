package org.example.server.system.log;

import java.util.Map;

public class SystemLogger {
	private String methodName;
	private String methodDesc;
	private String requestIp;
	private String requestStartTime;
	private String requestEndTime;
	private Map<String, Object> params;
	private Object result;
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getMethodDesc() {
		return methodDesc;
	}
	public void setMethodDesc(String methodDesc) {
		this.methodDesc = methodDesc;
	}
	public String getRequestIp() {
		return requestIp;
	}
	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}
	public String getRequestStartTime() {
		return requestStartTime;
	}
	public void setRequestStartTime(String requestStartTime) {
		this.requestStartTime = requestStartTime;
	}
	public String getRequestEndTime() {
		return requestEndTime;
	}
	public void setRequestEndTime(String requestEndTime) {
		this.requestEndTime = requestEndTime;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
}
