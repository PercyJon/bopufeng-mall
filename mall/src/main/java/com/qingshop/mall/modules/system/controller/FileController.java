package com.qingshop.mall.modules.system.controller;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.qingshop.mall.common.bean.Rest;
import com.qingshop.mall.common.constant.Constants;
import com.qingshop.mall.common.utils.JsonUtils;
import com.qingshop.mall.common.utils.PropertiesUtil;
import com.qingshop.mall.common.utils.StringUtils;
import com.qingshop.mall.modules.common.BaseController;
import com.qingshop.mall.modules.oss.OssFactory;
import com.qingshop.mall.modules.system.service.ISysConfigService;
import com.qingshop.mall.modules.system.vo.ConfigStorageVo;
import com.qingshop.mall.modules.system.vo.SysUploadFile;

@Controller
@RequestMapping("/system/sysfile")
public class FileController extends BaseController {

	@Autowired
	private ISysConfigService configService;

	/**
	 * 通用上传请求 (单个文件)
	 */
	@PostMapping("/upload")
	@ResponseBody
	public Map<String, Object> uploadImageFile(MultipartFile file) throws Exception {
		Map<String, Object> resultMap = new HashedMap<String, Object>();
		try {
			if (file.isEmpty()) {
				resultMap.put("error", "请选择文件！");
				return resultMap;
			} else {
				SysUploadFile fileResult = Objects.requireNonNull(OssFactory.init()).uploadFile(file, true);
				Rest rest = Rest.ok();
				rest.put("fileName", fileResult.getOriginalName());
				rest.put("fileUrl", fileResult.getFileUrl());
				return rest;
			}
		} catch (Exception e) {
			logger.error("文件上传失败", e);
			resultMap.put("error", "服务器异常请联系管理员！");
			return resultMap;
		}
	}

	/**
	 * 通用上传请求（多个文件）
	 */
	@PostMapping("/uploads")
	@ResponseBody
	public Map<String, Object> uploadFiles(MultipartFile[] file, HttpServletResponse httpResponse) throws Exception {
		Map<String, Object> resultMap = new HashedMap<String, Object>();
		try {
			if (file != null && file.length > 0) {
				List<SysUploadFile> fileInfos = new LinkedList<SysUploadFile>();
				for (MultipartFile tempfile : file) {
					SysUploadFile fileResult = Objects.requireNonNull(OssFactory.init()).uploadFile(tempfile, true);
					fileInfos.add(fileResult);
				}
				return Rest.okData(fileInfos);
			} else {
				httpResponse.setStatus(405);
				resultMap.put("error", "请选择文件！");
				return resultMap;
			}
		} catch (Exception e) {
			httpResponse.setStatus(405);
			resultMap.put("error", e.getMessage());
			return resultMap;
		}
	}

	/**
	 * 删除图片
	 * 
	 * @param filePath
	 * @return
	 */
	@PostMapping("/deleteFiles")
	@ResponseBody
	public Rest deleteFiles(String filePath) {
		if (StringUtils.isNotBlank(filePath)) {
			Objects.requireNonNull(OssFactory.init()).delete(filePath);
			return Rest.ok("删除成功！");
		}
		return Rest.failure("删除失败");
	}

	/**
	 * 文件存储缓存配置详情
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/setStorage")
	public String setConfig(Model model) {
		String json = configService.selectAll();
		ConfigStorageVo configStorageVo = JsonUtils.jsonToBean(json, ConfigStorageVo.class);
		String workDir = PropertiesUtil.getString(Constants.WORK_DIR_KEY);
		model.addAttribute("workDir", workDir.endsWith(File.separator) ? workDir : workDir + File.separator);
		model.addAttribute("storageConfig", configStorageVo);
		return "system/sysfile/config";
	}

}
