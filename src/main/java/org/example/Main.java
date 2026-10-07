package org.example;
import java.util.Scanner;

/**
 * главный поток, откуда вызывается поток записи и поток проверки
 */
public class Main {
    public static void main(String[] args) {
        DataWriter data_writer = new DataWriter();
        NewCurrData new_curr_data = new NewCurrData();
        //запуск потоков
        data_writer.start();
        new_curr_data.start();
        System.out.println("нажмите enter для остановки");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); //ожидаем действие от пользователя
        System.out.println("завершение работы");
        //прерываение потоков
        data_writer.interrupt();
        new_curr_data.interrupt();
        try {
            data_writer.join();
            new_curr_data.join();
        }
        catch (java.lang.InterruptedException e){
            System.out.println("прерван глав. поток по причине " + e.getMessage());
        }
        System.out.println("работа окончена");
    }
}