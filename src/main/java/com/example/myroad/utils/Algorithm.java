package com.example.myroad.utils;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @ClassName: MyRouteController
 * @Description: 代码测试
 * @Author: Shadon
 * @Date: 2022/3/15 15:34
 * @Version: v1.0
 */
class MinPos {
    double min_Length;//记录最小的距离
    int min_index;//记录最小距离的下标
}

class Route {
    private int point_num;
    private double[][] map;
    private double[][] time;
    private int[] sequence;
    private int start_point;
    private int end_point;
    private double route_length;
    private int[] shortest_route;

    public Route(int point_num, double[][] map, double[][] time, int[] sequence, int start_point, int end_point) {
        this.point_num = point_num;
        this.map = map;
        this.time = time;
        this.sequence = sequence;
        this.start_point = start_point;
        this.end_point = end_point;

        shortest_route=new int[point_num];
    }

    public void carculate() {
        for (int i = 0; i < this.point_num; i++) {
            for (int j = 0; j < this.point_num; j++) {
                System.out.println(map[i][j]);
            }
        }
//        初始化参数
        int ant_num = 100 / point_num;//蚂蚁数量 节点数越多，蚂蚁数量应该越小，减少系统崩溃
        double alpha = 1;//信息素重要程度因子
        double beta = 5;//启发函数重要程度因子
        double rho = 0.1;//信息素挥发因子
        double Q = 1;//常系数
        //启发函数
        double[][] Eta = new double[point_num][point_num];
        for (int i = 0; i < point_num; i++) {
            for (int j = 0; j < point_num; j++) {
                Eta[i][j] = 1 / map[i][j];
            }
        }
        //信息素矩阵,初始值为1
        double[][] Tau = new double[point_num][point_num];
        for (int i = 0; i < point_num; i++)
            Arrays.fill(Tau[i], 1);
        //当前各个蚂蚁所处的节点
        int[] NowAntPoint = new int[ant_num];
        //迭代次数
        int iter_max = 300 / point_num;//节点数越多，迭代次数应该越小，减少系统崩溃
        int iter = 0;//迭代次数初值
        int[] Route_best = new int[point_num];//当前最佳路径
        Arrays.fill(Route_best, 0);
        double Length_best = 1000000000;//最佳路径的长度 初始值为一个很大的值

        int[]now_time=new int[ant_num];
        Arrays.fill(now_time, 0);

        //迭代寻找最佳路径
        while (iter < iter_max) {
            //路径记录表
            int[][] Table = new int[ant_num][point_num];
            for(int i=0;i<ant_num;i++)
                Arrays.fill(Table[i], 0);
            //节点记录表
            int[][] PointTable = new int[ant_num][point_num];
            for(int i=0;i<ant_num;i++)
                Arrays.fill(PointTable[i], 0);
            //各个蚂蚁的起点都统一为用户定义的起点,并设置最后一个节点为用户定义的终点
            //int* start=new int[point_num];
            for (int i = 0; i < ant_num; i++) {
                Table[i][0] = start_point;
                NowAntPoint[i] = start_point;//当前蚂蚁所处位置为起点
                PointTable[i][start_point] = 1;//起点已经被走过了
            }
            //逐个蚂蚁路径选择
            for (int i = 0; i < ant_num; i++) {//逐个城市路径选择
                for (int j = 1; j < point_num; j++) {
                    double[] P = new double[point_num];//节点之间的转移概率
                    double p_sum = 0;//记录概率的总和,方便归一化
                    //计算节点之间的转移概率
                    for (int k = 0; k < point_num; k++) {
                        if (j != point_num - 1)//如果没遍历到最后一个节点
                        {
                            if (PointTable[i][k] == 0 && k != end_point && now_time[i]>)//如果该节点之前未被走过且不为终点,且在时间范围内
                            {
                                P[k] = Math.pow(Tau[NowAntPoint[i]][k], alpha) * Math.pow(Eta[NowAntPoint[i]][k], beta);
                                p_sum += P[k];
                            } else
                                P[k] = 0;
                        } else//如果遍历到最后一个节点
                        {
                            P[k] = 0;//节点的概率都为负数，防止出bug
                        }
                    }
                    if (j == point_num - 1) {
                        P[end_point] = 1;//除了终点概率都为0
                        p_sum = 1;
                    }
                    for (int k = 0; k < point_num; k++) {
                        P[k] = P[k] / p_sum;
                    }
                    //轮盘赌法选择下一个访问节点
                    double rand_num = Math.random();
                    double temp_probability = 0;
                    for (int k = 0; k < point_num; k++) {
                        temp_probability += P[k];
                        if (temp_probability >= rand_num)//获得下一个节点
                        {
                            Table[i][j] = k;
                            PointTable[i][k] = 1;
                            NowAntPoint[i] = k;
                            break;
                        }
                    }
                }
            }
            //计算各个蚂蚁的路径距离
            double[] Length = new double[ant_num];
            Arrays.fill(Length, 0);
            for (int i = 0; i < ant_num; i++) {
                for (int j = 0; j < point_num - 1; j++) {
                    Length[i] += map[Table[i][j]][Table[i][j + 1]];
                }
            }
            //计算最短路径距离及平均距离
            if (iter == 0) {
                MinPos minPos = new MinPos();
                Double_Min(Length, ant_num, minPos);//得到最小距离以及对应下标
                Length_best = minPos.min_Length;
                for (int i = 0; i < point_num; i++) {
                    Route_best[i] = Table[minPos.min_index][i];
                }
            } else {
                MinPos minPos = new MinPos();
                Double_Min(Length, ant_num, minPos);//得到最小距离以及对应下标
                if (minPos.min_Length <= Length_best) {
                    Length_best = minPos.min_Length;
                    for (int i = 0; i < point_num; i++) {
                        Route_best[i] = Table[minPos.min_index][i];
                    }
                }
            }
            //更新信息素
            double[][] Delta_Tau = new double[point_num][point_num];
            for(int i=0;i<point_num;i++)
                Arrays.fill(Delta_Tau[i], 0);
            //逐个蚂蚁计算
            for (int i = 0; i < ant_num; i++) {
                //逐个节点计算
                for (int j = 0; j < point_num - 1; j++) {
                    Delta_Tau[Table[i][j]][Table[i][j + 1]] = Delta_Tau[Table[i][j]][Table[i][j + 1]] + Q / Length[i];
                }
            }
            for (int i = 0; i < point_num; i++) {
                //逐个节点计算
                for (int j = 0; j < point_num; j++) {
                    Tau[i][j] = (1 - rho) * Tau[i][j] + Delta_Tau[i][j];
                }
            }

//            System.out.println(Length_best);
//            for (int i = 0; i < point_num; i++) {
//                System.out.println(Route_best[i]);
//            }
            iter+=1;//迭代次数加1
        }
        route_length=Length_best;
        System.out.println(route_length);
        for (int i = 0; i < point_num; i++) {
            shortest_route[i]=Route_best[i];
        }
        for (int i = 0; i < point_num; i++) {
            System.out.println(shortest_route[i]);
        }
    }

    //找到所有蚂蚁中路径最短的路径长度，并得到对应索引
    private void Double_Min(double[] Length, int ant_num, MinPos minPos) {
        minPos.min_Length = Length[0];
        minPos.min_index = 0;
        for (int i = 1; i < ant_num; i++) {
            if (Length[i] < minPos.min_Length) {
                minPos.min_Length = Length[i];
                minPos.min_index = i;
            }
        }
    }

    public double getRoute_length() {
        return route_length;
    }

    public int[] getShortest_route() {
        return shortest_route;
    }
}

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
        Route my_route = new Route(point_num, map, time, sequence, start_point, end_point);
        my_route.carculate();
        System.out.println(my_route.getRoute_length());
        int[] route = my_route.getShortest_route();
        for (int i = 0; i < route.length; i++) {
            System.out.println(route[i]);
        }
    }
}
