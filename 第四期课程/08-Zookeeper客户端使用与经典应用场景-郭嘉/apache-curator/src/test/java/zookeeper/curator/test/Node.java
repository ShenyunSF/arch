package zookeeper.curator.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.TimeUnit;

@Slf4j
public class Node {
    private static final String ZK_CONNECTION_STR = "192.168.109.200:2180";
    private static final int sessionTimeoutMs = 30000;
    private static final int connectionTimeoutMs = 5000;
    private static final String flag = "/finished";
    public static CuratorFramework curatorFramework;
    private static LeaderSelector leaderSelector;

    public Node() {
        init();
    }

    public static void main(String[] args) throws Exception {

        Node node = new Node();
        node.initCache();


        Thread.sleep(Integer.MAX_VALUE);
    }

    public void init() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 6);
        curatorFramework = CuratorFrameworkFactory.builder().connectString(ZK_CONNECTION_STR)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .build();
        curatorFramework.getConnectionStateListenable().addListener((client, newState) -> {
            if (newState == ConnectionState.CONNECTED) {
                log.info("连接成功！");
            }

        });

        curatorFramework.getUnhandledErrorListenable().addListener(new UnhandledErrorListener() {
            @Override
            public void unhandledError(String message, Throwable e) {
                log.error("message: {}, exception: {}", message, e);
            }
        });
        log.info("连接中......");
        curatorFramework.start();
    }

    public boolean isExist() throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(flag);
        return stat != null;
    }

    public boolean startIfneed() throws Exception {

        if (!isExist()) {
            curatorFramework.create().forPath(flag, "started".getBytes());
            return true;
        } else {
            byte[] bytes = curatorFramework.getData().forPath(flag);
            String data = new String(bytes);
            return !data.equalsIgnoreCase("finished");
        }
    }

    public void requestResource() throws Exception {
        int count = 0;
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            if (count == 15) {
                break;
            }
            if (System.getProperty("AppName").equalsIgnoreCase("App1")) {
                if (count == 10) {
                    cancel();
                }
            }

            log.info(" count: {}", count++);
        }
        finished();
    }

    public void initCache() throws Exception {

        String appName = System.getProperty("AppName");
        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                // if finished, set i flag to the zk. just like create a path and set a flag.
                log.info("{} is Leading now! ", appName);

                if (startIfneed()) {
                    requestResource();
                } else {
                    log.info("completed!  bye bye.");
                }

            }
        };
        leaderSelector = new LeaderSelector(curatorFramework, "/test-master", listener);
        //  leaderSelector.autoRequeue();

        leaderSelector.start();
        ;


    }

    public void finished() throws Exception {
        curatorFramework.setData().forPath(flag, "finished".getBytes());
    }

    public void cancel() {
        leaderSelector.interruptLeadership();
    }


}
