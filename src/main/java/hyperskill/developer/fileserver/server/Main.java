package hyperskill.developer.fileserver.server;

import java.io.IOException;

import static hyperskill.developer.fileserver.common.Constants.PORT;
import static hyperskill.developer.fileserver.common.Constants.SERVER_ADDRESS;

public class Main {

    public static void main(String[] args) {
        try {
            new FileServer(SERVER_ADDRESS, PORT);
        } catch (IOException e) {
            throw new RuntimeException("Server initialization failed", e);
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
