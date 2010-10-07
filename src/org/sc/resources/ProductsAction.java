package org.sc.resources;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/product")
public class ProductsAction{
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
}
