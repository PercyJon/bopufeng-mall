package com.qingshop.mall.framework.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qingshop.mall.framework.shiro.MallRealm;
import com.qingshop.mall.framework.shiro.web.UserFilter;
import com.qingshop.mall.framework.shiro.web.UserPermFilter;

/**
 * 权限配置加载
 */
@Configuration
public class ShiroConfig {
	
	/**
	 * 设置Session有效时间
	 */
	@Value("${shiro.session.globalSessionTimeout}")
	private int globalSessionTimeout;
	
	/**
	 * 是否开启redis实现集群部署
	 */
	@Value("${global.cluster}")
	private boolean cluster;

	/**
	 * 设置Cookie的域名
	 */
	@Value("${shiro.cookie.domain}")
	private String domain;

	/**
	 * 设置cookie的有效访问路径
	 */
	@Value("${shiro.cookie.path}")
	private String path;

	/**
	 * 设置HttpOnly属性
	 */
	@Value("${shiro.cookie.httpOnly}")
	private boolean httpOnly;

	/**
	 * 设置Cookie的过期时间，秒为单位
	 */
	@Value("${shiro.cookie.maxAge}")
	private int maxAge;

	/**
	 * 设置cipherKey密钥（安全密钥部署请重新生成）
	 */
	@Value("${shiro.cookie.cipherKey}")
	private String cipherKey;

	/**
	 * 登录地址
	 */
	@Value("${shiro.user.loginUrl}")
	private String loginUrl;

	/**
	 * 权限认证失败地址
	 */
	@Value("${shiro.user.unauthorizedUrl}")
	private String unauthorizedUrl;

	
	/**
	 * 注入缓存管理配置类
	 */
	@Autowired
	private CacheManagerConfig cacheManagerConfig;

	/**
	 * 单机环境，session交给shiro管理
	 */
	@Bean
	@ConditionalOnProperty(prefix = "global", name = "cluster", havingValue = "false")
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionIdUrlRewritingEnabled(false);
		sessionManager.setSessionValidationInterval(globalSessionTimeout * 1000);
		sessionManager.setGlobalSessionTimeout(globalSessionTimeout * 1000);
		return sessionManager;
	}

	/**
	 * 集群环境，session交给spring-session管理
	 */
	@Bean
	@ConditionalOnProperty(prefix = "global", name = "cluster", havingValue = "true")
	public ServletContainerSessionManager servletContainerSessionManager() {
		return new ServletContainerSessionManager();
	}

	/**
	 * 自定义Realm
	 */
	@Bean
	public MallRealm myShiroRealm() {
		MallRealm myShiroRealm = new MallRealm();
		myShiroRealm.setCacheManager(cluster ? cacheManagerConfig.getRedisCacheManager() : cacheManagerConfig.getEhCacheManager());
		myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return myShiroRealm;
	}

	/**
	 * 密码加密
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("md5");
		hashedCredentialsMatcher.setHashIterations(1024);
		return hashedCredentialsMatcher;
	}

	@Bean("securityManager")
	public SecurityManager securityManager(SessionManager sessionManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myShiroRealm());
		securityManager.setSessionManager(sessionManager);
		securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}

	/**
	 * Shiro过滤器配置
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setLoginUrl(loginUrl);
		shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
		// 自定义拦截器
		Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
		filtersMap.put("user", new UserFilter());
		filtersMap.put("perms", new UserPermFilter());
		shiroFilterFactoryBean.setFilters(filtersMap);
		LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

		filterChainDefinitionMap.put("/app/**", "anon");
		filterChainDefinitionMap.put("/common/**", "anon");
		filterChainDefinitionMap.put("/images/**", "anon");
		filterChainDefinitionMap.put("/plugins/**", "anon");
		filterChainDefinitionMap.put("/captcha", "anon");
		filterChainDefinitionMap.put("/doLogin", "anon");
		filterChainDefinitionMap.put("/login", "anon");
		filterChainDefinitionMap.put("/druid/**", "anon");
		filterChainDefinitionMap.put("/file/**", "anon");
		filterChainDefinitionMap.put("/wx/**", "anon");
		filterChainDefinitionMap.put("/**", "user,perms");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}

	/**
	 * cookie 属性设置
	 */
	public SimpleCookie rememberMeCookie() {
		SimpleCookie cookie = new SimpleCookie("rememberMe");
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookie.setHttpOnly(httpOnly);
		cookie.setMaxAge(maxAge * 24 * 60 * 60);
		return cookie;
	}

	/**
	 * 记住我
	 */
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		cookieRememberMeManager.setCipherKey(Base64.decode(cipherKey));
		return cookieRememberMeManager;
	}

}
