package com.pinyougou;

import com.pinyougou.mapper.TbUserMapper;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.pojo.TbUserExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-dao.xml")
public class AppTest 
{
    @Autowired
    private TbUserMapper userMapper;

    @Test
    public void add(){
        TbUser user = new TbUser();
        user.setUsername("zhuzhu");
        user.setPassword("123456");
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //userMapper.insert(user);
        userMapper.insertSelective(user);
        //insert和insertSelective区别：后者忽略null值！
    }

    @Test
    public void delete(){
        //userMapper.deleteByPrimaryKey(17l);
        //创建TbuserExample对象
        TbUserExample example = new TbUserExample();
        //创建条件对象
        TbUserExample.Criteria criteria = example.createCriteria();
        //设置条件
        criteria.andIdEqualTo(16l);
        userMapper.deleteByExample(example);
    }

    @Test
    public void update(){
        TbUser user = new TbUser();
        user.setUsername("jingjing");

        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andPhoneLike("%999999%");

        userMapper.updateByExampleSelective(user,example);
    }

    @Test
    public void search(){

        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameLike("%a%");
        List<TbUser> userList = userMapper.selectByExample(example);
        for (TbUser user : userList){
            System.out.println("username:=" + user.getUsername());
        }

    }
}
