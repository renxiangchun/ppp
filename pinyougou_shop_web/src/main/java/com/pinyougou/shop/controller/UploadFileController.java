package com.pinyougou.shop.controller;

import com.pinyougou.common.FastDFSClient;
import com.pinyougou.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadFileController {

	@Value("${SHOP_WEB_IMAGES_URL}")
	private String SHOP_WEB_IMAGES_URL;

	@RequestMapping("/upload")
	public Result upload(MultipartFile file){

		try {
			//1、先获取文件全名
			String filename = file.getOriginalFilename();
			//2、获取文件的扩展名 : 截取扩展名：李进.很帅 .png  7
			String extName = filename.substring(filename.lastIndexOf(".") + 1);
			//3、上传图片（返回URL）
			FastDFSClient client = new FastDFSClient("classpath:config/tracker_server.conf");
			String path = client.uploadFile(file.getBytes(), extName);
			//http://192.168.25.133/ group1/M00/00/00/345678op.png
			String url = SHOP_WEB_IMAGES_URL + path;
			//4、封装返回结果集
			return new Result(true,url);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"上传失败");
		}
	}
}
