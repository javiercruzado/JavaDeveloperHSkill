package hyperskill.developer.jsondatabase.server;


public interface RequestHandlerCallback {
    void exit();

    void closeSocket();
}
