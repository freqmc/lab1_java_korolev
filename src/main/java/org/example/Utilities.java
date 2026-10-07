package org.example;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Semaphore;

/**
 * вспомогательный класс, который содержит методы для двух потоков
 */

public class Utilities {
    public static final Semaphore semaphore = new Semaphore(1);

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
     выполняет посимвольную запись текста в файл с паузой 200мс между символами, а после записи текста добавляет временную метку.
     * <p>
     * метод захватывает семафор перед началом операции и освобождает его в блоке {@code finally},
     * гарантируя, что другие потоки не получат доступ к файлу во время записи.
     *
     * @param file файл для записи.
     * @param text записываемый текст.
     * @throws InterruptedException если поток был прерван во время ожидания семафора или в процессе записи (сна).
     */
    public void write_data(File file, String text) throws InterruptedException {
        semaphore.acquire();
        try {
            try (FileOutputStream curr_data = new FileOutputStream(file, true)) {
                for (char c : text.toCharArray()) {
                    curr_data.write(c);
                    curr_data.flush();
                    Thread.sleep(200);
                }
                String timestamp = get_timestamp();
                curr_data.write(timestamp.getBytes());
                curr_data.flush();
            } catch (java.io.IOException e) {
                System.out.println("не удалось записать данные в файл по причине " + e.getMessage());
            }
        } finally {
            semaphore.release();
        }
    }

    /**
     * проверяет размер файла. если его размер >200 байт, то он переименовывается с добавлением временной метки, а исходный файл обнуляется.
     * <p>
     * семафор нужен для предотвращения конфликта с записью.
     *
     * @param file проверяемый файл
     * @throws InterruptedException если поток был прерван во время ожидания семафора.
     */
    public void check_file(File file) throws InterruptedException{
       semaphore.acquire();
        try {
           if (file.exists() && file.length() > 200){
               String new_filename = "currentdata_" + get_timestamp() + ".txt";
               File new_file = new File(new_filename);
               if (file.renameTo(new_file)){
                   System.out.println("файл больше 200 байт");
                   try {
                        file.createNewFile();
                   } catch (java.io.IOException e) {
                       System.out.println("ошибка при сохранении файла: " + e.getMessage());
                   }
               }
           }
           else {
               System.out.println("не удалось отделить файл");
           }
        } finally {
            semaphore.release();
        }
    }
}
