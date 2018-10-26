package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.PageResult;
import com.pinyougou.common.Result;
import com.pinyougou.manager.service.SpecificationService;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.vo.SpecificationVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("specification")
public class SpecificationController {

	@Reference
	private SpecificationService specificationService;

	@RequestMapping("search")
	public PageResult search(Integer pageNum, Integer pageSize, @RequestBody TbSpecification specification){
		return  specificationService.search(pageNum,pageSize,specification);
	}

	@RequestMapping("add")
	public Result add(@RequestBody SpecificationVo vo){
		try {
			specificationService.add(vo);
			return new Result(true,"操作成功" );
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"操作失败");
		}
	}

	@RequestMapping("/findOne")
	public SpecificationVo findOne(Long id){
		SpecificationVo vo = specificationService.findOne(id);
		return vo;
	}

	@RequestMapping("/update")
	public Result update(@RequestBody SpecificationVo vo){
		try {
			specificationService.update(vo);
			return new Result(true,"操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"操作失败");
		}
	}

	@RequestMapping("/delete")
	public Result delete(Long[] ids){
		try {
			specificationService.delete(ids);
			return new Result(true,"操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"操作失败");
		}
	}

	@RequestMapping("findMap")
	public List<Map> findMap(){
		return specificationService.findMap();
	}
}
