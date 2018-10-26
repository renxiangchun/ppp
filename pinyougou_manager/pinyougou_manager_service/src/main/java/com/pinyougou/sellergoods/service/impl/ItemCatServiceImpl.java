package com.pinyougou.sellergoods.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.common.PageResult;
import com.pinyougou.manager.service.ItemCatService;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemCatExample;
import com.pinyougou.pojo.TbItemCatExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbItemCat> findAll() {
		return itemCatMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbItemCat> page=   (Page<TbItemCat>) itemCatMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbItemCat itemCat) {
		itemCatMapper.insert(itemCat);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbItemCat itemCat){
		itemCatMapper.updateByPrimaryKey(itemCat);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbItemCat findOne(Long id){
		return itemCatMapper.selectByPrimaryKey(id);
	}

	/**
	 * 从页面选择复选框（选择了selectIDS——>Controller接受，然后调用Service到实现类）
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){

			//创建一个集合，把要删除的id和子节点的所有id加到这个集合中
			List<Long> idList = new ArrayList<>();
			idList.add(id);
			//用递归的方法把当前id作为父节点查询子节点
			this.getIds(id,idList);
			//itemCatMapper.deleteByPrimaryKey(id);

			TbItemCatExample example = new TbItemCatExample();
			example.createCriteria().andIdIn(idList);
			itemCatMapper.deleteByExample(example);
		}		
	}
	/**
	 * 专门来根据父节点查询子节点的ID列表
	 * @param id  1
	 * @param idList
	 */
	private void getIds(Long id,List<Long> idList){
		TbItemCatExample example = new TbItemCatExample();
		example.createCriteria().andParentIdEqualTo(id);
		List<TbItemCat> itemCatList = itemCatMapper.selectByExample(example);
		for (TbItemCat itemCat:itemCatList){
			//1、把id放在list集合中
			idList.add(itemCat.getId());
			//2、根据此id查询子节点id的列表
			this.getIds(itemCat.getId(),idList);
		}
	}
	
	
		@Override
	public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbItemCatExample example=new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		
		if(itemCat!=null){			
						if(itemCat.getName()!=null && itemCat.getName().length()>0){
				criteria.andNameLike("%"+itemCat.getName()+"%");
			}
	
		}
		
		Page<TbItemCat> page= (Page<TbItemCat>)itemCatMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<TbItemCat> findByParentId(Long parentId) {

		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		return itemCatMapper.selectByExample(example);
	}


}
