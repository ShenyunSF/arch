package zookeeper.curator.discovery;


import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.junit.Test;
import zookeeper.curator.CuratorStandaloneBase;

import java.util.Collection;

@Slf4j
public class DiscoveryTest extends CuratorStandaloneBase {

    private static final String     PATH = "/discovery/example";
    private static ServiceDiscovery<InstanceInfo> serviceDiscovery;


    @Test
    public void testRegister() throws Exception {

        CuratorFramework curatorFramework = getCuratorFramework();
        JsonInstanceSerializer<InstanceInfo> serializer = new JsonInstanceSerializer<InstanceInfo>(InstanceInfo.class);

        serviceDiscovery = ServiceDiscoveryBuilder
                .builder(InstanceInfo.class)
                .client(curatorFramework)
                .basePath(PATH)
                .serializer(serializer)
                .build();

        ServiceInstance<InstanceInfo> build =
                ServiceInstance.<InstanceInfo>builder()
                        .name("server-name-random")
                        .payload(InstanceInfo.builder().myAppName("yyy").description("yyy desc ").build())
                        .build();
        serviceDiscovery.registerService(build);
        printInstance();
    }

    public void printInstance() throws Exception {

        Collection<ServiceInstance<InstanceInfo>> serviceInstances =
                serviceDiscovery.queryForInstances("server-name-random");
        serviceInstances.forEach(item->{
            log.info(" Instance: {}",item);
        });
    }


}
