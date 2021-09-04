package com.config;

import com.filter.CORSAuthenticationFilter;
import com.filter.XssFilter;
import com.realm.AuthRealm;
import com.realm.CustomSessionIdGenerator;
import com.realm.CustomSessionManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.yaml.snakeyaml.scanner.Constant;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
import java.util.Properties;

@Configuration
public class ShiroConfig {

    @Value("${bluebook.shiro-redis.host}")
    private String shiroRedisHost;

    @Value("${bluebook.shiro-redis.port}")
    private Integer shiroRedisPort;

    private static Logger log = LoggerFactory.getLogger(ShiroConfig.class);


    @Bean("hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1024);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }



    @Bean("authRealm")
    public AuthRealm authRealm() {
        AuthRealm authRealm = new AuthRealm();
        authRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return authRealm;
    }


    @Bean("sessionManager")
    public SessionManager sessionManager(){
        CustomSessionManager customSessionManager = new CustomSessionManager();
        customSessionManager.setGlobalSessionTimeout(60 * 1000 * 60 * 24);
        customSessionManager.setSessionDAO(redisSessionDAO());
        return customSessionManager;
    }


    @Bean("securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setSessionManager(sessionManager());
        manager.setCacheManager(redisCacheManager());
        manager.setRealm(authRealm());
        return manager;
    }


    public RedisManager getRedisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(shiroRedisHost);
        redisManager.setPort(shiroRedisPort);
        return redisManager;
    }


    @Bean("redisCacheManager")
    public RedisCacheManager redisCacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(getRedisManager());
        redisCacheManager.setPrincipalIdFieldName("id");
        return redisCacheManager;
    }


    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(getRedisManager());
        redisSessionDAO.setSessionIdGenerator(new CustomSessionIdGenerator());
        return redisSessionDAO;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(org.apache.shiro.mgt.SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);


        shiroFilter.setLoginUrl("login");
        shiroFilter.setUnauthorizedUrl("/");


        Map<String, String> filterMap = new LinkedHashMap<String, String>();

        filterMap.put("/", "anon");
        filterMap.put("/user", "authc");
        filterMap.put("/editor", "roles[admin]");
        filterMap.put("/SuperAdmin", "roles[admin]");
        filterMap.put("/**", "anon");

        shiroFilter.setFilterChainDefinitionMap(filterMap);


        Map<String, Filter> customFilterMap = new LinkedHashMap<>();
        customFilterMap.put("corsAuthenticationFilter", new CORSAuthenticationFilter());
        customFilterMap.put("XssFilter", new XssFilter());
        shiroFilter.setFilters(customFilterMap);

        return shiroFilter;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }


    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }



}