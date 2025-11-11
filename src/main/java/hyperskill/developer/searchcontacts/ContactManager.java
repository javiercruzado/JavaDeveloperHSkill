package hyperskill.developer.searchcontacts;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class ContactManager {

    private List<Contact> contacts;
    private StorageManager storageManager = null;

    ContactManager(String fileName) {
        if (!Objects.equals(fileName, "")) {
            storageManager = new StorageManager(fileName);
            contacts = storageManager.LoadContacts();
        }
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
    }

    void addContact() {

        System.out.print("Enter the type (person, organization): ");
        String contactType = Main.scanner.nextLine();

        Contact contact;
        if (contactType.equalsIgnoreCase("person")) {
            System.out.print("Enter the name of the person: ");
            String name = Main.scanner.nextLine();

            System.out.print("Enter the surname of the person: ");
            String surname = Main.scanner.nextLine();

            System.out.print("Enter the birth date: ");
            String birthDayInput = Main.scanner.nextLine();
            LocalDate birthDay = null;
            try {
                birthDay = LocalDate.parse(birthDayInput);
            } catch (Exception e) {
                System.out.println("Bad birth date!");
            }

            System.out.print("Enter the gender (M, F): ");
            String genderInput = Main.scanner.nextLine();
            Character gender = null;
            if (genderInput.equalsIgnoreCase("M") || genderInput.equalsIgnoreCase("F")) {
                gender = genderInput.charAt(0);
            } else {
                System.out.println("Bad gender!");
            }

            System.out.print("Enter the number: ");
            String phoneNumber = Main.scanner.nextLine();
            contact = new Person(name, surname, phoneNumber, birthDay, gender);

        } else if (contactType.equalsIgnoreCase("organization")) {
            System.out.print("Enter the organization name: ");
            String name = Main.scanner.nextLine();

            System.out.print("Enter the address: ");
            String address = Main.scanner.nextLine();

            System.out.print("Enter the number: ");
            String phoneNumber = Main.scanner.nextLine();

            contact = new Organization(name, address, phoneNumber);
        } else {
            System.out.println("This contact manager only supports 'person' type contacts.");
            return;
        }
        this.contacts.add(contact);
        persistContacts();

        System.out.println("The record added.");
        System.out.println();

    }

    private void persistContacts() {
        if (storageManager != null) {
            storageManager.saveContacts(this.contacts);
        }
    }

    void removeContact() {
        if (this.contacts.isEmpty()) {
            System.out.println("No records to remove.");
        } else {
            printContacts();
            System.out.println("Select a record:");
            int indexToRemove = Main.scanner.nextInt() - 1;
            Main.scanner.nextLine(); // Consume the newline character
            if (indexToRemove >= 0 && indexToRemove < this.contacts.size()) {
                this.contacts.remove(indexToRemove);
                System.out.println("The record removed!");
            } else {
                System.out.println("Invalid index. No record removed.");
            }
            persistContacts();
            System.out.println();
        }
    }

    void editContact(Contact contact) {

        if (contact instanceof Person person) {
            System.out.print("Select a field (" + String.join(", ", contact.getEditableFields()) + "): ");
            String fieldToEdit = Main.scanner.nextLine().toLowerCase();
            switch (fieldToEdit) {
                case "name" -> {
                    System.out.print("Enter new name: ");
                    String newName = Main.scanner.nextLine();
                    person.setName(newName);
                }
                case "surname" -> {
                    System.out.print("Enter new surname: ");
                    String newSurname = Main.scanner.nextLine();
                    person.setSurname(newSurname);
                }
                case "birth" -> {
                    System.out.print("Enter new birth date: ");
                    String birthDayInput = Main.scanner.nextLine();
                    LocalDate birthDate;
                    try {
                        birthDate = LocalDate.parse(birthDayInput);
                        person.setBirthDate(birthDate);
                    } catch (Exception e) {
                        System.out.println("Bad birth date!");
                    }
                }
                case "gender" -> {
                    System.out.print("Enter new gender (M, F): ");
                    String genderInput = Main.scanner.nextLine();
                    char gender;
                    if (genderInput.equalsIgnoreCase("M") || genderInput.equalsIgnoreCase("F")) {
                        gender = genderInput.charAt(0);
                        person.setGender(gender);
                    } else {
                        System.out.println("Bad gender!");
                    }
                }
                case "number" -> {
                    System.out.print("Enter new number: ");
                    String newNumber = Main.scanner.nextLine();
                    contact.setPhoneNumber(newNumber);
                }
                default -> {
                    System.out.println("Unknown field. No changes made.");
                    System.out.println();
                    return;
                }
            }
            System.out.println("The record updated!");
            persistContacts();
        } else if (contact instanceof Organization organization) {
            System.out.print("Select a field (" + String.join(", ", contact.getEditableFields()) + "): ");
            String fieldToEdit = Main.scanner.nextLine().toLowerCase();
            switch (fieldToEdit) {
                case "name" -> {
                    System.out.print("Enter new name: ");
                    String newName = Main.scanner.nextLine();
                    organization.setName(newName);
                }
                case "number" -> {
                    System.out.print("Enter new number: ");
                    String newNumber = Main.scanner.nextLine();
                    contact.setPhoneNumber(newNumber);
                }
                case "address" -> {
                    System.out.print("Enter new address: ");
                    String newAddress = Main.scanner.nextLine();
                    organization.setAddress(newAddress);
                }
                default -> {
                    System.out.println("Unknown field. No changes made.");
                    System.out.println();
                    return;
                }
            }
            System.out.println("The record updated!");
            persistContacts();
        } else {
            System.out.println("Unknown contact type.");
        }

        System.out.println();

    }

    public int getContactsCount() {
        return contacts.size();
    }

    public void printContacts() {
        int idx = 1;
        for (Contact contact : this.contacts) {
            System.out.printf("%d. %s%n", idx, contact);
            idx++;
        }
        System.out.println();
    }

    public void info() {
        printContacts();
        System.out.println("Enter index to show info:");
        int indexToShow = Main.scanner.nextInt() - 1;
        Main.scanner.nextLine(); // Consume the newline character
        if (indexToShow >= 0 && indexToShow < this.contacts.size()) {
            this.contacts.get(indexToShow).showInfo();
        }
        System.out.println();
    }

    public void search() {
        boolean again = false;
        do {
            System.out.print("Enter search query: ");
            String query = Main.scanner.nextLine().toLowerCase();
            List<Contact> searchResults = this.contacts.stream().filter(c -> c.matches(query)).toList();
            System.out.printf("Found %d results:%n", searchResults.size());
            int idx = 1;
            for (Contact contact : searchResults) {
                System.out.printf("%d. %s%n", idx, contact.getName());
                idx++;
            }

            System.out.print("[search] Enter action ([number], back, again): ");
            String action = Main.scanner.nextLine().toLowerCase();
            if (action.equals("back")) {
                System.out.println();
                again = false;
            } else if (action.equals("again")) {
                again = true;
            } else {
                try {
                    int index = Integer.parseInt(action) - 1;
                    if (index >= 0 && index < searchResults.size()) {
                        var contact = searchResults.get(index);
                        contact.showInfo();
                        boolean backToMenu = false;
                        do {
                            System.out.print("[record] Enter action (edit, delete, menu): ");
                            action = Main.scanner.nextLine().toLowerCase();
                            switch (action) {
                                case "edit" -> editContact(contact);
                                case "delete" -> {
                                    this.contacts.remove(contact);
                                    System.out.println("The record removed!");
                                }
                                case "menu" -> {
                                    again = false;
                                    backToMenu = true;
                                }
                                default -> System.out.println("Unknown action. Please try again.");
                            }
                        } while (again);
                        if (backToMenu) {
                            System.out.println();
                            break;
                        }
                    } else {
                        System.out.println("Invalid index.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid action.");
                }
                again = true;
            }
        } while (again);
    }

    public void listContacts() {
        printContacts();
        outer:
        do {
            System.out.println("[list] Enter action ([number], back): ");
            String action = Main.scanner.nextLine().toLowerCase();
            if (action.equals("back")) {
                System.out.println();
                break;
            }
            try {
                int index = Integer.parseInt(action) - 1;
                if (index >= 0 && index < this.contacts.size()) {
                    var contact = this.contacts.get(index);
                    contact.showInfo();
                    do {
                        System.out.print("[record] Enter action (edit, delete, menu): ");
                        action = Main.scanner.nextLine().toLowerCase();
                        switch (action) {
                            case "edit" -> editContact(contact);
                            case "delete" -> {
                                this.contacts.remove(contact);
                                System.out.println("The record removed!");
                            }
                            case "menu" -> {
                                break outer;
                            }
                            default -> System.out.println("Unknown action. Please try again.");
                        }
                    } while (true);
                } else {
                    System.out.println("Invalid index.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid action.");
            }
        } while (true);

    }
}
