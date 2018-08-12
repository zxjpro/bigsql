package com.xiaojiezhu.bigsql.server.config;

/**
 * this is a single instance
 * @author xiaojie.zhu
 */
public class BigSqlConfig {
    private String host = "0.0.0.0";
    private int port = 3307;
    private int bossThread = 1;
    private int workerThread = Runtime.getRuntime().availableProcessors() * 2;
    private int bigSqlThread = 512;
    private int executeConcurrent = 5;

    //TODO : 这里的参数还没有设置到System.setProperties()
    private BigSqlConfig() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getExecuteConcurrent() {
        return executeConcurrent;
    }

    public void setExecuteConcurrent(int executeConcurrent) {
        this.executeConcurrent = executeConcurrent;
    }

    public static BigSqlConfig getInstance(){
        return Instance.INSTANCE;
    }

    public int getBossThread() {
        return bossThread;
    }

    public void setBossThread(int bossThread) {
        this.bossThread = bossThread;
    }

    public int getWorkerThread() {
        return workerThread;
    }

    public void setWorkerThread(int workerThread) {
        this.workerThread = workerThread;
    }

    public int getBigSqlThread() {
        return bigSqlThread;
    }

    public void setBigSqlThread(int bigSqlThread) {
        this.bigSqlThread = bigSqlThread;
    }

    private static class Instance{
        private static final BigSqlConfig INSTANCE = new BigSqlConfig();
    }
}
