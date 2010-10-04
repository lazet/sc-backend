package org.sc.base;

import static org.sc.base.Constants.*;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import org.apache.commons.lang.StringUtils;;

public class MerchantAction {
	private JavaMailSender mailSender = null;
	public JavaMailSender getMailSender() {
		return mailSender;
	}
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	private Mongo mongo;
	
	/**
	 * 检查编号是否是否有效
	 * @param merchantId
	 * @return
	 */
	public boolean checkIsValid(String merchantId){
		if(merchantId == null || BASE_DB.equalsIgnoreCase(merchantId)){
			return false;
		}
		DB db = mongo.getDB(BASE_DB);
		DBObject o = db.getCollection(MERCHANT).findOne(new BasicDBObject(MERCHANT_ID,merchantId));
		if (o!= null){
			return false;
		}
		else{
			return true;
		}
	}
	/**
	 * 商户信息注册
	 * /dwr/call/plaincall/MerchantAction.register.dwr
	 * @param merchantName
	 * @param userName
	 * @param password
	 * @param merchantType
	 * @return
	 */
	public String register(String merchantId,String merchantName,String email, String userName,String passwd, String merchantType){
		//检查数据的合法性
		if(StringUtils.isBlank(merchantId) || !StringUtils.isAlphanumeric(merchantId)){
			return "{'error':'true','reason':'merchantId is null!'}";
		}
		if(StringUtils.isBlank(merchantName)){
			return "{'error':'true','reason':'merchantName is null!'}";
		}
		if(StringUtils.isBlank(email)){
			return "{'error':'true','reason':'email is null!'}";
		}
		if(StringUtils.isBlank(userName)){
			return "{'error':'true','reason':'userName is null!'}";
		}
		if(StringUtils.isBlank(passwd)){
			return "{'error':'true','reason':'passwd is null!'}";
		}
		if(!("single".equals(merchantType.trim()) || "chain".equals(merchantType.trim()))){
			return "{'error':'true','reason':'merchantType error,should be single or chain!'}";
		}
		
		DB db = mongo.getDB(BASE_DB);
		DBObject o = db.getCollection(MERCHANT).findOne(new BasicDBObject(MERCHANT_ID,merchantId));
		if (o!= null){
			return "{'error':'true','reason':'merchantId used!'}";
		}
		else{
			DBObject dbo =  new BasicDBObject();
			dbo.put(MERCHANT_ID, merchantId);
			dbo.put(MERCHANT_NAME, merchantName);
			dbo.put(ROOT_USER_NAME, userName);
			dbo.put(MERCHANT_TYPE, merchantType);	
			db.getCollection(MERCHANT).save(dbo);
			//开户
			DB mdb = mongo.getDB(merchantId);
			mdb.getCollection(USER).ensureIndex(new BasicDBObject(USER_ID,1));
			//初始化用户表
			DBObject dbUser =  new BasicDBObject();
			dbUser.put(USER_ID, userName);
			dbUser.put(USER_PASSWD, passwd);
			dbUser.put(ENABLED, true);
			dbUser.put(EMAIL,email);
			
			mdb.getCollection(USER).save(dbUser);
			//初始化帐户信息表
			mdb.getCollection(CONFIG).ensureIndex(new BasicDBObject(CONFIG_ID,1));
			DBObject config =  new BasicDBObject();
			config.put(CONFIG_ID, MERCHANT_NAME);
			config.put(CONFIG_VALUE, merchantName);
			mdb.getCollection(CONFIG).save(config);
			
			//模块奖励(奖励哪些模块，调用模块初始化）

			
			sendMail(email,"开户通知","你的帐户已开通，下面是访问地址：" + merchantId);
			
			return "{'error':'false'}";
		}		
	}
	/***
	 * 发送邮件
	 * @param email
	 * @param content
	 */
	private boolean sendMail(String email, String subject, String content){
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(email);
			helper.setSubject(subject);
			helper.setText(content, true);
			this.mailSender.send(message);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}
	public Mongo getMongo() {
		return mongo;
	}
}
