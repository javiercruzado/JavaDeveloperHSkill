package hyperskill.developer.fileserver.server;

import java.io.IOException;

public interface ResponseFileCallback {
    void onResponse(String status, Integer fileId, byte[] fileContent) throws IOException;
}
