package com.qingshop.mall.modules.system.controller;

import java.io.File;
import java.util.Objects;

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
	 * 通用图片上传请求
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/image")
	@ResponseBody
	public Rest uploadImageFile(MultipartFile file) throws Exception {
		try {
			if (file.isEmpty()) {
				return Rest.failure("请选择文件！");
			} else {
				SysUploadFile fileResult = Objects.requireNonNull(OssFactory.init()).uploadFile(file, true);
				Rest rest = Rest.ok();
				rest.put("originalName", fileResult.getOriginalName());
				rest.put("fileUrl", fileResult.getFileUrl());
				return rest;
			}
		} catch (Exception e) {
			logger.error("文件上传失败", e);
			return Rest.failure("服务器异常请联系管理员！");
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
