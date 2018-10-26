package com.pinyougou.mapper;

import com.pinyougou.pojo.TbBrand;

import java.util.List;
import java.util.Map;

public interface TbBrandMapper {

	public List<TbBrand> findAll();

	void add(TbBrand brand);

	//根据ID查询品牌数据，用于修改回显
	TbBrand findOne(Long id);

	//修改品牌
	void update(TbBrand brand);

	void delete(Long id);

	List<TbBrand> search(TbBrand brand);

	List<Map> selectOptionList();
}
