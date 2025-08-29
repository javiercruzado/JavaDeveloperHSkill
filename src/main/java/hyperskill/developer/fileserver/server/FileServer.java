package hyperskill.developer.fileserver.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileServer {
    private static final int THREAD_POOL_SIZE = 10;

    FileServer(String serverAddress, int port) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try (executor;
             ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(serverAddress))) {
            System.out.println("Server started!");
            final boolean[] processing = {true};
            while (processing[0]) {
                var clientSocket = server.accept();
                // Submit a callable and optionally capture the result
                executor.submit(new ClientRequestHandler(clientSocket, () -> {
                    //noinspection finally
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        processing[0] = false;
                        System.exit(0);
                    }
                }));
            }
        } catch (Exception ex) {
            logExceptionToFile(ex);
        } finally {
            executor.shutdown();
        }
    }

    private static void logExceptionToFile(Exception ex) {
        try (var logWriter = new java.io.FileWriter("server-error.log", true)) {
            logWriter.write("An unexpected error occurred: " + ex.getMessage() + "\n");
            java.io.StringWriter sw = new java.io.StringWriter();
            ex.printStackTrace(new java.io.PrintWriter(sw));
            logWriter.write(sw.toString());
        } catch (IOException logEx) {
            System.out.println("Failed to write to log file: " + logEx.getMessage());
        }
    }
}
