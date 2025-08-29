package hyperskill.developer.fileserver.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileEntryStore {
    private final List<FileEntry> entries = new ArrayList<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Path filePath;

    public FileEntryStore(String fileName) {
        this.filePath = Paths.get(fileName);
        loadFromFile();
    }

    public void add(int id, String name) {
        lock.writeLock().lock();
        try {
            entries.add(new FileEntry(id, name));
            saveToFile();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<FileEntry> getAll() {
        lock.readLock().lock();
        try {
            return List.copyOf(entries);
        } finally {
            lock.readLock().unlock();
        }
    }

    public FileEntry getEntryById(int id) {
        lock.readLock().lock();
        try {
            return entries.stream()
                    .filter(entry -> entry.id() == id)
                    .findFirst()
                    .orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void remove(int id) {
        lock.writeLock().lock();
        try {
            entries.removeIf(entry -> entry.id() == id);
            saveToFile();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void saveToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (FileEntry entry : entries) {
                writer.write(entry.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Save error: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        if (Files.exists(filePath)) {
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    entries.add(FileEntry.fromString(line));
                }
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Load error: " + e.getMessage());
            }
        }
    }
}