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
    private String password = "123";

    /**
     * slow query time out ,millisecond
     */
    private int slowQueryTimeOut = 1000;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSlowQueryTimeOut() {
        return slowQueryTimeOut;
    }

    public void setSlowQueryTimeOut(int slowQueryTimeOut) {
        this.slowQueryTimeOut = slowQueryTimeOut;
    }

    public int getExecuteConcurrent() {
        return executeConcurrent;
    }

    public void setExecuteConcurrent(int executeConcurrent) {
        this.executeConcurrent = executeConcurrent;
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

    public static BigSqlConfig getInstance(){
        return Instance.INSTANCE;
    }
}
