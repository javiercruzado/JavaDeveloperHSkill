package hyperskill.developer.sortingtool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

abstract class Sorter<T> {

    List<T> readData(String inputFile) {
        List<T> numbers = new ArrayList<>();
        if (!inputFile.isEmpty()) {
            try (Scanner scanner = new Scanner(new File(inputFile))) {
                numbers = readWithScanner(scanner);
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            }
        } else {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextInt()) {
                numbers = readWithScanner(scanner);
            }
            scanner.close();
        }
        return numbers;
    }

    abstract List<T> readWithScanner(Scanner scanner);


    void printData(List<T> data, String sortType, String outputFile) {
        if ("byCount".equalsIgnoreCase(sortType)) {
            Map<T, Long> sortedMapAsc = getSortedMapAsc(data);
            if (!outputFile.isEmpty()) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                    printSortedDataToFile(data, sortType, sortedMapAsc, writer);
                } catch (IOException e) {
                    System.out.println("Error writing to file");
                }
            } else {
                printSortedDataToConsole(data, sortType, sortedMapAsc);
            }
        } else {
            String sorted = String.join(" ", data.stream().map(Object::toString).toList());
            if (!outputFile.isEmpty()) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                    writer.printf("Total numbers: %d.%n", data.size());
                    writer.printf("Sorted data: %s", sorted);
                } catch (IOException e) {
                    System.err.println("Error writing to file: " + e.getMessage());
                }
            } else {
                System.out.printf("Total numbers: %d.%n", data.size());
                System.out.printf("Sorted data: %s", sorted);
            }
        }
    }

    abstract void printSortedDataToConsole(List<T> data, String sortType, Map<T, Long> sortedMapAsc);

    abstract void printSortedDataToFile(List<T> data, String sortType, Map<T, Long> sortedMapAsc, PrintWriter writer);

    abstract Map<T, Long> getSortedMapAsc(List<T> data);
}
