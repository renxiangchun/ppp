package com.itheima.fastdfs;

import org.csource.fastdfs.*;
import org.junit.jupiter.api.Test;

public class FastDFSTest {

	/**
	 * 测试FastDFS怎么使用
	 * 怎么上传图片
	 */
	@Test
	public void test() throws Exception{
		//1、初始化一个TrackerServer的地址
		ClientGlobal.init("D:\\itheima\\IdeaProjects\\itheima_fastdfs\\src\\main\\resources\\tracker_server.conf");
		//2、创建TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//3、有TrackerClient对象获取一个TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//4、声明一个StorageServer对象
		StorageServer storageServer = null;
		//5、创建StorageClient：需要两个参数1、TrackerServer，StorageServer
		StorageClient storageClient = new StorageClient(trackerServer,storageServer);
		//6、StorageClient来上传
		//参数说明：1、指定上传文件路径（或者byte字节流）；2、指定上传文件的扩展名(不带.)；3、null
		String[] file = storageClient.upload_appender_file("C:\\Users\\84548\\Desktop\\a.jpg", "jpg", null);
		//7、返回结果集：打印一下
		for (String f:file){
			System.out.println(f);
		}
		System.out.println("上传成功");
	}
}
//http://192.168.25.133/group1/M00/00/00/wKgZhVuZPR6EYSWMAAAAAKnlY1s625.jpg
