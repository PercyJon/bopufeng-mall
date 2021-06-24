package com.qingshop.mall.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2的接口配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	/**
	 * 创建API
	 */
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()) // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
				.select() // 设置哪些接口暴露给Swagger展示
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) // 扫描所有有注解的api，用这种方式更灵活
				// 扫描指定包中的swagger注解
				// .apis(RequestHandlerSelectors.basePackage("com.qingcong.mall.controller"))
				// 扫描所有 .apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	/**
	 * 添加摘要信息
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("微信小程序接口文档") // 设置标题
				.description("用于对接微信小程序接口") // 描述
				.contact(new Contact("mall", null, null)) // 作者信息
				.version("版本号: 1.0.0").build(); // 版本
	}
}
