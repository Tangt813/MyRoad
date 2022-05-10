package com.example.myroad.utils.Algorithm;


/**
 * @ClassName: MyRouteController
 * @Description: 代码测试
 * @Author: Shadon
 * @Date: 2022/3/15 15:34
 * @Version: v1.0
 */
public class Algorithm {
    public static void main(String[] args) {
//        假设有6个点
        int point_num = 6;
//        初始化地图
        double[][] map = new double[6][6];
        for (int i = 0; i < point_num; i++) {
            for (int j = i + 1; j < point_num; j++) {
                map[i][j] = Math.random() * 10;
                map[j][i] = map[i][j];
            }
            map[i][i] = 0;
        }
//        无时间限制
        double[][] time = new double[6][2];
        for (int i = 0; i < point_num; i++) {
            time[i][0] = -1;
            time[i][1] = -1;
        }
//        无绝对位置限制
        int[] sequence = new int[6];
        for (int i = 0; i < point_num; i++) {
            sequence[i] = -1;
        }
//        起点为0
        int start_point = 0;
//        终点为最后一个点
        int end_point = 5;
        MyRoute my_route = new MyRoute(point_num, map, time, sequence, start_point, end_point);
        my_route.carculate();
        System.out.println(my_route.getRoute_length());
        int[] route = my_route.getShortest_route();
        for (int i = 0; i < route.length; i++) {
            System.out.println(route[i]);
        }
    }
}
