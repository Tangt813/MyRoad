package com.example.myroad.service.impl;

import com.example.myroad.entity.RoadData;
import com.example.myroad.service.RoadService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;


/**
 * @ClassName: RoadServiceImpl
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2022/3/15 14:13
 * @Version: v1.0
 */
@Service
public class RoadServiceImpl implements RoadService {
    static String key = "dac759bbca955bfb55c0bcdac3618995";
    static String carUrl = "https://restapi.amap.com/v3/direction/driving?strategy=0";//?parameters
    static String walkUrl = "https://restapi.amap.com/v5/direction/walking?";
    static String bicycleUrl = "https://restapi.amap.com/v5/direction/bicycling?";
    static String electrobikeUrl = "https://restapi.amap.com/v5/direction/electrobike?";
    static String integratedUrl = "https://restapi.amap.com/v5/direction/transit/integrated?";//公交

    @Override
    public Object roadPlan(JSONObject roadDataList) {
        String url = carUrl;//驾车规划
        switch (roadDataList.getInt("wayType")) {
            case 0:
                url = carUrl;
                break;
            case 1:
                url = walkUrl;
                break;
            case 2:
                url = bicycleUrl;
                break;
            case 3:
                url = electrobikeUrl;
                break;
            case 4:
                url = integratedUrl;
                break;

        }
        JSONArray location= roadDataList.getJSONArray("location");
        int dataLen = location.size();


        for (int i = 0; i < dataLen; i++) {
            for (int j = 0; j < dataLen; j++) {
                if (i != j) {
                    try {
                        URL restServiceURL = new URL(url);
                        try {
//                            JsonTest jsonTest = JSONObject.parseObject(json,JsonTest.class);

                            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) restServiceURL.openConnection();
                            httpsURLConnection.setRequestMethod("GET");
                            httpsURLConnection.setDoOutput(true);
                            //传递参数
                            StringBuilder input = new StringBuilder();
                                   input.append("&key=").append(URLEncoder.encode(key, "UTF-8")) ;
                            String startLocation = "", endLocation = "";
                            startLocation += location.getString(i);//TODO 获取经纬度
                            endLocation += location.getString(j);
                            System.out.println(startLocation+"  "+endLocation);
                            input.append("&origin=" ).append(URLEncoder.encode(startLocation, "UTF-8"));
                            input.append("&destination=").append(URLEncoder.encode(endLocation, "UTF-8"));

                            OutputStream outputStream = httpsURLConnection.getOutputStream();
                            outputStream.write(input.toString().getBytes());
                            outputStream.flush();

                            if (httpsURLConnection.getResponseCode() != 200) {
                                throw new RuntimeException(
                                        "HTTP GET Request Failed with Error code : "
                                                + httpsURLConnection.getResponseCode());
                            }
                            BufferedReader responseBuffer = new BufferedReader(
                                    new InputStreamReader((httpsURLConnection.getInputStream())));
                            String output;//输出结果
                            System.out.println("Output from Server:  \n");
                            while ((output = responseBuffer.readLine()) != null) {
                                System.out.println(output);
                            }
                            httpsURLConnection.disconnect();
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
