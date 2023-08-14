package com.kob.backend.consumer.utils;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Record;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;
    final private int[][] g;
    private Player playerA,playerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    final private static int[] dx = {-1,0,1,0};
    final private static int[] dy = {0,1,0,-1};
    private String status = "playing";//游戏状态 playing-->finshed
    private String loser = "";//all:平:A:A输 B:B输了
    private ReentrantLock lock = new ReentrantLock();



    private boolean nextStep(){
        //等待两名玩家的下一步操作
        //由于前端动画200ms才能画一个格子
        //如果在此期间接收到的输入多于一步 只会留最后一步 多余的会被覆盖
        //因此在每一个下一步都要先休息200ms
        try {
            Thread.sleep(200);
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
        System.out.println("nextStep函数");
        //如果10秒内有玩家没有输入，就返回false
        for(int i=0;i<100;i++){
            try{
                Thread.sleep(100);
                lock.lock();
                try{
                    if(nextStepA != null && nextStepB != null){
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                }finally {
                    lock.unlock();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try{
            this.nextStepA = nextStepA;
        }finally {
            lock.unlock();
        }
    }
    public void setNextStepB(Integer nextStepB){
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        }finally {
            lock.unlock();
        }
    }


    public Game(Integer rows, Integer cols, Integer inner_walls_count,Integer idA,Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        playerA = new Player(idA,this.rows-2,1,new ArrayList<>());
        playerB = new Player(idB,1,this.cols-2,new ArrayList<>());
    }

    public Player getPlayerA(){
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
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
        System.out.println("随机绘制地图");
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

    @Override
    public void run() {
        System.out.println("执行了run函数");
        for (int i = 0; i < 1000; i++) {//1000步之内游戏肯定结束
            if (nextStep()) {
                //如果获取两个玩家的下一步操作
                judge();
                System.out.println("获取到nextstep");
                if (status.equals("playing")) {
                    sentMove();
                } else {
                    sentResult();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sentResult();
                break;
            }
        }
    }
        private void sentAllmessage(String message){//工具函数:向两名玩家广播信息
            WebSocketServer.userConnectionInfo.get(playerA.getId()).sendMessage(message);
            WebSocketServer.userConnectionInfo.get(playerB.getId()).sendMessage(message);
        }

        private void sentMove(){
            //向两个client广播玩家操作信息
            System.out.println("发送move消息");
            lock.lock();
            try{
                JSONObject resp = new JSONObject();
                resp.put("event","move");
                resp.put("a_direction",nextStepA);
                resp.put("b_direction",nextStepB);
                nextStepA = nextStepB = null;//清空操作
                sentAllmessage(resp.toJSONString());
            }finally {
                lock.unlock();
            }
        }

        private void sentResult() {//向两个client公布结果信息
            JSONObject resp = new JSONObject();
            resp.put("event","result");//定义事件
            resp.put("loser",loser);
            saveToDatabase();
            sentAllmessage(resp.toJSONString());
        }

        private boolean check_valid(List<Cell>cellsA,List<Cell>cellsB){
            int n = cellsA.size();
            Cell cell = cellsA.get(n-1);//取到A的最后一步
            //三种不合法操作: A撞墙,A撞A,A撞B
            //A撞墙
            if(g[cell.getX()][cell.getY()] == 1)
            {
                return false;
            }
            //A撞A
            for(int i=0;i<n-1;i++)
            {
                if(cellsA.get(i).getX().equals(cell.getX())&&cellsA.get(i).getY().equals(cell.getY())){
                    return false;
                }
            }
            //A撞B
            for(int i=0;i<n-1;i++){
                if(cellsB.get(i).getX().equals(cell.getX())&&cellsB.get(i).getY().equals(cell.getY())){
                    return false;
                }
            }
            return true;
        }

        private void judge() {
            List<Cell>cellsA = playerA.getCells();
            List<Cell>cellsB = playerB.getCells();
            //判断两名玩家最后一步操作是否合法
            boolean validA = check_valid(cellsA,cellsB);
            boolean validB = check_valid(cellsB,cellsA);
            if(!validA || !validB){
                status = "finished";
                if(validA){
                    loser = "B";
                }else if(validB){
                    loser = "A";
                }else{
                    loser = "all";
                }
            }
        }
    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i ++ ) {
            for (int j = 0; j < cols; j ++ ) {
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    private void saveToDatabase(){
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }






}
