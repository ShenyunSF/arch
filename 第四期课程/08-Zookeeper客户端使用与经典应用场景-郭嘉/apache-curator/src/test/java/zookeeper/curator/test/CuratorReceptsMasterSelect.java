package zookeeper.curator.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.junit.Test;
import zookeeper.curator.CuratorBaseOperations;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CuratorReceptsMasterSelect extends CuratorBaseOperations {



    @Test
    public void testLeadSelection() throws InterruptedException {
        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {

            public void takeLeadership(CuratorFramework client) throws Exception {

                log.info("{} : i'm leading now! ", Thread.currentThread().getName());
                log.info("do something ");

                TimeUnit.SECONDS.sleep(5);
                // 执行计划 TODO list


                log.info("done!");
            }
        };


        CuratorFramework curatorFramework = getCuratorFramework();

        LeaderSelector selector = new LeaderSelector(curatorFramework, "/leader-node", listener);
        selector.autoRequeue();  // not required, but this is behavior that you will probably expect
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
