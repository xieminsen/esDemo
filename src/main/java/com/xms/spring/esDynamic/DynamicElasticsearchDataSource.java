package com.xms.spring.esDynamic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;


@Configuration
public class DynamicElasticsearchDataSource implements EnvironmentAware{

	// 用户自定义数据源
		private static Map<String, Object> targetElasticsearchSources = new HashMap<>();

		public static Map<String, Object> getTargetElasticsearchSources() {
			return targetElasticsearchSources;
		}

		public static void setTargetElasticsearchSources(Map<String, Object> targetElasticsearchSources) {
			DynamicElasticsearchDataSource.targetElasticsearchSources = targetElasticsearchSources;
		}

		@Override
		public void setEnvironment(Environment environment) {
			initTargetElasticsearchSources(environment);
		}

		// 初始化自定义数据源
		private void initTargetElasticsearchSources(Environment env) {
			String dsPrefixs = env.getProperty("dsl.elasticSearch.targets");
			for (String dsPrefix : dsPrefixs.split(",")) {
				// 多个数据源
				Map<String, Object> dsMap = new HashMap<>();
				dsMap.put("host", env.getProperty("spring.elasticSearch." + dsPrefix + ".host"));
				dsMap.put("port", env.getProperty("spring.elasticSearch." + dsPrefix + ".port"));
				dsMap.put("connecttimeout", env.getProperty("spring.elasticSearch.connect.timeout"));
				dsMap.put("sockettimeout", env.getProperty("spring.elasticSearch.socket.timeout"));
				dsMap.put("requesttimeout", env.getProperty("spring.elasticSearch.connection.request.timeout"));
				JestClient client = createJestClient(dsMap);
				DynamicElasticSourceContextHolder.elasticsearchSourceIds.add(dsPrefix);
				targetElasticsearchSources.put(dsPrefix, client);
			}
		}

		public JestClient createJestClient(Map<String, Object> dsMap) {
			
			JestClientFactory factory = new JestClientFactory();

			String connectionUrl = "http://"+dsMap.get("host").toString()+":"+new Integer(dsMap.get("port").toString());

			factory.setHttpClientConfig(new HttpClientConfig
			        .Builder(connectionUrl) //参数可以是集群，请先定义一个list集合，将节点url分别添加到list
			        //.defaultCredentials("elastic","changeme") //如果使用了x-pack，就要添加用户名和密码
			        .multiThreaded(true) //多线程模式
			        .connTimeout(60000) //连接超时
			        .readTimeout(60000) //由于是基于http，所以超时时间必不可少，不然经常会遇到socket异常：read time out
			        .build()); //更多参数请查看api
			JestClient client=factory.getObject();
			return client;
			
			
			
		}

}
