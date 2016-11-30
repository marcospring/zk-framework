package com.zhangk.datasource.result;

import java.io.Serializable;

/**
 * 各个独立系统返回的结果数据对象，将返回结构封装为Result对象，转化为JSON返回
 * @ClassName:  Result   
 * @Description:TODO 
 * @author: zhangk  
 * @date:   2015年11月6日 下午2:23:56   
 *
 */
public class Result<T> implements Serializable {
	/**   
	 * @Fields serialVersionUID : TODO  
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 结果返回码
	 */
	private int code;
	/**
	 * 结果返回信息
	 */
	private String message;
	/**
	 * 结果返回内容
	 */
	private T result;

	public Result() {
		// TODO Auto-generated constructor stub
	}
	public Result(int code,String message) {
		setCode(code);
		setMessage(message);
	}
	public Result(ResultInfo info) {
		setCode(info.getCode());
		setMessage(info.getMessage());
	}

	public Result(ResultInfo info, T result) {
		this(info);
		setResult(result);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public void setResultInfo(ResultInfo info) {
		setCode(info.getCode());
		setMessage(info.getMessage());
	}
}
