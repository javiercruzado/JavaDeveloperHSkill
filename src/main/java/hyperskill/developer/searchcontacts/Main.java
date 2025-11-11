package hyperskill.developer.searchcontacts;

import java.util.Scanner;

public class Main {
    static Scanner scanner;

    public static void main(String[] args) {
        String fileName = "";
        if (args.length == 1) {
            fileName = args[0];
        }

        scanner = new Scanner(System.in);

        ContactManager contactManager = new ContactManager(fileName);
        boolean isRunning = true;
        do {
            System.out.print("[menu] Enter action (add, list, search, count, exit): ");
            String action = scanner.nextLine();

            switch (action) {
                case "add" -> contactManager.addContact();
                case "list" -> contactManager.listContacts();
                case "search" -> contactManager.search();
                case "remove" -> contactManager.removeContact();
                case "count" ->
                        System.out.printf("The Phone Book has %d records.%n%n", contactManager.getContactsCount());
                case "info" -> contactManager.info();
                case "exit" -> isRunning = false;
                default -> System.out.println("Unknown action. Please try again.");
            }

        } while (isRunning);

        scanner.close();
    }

}

