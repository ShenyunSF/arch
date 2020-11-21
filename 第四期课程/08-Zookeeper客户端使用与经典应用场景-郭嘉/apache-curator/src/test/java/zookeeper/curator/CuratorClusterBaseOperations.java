package zookeeper.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CuratorClusterBaseOperations extends CuratorClusterBase {


    @Test
    public void testCluster() throws Exception {
        CuratorFramework curatorFramework = getCuratorFramework();
        String pathWithParent = "/test";
        byte[] bytes = curatorFramework.getData().forPath(pathWithParent);
        System.out.println(new String(bytes));
        while (true) {

            try {
                byte[] bytes2 = curatorFramework.getData().forPath(pathWithParent);
                System.out.println(new String(bytes2));

                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
                testCluster();
            }

        }

    }


    public static void main(String[] args) throws Exception {


        RetryPolicy retryPolicy=new ExponentialBackoffRetry( 5*1000, 10 );

        String connectStr = "192.168.109.200:2181,192.168.109.200:2182,192.168.109.200:2183,192.168.109.200:2184";
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(connectStr, retryPolicy);
        curatorFramework.start();



        String pathWithParent = "/test";
        byte[] bytes = curatorFramework.getData().forPath(pathWithParent);
        System.out.println(new String(bytes));
        while (true) {

            try {
                byte[] bytes2 = curatorFramework.getData().forPath(pathWithParent);
                System.out.println(new String(bytes2));

                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
             }

        }










    }

}
