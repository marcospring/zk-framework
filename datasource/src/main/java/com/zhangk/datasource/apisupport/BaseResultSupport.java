package com.zhangk.datasource.apisupport;

import java.util.ArrayList;
import java.util.List;

import com.zhangk.datasource.constant.Constants;
import com.zhangk.datasource.exception.BusinessException;
import com.zhangk.datasource.obj.BasePo;
import com.zhangk.datasource.obj.Pagination;
import com.zhangk.datasource.result.MessageContext;
import com.zhangk.datasource.result.Result;
import com.zhangk.datasource.result.ResultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.csyy.common.JSONUtils;

/**
 * Created by zhangkui on 16/4/29.
 */
public class BaseResultSupport {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected MessageContext context;

	/**
	 * 根据结果消息容器键key，获取结果消息
	 *
	 * @param key
	 * @return ResultInfo
	 */
	protected ResultInfo getResultInfo(String key) {
		ResultInfo result = context.getResultInfo(key);
		return result == null ? context
				.getResultInfo(Constants.RESULT_INFO_NOT_FOUNT) : result;
	}

	/**
	 * 根据结果消息容器键key，获得结果信息
	 *
	 * @param key
	 * @return Result
	 */
	protected Result getResult(String key) {
		return new Result(getResultInfo(key));
	}

	/**
	 * 获得操作成功结果信息
	 *
	 * @return Result
	 */
	protected Result getSuccessResult() {
		return new Result(getResultInfo(Constants.ResultStatus.RESULT_SUCCESS));
	}

	/**
	 * 获得操作成功的结果信息
	 *
	 * @param result
	 * @return Result
	 */
	protected Result getSuccessResult(Object result) {
		Result res = new Result(
				getResultInfo(Constants.ResultStatus.RESULT_SUCCESS));
		res.setResult(result);
		return res;
	}

	/**
	 * 获得操作成功结果信息的JSON串
	 *
	 * @return String
	 */
	protected String successResultJSON() {
		return JSONUtils.toJson(getSuccessResult());
	}

	/**
	 * 获得操作成功结果信息JSON串，带有返回的结果内容
	 *
	 * @param result
	 * @return String
	 */
	protected String successResultJSON(Object result) {
		Result res = getSuccessResult();
		res.setResult(result);
		// String resultJson = JSONUtils.toJson(res);
		// 改输出String字段null值为""
		String resultJson = JSONUtils.toJsonWithStringNull2Empty(res);
		logger.info("调用接口成功，返回JSON数据 resultJson:[{}]", resultJson);
		return resultJson;
	}

	/**
	 * 获得操作成功结果信息JSON串，带有返回的结果内容,时间带有时分
	 *
	 * @param result
	 * @return
	 */
	protected String successResultJSONHaveTime(Object result) {
		Result res = getSuccessResult();
		res.setResult(result);
		// String resultJson = JSONUtils.toJson(res);
		// 改输出String字段null值为""
		String resultJson = JSONUtils.toJson(res,
				JSONUtils.DEFAULT_DATE_NO_SECOND);
		logger.info("调用接口成功，返回JSON数据 resultJson:[{}]", resultJson);
		return resultJson;
	}

	/**
	 * 获得操作成功结果信息JSON串，带有返回的结果内容,使用完整的时间表示形式
	 *
	 * @param result
	 * @return String
	 */
	protected String successResultJSONIncludeFullDate(Object result) {
		Result res = getSuccessResult();
		res.setResult(result);
		String resultJson = JSONUtils.toJson(res, null, true, null,
				"yyyy-MM-dd HH:mm:ss", false, false);
		;
		logger.info("调用接口成功，返回JSON数据 resultJson:[{}]", resultJson);
		return resultJson;
	}

	/**
	 * @param result
	 * @param nullAble
	 *            是否序列化为null的字段
	 * @return
	 */
	protected String successResultJSON(Object result, boolean nullAble) {
		Result res = getSuccessResult();
		res.setResult(result);
		String resultJson = JSONUtils.toJson(res, nullAble);
		logger.debug("调用接口成功，返回JSON数据 resultJson:[{}]", resultJson);
		return resultJson;
	}

	/**
	 * 获得结果信息的JSON串
	 *
	 * @param result
	 * @return String
	 */
	protected String resultJSON(Result result) {
		return JSONUtils.toJson(result);
	}

	/**
	 * 获得结果信息的JSON串
	 *
	 * @param resultKey
	 * @return String
	 */
	protected String resultJSON(String resultKey) {
		return JSONUtils.toJson(getResult(resultKey));
	}

	/**
	 * 根据异常信息返回错误消息
	 *
	 * @param ex
	 * @return String
	 */
	protected String resultJSON(BusinessException ex) {
		Result result = null;
		if (!StringUtils.isEmpty(ex.getExceptionKey())) {
			result = getResult(ex.getExceptionKey());
		} else {
			result = getResult(Constants.ResultStatus.RESULT_SYSTEM_ERROR);
		}
		return JSONUtils.toJson(result);
	}

	/**
	 * java顶级异常处理
	 *
	 * @param e
	 * @return
	 */
	protected String resultJSON(Exception e) {
		Result result = null;
		BusinessException ex = new BusinessException(e);
		if (!StringUtils.isEmpty(ex.getExceptionKey())) {
			result = getResult(ex.getExceptionKey());
		} else {
			result = getResult(Constants.ResultStatus.RESULT_SYSTEM_ERROR);
		}
		return JSONUtils.toJson(result);
	}

	/**
	 * 将Pagination对象转换为指定类型的Pagination对象
	 *
	 * @param pagination
	 * @param <T>
	 * @return
	 */
	protected <T> Pagination<T> getVOPagination(
			Pagination<? extends BasePo> pagination, VOTransfer<T> transfer) {
		List<T> data = new ArrayList<>();
		for (BasePo po : pagination.getData()) {
			data.add(transfer.trans(po));
		}
		Pagination<T> result = new Pagination<T>(data, pagination.getPageNo(),
				pagination.getPageSize());
		result.setPageCount(pagination.getPageCount());
		result.setRecordsTotal(pagination.getRecordsTotal());
		return result;
	}

	// po2VO
	protected interface VOTransfer<T> {
		T trans(BasePo po);
	}

}
