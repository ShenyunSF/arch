package zookeeper.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AclOperations extends StandaloneBase {




    /**
     *  用 world 模式创建节点
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void createWithAclTest1() throws KeeperException, InterruptedException {

        List<ACL> acLList = new ArrayList<ACL>();
        ACL e = new ACL();
        Id m_ = new Id();
        m_.setId("anyone");
        m_.setScheme("world");

        int perms = ZooDefs.Perms.ADMIN  |  ZooDefs.Perms.READ;
        e.setId(m_);
        e.setPerms(perms);
        acLList.add(e);
        String s = getZooKeeper().create("/zk-node-1", "gj".getBytes(), acLList, CreateMode.PERSISTENT);
        log.info("create path: {}",s);
    }

    /**
     *
     * 用授权模式创建节点
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void createWithAclTest2() throws KeeperException, InterruptedException {

        // 对连接添加授权信息
        getZooKeeper().addAuthInfo("digest","u400:p400".getBytes());

        List<ACL> acLList = new ArrayList<ACL>();
        ACL e = new ACL();
        Id m_ = new Id();
        m_.setId("u400:p400");
        m_.setScheme("auth");

        int perms = ZooDefs.Perms.ADMIN  |  ZooDefs.Perms.READ;
        e.setId(m_);
        e.setPerms(perms);
        acLList.add(e);

        String s = getZooKeeper().create("/zk-node-2", "gj".getBytes(), acLList, CreateMode.PERSISTENT);
        log.info("create path: {}",s);
    }


    @Test
    public void createWithAclTest3() throws KeeperException, InterruptedException {

        // 对连接添加授权信息
        getZooKeeper().addAuthInfo("digest","u400:p400".getBytes());


        byte[] data = getZooKeeper().getData("/test", false, null);

        log.info("GET_DATA : {}",new String(data));
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {
        String sId = DigestAuthenticationProvider.generateDigest("gj:123");
        System.out.println(sId);
        //  -Dzookeeper.DigestAuthenticationProvider.superDigest=gj:X/NSthOB0fD/OT6iilJ55WJVado=
    }
}
