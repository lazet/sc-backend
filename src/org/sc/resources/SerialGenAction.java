package org.sc.resources;

import javax.servlet.http.HttpServletRequest;

import static org.sc.base.Constants.*;

import org.sc.security.MongoDbUtil;
import org.sc.util.DateUtil;
import org.sc.util.GeneralResult;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONCallback;

@Controller
@RequestMapping("/serial")
public class SerialGenAction {
	
	@RequestMapping(value="/gen", method=RequestMethod.POST )
	public @ResponseBody String gen(@RequestParam(value="type", required=true)String type,
			@RequestParam(value="key", required=true)String key,
			@RequestParam(value="number", required=true)int number
			){
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(SERIAL);
		BasicDBObject dbcondition = MongoDbUtil.newQueryDBObject();
		dbcondition.append(SERIAL_TYPE, type)
			.append(SERIAL_KEY, key);
		
		DBObject dbIncrease = new BasicDBObject();
		dbIncrease.put("$inc", new BasicDBObject(SERIAL_VALUE, number));
		
		DBObject dbo = dbc.findAndModify(dbcondition, null, null, false, dbIncrease, true, true);
		dbo.put("number", number);
		return new  GeneralResult( "serial." + type ,dbo).toString();
	}
	@RequestMapping(value="/consume", method=RequestMethod.POST )
	public @ResponseBody String genConsumeSerialCode(
			@RequestParam(value="number", required=true)int number
			){
		String type = "consume";
		String key = DateUtil.getCurrentDate();
		return gen(type, key, number);
	}
}
