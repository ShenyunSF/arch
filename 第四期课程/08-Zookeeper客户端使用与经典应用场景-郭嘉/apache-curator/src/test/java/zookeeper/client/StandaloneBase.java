package zookeeper.client;


import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class  StandaloneBase {

    private static final  String  CONNECT_STR="192.168.109.200:2181";

    private static final  int SESSION_TIMEOUT=30 * 1000;

    private static ZooKeeper  zooKeeper =null;

    private static  CountDownLatch countDownLatch = new CountDownLatch(1);

    private Watcher watcher =new Watcher() {
        @Override
        public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected
                            && event.getType()== Event.EventType.None){
                        countDownLatch.countDown();
                        log.info("连接建立");
                    }
        }
    };


    @Before
    public void init(){
        try {
            log.info(" start to connect to zookeeper server: {}",getConnectStr());
             zooKeeper=new ZooKeeper(getConnectStr(), getSessionTimeout(), watcher);
            log.info(" 连接中...");
            countDownLatch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }





    public static ZooKeeper getZooKeeper() {
        return zooKeeper;
    }


    @After
    public void   test(){
        try {
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    protected     String getConnectStr(){
        return CONNECT_STR;
    }

    protected   int getSessionTimeout() {
        return SESSION_TIMEOUT;
    }
}
