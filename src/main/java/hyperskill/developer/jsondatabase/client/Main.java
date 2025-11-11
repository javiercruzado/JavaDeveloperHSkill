package hyperskill.developer.jsondatabase.client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import hyperskill.developer.jsondatabase.server.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {


    public static final String ADDRESS = "127.0.0.1";
    public static final int PORT = 23456;

    public static final String clientDataPath = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "client" + File.separator + "data" + File.separator;

    public static void main(String[] args) {

        Args commandArgs = new Args();
        JCommander.newBuilder()
                .addObject(commandArgs)
                .build()
                .parse(args);

        String requestJson;

        if (Objects.equals(commandArgs.fileName, "")) {
            requestJson = new Gson().toJson(new Request(commandArgs.type, commandArgs.key, commandArgs.value));
        } else {
            requestJson = readFile(commandArgs.fileName, clientDataPath);
        }


        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            {
                System.out.println("Client started!");

                System.out.printf("Sent: %s%n", requestJson);
                output.writeUTF(requestJson);

                String response = input.readUTF();
                System.out.printf("Received: %s%n", response);

                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFile(String fileName, String dataPath) {
        var filePath = Paths.get(dataPath, fileName);
        String fileContent = "";
        if (Files.exists(filePath)) {
            try {
                fileContent = new String(Files.readAllBytes(filePath));
            } catch (IOException e) {
                System.out.printf("Reading file exception %s%n", fileName);
            }
        } else {
            System.out.printf("File not found %s %s%n", dataPath, fileName);
        }
        return fileContent;
    }
}

