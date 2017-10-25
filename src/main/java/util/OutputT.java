package util;

import com.sun.corba.se.impl.ior.OldJIDLObjectKeyTemplate;
import dataStr.FileObject;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class OutputT implements Runnable {
    private String fileName;
    private int totLine;
    private int lineCount;
    private boolean is_run;
    private static final Logger log = Logger.getLogger(OutputT.class);
    public OutputT(String fileName, int totLine) {
        this.fileName = fileName;
        this.totLine = totLine;
        this.is_run = true;
    }
    @Override
    public void run() {

        while(is_run){
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LinkedList<Object> list=new LinkedList<>();
            try {
    //            LinkedList<Object> list = WriterQueue.getQueue().takeAll();
                int i=0;
                if(BlockingQ.getQueue().size()>0) {
                    for (i = 0; i < BlockingQ.getQueue().size(); i++) {
                            list.add(BlockingQ.getQueue().take());
                    }
                    System.out.println("get " + i +" s data");
                    write2Disk(list);
                    list = null;
                }else{
                    System.out.println("no data , sleep 100 ms");
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void stopTask(){
        this.is_run=false;
    }

    private void write2Disk(LinkedList<Object> list) {

        if(list==null ||list.size()==0){
            System.out.println("no data...");
            return;
        }

        System.out.println("开始序列化数据 "+fileName);

//        String path = "E:/tmp/";
        File outputFile = new File(fileName);

        if(!outputFile.getParentFile().exists())outputFile.getParentFile().mkdir();

        if(outputFile==null ||!outputFile.exists()){
            try {
                outputFile.createNewFile();
                lineCount=0;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out = null;
        OutputStreamWriter writer = null;
        BufferedWriter bw = null;

        try {
            //行数统计
            lineCount = countLine(outputFile);
            System.out.println("curr " + lineCount);
            out = new FileOutputStream(outputFile, true);
            writer = new OutputStreamWriter(out);
            bw = new BufferedWriter(writer);

            for(Object content : list){
                //FileObject 对象
                FileObject fobj = (FileObject)content;
                String url = new String(fobj.getUrl());
                String data = new String(fobj.getData());
//                System.out.println(data);
                data.replaceAll("\\r\\n","\\|\\|");
                data.replaceAll("\\n","\\|\\|");
                data.replaceAll("\\r","\\|\\|");
                data = url + "||" +data;
                bw.write(data);
                bw.newLine();
                bw.flush();
                lineCount++;
                if(lineCount > this.totLine){
                    log.info("rename and create");
                    //文件关闭
                    bw.close();
                    //文件重命名
                    renameFile(outputFile);
                    //重新打开文件
                    lineCount = 0;
                    outputFile.createNewFile();
                    out = new FileOutputStream(outputFile, true);
                    writer = new OutputStreamWriter(out);
                    bw = new BufferedWriter(writer);
                    //lineCount=0
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(writer!=null)
                    writer.close();
                if(bw!=null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sleep(int millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    public int countLine(File file) {
        int lines = 0;
        long fileLength = file.length();
        LineNumberReader rf = null;
        try {
            rf = new LineNumberReader(new FileReader(file));
            if (rf != null) {
                rf.skip(fileLength);
                lines = rf.getLineNumber();
                rf.close();
            }
        } catch (IOException e) {
            if (rf != null) {
                try {
                    rf.close();
                } catch (IOException ee) {
                }
            }
        }
        return lines;
    }
    public static void renameFile(File file){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String time = df.format(new Date());// new Date()为获取当前系统时间
        file.renameTo(new File(file.toString()+ time));
    }

}