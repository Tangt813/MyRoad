package com.example.myroad.utils;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @ClassName: HTTPS
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2022/4/19 16:00
 * @Version: v1.0
 */
public class HttpsUtil {

    public static String httpsRequest(String requestUrl, Map params)
    {
        StringBuffer stringBuffer=new StringBuffer();
        try
        {
            //TODO 封装访问api接口
            URL url=new URL(requestUrl+"?"+ urlencode(params));
            HttpsURLConnection httpsURLConnection=(HttpsURLConnection) url.openConnection();
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.connect();
        }
        catch (Exception e)
        {
            System.out.println("fail to connect!");
            return "fail";
        }
        return "...";
    }
}
