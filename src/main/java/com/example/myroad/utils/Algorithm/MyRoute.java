package com.example.myroad.utils.Algorithm;

import java.util.Arrays;

/**
 * @ClassName: MyRoute
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2022/5/10 13:38
 * @Version: v1.0
 */
public class MyRoute {
    private int point_num;
    private double[][] map;
    private double[][] time;
    private int[] sequence;
    private int start_point;
    private int end_point;
    private double route_length;
    private int[] shortest_route;

    public MyRoute(int point_num, double[][] map, double[][] time, int[] sequence, int start_point, int end_point) {
        this.point_num = point_num;
        this.map = map;
        this.time = time;
        this.sequence = sequence;
        this.start_point = start_point;
        this.end_point = end_point;

        shortest_route=new int[point_num];
        Arrays.fill(shortest_route,-1);
    }

    public void carculate() {
//        初始化参数
        int ant_num = 100 / point_num;//蚂蚁数量 节点数越多，蚂蚁数量应该越小，减少系统崩溃
        double alpha = 2;//信息素重要程度因子
        double beta = 0.2;//启发函数重要程度因子
        double rho = 0.1;//信息素挥发因子
        double Q = 1;//常系数
        //启发函数
        double[][] Eta = new double[point_num][point_num];
        for (int i = 0; i < point_num; i++) {
            for (int j = 0; j < point_num; j++) {
                if(map[i][j]!=0)
                    Eta[i][j] = 1 / map[i][j];
                else
                    Eta[i][j]=1;
            }
        }
        //信息素矩阵,初始值为1
        double[][][] Tau = new double[point_num-1][point_num][point_num];
        for (int i = 0; i < point_num-1; i++)
            for (int j = 0; j < point_num; j++) {
                Arrays.fill(Tau[i][j], 1);
            }
        //当前各个蚂蚁所处的节点
        int[] NowAntPoint = new int[ant_num];
        //迭代次数
        int iter_max = 300 / point_num;//节点数越多，迭代次数应该越小，减少系统崩溃
        int iter = 0;//迭代次数初值
        int[] Route_best = new int[point_num];//当前最佳路径
        Arrays.fill(Route_best, 0);

        double initial_route=1000000000;
        double Length_best = initial_route;//最佳路径的长度 初始值为一个很大的值

        double[]now_time=new double[ant_num];//蚂蚁的当前时间
        int[]finished=new int[ant_num];//蚂蚁是否走完了路径
        Arrays.fill(finished, 1);//最初假设都走完了
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
            for (int i = 0; i < ant_num; i++) {//逐个路径选择
                for (int j = 1; j < point_num; j++) {
                    double[] P = new double[point_num];//节点之间的转移概率
                    double p_sum = 0;//记录概率的总和,方便归一化
                    //计算节点之间的转移概率
                    for (int k = 1; k < point_num; k++) {
                        if (j != point_num - 1)//如果没遍历到最后一个节点
                        {
                            if (PointTable[i][k] == 0 && k != end_point && now_time[i]+map[NowAntPoint[i]][k]>=time[k][0]&&now_time[i]+map[NowAntPoint[i]][k]<=time[k][1] && (sequence[k] == -1 || sequence[k] == j) )//如果该节点之前未被走过且不为终点,且在时间范围内.若有顺序,则要保证与顺序一致
                            {
                                P[k] = Math.pow(Tau[j-1][NowAntPoint[i]][k], alpha) + Math.pow(Eta[NowAntPoint[i]][k], beta);
                                p_sum += P[k];
                            } else
                                P[k] = 0;
                        } else
                        {
                            P[k] = 0;//节点的概率都为负数，防止出bug
                        }
                    }
                    if (j == point_num - 1) {
                        P[end_point] = 1;//除了终点概率都为0
                        p_sum = 1;
                    }
                    if(p_sum==0){//如果总的概率为0，即还未到终点，但已经没有可以去的下一个节点了（受时间范围影响）
                        finished[i]=0;
                        break;
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
                            now_time[i]+=map[Table[i][j-1]][Table[i][j]];
                            break;
                        }
                    }
                }
            }
            //计算各个蚂蚁的路径距离
            double[] Length = new double[ant_num];
            Arrays.fill(Length, 0);
            for (int i = 0; i < ant_num; i++) {
                if(finished[i]==0) {
                    Length[i] = initial_route;//一个特别大的数字
                    continue;
                }
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
            double[][][] Delta_Tau = new double[point_num-1][point_num][point_num];
            for(int i=0;i<point_num-1;i++)
                for (int j = 0; j < point_num; j++) {
                    Arrays.fill(Delta_Tau[i][j], 0);
                }
            //逐个蚂蚁计算
            for (int i = 0; i < ant_num; i++) {
                if(finished[i]==0)
                    ;
                else {
                    //逐个节点计算
                    for (int j = 0; j < point_num - 1; j++) {
                        Delta_Tau[j][Table[i][j]][Table[i][j + 1]] = Delta_Tau[j][Table[i][j]][Table[i][j + 1]] + Q / Length[i];
                    }
                }
            }
            for(int i=0;i<point_num-1;i++)
                for (int j = 0; j < point_num; j++) {
                    //逐个节点计算
                    for (int k = 0; k < point_num; k++) {
                        Tau[i][j][k] = (1 - rho) * Tau[i][j][k] + Delta_Tau[i][j][k];
                    }
                }
            iter+=1;//迭代次数加1
        }
        route_length=Length_best;

        if(Length_best == initial_route)
            return;
        for (int i = 0; i < point_num; i++) {
            shortest_route[i]=Route_best[i];
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
