package hyperskill.developer.jsondatabase.server;

import com.google.gson.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class RequestHandler implements Runnable {

    private final Socket socket;
    private final JsonDatabaseManager database;
    private final RequestHandlerCallback callback;

    public RequestHandler(Socket socket, JsonDatabaseManager database, RequestHandlerCallback callback) {
        this.database = database;
        this.socket = socket;
        this.callback = callback;
    }

    @Override
    public void run() {
        String request;
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            request = input.readUTF();

            JsonObject requestJson = JsonParser.parseString(request).getAsJsonObject();

            String command = requestJson.get("type").getAsString();
            JsonElement keyElement = requestJson.get("key");
            JsonElement keyValue = requestJson.get("value");

            switch (command) {

                case "get": {
                    String value = null;
                    if (keyElement.isJsonPrimitive()) {
                        value = database.getDataByKey(keyElement.getAsString());
                    } else if (keyElement.isJsonArray()) {
                        JsonArray path = keyElement.getAsJsonArray();
                        String key = path.get(0).getAsString();
                        String dataByKey = database.getDataByKey(key);
                        if (dataByKey != null) {
                            JsonElement currentElement = JsonParser.parseString(dataByKey).getAsJsonObject();
                            for (int i = 1; i < path.size(); i++) {
                                key = path.get(i).getAsString();
                                if (!currentElement.isJsonObject()) {
                                    break;
                                }
                                currentElement = ((JsonObject) currentElement).get(key);
                            }
                            if (currentElement != null) {
                                value = currentElement.toString();
                            }
                        }
                    }
                    if (!Objects.equals(value, null)) {
                        output.writeUTF(
                                new Gson().toJson(new Response("OK", null, value))
                        );
                    } else {
                        output.writeUTF(
                                new Gson().toJson(new Response("ERROR", "No such key", null))
                        );
                    }
                    break;
                }

                case "set": {

                    if (keyElement.isJsonPrimitive()) {
                        database.setRecord(keyElement.getAsString(), keyValue.toString());
                    } else if (keyElement.isJsonArray()) {
                        JsonArray path = keyElement.getAsJsonArray();
                        String key = path.get(0).getAsString();
                        String dataByKey = database.getDataByKey(key);
                        if (dataByKey != null) {
                            JsonElement currentElement = JsonParser.parseString(dataByKey).getAsJsonObject();
                            var toSave = currentElement;
                            boolean notFound = false;
                            for (int i = 1; i < path.size() - 1; i++) {
                                key = path.get(i).getAsString();
                                if (!currentElement.isJsonObject()) {
                                    notFound = true;
                                    break;
                                }
                                currentElement = ((JsonObject) currentElement).get(key);
                            }
                            if (notFound) {
                                output.writeUTF(
                                        new Gson().toJson(new Response("ERROR", "No such key", null))
                                );
                            } else {
                                var currentJson = (JsonObject) currentElement;
                                if (keyValue.isJsonObject()) {
                                    currentJson.addProperty(path.get(path.size() - 1).getAsString(), keyValue.toString());
                                } else {
                                    currentJson.addProperty(path.get(path.size() - 1).getAsString(), keyValue.getAsString());
                                }
                                database.setRecord(path.get(0).getAsString(), toSave.toString());
                                output.writeUTF(
                                        new Gson().toJson(new Response("OK", null, null))
                                );
                            }
                        } else {
                            output.writeUTF(
                                    new Gson().toJson(new Response("ERROR", "No such key", null))
                            );
                        }
                    }
                    break;
                }
                case "delete":

                    if (keyElement.isJsonPrimitive()) {
                        database.deleteRecord(keyElement.getAsString());
                        output.writeUTF(
                                new Gson().toJson(new Response("OK", null, null))
                        );
                    } else if (keyElement.isJsonArray()) {
                        JsonArray path = keyElement.getAsJsonArray();
                        String key = path.get(0).getAsString();
                        String dataByKey = database.getDataByKey(key);
                        if (dataByKey != null) {
                            JsonElement currentElement = JsonParser.parseString(dataByKey).getAsJsonObject();
                            var toSave = currentElement;
                            boolean notFound = false;
                            for (int i = 1; i < path.size() - 1; i++) {
                                key = path.get(i).getAsString();
                                if (!currentElement.isJsonObject()) {
                                    notFound = true;
                                    break;
                                }
                                currentElement = ((JsonObject) currentElement).get(key);
                            }
                            if (notFound) {
                                output.writeUTF(
                                        new Gson().toJson(new Response("ERROR", "No such key", null))
                                );
                            } else {
                                var currentJson = (JsonObject) currentElement;
                                currentJson.remove(path.get(path.size() - 1).getAsString());
                                database.setRecord(path.get(0).getAsString(), toSave.toString());
                                output.writeUTF(
                                        new Gson().toJson(new Response("OK", null, null))
                                );
                            }
                        } else {
                            output.writeUTF(
                                    new Gson().toJson(new Response("ERROR", "No such key", null))
                            );
                        }
                    }
                    break;
                case "exit":
                    output.writeUTF(
                            new Gson().toJson(new Response("OK", null, null))
                    );
                    callback.exit();
                default:
                    output.writeUTF(
                            new Gson().toJson(new Response("ERROR", null, null))
                    );
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        callback.closeSocket();
    }

}
