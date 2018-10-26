package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinoyougou.search.service.SearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@RequestMapping("/search")
public class SearchController {

	@Reference(timeout = 1000000)
	private SearchService searchService;

	@RequestMapping("search")
	public Map search(@RequestBody Map map){

		//接受页面传递的参数可以用POJO对象，但是，POJO对象中属性名必须和传递过来的对象中的属性名一致；
		//有：页面中的属性名不确定那么后台接受的这个POJO对象属性名也确定，这个后台的POJO属性名会根据前台的对象属性名变动而变动
		//建议：用Map对象来接受！这样就不会根据前端对象来改变了！
		//执行查询、
		Map searchMap = searchService.search(map);
		return searchMap;
	}
}
