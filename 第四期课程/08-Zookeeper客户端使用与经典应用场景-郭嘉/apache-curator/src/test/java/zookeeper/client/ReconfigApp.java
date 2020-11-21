package zookeeper.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.server.util.ConfigUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ReconfigApp {

    private final  static  String connectString = "192.168.109.200:2181,192.168.109.200:2182,192.168.109.200:2183,192.168.109.200:2184";

    private static int SESSION_TIMEOUT=5* 1000;

    private static CountDownLatch countDownLatch=new CountDownLatch(1);

    private  static  ZooKeeper zookeeper =null;

    private static  Watcher watcher = new Watcher() {
       @Override
       public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.None
                            && event.getState() == Event.KeeperState.SyncConnected){
                              countDownLatch.countDown();
                              log.info(" 连接建立");
                              // start to watch config
                        try {
                            log.info(" 开始监听：{}",ZooDefs.CONFIG_NODE);
                            zookeeper.getConfig(true,null);
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else if( event.getPath()!=null  &&  event.getPath().equals(ZooDefs.CONFIG_NODE)){
                        try {
                            byte[] config = zookeeper.getConfig(this, null);
                            String clientConfigStr = ConfigUtils.getClientConfigStr(new String(config));
                            log.info(" 配置发生变更: {}",clientConfigStr);
                            zookeeper.updateServerList(clientConfigStr.split(" ")[1]);
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
       }
   };




    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        zookeeper =   new ZooKeeper(connectString, SESSION_TIMEOUT, watcher);
        countDownLatch.await();

        Scanner scanner =new Scanner(System.in);
        while (true){
            byte[] data = zookeeper.getData("/zookeeper/config", true, null);
             scanner.next();
            log.info("DATA: {}",new String(data));
        }

    }
}
