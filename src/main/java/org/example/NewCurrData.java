package org.example;

import java.io.File;

/**
 * поток проверки на превышение объёма файла. проверка происходит раз в 10 секунд.
 */
public class NewCurrData extends Thread{
    private Utilities util = new Utilities();
    private final Object lock = new Object();
    private final File file = new File("currentdata.txt");
    private volatile boolean is_running = true;

    public void stop_thread(){
        is_running = false;
        this.interrupt();
    }

    public void run(){
        while (is_running){
            try{
                Thread.sleep(10000);
                util.check_file(file, lock);
            }
            catch (java.lang.InterruptedException e){
                System.out.println("поток прерван");
                break;
            }
        }
    }
}
