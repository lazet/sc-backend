package org.sc.resources;

import static org.sc.base.Constants.CURRENT_USER;
import static org.sc.base.Constants.DATA_TYPE;
import static org.sc.base.Constants.INTEGER_TYPE;
import static org.sc.base.Constants.ITEM_NAME;
import static org.sc.base.Constants.KEYWORDS;
import static org.sc.base.Constants.MONEY_TYPE;
import static org.sc.base.Constants.NOW;
import static org.sc.base.Constants.PRODUCT_CATEGORY_TYPE;
import static org.sc.base.Constants.SELECT_ONE_TYPE;
import static org.sc.base.Constants.STRING_TYPE;
import static org.sc.base.Constants.UNIQUE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sc.security.MongoDbUtil;
import org.sc.util.DateUtil;
import org.sc.util.GeneralResult;
import org.sc.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

@Controller
@RequestMapping("/discount")
public class DiscountAction {
	static public String DISCOUNT_DEFINE = "discountDefine";
	static public String DISCOUNT = "discount";
	static public Log logger = LogFactory.getLog(DiscountAction.class);
	
	@RequestMapping("/add")
	public @ResponseBody String add(HttpServletRequest  request) {
		//获取折扣定义列表
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(DISCOUNT_DEFINE);
		List<DBObject> result = new ArrayList<DBObject>();
		DBCursor cursor = dbc.find();
		while(cursor.hasNext()){
			DBObject dbo = cursor.next();
			result.add(dbo);
		}
		DBCollection discountC = MongoDbUtil.getCurrentDb().getCollection(DISCOUNT);
		//生成数据对象
		DBObject dbo =  new BasicDBObject();
		Set<String> keywords = new HashSet<String>(); 
		//校验数据，如果不合格，则返回错误信息
		for(DBObject o: result){
			String normalItemName = (String) o.get(ITEM_NAME);
			try{
			System.out.println(new String(normalItemName.getBytes("iso-8859-1"),"utf-8"));
			}
			catch(Exception e){}
			String value = request.getParameter(normalItemName);
			//判断类型
			if (STRING_TYPE.equals(o.get(DATA_TYPE))){
				if(value != null && !"".equals(value)){
					keywords.add(value);
				}
				dbo.put(normalItemName, value);
			}else if (INTEGER_TYPE.equals(o.get(DATA_TYPE))){
				if(value != null && !"".equals(value)){
					keywords.add(value);
				}
				try{
					dbo.put(normalItemName, Integer.parseInt(value));
				}catch(Exception e){
					e.printStackTrace();
					logger.info(normalItemName + " " + o.get(DATA_TYPE),e);
				}
			}else if (MONEY_TYPE.equals(o.get(DATA_TYPE))){
				if(value != null && !"".equals(value)){
					keywords.add(value);
				}
				try{
					if(value != null)
						dbo.put(normalItemName, (new BigDecimal(value).multiply(new BigDecimal("100"))).intValue());
				}catch(Exception e){
					e.printStackTrace();
					logger.info(normalItemName + " " + o.get(DATA_TYPE),e);
				}
			}else if (SELECT_ONE_TYPE.equals(o.get(DATA_TYPE))){
				if(value != null && !"".equals(value)){
					keywords.add(value);
				}
				try{
					dbo.put(normalItemName, value);
				}catch(Exception e){
					e.printStackTrace();
					logger.info(normalItemName + " " + o.get(DATA_TYPE),e);
				}
			}else if (PRODUCT_CATEGORY_TYPE.equals(o.get(DATA_TYPE))){
				try{
					if(value != null){
						String[] tags = StringUtils.split(value, ' ');
						Set<String> categorys = new HashSet<String>(); 
						for (String tag : tags){
							if(tag != null && !"".equals(tag)){
								keywords.add(tag);
								categorys.add(tag);
							}
						}
						dbo.put(normalItemName, categorys);
					}
				}catch(Exception e){
					e.printStackTrace();
					logger.info(normalItemName + " " + o.get(DATA_TYPE),e);
				}
			}else if (NOW.equals(o.get(DATA_TYPE))){
				keywords.add(DateUtil.getCurrentDate());
				dbo.put(normalItemName, DateUtil.getCurrentTime());
			}else if (CURRENT_USER.equals(o.get(DATA_TYPE))){
				keywords.add(MongoDbUtil.getCurrentLoginName());
				dbo.put(normalItemName, MongoDbUtil.getCurrentLoginName());
			}
			//校验是否唯一
			if((Boolean) o.get(UNIQUE)){
				DBObject condition =  new BasicDBObject();
				if(value != null){
					condition.put(normalItemName, value);
					DBObject r =discountC.findOne(condition);
					if(r != null){
						return new GeneralResult("addProduct.failed",normalItemName + StringUtil.toIso8859("不唯一")).toString();
					}
				}
			}
		}
		dbo.put(KEYWORDS, keywords);
		//保存
		WriteResult wr = null;
		try{
			wr = discountC.save(dbo);
		}
		catch(Exception e){
			e.printStackTrace();
			return new GeneralResult("addDiscount.failed",e.getMessage()).toString();
		}
		return new GeneralResult("addDiscount.success",wr.getLastError()).toString();
	}
}
