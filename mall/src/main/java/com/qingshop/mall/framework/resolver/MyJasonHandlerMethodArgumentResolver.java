package com.qingshop.mall.framework.resolver;

import java.util.Iterator;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MyJasonHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(JasonModel.class);
	}

	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		StringBuffer jason = new StringBuffer("{");
		StringBuffer tmp;
		String[] val;
		int idx = 0;
		for (Iterator<String> itr = webRequest.getParameterNames(); itr.hasNext();) {
			tmp = new StringBuffer(itr.next());
			val = webRequest.getParameterValues(tmp.toString());
			int lastIndex = tmp.lastIndexOf("[]");
			if (lastIndex != -1) {
				tmp = tmp.delete(lastIndex, lastIndex + 2);
			}
			jason.append("\"");
			jason.append(tmp);
			jason.append("\":");
			if (val != null && val.length > 0) {
				if (val.length == 1) {
					jason.append("\"");
					jason.append(StringEscapeUtils.escapeJson(val[0]));
					jason.append("\"");
				} else {
					jason.append("[");
					for (String v : val) {
						idx++;
						jason.append("\"");
						jason.append(StringEscapeUtils.escapeJson(v));
						if (idx < val.length) {
							jason.append("\",");
						} else {
							jason.append("\"");
							idx = 0;
						}
					}
					jason.append("]");
				}
				jason.append(",");
			} else {
				jason.append("null,");
			}
		}
		String result = jason.toString().equals("{") ? jason.toString() : jason.substring(0, jason.length() - 1);
		result = result + "}";
		return result;
	}
}
