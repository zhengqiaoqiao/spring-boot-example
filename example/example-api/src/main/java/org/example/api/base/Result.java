package org.example.api.base;

public class Result<T> extends Base{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**返回状态编号**/
	private int code;
	/**返回信息说明**/
	private String msg;
	/**返回结果**/
	private T result;
	
	public static <T> Result<T> create(int code, String msg, T o){
		Result<T> rs = new Result<T>();
		rs.setCode(code);
		rs.setMsg(msg);
		rs.setResult(o);
		return rs;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}

	

}
