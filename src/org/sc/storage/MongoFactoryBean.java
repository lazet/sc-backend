package org.sc.storage;

import static org.sc.base.Constants.*;

import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;

import org.sc.security.MongoDbUtil;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class MongoFactoryBean implements InitializingBean,FactoryBean{
	private String address;
	public String getAddress(){
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	private int port;
	
	private Mongo m;
	
	@Override
	public void afterPropertiesSet() throws Exception {

		m = new Mongo( address , port );

		//初始化商户列表的索引
		m.getDB(BASE_DB).getCollection(MERCHANT).ensureIndex(new BasicDBObject(MERCHANT_ID, 1));
		MongoDbUtil.setMongo(m);
	}
	@Override
	public Object getObject() throws Exception {
		return m;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Class getObjectType() {
		return Mongo.class;
	}
	@Override
	public boolean isSingleton() {
		return true;
	}
}
