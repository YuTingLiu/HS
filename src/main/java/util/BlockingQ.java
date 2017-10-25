package util;

import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

public class BlockingQ {

    private static ArrayBlockingQueue queue = new ArrayBlockingQueue<Object>(1000);

    public static ArrayBlockingQueue getQueue(){

        return queue;
    }

//    public void put(Object phone){
//        try {
//            queue.put(phone);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public Object takeAll(){
//
//        Object retVal=null;
//
//        try {
//            retVal = queue.take();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return retVal;
//    }
}
