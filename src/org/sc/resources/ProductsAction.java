package org.sc.resources;

import static org.sc.base.Constants.USER;

import java.util.Iterator;
import java.util.List;

import org.sc.security.MongoDbUtil;
import org.sc.util.GeneralResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.BasicDBList;

/**
 * 商品管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/product")
public class ProductsAction{
	static public String CATEGORY = "category";
	@RequestMapping("/add")
	public @ResponseBody String add(
			@RequestParam("productName") String productName,
			@RequestParam("currentPrice") String currentPrice,
			@RequestParam("sn") String sn,
			@RequestParam("unit") String unit,
			@RequestParam("category") String category,
			@RequestParam("mnemonicCode") String mnemonicCode,
			@RequestParam("productDesc") String productDesc) {
		
		
		return null;
	}
	@RequestMapping("/category/get")
	public @ResponseBody String getCategory(@RequestParam("size") int size) {
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(CATEGORY);
		List result = new BasicDBList();
		DBCursor cursor = dbc.find();
		int count = 0;
		while(cursor.hasNext()){
			if(count >= size){
				break;
			}
			DBObject dbo = cursor.next();
			result.add(dbo);
			count ++;
		}
		
		return new GeneralResult("getProductCategory.success",result.toString()).toJson();
	}
}
