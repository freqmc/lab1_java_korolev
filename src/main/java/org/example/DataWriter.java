package org.example;
import java.io.File;

/**
 * поток для записи данных в файл. прерывается при действии пользователя
 */
public class DataWriter extends Thread{
    private Utilities util = new Utilities();
    private final File file = new File("currentdata.txt");
    private String data = "the first thread writes this time:";

    public void run(){
        while (!isInterrupted()){
            try{
                util.write_data(file, data);
                if (!isInterrupted()){
                    Thread.sleep(1000);
                }
            }
            catch (java.lang.InterruptedException e){
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("поток прерван");
    }
}
