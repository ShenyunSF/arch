package zookeeper.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ClusterOperations extends ClusterBase {


    @Test
    public void testReconnect() throws InterruptedException {

        while (true){
            try {
                Stat stat = new Stat();
                byte[] data = getZooKeeper().getData("/zookeeper", false, stat);
                log.info("get data : {}", new String(data));

                TimeUnit.SECONDS.sleep(5);
            }catch (Exception e){
                e.printStackTrace();
                log.info(" 开始重连......");

                while (true){
                    log.info("zookeeper status :{}",getZooKeeper().getState().name());
                    if (getZooKeeper().getState().isConnected()) {
                        break;
                    }
                    TimeUnit.SECONDS.sleep(3);
                }

            }
        }
    }


    @Test
    public void testReadonly() throws KeeperException, InterruptedException {

        ZooKeeper zooKeeper = getZooKeeper();
        Stat stat = new Stat();
        Watcher watcher = new Watcher() {
            @SneakyThrows
            @Override
            public void process(WatchedEvent event) {
                if ((event.getState() == Event.KeeperState.SyncConnected
                        || event.getState() == Event.KeeperState.ConnectedReadOnly)
                        && event.getType() == Event.EventType.NodeDataChanged) {
                    byte[] data = zooKeeper.getData("/node", this, stat);
                    log.info("数据发生变化: {}",new String(data));
                }
            }
        };
        while (true){
            try {
                byte[] data = zooKeeper.getData("/node", watcher, stat);
                log.info("session:{},  data from test node :{} ",zooKeeper.getSessionId(),new String(data));
                TimeUnit.SECONDS.sleep(3);
            }catch (Exception e){
                int count = 0;
                while (zooKeeper.getState() != ZooKeeper.States.CONNECTED
                        && zooKeeper.getState() != ZooKeeper.States.CONNECTEDREADONLY ){


                    TimeUnit.SECONDS.sleep(3);
                    log.info("now state: {} , try: {} times ",zooKeeper.getState().name(), ++count);
                }
            }

        }
    }


}
