package com.kob.backend.consumer.utils;

import java.util.Random;

public class Game {
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;
    final private int[][] g;

    final private static int[] dx = {-1,0,1,0};
    final private static int[] dy = {0,1,0,-1};
    public Game(Integer rows, Integer cols, Integer inner_walls_count) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
    }

    public int[][] getG() {//返回地图
        return g;
    }

    private boolean check_connectivity(int sx,int sy,int tx,int ty){
        if(sx == tx && sy == ty)
        {
            return true;
        }
        g[sx][sy]=1;
        for(int i=0;i<4;i++)
        {
            int x = sx + dx[i];
            int y = sy + dy[i];
            if(x >= 0 && x < this.rows && y >=0 && y < this.cols && g[x][y] ==0){
                if(check_connectivity(x,y,tx,ty))
                {
                    g[sx][sy]=0;//恢复现场
                    return true;
                }
            }
        }
        g[sx][sy]=0;//恢复现场
        return false;
    }

    private boolean draw(){//绘制地图
        for(int i=0;i<this.rows;i++){
            for(int j=0;j<this.cols;j++){
                g[i][j]=0;//0 表示 可通行区域 1表示障碍物
            }
        }
        //给四周加上障碍
        for(int r=0;r<this.rows;r++)//给左右两侧设置为1
        {
            g[r][0]=1;
            g[r][this.cols-1]=1;
        }
        for(int c=0;c<this.cols;c++)//给上下两侧设置为1
        {
            g[0][c]=g[this.rows-1][c]=1;
        }
        //在内部随机生成inner_walls_count个对称的障碍物
        Random random = new Random();
        for(int i = 0; i < this.inner_walls_count / 2; i++){
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);//返回0~rows-1的随机值
                int c = random.nextInt(this.cols);//返回0~cols-1的随机值
                if(g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1)
                    continue;//已经有了 不能重复添加 直接进入下一轮循环 j++
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols-2)
                    continue;//保证左上角和右下角不能有障碍物

                //成功设置一个障碍物后 直接退出当前for i++
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }
        return check_connectivity(this.rows-2,1,1,this.cols-2);
    }

    public void createMap(){
        for (int i=0;i<1000;i++){
            if(draw())
            {
                break;
            }
        }
    }


}
