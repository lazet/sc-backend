package org.sc.module;

import java.util.List;

/**
 * 模块操作
 * @author admin
 *
 */
public abstract class ModuleAction {
	/**
	 * 获取已开通的功能列表
	 * @return
	 */
	abstract List<Module> getOpenedModules();
	/**
	 * 获取未开通的模块列表
	 * @return
	 */
	abstract List<Module> getUnopenedModules();
	/**
	 * 开通一个模块
	 * @param moduleId
	 * @return
	 */
	abstract boolean openModule(String moduleId);
}
