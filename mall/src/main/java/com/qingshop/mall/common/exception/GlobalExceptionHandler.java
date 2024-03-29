package com.qingshop.mall.common.exception;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.qingshop.mall.common.bean.Rest;
import com.qingshop.mall.common.utils.ServletUtils;

/**
 * 全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * 权限校验失败 如果请求为ajax返回json，普通请求跳转页面
	 */
	@ExceptionHandler(AuthorizationException.class)
	public Object handleAuthorizationException(HttpServletRequest request, AuthorizationException e) {
		log.error(e.getMessage(), e);
		if (ServletUtils.isAjaxRequest(request)) {
			return Rest.failure("权限不足");
		} else {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("error/unauth");
			return modelAndView;
		}
	}

	/**
	 * 请求方式不支持
	 */
	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	public Rest handleException(HttpRequestMethodNotSupportedException e) {
		log.error(e.getMessage(), e);
		return Rest.failure("不支持' " + e.getMethod() + "'请求");
	}

	/**
	 * 拦截未知的运行时异常
	 */
	@ExceptionHandler(RuntimeException.class)
	public Rest notFount(RuntimeException e) {
		log.error("运行时异常:", e);
		return Rest.failure("运行时异常:" + e.getMessage());
	}

	/**
	 * 系统异常
	 */
	@ExceptionHandler(Exception.class)
	public Rest handleException(Exception e) {
		log.error(e.getMessage(), e);
		return Rest.failure("服务器错误，请联系管理员");
	}

	/**
	 * 业务异常
	 */
	@ExceptionHandler(BusinessException.class)
	public Object businessException(HttpServletRequest request, BusinessException e) {
		log.error(e.getMessage(), e);
		if (ServletUtils.isAjaxRequest(request)) {
			return Rest.failure(e.getMessage());
		} else {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("errorMessage", e.getMessage());
			modelAndView.setViewName("error/500");
			return modelAndView;
		}
	}

	/**
	 * 自定义验证异常
	 */
	@ExceptionHandler(BindException.class)
	public Rest validatedBindException(BindException e) {
		log.error(e.getMessage(), e);
		String message = e.getAllErrors().get(0).getDefaultMessage();
		return Rest.failure(message);
	}

}
