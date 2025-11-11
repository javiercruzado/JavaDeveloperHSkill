package hyperskill.developer.jsondatabase.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonDatabaseManager {
    public static final String SERVER_DATA_PATH = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator;

    private final Path filePath;
    Map<String, String> data = new HashMap<>();

    final ReadWriteLock lock = new ReentrantReadWriteLock();
    final Lock readLock = lock.readLock();
    final Lock writeLock = lock.writeLock();


    public JsonDatabaseManager() {
        String DB_FILE_NAME = "db.json";
        this.filePath = Paths.get(SERVER_DATA_PATH, DB_FILE_NAME);
    }

    public String getDataByKey(String key) {
        loadFromFile();
        return data.get(key);
    }

    public void setRecord(String key, String text) {
        loadFromFile();
        data.put(key, text);
        saveToFile();
    }

    public void deleteRecord(String key) {
        loadFromFile();
        data.remove(key);
        saveToFile();
    }

    private void saveToFile() {

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writeLock.lock();
            writer.write(new Gson().toJson(data));
        } catch (IOException e) {
            System.err.println("Save error: " + e.getMessage());
        } finally {
            writeLock.unlock();
        }
    }

    private void loadFromFile() {
        if (Files.exists(filePath)) {
            try {
                readLock.lock();
                String content = Files.readString(filePath);
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                data = new Gson().fromJson(content, type);
                if (data == null) {
                    data = new HashMap<>();
                }
            } catch (IOException e) {
                System.err.println("Load error: " + e.getMessage());
            } finally {
                readLock.unlock();
            }

        }
    }
}
