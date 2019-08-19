package com.xms.spring.esDynamic.factory;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

/**
 * EliasticSearch连接池工厂对象
 * @author xieminsen
 *
 */
public class EsClientPoolFactory implements PooledObjectFactory<JestClient>{

	
	/**
	 * 生产对象
	 */
//	@SuppressWarnings({ "resource" })
	@Override
	public PooledObject<JestClient> makeObject() throws Exception {
		JestClientFactory factory = new JestClientFactory();

		String connectionUrl = "http://192.168.233.171:9200";
		//Settings settings = Settings.builder().put("cluster.name","elasticsearch").build();
		JestClient client = null;
		try {
			/*client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"),9300));*/
			factory.setHttpClientConfig(new HttpClientConfig
			        .Builder(connectionUrl) //参数可以是集群，请先定义一个list集合，将节点url分别添加到list
			        //.defaultCredentials("elastic","changeme") //如果使用了x-pack，就要添加用户名和密码
			        .multiThreaded(true) //多线程模式
			        .connTimeout(60000) //连接超时
			        .readTimeout(60000) //由于是基于http，所以超时时间必不可少，不然经常会遇到socket异常：read time out
			        .build()); //更多参数请查看api
			 client=factory.getObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new DefaultPooledObject<JestClient>(client);
	}

	/**
	 * 销毁对象
	 */
	@Override
	public void destroyObject(PooledObject<JestClient> p) throws Exception {
		JestClient highLevelClient = p.getObject();
		highLevelClient.close();
		
	}

	@Override
	public boolean validateObject(PooledObject<JestClient> p) {
		return true;
	}

	@Override
	public void activateObject(PooledObject<JestClient> p) throws Exception {
		System.out.println("activateObject");
		
	}

	@Override
	public void passivateObject(PooledObject<JestClient> p) throws Exception {
		System.out.println("passivateObject");
		
	}

}
