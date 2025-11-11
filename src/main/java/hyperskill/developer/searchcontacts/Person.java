package hyperskill.developer.searchcontacts;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

class Person extends Contact {
    private String surname;
    private LocalDate birthDate;
    private Character gender;

    public Person(String name, String surname, String phoneNumber, LocalDate birthDate, Character gender) {
        super(name, phoneNumber);
        this.surname = surname;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        updatedAt = LocalDateTime.now();
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        updatedAt = LocalDateTime.now();
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "%s %s".formatted(name, surname);
    }

    public void showPersonInfo() {
        System.out.printf("Name: %s%n", name);
        System.out.printf("Surname: %s%n", surname);
        System.out.printf("Birth date: %s%n", (birthDate != null ? birthDate : "[no data]"));
        System.out.printf("Gender: %s%n", (gender != null ? gender : "[no data]"));
        System.out.printf("Number: %s%n", (hasNumber() ? phoneNumber : "[no number]"));
        System.out.printf("Time created: %s%n", createdAt != null ? createdAt.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "");
        System.out.printf("Time last edit: %s%n", updatedAt != null ? updatedAt.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "");
    }

    @Override
    List<String> getSearchableFields() {
        return List.of(this.name, this.phoneNumber, this.surname, this.birthDate.format(DateTimeFormatter.ISO_DATE));
    }

    @Override
    List<String> getEditableFields() {
        return List.of("name", "surname", "birth", "gender", "number");
    }
}

