package com.pinyougou.manager.service;

import com.pinyougou.common.PageResult;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.vo.SpecificationVo;

import java.util.List;
import java.util.Map;

public interface SpecificationService {

	/**
	 * 根据条件分页查询
	 * @param pageNum
	 * @param pageSize
	 * @param specification
	 * @return
	 */
	PageResult search(Integer pageNum, Integer pageSize, TbSpecification specification);

	/**
	 * 新增规格
	 * @param vo
	 */
	void add(SpecificationVo vo);

	/**
	 * 根据id回显实体类
	 * @param id
	 * @return
	 */
	public SpecificationVo findOne(long id);

	/**
	 * 修改
	 * @param vo
	 */
	public void update(SpecificationVo vo);

	/**
	 * 根据多个id删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 规格下拉框数据
	 * @return
	 */
	List<Map> findMap();
}
