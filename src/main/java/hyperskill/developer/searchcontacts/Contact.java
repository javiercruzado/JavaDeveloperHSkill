package hyperskill.developer.searchcontacts;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

abstract class Contact implements Serializable {
    protected String name;
    protected String phoneNumber;
    protected final LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        if (phoneNumber == null || phoneNumber.isEmpty() || isInvalidPhoneNumber(phoneNumber)) {
            System.out.println("Wrong number format!");
            this.phoneNumber = "[no number]";
        } else {
            this.phoneNumber = phoneNumber;
        }
        createdAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updatedAt = LocalDateTime.now();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty() || isInvalidPhoneNumber(phoneNumber)) {
            this.phoneNumber = "[no number]";
            updatedAt = LocalDateTime.now();
            return;
        }
        this.phoneNumber = phoneNumber;
        updatedAt = LocalDateTime.now();
    }

    protected boolean isInvalidPhoneNumber(String phoneNumber) {
        String regex = "^\\+?" +
                "\\(?[a-zA-Z0-9]+\\)?" +
                "([\\s-]\\(?[a-zA-Z0-9]{2,}\\)?)?" +
                "([\\s-][a-zA-Z0-9]{2,})*";
        boolean isValid = phoneNumber.matches(regex);

        if (!isValid) {
            return true;
        }

        String[] parts = phoneNumber.split("[ -]");
        for (String part : parts) {
            if (part.contains("(") && !part.contains(")")) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNumber() {
        return phoneNumber != null && !phoneNumber.isEmpty();
    }

    public void showInfo() {
        if (this instanceof Person person) {
            person.showPersonInfo();
        } else if (this instanceof Organization organization) {
            organization.showOrganizationInfo();
        } else {
            System.out.println("Unknown contact type.");
        }
        System.out.println();
    }

    abstract List<String> getSearchableFields();

    abstract List<String> getEditableFields();

    boolean matches(String query) {
        Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        return getSearchableFields().stream()
                .filter(Objects::nonNull)
                .anyMatch(f -> pattern.matcher(f).find());
    }
}
