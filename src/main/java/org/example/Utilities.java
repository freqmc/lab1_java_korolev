package org.example;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * вспомогательный класс, который содержит методы для двух потоков
 */

public class Utilities {
    /**
     * метод get_timestamp нужен для создания временной метки
     * @экземпляр календаря
     * @преобразование к шаблону
     * @return форматированная временная метка
     */
    public String get_timestamp(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        return sdf.format(calendar.getTime());
    }

    /**
     * метод write_data предназначен для посимвольной записи в файл с паузой в 200 мс.
     * @param file - файл, куда производится запись
     * @param text - текст, который будет записан в файл
     * @param lock - объект-блокировка для синхронизации потоков
     */
    public void write_data(File file, String text, Object lock){
        synchronized (lock){
            try (FileOutputStream curr_data = new FileOutputStream(file, true)){
                for(char c : text.toCharArray()){
                    curr_data.write(c);
                    curr_data.flush();
                    Thread.sleep(200);
                }

                String timestamp = get_timestamp();
                curr_data.write(timestamp.getBytes());
                curr_data.flush();
            }
            catch (java.io.IOException e){
                System.out.println("не удалось записать данные в файл по причине " + e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("поток прерван");
            }
        }
    }

    /**
     * метод check_file предназначен для проверки превышение допустимого размера файла (200 байт). если объём>200 байт файл сохраняется под уникальным именем, а исходный - перезаписывается
     * @param file
     * @param lock
     */
    public void check_file(File file, Object lock){
       synchronized (lock){
           if (file.exists() && file.length() > 200){
               String new_filename = "currentdata_" + get_timestamp() + ".txt";
               File new_file = new File(new_filename);
               if (file.renameTo(new_file)){
                   System.out.println("файл больше 200 байт");
                   try{
                       file.createNewFile();
                   } catch (java.io.IOException e) {
                       System.out.println("ошибка при сохранении файла: " + e.getMessage());
                   }
               }
           }
           else {
               System.out.println("не удалось отделить файл");
           }
       }
    }
}
