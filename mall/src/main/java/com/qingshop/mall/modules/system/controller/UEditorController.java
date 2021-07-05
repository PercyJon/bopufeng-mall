package com.qingshop.mall.modules.system.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qingshop.mall.common.utils.StringUtils;
import com.qingshop.mall.common.utils.UUIDUtil;
import com.qingshop.mall.framework.config.OssConfig;
import com.qingshop.mall.modules.common.BaseController;
import com.qingshop.mall.modules.oss.OssFactory;
import com.qingshop.mall.modules.system.vo.SysUploadFile;

/**
 * Ueditor 富文本编辑器配置类
 *
 */
@RestController
@RequestMapping("/plugins/UEditor/ueditor")
public class UEditorController extends BaseController {
	
	@Autowired
	private OssConfig ossConfig;

	/**
	 * ueditor
	 */
	@RequestMapping("/exec")
	public Object ueditor(HttpServletRequest request, @RequestParam(value = "action", required = true) String action,
			MultipartFile upfile) throws Exception {

		// 配置参数
		if (action.equals("config")) {
			return ueditorconfig();
		}

		// 上传图片
		if (action.equals("uploadimage")) {
			return ueditoruploadimage(request, upfile);
		}

		// 抓取远程图片
		if (action.equals("catchimage")) {
			return ueditorcatchimage(request);
		}

		return null;
	}

	/**
	 * 读取配置文件
	 */
	public JSONObject ueditorconfig() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource("ueditor-config.json");
		String jsonString = new BufferedReader(new InputStreamReader(classPathResource.getInputStream())).lines()
				.parallel().collect(Collectors.joining(System.lineSeparator()));
		JSONObject json = JSON.parseObject(jsonString, JSONObject.class);
		return json;
	}

	/**
	 * 上传图片
	 */
	public JSONObject ueditoruploadimage(HttpServletRequest request, MultipartFile upfile) throws Exception {
		JSONObject json = new JSONObject();
		SysUploadFile fileResult = Objects.requireNonNull(OssFactory.init(ossConfig)).uploadFile(upfile, true);
		json.put("state", "SUCCESS");
		json.put("url", fileResult.getFileUrl());
		return json;
	}

	/**
	 * 抓取图片
	 */
	public JSONObject ueditorcatchimage(HttpServletRequest request) throws Exception {
		JSONObject json = new JSONObject();
		Map<String, String[]> maps = request.getParameterMap();
		String[] imageArray = maps.get("source[]");
		List<Map<String, Object>> replaceList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < imageArray.length; i++) {
			String source = imageArray[i];
			if (StringUtils.isNotBlank(source)) {
				URL url = new URL(source);
				URLConnection con = url.openConnection();
				try (InputStream is = con.getInputStream();) {
					// 取出后缀名
					String extensionName = source.substring(source.lastIndexOf("."), source.lastIndexOf(".") + 4);
					String suffix = UUIDUtil.generateShortUuid() + extensionName;
					SysUploadFile fileResult = Objects.requireNonNull(OssFactory.init(ossConfig)).upload(is, suffix, true);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("source", source);
					map.put("state", "SUCCESS");
					map.put("url", fileResult.getFileUrl());
					replaceList.add(map);
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);
				}
			}

		}
		json.put("state", "SUCCESS");
		json.put("list", replaceList);
		return json;
	}

}
