package com.xms.spring.esDynamic;

import org.springframework.stereotype.Component;

import io.searchbox.client.JestClient;

@Component("dynamicElasticSearchTemplate")
public class DynamicElasticSearchTemplate {

	//定义切换类
	public JestClient build() {
		String target = DynamicElasticSourceContextHolder.getElasticsearchSourceType();
		//默认使用one作为数据源
		if (target == null) {
			target = "one";
		}
		JestClient client = (JestClient) DynamicElasticsearchDataSource.getTargetElasticsearchSources()
				.get(target);
		return client;
	}


}
