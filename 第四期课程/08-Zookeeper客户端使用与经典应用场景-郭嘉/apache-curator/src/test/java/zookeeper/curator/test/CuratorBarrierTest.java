package zookeeper.curator.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.junit.Test;
import zookeeper.curator.CuratorBaseOperations;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CuratorBarrierTest extends CuratorBaseOperations {

    static final String ZK_NODE = "/zk-node";


    @Test
    public void testBarrier(){

        CuratorFramework curatorFramework = getCuratorFramework();

        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                DistributedDoubleBarrier distributedBarrier=new DistributedDoubleBarrier(curatorFramework,"/barrier/node",5);
                try {
                    sleep(10);
                    distributedBarrier.enter();
                    log.info(" enter: barrier.");
                    sleep(10);
                    distributedBarrier.leave();
                    log.info(" exit: barrier.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        try {
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void sleep(Integer bound){
        try {
            int timeout = new Random().nextInt(bound);
            log.info(" i'm gonna sleep {} seconds",timeout);
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
