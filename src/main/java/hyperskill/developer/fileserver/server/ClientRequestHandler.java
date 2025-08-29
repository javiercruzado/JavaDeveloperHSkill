package hyperskill.developer.fileserver.server;

import hyperskill.developer.fileserver.common.FileEntry;
import hyperskill.developer.fileserver.common.FileEntryStore;
import hyperskill.developer.fileserver.common.RequestFileMessage;
import hyperskill.developer.fileserver.common.ResponseFileMessage;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientRequestHandler implements Runnable {

    public static final String serverDataPath = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator;

    private final Socket socket;
    final ServerHandlerCallback serverHandlerCallback;

    private static final FileEntryStore store = new FileEntryStore("entries.txt");

    ClientRequestHandler(Socket socket, ServerHandlerCallback serverHandlerCallback) {
        this.socket = socket;
        this.serverHandlerCallback = serverHandlerCallback;
    }

    @Override
    public void run() {

        try (
                var objectInput = new ObjectInputStream(socket.getInputStream());
                var objectOutput = new ObjectOutputStream(socket.getOutputStream())
        ) {
            //noinspection InfiniteLoopStatement
            while (true) {
                var requestFileMessage = (RequestFileMessage) objectInput.readObject();

                var action = requestFileMessage.getAction();
                switch (action.toUpperCase()) {
                    case "GET" ->
                            handleFileInServer(requestFileMessage, serverDataPath, "read", (status, fileId, fileContent) -> {
                                var responseFileMessage = new ResponseFileMessage();
                                responseFileMessage.setStatus(status);
                                responseFileMessage.setFileContent(fileContent);
                                objectOutput.writeObject(responseFileMessage);
                                objectOutput.flush();

                            });
                    case "PUT" ->
                            handleFileInServer(requestFileMessage, serverDataPath, "write", (status, fileId, fileContent) -> {
                                var responseFileMessage = new ResponseFileMessage();
                                responseFileMessage.setStatus(status);
                                responseFileMessage.setFileId(fileId);
                                objectOutput.writeObject(responseFileMessage);
                                objectOutput.flush();
                            });
                    case "DELETE" ->
                            handleFileInServer(requestFileMessage, serverDataPath, "delete", (status, fileId, fileContent) -> {
                                var responseFileMessage = new ResponseFileMessage();
                                responseFileMessage.setStatus(status);
                                objectOutput.writeObject(responseFileMessage);
                                objectOutput.flush();
                            });
                    case "EXIT" -> closeSocket();
                    default -> System.err.println("Unknown action received!");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.printf("Error processing request: %s%n", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            closeSocket();
        }
    }

    private void closeSocket() {
        serverHandlerCallback.exitServer();
    }

    public static void handleFileInServer(RequestFileMessage requestFileMessage, String serverFilesPath, String operation, ResponseFileCallback responseFileCallback) throws IOException {

        Path filePath = null;
        if (requestFileMessage.getFileNameOnServer() != null && !requestFileMessage.getFileNameOnServer().isEmpty()) {
            filePath = Paths.get(serverFilesPath, requestFileMessage.getFileNameOnServer());
        } else if (requestFileMessage.getFileName() != null && !requestFileMessage.getFileName().isEmpty()) {
            filePath = Paths.get(serverFilesPath, requestFileMessage.getFileName());
        } else if (requestFileMessage.getFileId() != null && requestFileMessage.getFileId() > 0 && store.getEntryById(requestFileMessage.getFileId()) != null) {
            filePath = Paths.get(serverFilesPath, store.getEntryById(requestFileMessage.getFileId()).name());
        }

        assert filePath != null;
        if (!Files.isRegularFile(filePath)) {
            responseFileCallback.onResponse("404", 0, null);
        }

        switch (operation.toLowerCase()) {
            case "read":
                if (Files.exists(filePath)) {
                    try {
                        byte[] fileContent = Files.readAllBytes(filePath);
                        responseFileCallback.onResponse("200", 0, fileContent);
                    } catch (IOException e) {
                        System.out.println("Error reading file: " + e.getMessage());
                        responseFileCallback.onResponse("400", 0, null);
                    }
                } else {
                    responseFileCallback.onResponse("404", 0, null);
                }
                break;

            case "write":
                try {
                    if (!Files.exists(filePath)) {
                        Files.write(filePath, requestFileMessage.getFileContent());
                        var fileId = store.getAll().stream().mapToInt(FileEntry::id).max().orElse(0) + 1;
                        store.add(fileId, requestFileMessage.getFileNameOnServer());
                        responseFileCallback.onResponse("200", fileId, null);
                    } else {
                        responseFileCallback.onResponse("403", 0, null);
                    }
                } catch (IOException e) {
                    responseFileCallback.onResponse("403", 0, null);
                }
                break;

            case "delete":
                if (Files.exists(filePath)) {
                    try {
                        Files.delete(filePath);
                        int idToRemove = store.getAll().stream()
                                .filter(entry -> entry.name().equals(requestFileMessage.getFileName()))
                                .mapToInt(FileEntry::id)
                                .findFirst()
                                .orElse(-1);

                        store.remove(idToRemove);
                        responseFileCallback.onResponse("200", 0, null);
                    } catch (IOException e) {
                        responseFileCallback.onResponse("404", 0, null);
                    }
                } else {
                    responseFileCallback.onResponse("404", 0, null);
                }
                break;

            default:
                System.out.println("Invalid operation. Use 'read', 'write', or 'delete'.");
                break;
        }
    }
}
