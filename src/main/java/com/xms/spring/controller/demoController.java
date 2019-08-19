package com.xms.spring.controller;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xms.spring.esDynamic.DynamicElasticSearchTemplate;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Get;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

@Controller
@ResponseBody
public class demoController {

	
	@Autowired
	@Qualifier("dynamicElasticSearchTemplate")
	private DynamicElasticSearchTemplate dynamicElasticSearchTemplate;
	
	/**
	 * elasticsearch查询
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("query/dsl")
	public String query(String target, String queryString, String storageType)  {
		JestClient client = dynamicElasticSearchTemplate.build();
		 //查询表达式
		  String json = "{\"query\":{\"match_all\":{}}}";
		  //构建搜索功能
		  Search search = new Search.Builder(json).addIndex("index1").addType("table1").build();
		  try {
		   SearchResult result = client.execute(search);
		   System.out.println(result.getJsonString());
		   return result.getJsonString();
		  } catch (IOException e) {
		   e.printStackTrace();
		  }
		return null;
	}
	/**
	 * elasticsearch查询
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("query/esclient")
	public String querydsl(String target, String queryString, String storageType)  {
		JestClient client = dynamicElasticSearchTemplate.build();
		String id = "1";
		JestResult result;
		try {
			result = get(client, "index1", "table1", id);
			if (result.isSucceeded()) {
				System.out.println(result.getJsonString());
			}
			return result.getJsonString();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * Get文档
	 * @param jestClient
	 * @param indexName
	 * @param typeName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static JestResult get(JestClient jestClient, String indexName, String typeName, String id) throws Exception {
		  
	    Get get = new Get.Builder(indexName, id).type(typeName).build();
	    return jestClient.execute(get);
	}
}
