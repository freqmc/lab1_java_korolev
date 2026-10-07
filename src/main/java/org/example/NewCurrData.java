package org.example;

import java.io.File;
/**
 * поток проверки на превышение объёма файла. проверка происходит раз в 10 секунд.
 */
public class NewCurrData extends Thread{
    private Utilities util = new Utilities();
    private final File file = new File("currentdata.txt");

    public void run(){
        while (!isInterrupted()){
            try{
                Thread.sleep(10000);
                util.check_file(file);
            }
            catch (java.lang.InterruptedException e){

                break;
            }
        }
        System.out.println("поток прерван");
    }
}
