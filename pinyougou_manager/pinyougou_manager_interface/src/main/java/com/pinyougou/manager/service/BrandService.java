package com.pinyougou.manager.service;

import com.pinyougou.common.PageResult;
import com.pinyougou.pojo.TbBrand;

import java.util.List;
import java.util.Map;

public interface BrandService {

	/**
	 * 好习惯：在接口上加注释
	 * 品牌列表
	 * @return
	 */
	public List<TbBrand> findAll();

	/**
	 * 返回全部分页列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	PageResult findPage(int pageNum, int pageSize);

	/**
	 * 增加
	 * @param brand
	 */
	public void add(TbBrand brand);

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbBrand findOne(Long id);

	/**
	 * 修改
	 * @param brand
	 */
	public void update(TbBrand brand);

	/**
	 * 根据多个id来删除品牌
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 根据对象分页搜索
	 * @param pageNum 当前页
	 * @param pageSize 每页大小
	 * @param brand 品牌对象
	 * @return
	 */
	PageResult search(Integer pageNum,Integer pageSize,TbBrand brand);

	/**
	 * 品牌下拉框数据
	 * @return
	 */
	List<Map> selectOptionList();
}
