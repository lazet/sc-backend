package org.sc.storage;

import java.util.List;

import org.sc.security.MongoDbUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBList;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import org.sc.util.GeneralResult;

@Controller
@RequestMapping("/data")
public class DataAction {
	@RequestMapping("/insert")
	public @ResponseBody String save(
			@RequestParam("collection") String collection,
			@RequestParam("obj") String json
			){
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(collection);
		DBObject dbo = (DBObject)JSON.parse(json);
		dbc.save(dbo);
		return new  GeneralResult(collection + ".insert" ,dbo).toString();
	}
	@RequestMapping("/update")
	public @ResponseBody String update(
			@RequestParam("collection") String collection,
			@RequestParam("condition") String condition,
			@RequestParam("obj") String json
			){
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(collection);
		DBObject dbcondition = (DBObject)JSON.parse(condition);
		DBObject dbo = (DBObject)JSON.parse("{\"$set\":" + json + "}");
		
		DBObject oldObject = dbc.findAndModify(dbcondition, dbo);
		return new  GeneralResult(collection + ".update" ,oldObject).toString();
	}
	@RequestMapping("/get")
	public @ResponseBody String get(
			@RequestParam("collection") String collection,
			@RequestParam("condition") String condition
			){
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(collection);
		DBObject dbcondition = (DBObject)JSON.parse(condition);
		DBObject dbo = dbc.findOne(dbcondition);
		return new  GeneralResult(collection + ".get" ,dbo).toString();
	}
	@RequestMapping("/delete")
	public @ResponseBody String delete(
			@RequestParam("collection") String collection,
			@RequestParam("condition") String condition
			){
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(collection);
		DBObject dbcondition = (DBObject)JSON.parse(condition);
		DBObject dbo = dbc.findAndRemove(dbcondition);
		return new  GeneralResult(collection + ".delete" ,dbo).toString();
	}
	@RequestMapping("/findAll")
	public @ResponseBody String find(
			@RequestParam("collection") String collection,
			@RequestParam("condition") String condition,
			@RequestParam("order") String order
			){
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(collection);
		DBObject dbcondition = (DBObject)JSON.parse(condition);
		DBObject orderBy = (DBObject)JSON.parse(order);
		DBCursor cursor = null;
		if(order == null || "".equals(order))
			cursor = dbc.find(dbcondition);
		else
			cursor = dbc.find(dbcondition).sort(orderBy);
		List result = new BasicDBList();
		int count = 0;
		while(cursor.hasNext()){
			if(count >= 500){
				break;
			}
			DBObject dbo = cursor.next();
			result.add(dbo);
			count ++;
		}
		return new GeneralResult(collection + ".findAll",result).toString();
	}
	@RequestMapping("/findByPage")
	public @ResponseBody String findByPage(
			@RequestParam("collection") String collection,
			@RequestParam("condition") String condition,
			@RequestParam("order") String order,
			@RequestParam("pageFrom") int pageFrom,
			@RequestParam("size") int size
			){
		DBCollection dbc = MongoDbUtil.getCurrentDb().getCollection(collection);
		DBObject dbcondition = (DBObject)JSON.parse(condition);
		DBObject orderBy = (DBObject)JSON.parse(order);
		
		DBCursor cursor = null;
		if(order == null || "".equals(order))
			cursor = dbc.find(dbcondition).skip((pageFrom-1) * size).limit(size);
		else
			cursor = dbc.find(dbcondition).sort(orderBy).skip((pageFrom-1) * size).limit(size);
		List result = new BasicDBList();
		int count = 0;
		while(cursor.hasNext()){
			if(count >= 500){
				break;
			}
			DBObject dbo = cursor.next();
			result.add(dbo);
			count ++;
		}		
		return new GeneralResult(collection + ".findByPage",result).toString();
	}
}
