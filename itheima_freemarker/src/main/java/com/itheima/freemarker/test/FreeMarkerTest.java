package com.itheima.freemarker.test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class FreeMarkerTest {

	@Test
	public void test() throws Exception {

		//1、创建Freemarker的Configuration核心对象:需要指定FreeMarker的版本号
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
		//2、用核心对象指定模板的地址
		configuration.setDirectoryForTemplateLoading(new File("D:\\itheima\\IdeaProjects\\itheima_freemarker\\src\\main\\resources"));
		//3、设置模板的编码集
		configuration.setDefaultEncoding("UTF-8");
		//4、创建模板对象：根据核心对象中设置的模板路径来获取模板对象:官方推荐使用模板名后缀.ftl
		Template template = configuration.getTemplate("test.ftl");
		//5、封装数据：官方推荐使用Map对象来封装
		Map map = new HashMap<>();
		map.put("name","张三");
		map.put("message","欢迎来到Freemarker世界");
		map.put("freemarkers","include引入的模板");
		map.put("success",false);

		List goodsList=new ArrayList();
		Map goods1=new HashMap();
		goods1.put("name", "苹果");
		goods1.put("price", 5.8);
		Map goods2=new HashMap();
		goods2.put("name", "香蕉");
		goods2.put("price", 2.5);
		Map goods3=new HashMap();
		goods3.put("name", "橘子");
		goods3.put("price", 3.2);
		goodsList.add(goods1);
		goodsList.add(goods2);
		goodsList.add(goods3);
		map.put("goodsList", goodsList);

		map.put("date",new Date());
		map.put("goodsId",2345678945678l);

		map.put("aaa","Freemarker很简单！");
		//6、new FileWriter文件指定输出的文件路径
		FileWriter fileWriter = new FileWriter(new File("D:\\item\\123.html"));
		//7、用模板对象中的process方法来输出
		//参数说明：1、指定输出的数据;2、指定输出的文件路径流
		template.process(map,fileWriter);
		//8、关闭流
		fileWriter.close();
	}
}
