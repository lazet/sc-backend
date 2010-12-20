package org.sc.resources;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static org.sc.base.Constants.*;

import org.sc.security.MongoDbUtil;
import org.sc.util.DateUtil;
import org.sc.util.GeneralResult;
import org.sc.util.IdUtil;
import org.sc.util.StringUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Controller
@RequestMapping("/consumeRecord")
public class ConsumeRecordAction {
	static public String CONSUME_RECORD_DEFINE = "consumeRecordDefine";
	static public String CONSUME_RECORD = "consumeRecord";
	static public Log logger = LogFactory.getLog(ConsumeRecordAction.class);
	
	@RequestMapping("/add")
	public @ResponseBody String add(@RequestParam("consumeRecord") String record) {
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(CONSUME_RECORD);
		DBObject dbo = (DBObject)JSON.parse(record);
		dbo.put(MERCHANT_ID, MongoDbUtil.getCurrentUnitName());
		dbo.put(CREATED_TIME, DateUtil.getCurrentTime());
		dbo.put(CREATOR, MongoDbUtil.getCurrentLoginName());
		dbo.put(ID, IdUtil.newId());
		dbo.put(KEYWORDS, generatedKeyWords(dbo,new HashSet<String>()));
		dbc.save(dbo);
		return new  GeneralResult("PURCHASED_PRODUCT_SAVED" ,dbo).toString();
	}
	@RequestMapping("/regret")
	public @ResponseBody String add(@RequestParam("id") String id, @RequestParam("reason") String reason) {
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(CONSUME_RECORD);
		DBObject dbo = dbc.findOne(new BasicDBObject("id",id).append(MERCHANT_ID, MongoDbUtil.getCurrentUnitName()));
		if(dbo == null)
			return new GeneralResult("regret.failed",StringUtil.toIso8859("要悔单的交易不存在")).toString();
		if(dbo.get(StringUtil.toIso8859("状态")) == StringUtil.toIso8859("已悔")){
			return new GeneralResult("regret.failed",StringUtil.toIso8859("此单已悔，不能再悔单")).toString();
		}
		else{
			dbo.put(StringUtil.toIso8859("状态"), StringUtil.toIso8859("已悔"));
		}
		BasicDBObject regret = new BasicDBObject(StringUtil.toIso8859("悔单原因"),reason)
			.append(StringUtil.toIso8859("悔单时间"), DateUtil.getCurrentTime())
			.append(StringUtil.toIso8859("悔单操作员"), MongoDbUtil.getCurrentLoginName());
		dbo.put(StringUtil.toIso8859("悔单信息"), regret);
		dbo.put(KEYWORDS, generatedKeyWords(dbo,new HashSet<String>()));
		dbc.save(dbo);
		return new  GeneralResult("PURCHASED_PRODUCT_REGRETED" ,dbo).toString();
	}
	protected Set<String> generatedKeyWords(DBObject dbo,Set<String> unindexItemNames){
		Set<String> keySet = dbo.keySet();
		Set<String> keywords = new HashSet<String>(); 
		for(String key:keySet){
			if(KEYWORDS.equals(key) || "_id".equals(key) || unindexItemNames.contains(key) || "mx_internal_uid".equals(key))
				continue;
			Object v = dbo.get(key);
			if(v == null)
				continue;
			else{
				if(v instanceof Collection){
					Collection c = (Collection)v;
					for(Object o: c){
						if(o == null){
							continue;
						}else if(o instanceof DBObject){
							keywords.addAll(generatedKeyWords((DBObject)o,unindexItemNames));
						}
						else{
							keywords.add(o.toString());
						}
					}
				}
				else if( v instanceof DBObject){
					keywords.addAll(generatedKeyWords((DBObject)v,unindexItemNames));
				}
				else{
					String value = String.valueOf(v);
					if("".equals(value))
						continue;
					keywords.add(value);
				}
			}
		}
		return keywords;
	}
	@RequestMapping("/searchkeywords")
	public @ResponseBody String searchKeywords(@RequestParam("condition") String condition){
		DBCollection dc = MongoDbUtil.getCurrentDb().getCollection(CONSUME_RECORD);
		DBObject dbcondition = (DBObject)JSON.parse(condition);
		List result = dc.distinct(KEYWORDS, dbcondition);
		return  new GeneralResult("consumeRecord.searchkeywords.success",result).toString();
	}
}
