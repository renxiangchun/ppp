package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.vo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service(timeout = 600000)
public class CartServiceImpl implements CartService {

	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	//登录后将redis中的购物车集合删除
	public void delCartListRedis(String key) {
		redisTemplate.boundHashOps("cartList").delete(key);
	}

	@Override
	public void addCartListToRedis(String key, List<Cart> cartList) {
		redisTemplate.boundHashOps("cartList").put(key,cartList);
	}

	@Override
	//把未登录的购物车集合和登录的购物车集合合并
	public List<Cart> mergeCartList(List<Cart> cartList, List<Cart> cartList_session) {
		for (Cart cart:cartList_session){
			for(TbOrderItem orderItem:cart.getOrderItemList()){
				this.addGoodsCartList(cartList,orderItem.getItemId(),orderItem.getNum());
			}
		}
		return cartList;
	}

	@Override
	public List<Cart> queryCartListByRedis(String key) {
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(key);
		if(cartList != null){
			return cartList;
		}
		return new ArrayList<>();
	}

	@Override
	public List<Cart> addGoodsCartList(List<Cart> cartList, Long itemId, Integer num) {

		//1、根据SKUID获取item对象
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//2、判断此item对象是否正确的，判断状态是否正确：1
		if( item == null){
			throw new RuntimeException("没有此商品");
		}
		if(!"1".equals(item.getStatus())){
			throw new RuntimeException("无效的商品");
		}
		//3、判断此商品对应的商家ID是否在购物车集合中
	 	Cart cart = this.selectCartByCartList(cartList,item.getSellerId());
		//4、如果此商品对应的商家没有在购物车集合中：添加一个购物车对象
		if(cart == null){
			//新建购物明细列表
			cart = new Cart();
			cart.setSellerId(item.getSellerId());
			cart.setSellerName(item.getSeller());
			//新建订单商品明细列表
			List<TbOrderItem> orderItemList = new ArrayList<>();
			//新建订单商品明细对象
			TbOrderItem orderItem = this.createOrderItem(item,num);
			//把商品明细对象加入商品明细列表中
			orderItemList.add(orderItem);
			//把商品明细列表加入购物车对象中
			cart.setOrderItemList(orderItemList);
			//把购物车对象加入购物车集合中
			cartList.add(cart);
		} else {
			//5、如果此商品对应的商家在购物车集合中，判断此商品是否在商品明细列表中
			TbOrderItem orderItem =  this.selectOrderItemByList(cart.getOrderItemList(),item.getId());
			//5.1如果此商品没有在购物车明细列表中:添加一个商品到购物车明细列表中
			if(orderItem == null){
				TbOrderItem orderItem2 = this.createOrderItem(item,num);
				cart.getOrderItemList().add(orderItem2);
			} else {

				//5.2如果此商品在商品明细列表中，添加商品的数量和修改总价格
				orderItem.setNum(orderItem.getNum() + num);
				orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue() * orderItem.getNum()));
				//5.3如果此商品的数量如果没有了呢？那么就在购物车明细列表中删除
				if(orderItem.getNum() < 1){
					cart.getOrderItemList().remove(orderItem);
				}
				//5.4如果此商品对应的购物车明细列表集合没有数据了呢，这个购物车对象就应该在这个集合中删除！
				if(cart.getOrderItemList().size() == 0){
					cartList.remove(cart);
				}
			}
		}

		return cartList;
	}

	/**
	 * 根据商品SKUID查询是否在商品明细列表中
	 * @param orderItemList
	 * @param itemId
	 * @return
	 */
	private TbOrderItem selectOrderItemByList(List<TbOrderItem> orderItemList,Long itemId){
		for (TbOrderItem orderItem:orderItemList){
			if(orderItem.getItemId().longValue() == itemId.longValue()){
				return orderItem;
			}
		}
		return null;
	}

	/**
	 * 根据item商品和数量创建订单商品明细对象
	 * @param item
	 * @param num
	 * @return
	 */
	private TbOrderItem createOrderItem(TbItem item,Integer num ){
		TbOrderItem orderItem = new TbOrderItem();
		orderItem.setGoodsId(item.getGoodsId());
		orderItem.setItemId(item.getId());
		orderItem.setNum(num);
		orderItem.setPicPath(item.getImage());
		orderItem.setPrice(item.getPrice());
		orderItem.setTitle(item.getTitle());
		orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue() * orderItem.getNum()));
		return orderItem;
	}

	/**
	 * 根据sellerId,判断商家在购物车集合中查询是否存在
	 * @param cartList
	 * @param sellerId
	 * @return
	 */
	private Cart selectCartByCartList(List<Cart> cartList,String sellerId){
		for( Cart cart:cartList){
			if(sellerId.equals(cart.getSellerId())){
				return cart;
			}
		}
		return null;
	}
}
