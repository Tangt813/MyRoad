package com.example.myroad.utils;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.Map;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import cn.hutool.json.JSONObject;

/**
 * @ClassName: HTTPS
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2022/4/19 16:00
 * @Version: v1.0
 */
public class HttpsUtil {

    public static JSONObject getResponse() {
        //接口地址
        String requestUrl = "https://restapi.amap.com/v3/place/text";
        //params用于存储要请求的参数
        Map params = new HashMap();

        params.put("key","af4d996c3d370dc138d60ee80a281a3e");

        params.put("types","010101");
        //调用httpRequest方法，这个方法主要用于请求地址，并加上请求参数
        String string = httpsRequest(requestUrl,params);

        //处理返回的JSON数据并返回
        //直接返回json数据，返回后再进行处理
        JSONObject jsonObject = new JSONObject(string);
        return jsonObject;
    }

    public static String httpsRequest(String requestUrl, Map params)
    {
        StringBuffer buffer = new StringBuffer();
        try
        {
            //TODO 封装访问api接口
            URL url = new URL(requestUrl+"?"+urlencode(params));
            HttpsURLConnection httpsURLConnection=(HttpsURLConnection) url.openConnection();
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.connect();

            //获得输入
            InputStream inputStream = httpsURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //将bufferReader的值给放到buffer里
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            //关闭bufferReader和输入流
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            //断开连接
            httpsURLConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("fail to connect!");
            return "fail";
        }



//        //返回字符串
        return buffer.toString();
    }

    public static String urlencode(Map<String,Object>data) {
        //将map里的参数变成像 showapi_appid=###&showapi_sign=###&的样子
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"", StandardCharsets.UTF_8)).append("&");
        }
        return sb.toString();
    }
}
