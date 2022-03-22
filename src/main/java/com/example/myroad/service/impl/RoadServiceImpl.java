package com.example.myroad.service.impl;

import com.example.myroad.service.RoadService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @ClassName: RoadServiceImpl
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2022/3/15 14:13
 * @Version: v1.0
 */
@Service
public class RoadServiceImpl implements RoadService {
    static String key="dac759bbca955bfb55c0bcdac3618995";
    static String carUrl="https://restapi.amap.com/v5/direction/walking?parameters";
    static String waklUrl=
    @Override
    public Object roadPlan(Object[] roadData) {
        int dataLen = roadData.length;
        String url = "https://restapi.amap.com/v5/direction/walking?parameters";//驾车规划
        for (int i = 0; i < dataLen; i++) {
            for (int j = 0; j < dataLen; j++) {
                if (i != j) {
                    try {
                        URL restServiceURL = new URL(url);
                        try {
                            HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
                            httpConnection.setRequestMethod("GET");
                            httpConnection.setDoOutput(true);
                            //传递参数
                            String input = "&key=" + URLEncoder.encode(key, "UTF-8");
                            String startLocation = "", endLocation = "";
                            startLocation += roadData[i].location;//TODO 获取经纬度
                            endLocation += roadData[j].location;
                            input += "&origin=" + URLEncoder.encode(startLocation, "UTF-8");
                            input += "&destination=" + URLEncoder.encode(endLocation, "UTF-8");
                            OutputStream outputStream = httpConnection.getOutputStream();
                            outputStream.write(input.getBytes());
                            outputStream.flush();

                            if (httpConnection.getResponseCode() != 200) {
                                throw new RuntimeException(
                                        "HTTP GET Request Failed with Error code : "
                                                + httpConnection.getResponseCode());
                            }
                            BufferedReader responseBuffer = new BufferedReader(
                                    new InputStreamReader((httpConnection.getInputStream())));
                            String output;//输出结果
                            System.out.println("Output from Server:  \n");
                            while ((output = responseBuffer.readLine()) != null) {
                                System.out.println(output);
                            }
                            httpConnection.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return null;
    }
}
