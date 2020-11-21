package zookeeper.curator.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.junit.Test;
import zookeeper.curator.CuratorBaseOperations;

import java.util.Map;

@Slf4j
public class TreeCacheTest extends CuratorBaseOperations{

    public static final String TREE_CACHE="/tree-path";

    @Test
    public void testTreeCache() throws Exception {
        CuratorFramework curatorFramework = getCuratorFramework();
        createIfNeed(TREE_CACHE);
        TreeCache treeCache = new TreeCache(curatorFramework, TREE_CACHE);
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                log.info(" tree cache: {}",event);

                Map<String, ChildData> currentChildren = treeCache.getCurrentChildren(TREE_CACHE);
                log.info("currentChildren: {}",currentChildren);
            }
        });
        treeCache.start();
    }
}
