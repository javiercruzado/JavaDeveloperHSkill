package hyperskill.developer.searchcontacts;

import java.io.*;
import java.util.List;

record StorageManager(String fileName) {

    void saveContacts(List<Contact> contacts) {
        if (fileName == null || fileName.isEmpty()) {
            return;
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(contacts);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    List<Contact> LoadContacts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<Contact>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
