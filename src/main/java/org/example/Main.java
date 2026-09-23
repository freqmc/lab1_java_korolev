package org.example;
import java.util.Scanner;
import java.io.File;

/**
 * главный поток, откуда вызывается поток записи и поток проверки
 */
public class Main {
    public static void main(String[] args) {
        File file = new File("currentdata.txt");
        Object lock = new Object();
        DataWriter data_writer = new DataWriter();
        NewCurrData new_curr_data = new NewCurrData();
        data_writer.start();
        new_curr_data.start();
        System.out.println("нажмите enter для остановки");
        Scanner scanner = new Scanner(System.in);
         scanner.nextLine();
        System.out.println("завершение работы");
        data_writer.stop_thread();
        new_curr_data.stop_thread();
        try {
            data_writer.join();
            new_curr_data.join();
        }
        catch (java.lang.InterruptedException e){
            System.out.println("не получилось завершить поток по причине " + e.getMessage());
        }
        System.out.println("работа окончена");
    }
}