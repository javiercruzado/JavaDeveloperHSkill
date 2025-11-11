package hyperskill.developer.searchcontacts;

import java.time.LocalDateTime;
import java.util.List;

class Organization extends Contact {
    private String address;

    public Organization(String name, String address, String phoneNumber) {
        super(name, phoneNumber);
        this.address = address;
        if (phoneNumber == null || phoneNumber.isEmpty() || isInvalidPhoneNumber(phoneNumber)) {
            System.out.println("Wrong number format!");
            this.phoneNumber = "[no number]";
        } else {
            this.phoneNumber = phoneNumber;
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "%s".formatted(name);
    }

    public void showOrganizationInfo() {
        System.out.printf("Organization name: %s%n", name);
        System.out.printf("Address: %s%n", address);
        System.out.printf("Number: %s%n", (hasNumber() ? phoneNumber : "[no number]"));
        System.out.printf("Time created: %s%n", createdAt != null ? createdAt.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "");
        System.out.printf("Time last edit: %s%n", updatedAt != null ? updatedAt.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "");
    }

    @Override
    List<String> getSearchableFields() {
        return List.of(this.name, this.phoneNumber, this.address);
    }

    @Override
    List<String> getEditableFields() {
        return List.of("name", "address", "number");
    }
}
