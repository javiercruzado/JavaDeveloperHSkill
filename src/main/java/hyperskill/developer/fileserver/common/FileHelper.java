package hyperskill.developer.fileserver.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHelper {
    public static void saveFile(String fileName, String dataPath, byte[] fileContent) throws IOException {
        var filePath = Paths.get(dataPath, fileName);
        Files.write(filePath, fileContent);
    }

    public static byte[] readFile(String fileName, String dataPath) throws IOException {
        var filePath = Paths.get(dataPath, fileName);
        if (Files.exists(filePath)) {
            return Files.readAllBytes(filePath);
        } else {
            throw new FileNotFoundException(fileName);
        }
    }
}
