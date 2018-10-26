package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.common.Result;
import com.pinyougou.pojo.vo.Cart;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Reference(timeout = 600000)
	private CartService cartService;

	@RequestMapping("/addGoodsCartList")
	//http://cart.pinyougou.com/cart/addGoodsCartList?itemId=1369280&num=1
	//是商品详情页面过来添加商品到购物车中的，那么你需要告诉它添加是否成功
	//@CrossOrigin(origins="http://item.pinyougou.com",allowCredentials="true") 注解设置其他服务器能访问此接口
	public Result addGoodsCartList(HttpSession session, Long itemId, Integer num, HttpServletResponse response){
		//设置哪个服务器能访问此接口
		//可以设置域名也可以设置IP，但一般设置设置
		response.setHeader("Access-Control-Allow-Origin", "http://item.pinyougou.com");
		//允许传递Cookie
		response.setHeader("Access-Control-Allow-Credentials", "true");
		try {
			//1、获取SessionID
			String key = session.getId();
			//获取当前登录者用户名
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			if("anonymousUser".equals(name)){
				//2.根据SessionID去redis中获取购物车集合
				List<Cart> cartList = cartService.queryCartListByRedis(key);
				//3、把商品加到购物车集合中
				cartList = cartService.addGoodsCartList(cartList,itemId,num);
				//4、再新的购物车集合放在redis中
				cartService.addCartListToRedis(key,cartList);
				//5、返回结果集
				return  new Result(true,"添加成功");
			} else {
				List<Cart> cartList = cartService.queryCartListByRedis(name);
				//3、把商品加到购物车集合中
				cartList = cartService.addGoodsCartList(cartList,itemId,num);
				//4、再新的购物车集合放在redis中
				cartService.addCartListToRedis(name,cartList);
				//5、返回结果集
				return  new Result(true,"添加成功");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return  new Result(false,"添加失败");
		}
	}

	@RequestMapping("/findCartList")
	public List<Cart> findCartList(HttpSession session){
		//1、获取HTTPSessionId
		String key = session.getId();
		//获取当前登录者用户名
		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		//2、根据sessionID去redis中获取数据
		List<Cart> cartList_session = cartService.queryCartListByRedis(key);

		if("anonymousUser".equals(name)){
			//3、返回结果集
			return cartList_session;
		}
		//上面的Session中的集合有没有，如果没有直接跳过，如果有：此时应该和sessionID对应的购物车集合整合
		//获取当前登录者对应的数据
		List<Cart> cartList = cartService.queryCartListByRedis(name);
		if(cartList_session.size() > 0){
			//合并购物车
			cartList = cartService.mergeCartList(cartList,cartList_session);
			//清除redis中sessionId的数据
			cartService.delCartListRedis(key);
			//将合并后的数据存入redis
			cartService.addCartListToRedis(name,cartList);
		}
		//3、返回结果集
		return cartList;
	}
}
