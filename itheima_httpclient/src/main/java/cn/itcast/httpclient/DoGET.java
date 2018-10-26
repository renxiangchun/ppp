package cn.itcast.httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class DoGET {

    public static void main(String[] args) throws Exception {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();//打开浏览器

        // 创建http GET请求
        HttpGet httpGet = new HttpGet("http://www.baidu.com/");//输入访问路径

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet);//回车，执行
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println("内容："+content);//打印内容
            }
        } finally {
            if (response != null) {
                response.close();//关闭浏览器
            }
            httpclient.close();
        }

    }

}
