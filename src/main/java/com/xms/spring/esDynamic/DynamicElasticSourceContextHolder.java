package com.xms.spring.esDynamic;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 动态数据源上下文管理
 */
public class DynamicElasticSourceContextHolder {

	// 存放当前线程使用的数据源类型信息
		private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

		// 存放数据源id
		public static List<String> elasticsearchSourceIds = new ArrayList<String>();

		// 设置数据源
		public static void setElasticsearchSourceType(String elasticsearchSourceType) {
			contextHolder.set(elasticsearchSourceType);
		}

		// 获取数据源
		public static String getElasticsearchSourceType() {
			return contextHolder.get();
		}

		// 清除数据源
		public static void clearElasticsearchSourceType() {
			contextHolder.remove();
		}

		// 判断当前数据源是否存在
		public static boolean isContainsElasticsearchSource(String elasticsearch) {
			return elasticsearchSourceIds.contains(elasticsearch);
		}
}
