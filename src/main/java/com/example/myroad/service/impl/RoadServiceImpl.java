package com.example.myroad.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.myroad.service.RoadService;
import com.example.myroad.utils.HttpsUtil;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


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
    static String carUrl = "https://restapi.amap.com/v3/direction/driving?strategy=0&show_fields=cost";//?parameters
    static String walkUrl = "https://restapi.amap.com/v5/direction/walking?show_fields=cost";
    static String bicycleUrl = "https://restapi.amap.com/v5/direction/bicycling?show_fields=cost";
    static String electrobikeUrl = "https://restapi.amap.com/v5/direction/electrobike?show_fields=cost";
    static String integratedUrl = "https://restapi.amap.com/v5/direction/transit/integrated?show_fields=cost";//公交


    @Override
    public cn.hutool.json.JSONObject test() {
        return HttpsUtil.getResponse();
    }

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
        JSONArray location = roadDataList.getJSONArray("location");
        int dataLen = location.size();
        double[][]needTime=new double[dataLen][dataLen];


        for (int i = 0; i < dataLen; i++) {
            for (int j = 0; j < dataLen; j++) {
                if (i != j) {
// TODO @郑启帆修改一下这里

                    //设置参数
                    Map params = new HashMap();

                    params.put("origin", location.get(i));
                    params.put("destination", location.get(j));
//                    input.append("&key=").append(URLEncoder.encode(key, "UTF-8")) ;
                    try {
                        params.put("key",URLEncoder.encode(key, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    String result = HttpsUtil.httpsRequest(url, params);
                    System.out.println(result);
                    cn.hutool.json.JSONObject jsonObject = new JSONObject(result);
                    System.out.println(jsonObject);

                    //提取出时间
                    JSONArray paths= (JSONArray) ((Map) jsonObject.get("route")).get("paths");
                    double min=Double.MAX_VALUE;
                    for (int k = 0; k < paths.size(); k++) {
                        double val=Double.parseDouble(((Map)((Map)paths.get(i)).get("cost")).get("duration").toString());
                        if(val<min)
                        {
                            min=val;
                        }
                    }
                    needTime[i][j]=min;


                }

            }
        }

        return null;
    }
}
