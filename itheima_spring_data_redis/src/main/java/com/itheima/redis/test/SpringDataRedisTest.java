package com.itheima.redis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext-redis.xml")
public class SpringDataRedisTest {

	@Autowired
	private RedisTemplate redisTemplate;

	//操作Key-value类型
	@Test
	public void addValue(){
		redisTemplate.boundValueOps("nameValue").set("张飞");
	}

	@Test
	public void queryValue(){
		System.err.println(redisTemplate.boundValueOps("nameValue").get());
	}

	//删除
	@Test
	public void deleteValue(){
		redisTemplate.delete("nameSet");
	}

	//Set 类型的
	@Test
	public void addSetValue(){
		redisTemplate.boundSetOps("nameSet").add("a");
		redisTemplate.boundSetOps("nameSet").add("b");
		redisTemplate.boundSetOps("nameSet").add("c");
		redisTemplate.boundSetOps("nameSet").add("d");
	}

	@Test
	public void querySetValue(){
		System.err.println(redisTemplate.boundSetOps("nameSet").members());
	}

	//List类型的

	/**
	 * List集合中添加有两种方式：左压栈，右压栈
	 * 左压栈：按顺序把添加的数据向左排序
	 * 右压栈：按顺序把添加的数据向右 排序
	 */
	@Test
	public void addListValueleftPush(){
		redisTemplate.boundListOps("nameList").leftPush("a");
		redisTemplate.boundListOps("nameList").leftPush("b");
		redisTemplate.boundListOps("nameList").leftPush("c");
		redisTemplate.boundListOps("nameList").leftPush("d");
	}

	@Test
	public void queryListValue(){
		List<String> nameList = redisTemplate.boundListOps("nameList").range(0, 10);
		for(String name : nameList){
			System.err.println(name);
		}
	}

	@Test
	public void addListValue(){
		redisTemplate.boundListOps("nameList").rightPush("1");
		redisTemplate.boundListOps("nameList").rightPush("2");
		redisTemplate.boundListOps("nameList").rightPush("3");
		redisTemplate.boundListOps("nameList").rightPush("4");
	}

	//SpringDataRedis是为了redis队列而写的！
	//Redis是单利的，

	@Test
	public void addHashValue(){
		//Key , Hash<key,value> key-value

		redisTemplate.boundHashOps("nameHash").put("a","张飞");
		redisTemplate.boundHashOps("nameHash").put("b","刘备");
		redisTemplate.boundHashOps("nameHash").put("c","关羽");
		redisTemplate.boundHashOps("nameHash").put("d","吕布");

	}

	@Test
	public void queryHashValueKey(){
		System.err.println(redisTemplate.boundHashOps("nameHash").keys());
	}

	@Test
	public void queryHashValue(){
		System.err.println(redisTemplate.boundHashOps("nameHash").values());
	}

	@Test
	public void queryHash(){
		//System.err.println(redisTemplate.boundHashOps("nameHash").get("a"));

		System.err.println(redisTemplate.boundHashOps("nameHash").delete("b"));
	}
}
