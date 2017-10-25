# HS
代码结构：<br>
    dataStr.<br>
        FileObject:定义一个数据结构{url,data}<br>
    RedisUtil.<br>
        UrlCheck:host检查主类<br>
        IRedisOperater:批量获取Redis集群数据接口<br>
        RedisOperator:接口实现<br>
    Util.<br>
        BlockingQ:定义一个消息队列接口<br>
        LoadData2Queue:加载数据到Queue线程<br>
        OutputT:由队列取数据写入文件<br>
        UrlFilter:定义BloomFilter<br>
    Main:主类<br>
 
