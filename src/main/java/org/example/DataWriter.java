package org.example;
import java.io.File;

/**
 * поток для записи данных в файл. прерывается при действии пользователя
 */
public class DataWriter extends Thread{
    private Utilities util = new Utilities();
    private final Object lock = new Object();
    private final File file = new File("currentdata.txt");
    private String data = "the first thread writes this time:";
    private volatile boolean is_running = true;

    /**
     * метод остановки потока. флаг меняет значение на false и прерывает состояние
     */
    public void stop_thread(){
        is_running = false;
        this.interrupt();
    }

    /**
     * основной метод потока. пока пользователь не прервал вызывается метод записи. после записи поток "спит" одну секунду
     */
    public void run(){
        while (is_running){
            try{
                util.write_data(file, data, lock);
                Thread.sleep(1000);
            }
            catch (java.lang.InterruptedException e){
                System.out.println("поток прерван");
                break;
            }
        }
    }
}
