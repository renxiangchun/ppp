package com.pinyougou.shop.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.manager.service.SellerService;
import com.pinyougou.pojo.TbSeller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
//商家认证类
public class UserDetailServiceImpl implements UserDetailsService {
	@Reference
	private SellerService sellerService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		//设置权限集合
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		//1.根据用户名查询商家
		TbSeller seller = sellerService.findOne(username);
		//2.判断商家是否存在
		if (seller != null){
			//2.1如果存在，判断商家状态是否审核通过
			if ("1".equals(seller.getStatus())){
				//2.2如果审核通过，那么根据用户名和密码判断登录
				return new User(username,seller.getPassword(),authorities);
			}
		}
		//3.如果不存在直接返回null
		return null;

	}
}
