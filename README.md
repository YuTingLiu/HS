# HS
代码结构：<br>
    dataStr.
        FileObject:定义一个数据结构{url,data}
    RedisUtil.
        UrlCheck:host检查主类
        IRedisOperater:批量获取Redis集群数据接口
        RedisOperator:接口实现
    Util.
        BlockingQ:定义一个消息队列接口
        LoadData2Queue:加载数据到Queue线程
        OutputT:由队列取数据写入文件
        UrlFilter:定义BloomFilter
    Main:主类
 
