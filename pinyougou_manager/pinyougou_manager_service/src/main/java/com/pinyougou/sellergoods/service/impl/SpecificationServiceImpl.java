package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.common.PageResult;
import com.pinyougou.manager.service.SpecificationService;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.pojo.vo.SpecificationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {
	@Autowired
	//注入规格mapper对象
	private TbSpecificationMapper specificationMapper;

	@Autowired
	//注入规格选项mapper对象
	private TbSpecificationOptionMapper specificationOptionMapper;

	@Override
	public PageResult search(Integer pageNum, Integer pageSize, TbSpecification specification) {

		//设置条件
		PageHelper.startPage(pageNum,pageSize);

		//判断条件
		TbSpecificationExample example = new TbSpecificationExample();
		TbSpecificationExample.Criteria criteria = example.createCriteria();
		if(specification.getId() != null){
			criteria.andIdEqualTo(specification.getId());
		}
		if (specification.getSpecName() != null){
			criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
		}
		Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(example);

		return new PageResult(page.getTotal(),page.getResult());
	}

	@Override
	public void add(SpecificationVo vo) {

		//先添加规格
		specificationMapper.insertSelective(vo.getSpecification());

		//再添加规格选项
		List<TbSpecificationOption> specificationOptionList = vo.getSpecificationOptionList();
		for(TbSpecificationOption option : specificationOptionList){
			//设置选项对应的规格ID
			option.setSpecId(vo.getSpecification().getId());
			specificationOptionMapper.insertSelective(option);
		}
	}

	@Override
	public SpecificationVo findOne(long id) {

		//先查询规格
		TbSpecification specification = specificationMapper.selectByPrimaryKey(id);
		//再查询规格选项
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);
		example.setOrderByClause("orders asc");
		List<TbSpecificationOption> specificationOptionList = specificationOptionMapper.selectByExample(example);
		//封装返回结果集
		SpecificationVo vo = new SpecificationVo();
		vo.setSpecificationOptionList(specificationOptionList);
		vo.setSpecification(specification);

		return vo;
	}

	@Override
	public void update(SpecificationVo vo) {

		//修改规格
		specificationMapper.updateByPrimaryKeySelective(vo.getSpecification());
		//修改规格选项？
		//先删除再添加
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(vo.getSpecification().getId());
		specificationOptionMapper.deleteByExample(example);

		List<TbSpecificationOption> specificationOptionList = vo.getSpecificationOptionList();
		for (TbSpecificationOption option:specificationOptionList){
			//设置选项对应的规格id
			option.setSpecId(vo.getSpecification().getId());
			specificationOptionMapper.insertSelective(option);
		}
	}

	@Override
	public void delete(Long[] ids) {
		for (Long id:ids){
			//规格删除，根据主键
			specificationMapper.deleteByPrimaryKey(id);
			//删除规格选项
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(id);
			specificationOptionMapper.deleteByExample(example);
		}
	}

	@Override
	public List<Map> findMap() {
		return specificationMapper.findMap();
	}
}
