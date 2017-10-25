import dataStr.FileObject;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FileReaderT {
    public static void main(String[] args) throws Exception{
        File file = new File("e:\\tmp\\upload.txt");

//        String a ="absd\r\nfsdfs";
//        System.out.println(a);
//        FileObject fobj = new FileObject();
//        fobj.setUrl("ad");
//        fobj.setData("sdfs".getBytes());
////        for(int i=0;i<100;i++) {
////            saveModel(file, fobj);
////        }
//        List<FileObject> fobj1;
//        fobj1 = loadModel(file,fobj);
//        for(FileObject obj : fobj1) {
//            System.out.println(new String(obj.getData()));
//            System.out.println(obj.getUrl());
//        }

        //对文件的写入和对文件的读取
//        FileOutputStream fos = new FileOutputStream(file);
//        //OutputStreamWriter这个函数的构造方法需要一个OutputStream对象
//        OutputStreamWriter osw = new OutputStreamWriter(fos);
//        //通常我们构造BufferedWriter去进行写入操作,是一个更有效的写入操作。参数需要Writer类型。
//        BufferedWriter bw = new BufferedWriter(osw);
//        for(int i=0;i<5;i++) {
//            bw.write("sdf");
//            bw.newLine();
//            bw.flush();
//        }
//        bw.close();
//
//
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String line=null;
        int n=0;
        while ((line=br.readLine())!=null){
            System.out.println("line || count:" +line.trim().split("\\|\\|").length+ " " +line.split("\\|\\|")[0]);
            System.out.println(line);
            for (String str : line.split("\\|\\|")){
                System.out.println(str);
            }
            n++;
//            if(n>10)break;
            break;
        }

//        System.out.println(br.readLine());
        br.close();
    }
    /**
     * Saves the extraction model to the file.
     */
    public static void saveModel(File file, Object obj) throws Exception {
        BufferedOutputStream bufferedOut =
                new BufferedOutputStream(new FileOutputStream(file,true)); // 文件路径
        ObjectOutputStream out = new ObjectOutputStream(bufferedOut);
        out.writeObject(obj);
        out.flush();
        out.close();
    }


    //从文件中加载信息到对象,可以这么做:
    /**
     * Loads the extraction model from the file.
     */
    public static List<FileObject> loadModel(File file, FileObject obj) throws Exception {
        List<FileObject> listobj = new ArrayList<FileObject>();
        ObjectInputStream inStream =
                new ObjectInputStream(new FileInputStream(file)); // 文件路径
        ObjectInputStream in = new ObjectInputStream(inStream);
        while(in.available()>0) {
            obj = (FileObject)in.readObject();
            listobj.add(obj);
        }
        in.close();
        return listobj;
    }
}
