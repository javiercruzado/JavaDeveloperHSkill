package hyperskill.developer.sortingtool;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

class IntegerSorter extends Sorter<Integer> implements SorterStrategy {
    @Override
    public void sortData(String sortType, String inputFile, String outputFile) {
        List<Integer> numbers = super.readData(inputFile);
        numbers.sort(Integer::compare);
        super.printData(numbers, sortType, outputFile);
    }

    @Override
    List<Integer> readWithScanner(Scanner scanner) {
        List<Integer> numbers = new ArrayList<>();
        while (scanner.hasNext()) {
            while (scanner.hasNextInt()) {
                Integer number = scanner.nextInt();
                numbers.add(number);
            }
        }
        return numbers;
    }

    @Override
    Map<Integer, Long> getSortedMapAsc(List<Integer> data) {
        Map<Integer, Long> numberCountMap = data.stream()
                .collect(Collectors.groupingBy(
                        num -> num,
                        Collectors.counting()
                ));

        return numberCountMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()).sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    void printSortedDataToConsole(List<Integer> data, String sortType, Map<Integer, Long> sortedMapAsc) {
        System.out.printf("Total numbers: %d.%n", data.size());
        if ("byCount".equalsIgnoreCase(sortType)) {
            sortedMapAsc.forEach(
                    (k, v) -> System.out.printf("%d: %d time(s), %d%%%n", k, v, Math.round((double) v * 100 / data.size()))
            );
        } else {
            String sorted = String.join(" ", data.stream().map(Object::toString).toList());
            System.out.printf("Total numbers: %d.%n", data.size());
            System.out.printf("Sorted data: %s", sorted);
        }
    }

    @Override
    void printSortedDataToFile(List<Integer> data, String sortType, Map<Integer, Long> sortedMapAsc, PrintWriter writer) {
        writer.printf("Total numbers: %d.%n", data.size());
        if ("byCount".equalsIgnoreCase(sortType)) {
            sortedMapAsc.forEach(
                    (k, v) ->
                            writer.printf("%d: %d time(s), %d%%%n", k, v, Math.round((double) v * 100 / data.size()))

            );
        } else {
            String sorted = String.join(" ", data.stream().map(Object::toString).toList());
            writer.printf("Total numbers: %d.%n", data.size());
            writer.printf("Sorted data: %s", sorted);
        }
    }
}
