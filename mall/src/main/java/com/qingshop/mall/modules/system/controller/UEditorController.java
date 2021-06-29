package com.qingshop.mall.modules.system.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.http.entity.ContentType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
		SysUploadFile fileResult = Objects.requireNonNull(OssFactory.init()).uploadFile(upfile, true);
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
			String imageStr = imageArray[i];
			String fileType = imageStr.substring(imageStr.lastIndexOf(".") + 1);
			MultipartFile upfile = createFileItem(imageStr, fileType);
			SysUploadFile fileResult = Objects.requireNonNull(OssFactory.init()).uploadFile(upfile, true);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("size", upfile.getSize());
			map.put("source", imageStr);
			map.put("state", "SUCCESS");
			map.put("title", upfile.getOriginalFilename());
			map.put("url", fileResult.getFileUrl());
			replaceList.add(map);
		}
		json.put("state", "SUCCESS");
		json.put("list", replaceList);
		return json;
	}

	/**
	 * url转变为 MultipartFile对象
	 * 
	 * @param url
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private static MultipartFile createFileItem(String url, String fileName) throws Exception {
		FileItem item = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setReadTimeout(30000);
			conn.setConnectTimeout(30000);
			// 设置应用程序要从网络链接读取数据
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = conn.getInputStream();
				FileItemFactory factory = new DiskFileItemFactory(16, null);
				String textFieldName = "uploadfile";
				item = factory.createItem(textFieldName, ContentType.APPLICATION_OCTET_STREAM.toString(), false,
						fileName);
				OutputStream os = item.getOutputStream();
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				is.close();
			}
		} catch (IOException e) {
			throw new RuntimeException("文件下载失败", e);
		}

		return new CommonsMultipartFile(item);
	}

}
