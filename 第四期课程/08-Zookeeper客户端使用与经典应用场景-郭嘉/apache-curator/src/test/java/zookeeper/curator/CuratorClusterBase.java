package zookeeper.curator;


public  class CuratorClusterBase extends CuratorStandaloneBase {

    private final static  String CLUSTER_CONNECT_STR="192.168.109.200:2181,192.168.109.200:2182,192.168.109.200:2183,192.168.109.200:2184";

    public   String getConnectStr() {
        return CLUSTER_CONNECT_STR;
    }
}
