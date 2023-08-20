package com.kob.botrunningsystem.service.impl.utils;

import com.sun.jndi.cosnaming.CNCtxFactory;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread{
    private static final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Queue<Bot>bots = new LinkedList<>();

    private void consum(Bot bot){
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000,bot);//最多执行两秒
    }
    public void addBot(Integer userId,String botCode,String input){
        lock.lock();
        try{
            bots.add(new Bot(userId,botCode,input));
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }
    @Override
    public void run(){
        while(true){
            lock.lock();
            if(bots.isEmpty()){
                try{
                    condition.await();
                }catch (InterruptedException e){
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            }else{
                Bot bot = bots.remove();//返回并删除队头
                lock.unlock();
                consum(bot);//编译并执行代码 比较耗时 可能会执行几秒钟
            }
        }
    }
}
