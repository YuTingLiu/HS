package RedisUtil;
/**
 * redis服务文件
 */

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.*;

public class UrlCheck {
    private static String urlList=null;
    private static UrlCheck check = new UrlCheck();
    private static JedisCluster jCluster;
    static{
        try {
            //读取properties文件中的配置连接信息
            InputStream in = UrlCheck.class.getClassLoader().getResourceAsStream("config.properties");
            Properties prop = new Properties();
            prop.load(in);

            //获取地址与端口信息
            urlList = prop.getProperty("redis.urls");

        }catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    private JedisCluster getJedisCluster() {
        String[] urls = urlList.split(";");
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        for(String url:urls) {
            jedisClusterNodes.add(new HostAndPort(url.split(":")[0], Integer.parseInt(url.split(":")[1])));
//            jedisClusterNodes.add(new HostAndPort("172.16.18.234", 7000));
//            jedisClusterNodes.add(new HostAndPort("172.16.18.234", 7001));
//            jedisClusterNodes.add(new HostAndPort("172.16.18.234", 7002));
//            jedisClusterNodes.add(new HostAndPort("172.16.18.234", 7003));
//            jedisClusterNodes.add(new HostAndPort("172.16.18.234", 7004));
//            jedisClusterNodes.add(new HostAndPort("172.16.18.234", 7005));
        }
        JedisCluster jCluster = new JedisCluster(jedisClusterNodes);
        return jCluster;
    }

    public static void UrlCheck(){
    }

    /**
     * 获取实例
     * @return
     * @throws Exception
     */
    public static UrlCheck getCheck() throws Exception {
        check = new UrlCheck();
        jCluster = check.getJedisCluster();
        return check;
    }

    /**
     * 查找是否存在某个key
     * @param host
     * @return
     */
    public List<String> checkUrl(String host){
        List<String> ret = jCluster.hmget(host,"STATUS");
        if(ret.get(0)==null){
            return null;
        }
        return ret;
    }

    public TreeSet<String> getAll(String patt){
        RedisOperator ro = new RedisOperator(jCluster);
        TreeSet<String> set = ro.keys(patt);
        return set;
    }

    public void put(String host, Map<String,String> set){
        jCluster.hmset(host,set);
    }
}
