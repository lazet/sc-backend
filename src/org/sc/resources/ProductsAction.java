package org.sc.resources;

import static org.sc.base.Constants.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.sc.security.MongoDbUtil;
import org.sc.util.DateUtil;
import org.sc.util.GeneralResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.BasicDBList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.lang.StringUtils;
/**
 * 商品管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/product")
public class ProductsAction{
	static public String CATEGORY = "category";
	static public String PRODUCT_DEFINE = "productDefine";
	static public String PRODUCTS = "products";
	static public Log logger = LogFactory.getLog(ProductsAction.class);
	
	@RequestMapping("/add")
	public @ResponseBody String add(HttpServletRequest  request) {
		//获取商品定义列表
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(PRODUCT_DEFINE);
		List<DBObject> result = new ArrayList<DBObject>();
		DBCursor cursor = dbc.find();
		while(cursor.hasNext()){
			DBObject dbo = cursor.next();
			result.add(dbo);
		}
		DBCollection productc = MongoDbUtil.getCurrentDb().getCollection(PRODUCTS);
		//生成数据对象
		DBObject dbo =  new BasicDBObject();
		Set<String> keywords = new HashSet<String>(); 
		//校验数据，如果不合格，则返回错误信息
		for(DBObject o: result){
			String normalItemName = null;
			try{
				normalItemName = new String(((String) o.get(ITEM_NAME)).getBytes("iso-8859-1"),"utf-8");
			}
			catch(Exception e){
				e.printStackTrace();
				logger.info(o.get(ITEM_NAME),e);
			}
			String value = request.getParameter(normalItemName);
			System.out.println(value);
			//判断类型
			if (STRING_TYPE.equals(o.get(DATA_TYPE))){
				if(value != null && !"".equals(value)){
					keywords.add(value);
				}
				dbo.put((String) o.get(ITEM_NAME), value);
			}else if (INTEGER_TYPE.equals(o.get(DATA_TYPE))){
				if(value != null && !"".equals(value)){
					keywords.add(value);
				}
				try{
					dbo.put((String) o.get(ITEM_NAME), Integer.parseInt(value));
				}catch(Exception e){
					e.printStackTrace();
					logger.info(o.get(ITEM_NAME) + " " + o.get(DATA_TYPE),e);
				}
			}else if (MONEY_TYPE.equals(o.get(DATA_TYPE))){
				if(value != null && !"".equals(value)){
					keywords.add(value);
				}
				try{
					if(value != null)
						dbo.put((String) o.get(ITEM_NAME), Integer.parseInt(value));
				}catch(Exception e){
					e.printStackTrace();
					logger.info(o.get(ITEM_NAME) + " " + o.get(DATA_TYPE),e);
				}
			}else if (SELECT_ONE_TYPE.equals(o.get(DATA_TYPE))){
				if(value != null && !"".equals(value)){
					keywords.add(value);
				}
				try{
					dbo.put((String) o.get(ITEM_NAME), value);
				}catch(Exception e){
					e.printStackTrace();
					logger.info(o.get(ITEM_NAME) + " " + o.get(DATA_TYPE),e);
				}
			}else if (PRODUCT_CATEGORY_TYPE.equals(o.get(DATA_TYPE))){
				try{
					if(value != null){
						String[] tags = StringUtils.split(value, ' ');
						for (String tag : tags){
							keywords.add(tag);
						}
						dbo.put((String) o.get(ITEM_NAME), tags);
					}
				}catch(Exception e){
					e.printStackTrace();
					logger.info(o.get(ITEM_NAME) + " " + o.get(DATA_TYPE),e);
				}
			}else if (NOW.equals(o.get(DATA_TYPE))){
				keywords.add(DateUtil.getCurrentDate());
				dbo.put((String) o.get(ITEM_NAME), DateUtil.getCurrentTime());
			}else if (CURRENT_USER.equals(o.get(DATA_TYPE))){
				keywords.add(MongoDbUtil.getCurrentLoginName());
				dbo.put((String) o.get(ITEM_NAME), MongoDbUtil.getCurrentLoginName());
			}
			//校验唯一性
			if((Boolean) o.get(UNIQUE)){
				DBObject condition =  new BasicDBObject();
				if(value != null){
					condition.put((String) o.get(ITEM_NAME), value);
					DBObject r =productc.findOne(condition);
					if(r != null){
						return new GeneralResult("addProduct.failed",(String) o.get(ITEM_NAME) + "不唯一").toString();
					}
				}
			}
		}
		dbo.put(KEYWORDS, keywords);
		//保存
		try{
			productc.save(dbo);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return new GeneralResult("addProduct.success",dbo).toString();
	}
	/***********以下是品类管理*************/
	@RequestMapping("/category/get")
	public @ResponseBody String getCategory(@RequestParam("pageFrom") int pageFrom,@RequestParam("size") int size) {
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(CATEGORY);
		List result = new BasicDBList();
		DBCursor cursor = dbc.find().skip((pageFrom-1) * size).limit(size);
		int count = 0;
		while(cursor.hasNext()){
			if(count >= size){
				break;
			}
			DBObject dbo = cursor.next();
			result.add(dbo);
			count ++;
		}
		
		return new GeneralResult("getProductCategory.success",result).toString();
	}
	@RequestMapping("/category/delete")
	public @ResponseBody String deleteCategory(@RequestParam("label") String label,@RequestParam("pageFrom") int pageFrom,@RequestParam("size") int size) {
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(CATEGORY);
		DBObject dbLabel =  new BasicDBObject();
		dbLabel.put("label", label);
		dbc.remove(dbLabel);
		List result = new BasicDBList();
		DBCursor cursor = dbc.find().skip((pageFrom-1) * size).limit(size);
		int count = 0;
		while(cursor.hasNext()){
			if(count >= size){
				break;
			}
			DBObject dbo = cursor.next();
			result.add(dbo);
			count ++;
		}
		
		return new GeneralResult("deleteProductCategory.success",result).toString();
	}
	@RequestMapping("/category/add")
	public @ResponseBody String addCategory(@RequestParam("label") String label,@RequestParam("pageFrom") int pageFrom,@RequestParam("size") int size) {
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(CATEGORY);
		DBObject dbLabel =  new BasicDBObject();
		dbLabel.put("label", label);
		DBObject oldLabel = dbc.findOne(dbLabel);
		if(oldLabel != null){
			return new GeneralResult("addProductCategory.failed","品类已存在").toString();
		}
		else{
			dbc.save(dbLabel);
		}
		List result = new BasicDBList();
		DBCursor cursor = dbc.find().skip((pageFrom-1) * size).limit(size);
		int count = 0;
		while(cursor.hasNext()){
			if(count >= size){
				break;
			}
			DBObject dbo = cursor.next();
			result.add(dbo);
			count ++;
		}
		
		return new GeneralResult("addProductCategory.success",result).toString();
	}
 
}
