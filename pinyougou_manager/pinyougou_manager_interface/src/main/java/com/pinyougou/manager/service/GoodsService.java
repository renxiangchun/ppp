package com.pinyougou.manager.service;
import com.pinyougou.common.PageResult;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.vo.GoodsVo;

import java.util.List;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbGoods goods);
	
	
	/**
	 * 修改
	 */
	public void update(TbGoods goods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbGoods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize);

	/**
	 * 增加包装类
	 * @param vo
	 */
	void addVo(GoodsVo vo);

	/**
	 * 运营商更改通过状态
	 * @param ids
	 * @param status
	 */
	void updateStatus(Long[] ids,String status);

	void maketableStatus(Long[] ids,String status);
}
