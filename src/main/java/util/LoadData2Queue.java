package util;

import RedisUtil.UrlCheck;
import dataStr.FileObject;
import org.apache.log4j.Logger;
import util.WriterQueue;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class LoadData2Queue implements Runnable{

    private final File file;
    private static final Logger log = Logger.getLogger(LoadData2Queue.class);

    public LoadData2Queue(File filelist) { this.file = filelist; }
    public void run() {
        // read and service request on socket
        try {
            handleZipFiles(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 解析 zip包数据，取出所有文件解析，得到每个HTML与TXT文件，并过滤。
     * @param file
     * @throws Exception
     */
    public static void handleZipFiles (File file) throws Exception{
        int n = 0;
        byte[] data={};
        InputStream in = null;
        ZipInputStream zin = null;
        FileOutputStream out = null;
        BufferedOutputStream writer = null;
        try {
            ZipFile zf = new ZipFile(file);
            in = new BufferedInputStream(new FileInputStream(file));
            zin = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                } else {
                    log.info("file - " + ze.getName() + " : "
                            + ze.getSize() + " bytes");
                    long size = ze.getSize();
                    if (size > 0) {
                        InputStream is = zf.getInputStream(ze);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                        //返回域名
                        FileObject fobj = getUrl(bufferedReader);
//                            System.out.println(url);
                        //bloomFilter过滤
                        if(fobj.getData()!=null && fobj.getUrl()!=null){
                            if(UrlFilter.getFilter().contains(fobj.getUrl())==false){
                                data = input2byte(is);
//                                WriterQueue.getQueue().put(fobj);
                                BlockingQ.getQueue().put(fobj);
                                //写入文件
//                                    Timestamp ts = new Timestamp(System.currentTimeMillis());
//                                    File outputFile = new File(dstRoot + url +"."+System.currentTimeMillis());
//                                    outputFile.createNewFile();
//                                    out = new FileOutputStream(outputFile, true);
//                                    writer = new BufferedOutputStream(out);
//                                    writer.write(data);
                            }else{
                                log.info("filtered www.baidu.com" + ze.getName());
                            }

                        }

                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            zin.closeEntry();
        }
//            n++;
//            if(n>10)Thread.sleep(2000);System.exit(0);
    }

    /**
     * 由文件中取出Host
     * @param br
     * @return FileObject
     * @throws Exception
     */
    private static FileObject getUrl(BufferedReader br) throws Exception {
        String str = null;
        String url=null;
        String data="";
        while((str = br.readLine()) != null)
        {
            String host="Host";
            if(str.indexOf(host)!=-1){
                if(str.split(":").length>=2) {
//                    System.out.println(str);
                    url = str.split(":")[1].trim();
                }
                data += str;
            }else{
                data += str;
            }
        }
        FileObject fobj = new FileObject();
        fobj.setData(data);
        fobj.setUrl(url);
        return fobj;
    }

    public static final InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }
    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

}
