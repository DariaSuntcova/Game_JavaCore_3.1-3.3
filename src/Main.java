import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    private static StringBuilder log = new StringBuilder();
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd 'в' HH:mm:ss");

    private static List<String> nameList = new ArrayList<>();

    private static List<String> savingPlayer = new ArrayList<>();


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

        GameProgress player1 = addNewPlayer("Петя", 100, 25, 3, 12.7);
        saveGame("F://Games//saveGames//player1.dat", player1);
        GameProgress player2 = addNewPlayer("Вася", 125, 51, 5, 31.1);
        saveGame("F://Games//saveGames//player2.dat", player2);
        GameProgress player3 = addNewPlayer("Коля", 70, 15, 2, 8.9);
        saveGame("F://Games//saveGames//player3.dat", player3);

        zipFiles("F://Games//saveGames//saveGames.zip");

        writeLog();

        deleteFile();

        openZip("F://Games//saveGames//saveGames.zip");

        GameProgress newPlayer = openProgress("F://Games//saveGames//player2.dat");

        writeLog();

        System.out.println(newPlayer);

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

    public static GameProgress addNewPlayer(String namePlayer, int health, int weapons, int lvl, double distance) {
        GameProgress player = new GameProgress(namePlayer, health, weapons, lvl, distance);
        logging("Добавлен новый игрок: " + namePlayer);
        return player;
    }

    public static void saveGame(String parent, GameProgress player) {
        try (FileOutputStream fos = new FileOutputStream(parent);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(player);
            logging("Игра сохранена. Игрок " + player.getNamePlayer());
            savingPlayer.add(parent);
        } catch (IOException e) {
            logging("Ошибка при попытке сохранить игру. Игрок " + player.getNamePlayer());
            System.out.println("Ошибка записи в файл " + parent);
        }
    }

    public static void zipFiles(String parent) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(parent))) {
            byte[] buffer;
            for (String name : savingPlayer) {
                try (FileInputStream fis = new FileInputStream(name)) {
                    ZipEntry entry = new ZipEntry(name);
                    zout.putNextEntry(entry);
                    buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                }
            }

            zout.closeEntry();
            logging("Создан новый архив " + parent);
        } catch (Exception e) {
            logging("Ошибка при создании архива " + parent);
            System.out.println("Ошибка при попытке создать архив " + parent);
            return;
        }
    }

    public static void deleteFile() {
        for (String name : savingPlayer) {
            File file = new File(name);
            if (file.delete()) {
                logging("Файл " + name + " удален");
            } else {
                logging("Ошибка удаления файла " + name);
                return;
            }
        }
        savingPlayer = new ArrayList<>();
    }

    public static void openZip(String from) {
        try (ZipInputStream zin = new ZipInputStream(
                new FileInputStream(from))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(name);
                for (int i = zin.read(); i != -1; i = zin.read()) {
                    fout.write(i);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
                logging("Распаковка файла " + name + " прошла успешно");
            }
        } catch (Exception e) {
            logging("Ошибка при распаковке файла");
        }
    }

    public static GameProgress openProgress(String parent) {
        GameProgress gameProgress = null;

        try (FileInputStream fis = new FileInputStream(parent);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            gameProgress = (GameProgress) ois.readObject();
            logging("Найден новый игрок: " + gameProgress.getNamePlayer());

        } catch (Exception e) {
            logging("Ошибка при распаковке нового игрока");
        }
        return gameProgress;
    }

}

