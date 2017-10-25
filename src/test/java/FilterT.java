import RedisUtil.UrlCheck;
import util.UrlFilter;

import java.util.List;
import java.util.TreeSet;

import static RedisUtil.UrlCheck.*;

public class FilterT {

    private final String[] URLS = {
            "http://www.csdn.net/",
            "http://www.baidu.com/",
            "http://www.google.com.hk",
            "http://www.cnblogs.com/",
            "http://www.zhihu.com/",
            "https://www.shiyanlou.com/",
            "http://www.google.com.hk",
            "https://www.shiyanlou.com/",
            "http://www.csdn.net/"
    };

    private void testBloomFilter() {
        for (int i = 0; i < URLS.length; i++) {
            if (UrlFilter.getFilter().contains(URLS[i])) {
                System.out.println("contain: " + URLS[i]);
                continue;
            }

            UrlFilter.getFilter().add(URLS[i]);
        }
    }
    public static void main(String[] args) throws Exception{
        FilterT t = new FilterT();
        t.testBloomFilter();
        List<String> content = UrlCheck.getCheck().checkUrl("www.baidu.com");
        System.out.println(content.size());
        for(String str : content){
            System.out.println(str);
        }
        String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        TreeSet<String> set = UrlCheck.getCheck().getAll("*");
        System.out.println(set.size());
        for(String str : set){
            System.out.println(str);
        }
    }
}