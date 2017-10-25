import org.apache.log4j.helpers.SyslogWriter;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main1 {
    public static List<File> filelist=new ArrayList<>();
    public static void main(String[] args) {
//        getFileList("E:\\Project1\\HS\\02 数据资料\\170811\\");
//        System.out.println(filelist.size());
//        System.out.println(filelist.get(0).toString());
        renameFile(new File("E:\\project1abc.txt"));
        try {
            new File("E:\\project1abc.txt").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        System.out.println(ts.toString());
        System.out.println(System.currentTimeMillis());
        String a = "aafdsfasdf\r\nsdfa";
        System.out.println(a.replaceAll("\\r\\n","\\|\\|"));
        String b = a.replaceAll("\\r\\n","\\|\\|");
        System.out.println(b.split("\\|\\|").length);

        String dir = "E:\\tmp1\\upload.txt";
        File file = new File(dir);
        System.out.println(file.getParentFile().exists());
        System.out.println(file.getParentFile().isDirectory());
        file.getParentFile().mkdir();
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
    public static void renameFile(File file){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String time = df.format(new Date());// new Date()为获取当前系统时间
        System.out.println(new File(file.toString()+ time).toString());
        file.renameTo(new File(file.toString()+ time));
    }
}
