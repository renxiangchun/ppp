package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.PageResult;
import com.pinyougou.common.Result;
import com.pinyougou.manager.service.BrandService;
import com.pinyougou.pojo.TbBrand;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

	@Reference
	private BrandService brandService;

	//返回全部列表
	@RequestMapping("findAll")
	public List<TbBrand> findAll(){
		List<TbBrand> brandList = brandService.findAll();
		return brandList;
	}

	//返回全部分页列表
	@RequestMapping("/findPage")
	public PageResult findPage(int pageNum, int pageSize){
		return brandService.findPage(pageNum,pageSize);
	}

	//增加
	@RequestMapping("/add")
	public Result add(@RequestBody TbBrand brand){
		try {
			brandService.add(brand);
			return new Result(true,"增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(true,"增加失败");
		}
	}

	@RequestMapping("/findOne")
	public TbBrand findOne(Long id){
		return brandService.findOne(id);
	}

	@RequestMapping("/update")
	public Result update(@RequestBody TbBrand brand){
		try {
			brandService.update(brand);
			return new Result(true,"修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"修改失败");
		}
	}

	@RequestMapping("/delete")
	public Result delete(Long[] ids){
		try {
			brandService.delete(ids);
			return new Result(true,"删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(true,"删除失败");
		}
	}

	@RequestMapping("/search")
	public PageResult search(Integer pageNum, Integer pageSize, @RequestBody TbBrand brand){
		PageResult pageResult = brandService.search(pageNum,pageSize,brand);
		return pageResult;
	}

	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return  brandService.selectOptionList();
	}
}
