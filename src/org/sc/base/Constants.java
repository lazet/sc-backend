package org.sc.base;

public interface Constants {
	//系统库命名
	static public String SYS_DB ="sys";
	//基础信息库变量名
	static public String BASE_DB = "base";
	//基础库变量名
	static public String MERCHANT = "merchant";
	static public String MERCHANT_ID = "merchantId";
	static public String MERCHANT_NAME = "merchantName";
	static public String ROOT_USER_NAME = "rootUserName";
	static public String MERCHANT_TYPE = "merchantType";
	static public String DB_NAME = "dbName";
	//SESSION库变量命名
	static public String SESSION_DB = "session";	
	static public String SESSION_INFO = "sessionInfo";
	static public String SESSION_ID = "sessionId";
	//商户库变量名
	static public String USER = "user";
	static public String USER_ID = "userId";
	static public String LOGIN_NAME = "loginName";
	static public String USER_PASSWD = "passwd";
	static public String EMAIL = "email";
	static public String ENABLED = "enabled";
	
	//拥有可访问的数据范围，一般指所在单位（商户或门店）
	static public String IN_UNIT = "in_unit";
	//所在小组,用于确定角色和功能权限
	static public String IN_GROUP ="in_group";
	//所属级别（商户或门店）
	static public String IN_UNIT_LEVEL = "in_unit_level" ;
	
	//商户基础配置信息
	static public String CONFIG = "config";
	static public String CONFIG_ID = "configId";
	static public String CONFIG_VALUE = "configValue";
	
	//商户已拥有的模块（模块自动添加组和和组权限
	static public String MERCHANT_MODULE = "merchant_module";
	static public String MODULE_ID = "moduleId";
	static public String EXPIRED_DATE = "expiredDate";
	
	//用户组，相当于角色，但更偏向于用户的定义，比角色概念清晰
	static public String GROUP = "group";
	static public String GROUP_ID = "group_id";
	static public String GROUP_NAME = "group_name";
	//组的权限
	static public String GROUP_FUNCTIONS = "group_functions";
	
	//以下放在系统库的DB中(用于自定义授权）
	static public String FUNCTION = "function";
	static public String FUNCTION_ID = "function_id";
	static public String FUNCTION_NAME = "function_name";
	static public String FUNCTION_LEVEL = "function_level";
	
	//数据类型
	static public String STRING_TYPE = "String";
	static public String INTEGER_TYPE = "Integer";
	static public String MONEY_TYPE = "Money";
	static public String SELECT_ONE_TYPE = "SelectOne";
	static public String PRODUCT_CATEGORY_TYPE = "ProductCategory";
	static public String DATE_TYPE = "Date";
	static public String TIME_TYPE = "Time";
	static public String CURRENT_USER = "currentUser";
	static public String NOW = "now";
	static public String KEYWORDS = "keywords";
	
	//数据类型定义
	static public String ITEM_NAME = "itemName";
	static public String UNIQUE = "unique";
	static public String SHOW_IN_LIST = "showInList";
	static public String LIMITS = "limits";
	static public String REQUIRED = "required";
	static public String DATA_TYPE = "dataType";
	static public String BASIC = "basic";
	static public String ORDER = "order";
}
