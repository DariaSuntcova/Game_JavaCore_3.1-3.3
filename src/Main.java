import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private static StringBuilder log = new StringBuilder();
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd 'в' HH:mm:ss");
    private static List<String> nameList = new ArrayList<>();

    public static void main(String[] args) {

        nameList = Arrays.asList("src", "res", "saveGames", "temp");
        addNewDir("F://Games");

        nameList = Arrays.asList("main", "test");
        addNewDir("F://Games//src");

        nameList = Arrays.asList("Main.java", "Utils.java");
        addNewFile("F://Games//src//main");

        nameList = Arrays.asList("drawables", "vectors", "icons");
        addNewDir("F://Games//res");

        nameList = List.of("temp.txt");
        addNewFile("F://Games//temp");

        writeLog();

    }

    public static void addNewFile(String parent) {
        for (String name : nameList) {
            File dir = new File(parent, name);
            try {
                if (dir.createNewFile()) {
                    logging("Файл " + name + " создан");
                }
            } catch (IOException e) {
                System.out.println("Что-то пошло не так при создании файла " + name);
                logging("Ошибка при создании файла " + name);
            }
        }
    }

    public static void addNewDir(String parent) {
        for (String name : nameList) {
            File dir = new File(parent, name);
            if (dir.mkdir()) {
                logging("Каталог " + name + " создан");
            } else {
                logging("ОШИБКА при создании каталога " + name);
            }
        }
    }

    public static void logging(String message) {
        log.append(FORMATTER.format(new Date()));
        log.append(" ");
        log.append(message);
        log.append("\n");
    }

    public static void writeLog() {
        try (FileWriter writer = new FileWriter("F://Games//temp//temp.txt", true)) {
            writer.write(log.toString());
            System.out.println("В файл temp.txt добавлена новая запись");
            System.out.println(log);
            log = new StringBuilder();

        } catch (IOException e) {
            System.out.println("Ошибка записи в файл temp.txt");
            System.out.println(log);
        }
    }

}
