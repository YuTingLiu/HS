/**
 * 主进程
 * 由unZipHeaser目录中读取数据,解析过滤后存放到dstPath中,
 * 参数定义单个文件长度,与数据生产线程数量
 */

import RedisUtil.UrlCheck;
import dataStr.FileObject;
import org.apache.log4j.Logger;
import util.*;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);
    private static String unZipHeaser = "E:\\01\\";
    private static String dstPath = "E:\\tmp\\upload.txt";
    private static ExecutorService fixedThreadPool;
    private static List<File> filelist = new ArrayList<>();
    private boolean exit=false;
    public static void main(String[] args) throws Exception{
        if(args.length<4){
            System.out.println("params is : filesdir lengthPerFILE Maxthreadnum outputFileName");
            System.out.println("java -jar 1-1.0-SNAPSHOT-jar-with-dependencies.jar ./170811/ 10000 20 ./tmp/upload.txt");
            System.exit(0);
        }
        unZipHeaser = args[0];
        final int length = Integer.parseInt(args[1]);
        int ths = Integer.parseInt(args[2]);
        dstPath = args[3];
        //初始化线程池
        fixedThreadPool = Executors.newFixedThreadPool(ths);
        //初始化 BloomFilter
        initBloomFilter();

        //获取文件列表
        getFileList(unZipHeaser);
        System.out.println("file sum :" + filelist.size());
        if(filelist.size()==0){
            System.out.println("none files exist");
            System.exit(0);
        }

        //数据处理线程
//        Thread dataHandle = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    handleZipFiles(unZipHeaser);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        try {
            for (int i=0;i<filelist.size();i++) {
                //生成数据生产线程，并放入队列中
                LoadData2Queue ld = new LoadData2Queue(filelist.get(i));
                fixedThreadPool.execute(ld);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fixedThreadPool.shutdown();
        }
        //数据消费线程，放入目标文件中
        OutputT output = new OutputT(dstPath,length);
        new Thread(output).start();

        //是否程序结束
        boolean is_run=true;
        while(is_run) {
            try {
                fixedThreadPool.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("fixedthp is termination");
            output.stopTask();
            is_run=false;
            Thread.sleep(1000);
        }

        System.exit(0);
//        test02(3);
    }

    private static void initBloomFilter() {
        try {
            TreeSet<String> set = UrlCheck.getCheck().getAll("*");
            System.out.println(set.size());
            for (String str : set) {
                System.out.println(str);
                UrlFilter.getFilter().add(str);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static  void getFileList(String strPath) {
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
                } else if (fileName.endsWith("zip")) { // 判断文件名是否以.avi结尾
                    String strFileName = files[i].getAbsolutePath();
//                    System.out.println("---" + strFileName);
                    filelist.add(files[i]);
                } else {
                    continue;
                }
            }

        }
//        System.out.println(filelist.size());
    }



}
