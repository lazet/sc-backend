package org.sc.module.impl;

import org.sc.module.ModuleInitialize;

/**
 * 当开通一个模块时，执行如下操作：
 * 通用模块初始化，一般是赋予用户组权限，可通过Spring Bean 配置用户组和其权限，其次是赋予使用截止日期（默认为30天有效期）
 * @author admin
 *
 */
public class GeneralModuleInitialize implements ModuleInitialize{

	@Override
	public void init(String merchantId) {
		//初始化权限
		
		//初始化组
		
	}

}
