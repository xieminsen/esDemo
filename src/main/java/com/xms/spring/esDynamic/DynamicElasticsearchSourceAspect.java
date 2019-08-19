package com.xms.spring.esDynamic;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @Description 动态数据源通知
 */
@Aspect
@Order(-1)
@Component
public class DynamicElasticsearchSourceAspect {

	private static final Logger log = LoggerFactory.getLogger(DynamicElasticsearchSourceAspect.class);

	// 改变数据源
	@Before("execution(* com.xms.spring.controller.demoController.*(..))")
	public void changeElasticsearchSource(JoinPoint joinPoint) {
		String storageType = getStorageType(joinPoint);
		if (storageType.equalsIgnoreCase("elasticsearch")) {
			String dbid = getElasticsearchSourceName(joinPoint);
			if (!DynamicElasticSourceContextHolder.isContainsElasticsearchSource(dbid)) {
				log.error("数据源 " + dbid + " 不存在，将使用默认数据源或者第一个数据源 -> " + joinPoint.getSignature());
			} else {
				log.info("使用数据源：" + dbid);
				DynamicElasticSourceContextHolder.setElasticsearchSourceType(dbid);
			}
		}
	}

	@After("execution(* com.xms.spring.controller.demoController.*(..))")
	public void clearElasticsearchSource(JoinPoint joinPoint) {
		String storageType = getStorageType(joinPoint);
		if (storageType.equalsIgnoreCase("elasticsearch")) {
			String dbid = getElasticsearchSourceName(joinPoint);
			log.info("清除数据源 " + dbid + " !");
			DynamicElasticSourceContextHolder.clearElasticsearchSourceType();
		}else {
			log.info("违法参数aop初始化多数据源 DynamicElasticsearchSourceAspect" + storageType + " !");
		}
	}

	/**
	 * 通过别名获取数据源名称
	 */
	private String getElasticsearchSourceName(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		String target = (String) args[0];
		return target;
	}

	/**
	 * 获取数据源存储类型
	 */
	private String getStorageType(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		String target = (String) args[2];
		return target;
	}
}
