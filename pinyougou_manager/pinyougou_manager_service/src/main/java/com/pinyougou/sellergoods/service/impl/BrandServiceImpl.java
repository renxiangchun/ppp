package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.common.PageResult;
import com.pinyougou.manager.service.BrandService;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

	@Autowired
	private TbBrandMapper brandMapper;

	@Override
	public List<TbBrand> findAll() {
		return brandMapper.findAll();
	}

	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		//先设置分页的属性
		PageHelper.startPage(pageNum,pageSize);
		//搜索
		Page<TbBrand> page = (Page<TbBrand>) brandMapper.findAll();
		//返回总数量，当前页结果
		return new PageResult(page.getTotal(),page.getResult());
	}

	@Override
	public void add(TbBrand brand) {
		brandMapper.add(brand);
	}

	@Override
	public TbBrand findOne(Long id) {
		return 	brandMapper.findOne(id);
	}

	@Override
	public void update(TbBrand brand) {
		brandMapper.update(brand);
	}

	@Override
	public void delete(Long[] ids) {

		for (Long id:ids){
			brandMapper.delete(id);
		}
	}

	@Override
	public PageResult search(Integer pageNum, Integer pageSize, TbBrand brand) {
		//设置分页属性
		PageHelper.startPage(pageNum,pageSize);
		Page<TbBrand> page = (Page<TbBrand>) brandMapper.search(brand);
		return new PageResult(page.getTotal(),page.getResult());
	}

	@Override
	public List<Map> selectOptionList() {
		return brandMapper.selectOptionList();
	}
}
