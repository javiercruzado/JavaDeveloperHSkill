package hyperskill.developer.sortingtool;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

class NumberSorter extends Sorter<Long> implements SorterStrategy {
    @Override
    public void sortData(String sortType, String inputFile, String outputFile) {
        List<Long> numbers = super.readData(inputFile);
        numbers.sort(Long::compare);
        super.printData(numbers, sortType, outputFile);
    }

    @Override
    List<Long> readWithScanner(Scanner scanner) {
        List<Long> numbers = new ArrayList<>();
        while (scanner.hasNext()) {
            if (!scanner.hasNextLong()) {
                System.out.printf("\"%s\" is not a long. It will be skipped.", scanner.next());
            }
            Long number = scanner.nextLong();
            numbers.add(number);
        }
        return numbers;
    }

    @Override
    Map<Long, Long> getSortedMapAsc(List<Long> data) {
        Map<Long, Long> numberCountMap = data.stream()
                .collect(Collectors.groupingBy(
                        num -> num,
                        Collectors.counting()
                ));
        return numberCountMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()).sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    void printSortedDataToConsole(List<Long> data, String sortType, Map<Long, Long> sortedMapAsc) {
        System.out.printf("Total numbers: %d.%n", data.size());
        if ("byCount".equalsIgnoreCase(sortType)) {
            sortedMapAsc.forEach(
                    (k, v) -> System.out.printf("%d: %d time(s), %d%%%n", k, v, Math.round((double) v * 100 / data.size()))
            );
        } else {
            String sorted = String.join(" ", data.stream().map(Object::toString).toList());
            System.out.printf("Sorted data: %s", sorted);
        }
    }

    @Override
    void printSortedDataToFile(List<Long> data, String sortType, Map<Long, Long> sortedMapAsc, PrintWriter writer) {
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
