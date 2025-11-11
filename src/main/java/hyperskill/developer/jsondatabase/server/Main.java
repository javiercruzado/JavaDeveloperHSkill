package hyperskill.developer.jsondatabase.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static final String ADDRESS = "127.0.0.1";
    public static final int PORT = 23456;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        System.out.println("Server started!");

        try (executor; ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {

            var database = new JsonDatabaseManager();
            final boolean[] exitServer = {false};
            while (!exitServer[0]) {
                Socket socket = serverSocket.accept();
                executor.submit(new RequestHandler(socket, database, new RequestHandlerCallback() {
                    @Override
                    public void exit() {
                        exitServer[0] = true;
                        executor.shutdown();
                        System.exit(0);
                    }

                    @Override
                    public void closeSocket() {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }
}
