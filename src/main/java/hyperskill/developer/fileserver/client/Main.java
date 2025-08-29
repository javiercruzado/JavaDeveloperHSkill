package hyperskill.developer.fileserver.client;

import hyperskill.developer.fileserver.common.Constants;
import hyperskill.developer.fileserver.common.FileHelper;
import hyperskill.developer.fileserver.common.RequestFileMessage;
import hyperskill.developer.fileserver.common.ResponseFileMessage;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static final String clientDataPath = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "client" + File.separator + "data" + File.separator;

    public static void main(String[] args) {
        try (var socket = new Socket(InetAddress.getByName(Constants.SERVER_ADDRESS), Constants.PORT);
             var scanner = new Scanner(System.in);
             var objectOutput = new ObjectOutputStream(socket.getOutputStream());
             var objectInput = new ObjectInputStream(socket.getInputStream())
        ) {
            boolean endClient = false;

            while (!endClient) {
                System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
                String action = scanner.nextLine();
                var requestMessage = new RequestFileMessage();

                switch (action) {

                    case "1" -> handleGetFile(scanner, objectInput, objectOutput, requestMessage);
                    case "2" -> handleCreateFile(scanner, objectInput, objectOutput, requestMessage);
                    case "3" -> handleDeleteFile(scanner, objectInput, objectOutput, requestMessage);
                    case "exit" -> {
                        handleExit(objectOutput, requestMessage);
                        endClient = true;
                    }
                    default -> System.out.println("Invalid action. Please try again.");
                }
            }
        } catch (IOException e) {
            if (e instanceof ConnectException) {
                System.err.println("Could not connect to the server. Please ensure the server is running.");
            } else {
                System.out.printf("An error occurred: %s%n", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        catch (Exception e){
            System.err.printf("An unexpected error occurred: %s%n", e.getMessage());
            logExceptionToFile(e);
        } finally {
            System.out.println("Client has been closed.");
        }
    }

    private static void handleGetFile(Scanner scanner, ObjectInputStream input, ObjectOutputStream output, RequestFileMessage requestMessage) throws IOException {
        requestMessage.setAction("GET");
        System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "" -> {
                System.out.println("Please choose an option (1 or 2).");
                return;
            }
            case "1" -> {
                System.out.print("Enter name of the file: ");
                String fileName = scanner.nextLine();
                if (fileName.isEmpty()) {
                    System.out.println("File name cannot be empty. Please try again.");
                    return;
                }
                requestMessage.setFileName(fileName);
            }
            case "2" -> {
                System.out.print("Enter id: ");
                int id = scanner.nextInt();
                requestMessage.setFileId(id);
                scanner.nextLine();
            }
            default -> {
                System.out.println("Invalid choice. Please try again.");
                return;
            }
        }

        try {
            output.writeObject(requestMessage);
            output.flush();
            System.out.println("The request was sent.");

            ResponseFileMessage message = (ResponseFileMessage) input.readObject();
            if ("200".equals(message.getStatus())) {
                System.out.println("The file was downloaded! Specify a name for it: ");
                String nameForDownloadedFile = scanner.nextLine();
                FileHelper.saveFile(nameForDownloadedFile, clientDataPath, message.getFileContent());
            } else if ("404".equals(message.getStatus())) {
                System.out.println("The response says that this file is not found!!");
            } else {
                System.out.println("Unknown response from server.");
            }
        } catch (ClassNotFoundException e) {
            System.out.printf("Error reading the response %s.%n", e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.printf("Error with the file: %s. Please check the file name and try again.%n", e.getMessage());
        }

    }

    private static void handleCreateFile(Scanner scanner, ObjectInputStream input, ObjectOutputStream output, RequestFileMessage requestMessage) throws IOException {
        requestMessage.setAction("PUT");
        System.out.print("Enter name of the file: ");
        requestMessage.setFileName(scanner.nextLine());

        System.out.print("Enter name of the file to be saved on server: ");
        requestMessage.setFileNameOnServer(scanner.nextLine());

        try {
            requestMessage.setFileContent(FileHelper.readFile(requestMessage.getFileName(), clientDataPath));
            output.writeObject(requestMessage);
            output.flush();

            System.out.println("The request was sent.");

            ResponseFileMessage message = (ResponseFileMessage) input.readObject();
            if ("200".equals(message.getStatus())) {
                System.out.printf("The response says that file is saved! ID=%d%n", message.getFileId());
            } else if ("403".equals(message.getStatus())) {
                System.out.println("The response says that creating the file was forbidden!");
            } else {
                System.out.println("The response says that creating the file was forbidden!");
            }

        } catch (ClassNotFoundException e) {
            System.out.printf("Error reading the response %s.%n", e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.printf("Error saving the file: %s. Please check the file name and try again.%n", e.getMessage());
        }

    }

    private static void handleDeleteFile(Scanner scanner, ObjectInputStream input, ObjectOutputStream output, RequestFileMessage requestMessage) throws IOException {
        requestMessage.setAction("DELETE");
        System.out.print("Enter filename: ");
        requestMessage.setFileName(scanner.nextLine());

        try {
            output.writeObject(requestMessage);
            output.flush();
            System.out.println("The request was sent.");

            ResponseFileMessage message;
            message = (ResponseFileMessage) input.readObject();
            if ("200".equals(message.getStatus())) {
                System.out.println("The response says that the file was successfully deleted!");
            } else if ("404".equals(message.getStatus())) {
                System.out.println("The response says that the file was not found!");
            } else {
                System.out.println("Unknown response from server.");
            }
        } catch (ClassNotFoundException e) {
            System.out.printf("Error reading the response %s.%n", e.getMessage());
        }

    }

    private static void handleExit(ObjectOutputStream output, RequestFileMessage requestMessage) throws IOException {
        requestMessage.setAction("EXIT");
        output.writeObject(requestMessage);
        output.flush();
        System.out.println("The request was sent.");
    }

    private static void logExceptionToFile(Exception ex) {
        try (var logWriter = new java.io.FileWriter("client-error.log", true)) {
            logWriter.write("An unexpected error occurred: " + ex.getMessage() + "\n");
            java.io.StringWriter sw = new java.io.StringWriter();
            ex.printStackTrace(new java.io.PrintWriter(sw));
            logWriter.write(sw.toString());
        } catch (IOException logEx) {
            System.out.println("Failed to write to log file: " + logEx.getMessage());
        }
    }
}
