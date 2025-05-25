package hyperskill.developer.sortingtool;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

class LineSorter extends Sorter<String> implements SorterStrategy {

    @Override
    public void sortData(String sortType, String inputFile, String outputFile) {
        List<String> lines = super.readData(inputFile);
        lines.sort(String::compareTo);
        super.printData(lines, sortType, outputFile);
    }

    @Override
    List<String> readWithScanner(Scanner scanner) {
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lines.add(line);
        }
        return lines;
    }

    @Override
    Map<String, Long> getSortedMapAsc(List<String> data) {
        Map<String, Long> numberCountMap = data.stream()
                .collect(Collectors.groupingBy(
                        num -> num,
                        Collectors.counting()
                ));
        return numberCountMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()).sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    }

    @Override
    void printSortedDataToConsole(List<String> data, String sortType, Map<String, Long> sortedMapAsc) {
        System.out.printf("Total lines: %d.%n", data.size());
        if ("byCount".equalsIgnoreCase(sortType)) {
            sortedMapAsc.forEach(
                    (k, v) -> System.out.printf("%s: %d time(s), %d%%%n", k, v, Math.round((double) v * 100 / data.size()))
            );
        } else {
            System.out.printf("Total numbers: %d.%n", data.size());
            String sorted = String.join(" ", data.stream().map(Object::toString).toList());
            System.out.printf("Sorted data: %s", sorted);
        }
    }

    @Override
    void printSortedDataToFile(List<String> data, String sortType, Map<String, Long> sortedMapAsc, PrintWriter writer) {
        writer.printf("Total lines: %d.%n", data.size());
        if ("byCount".equalsIgnoreCase(sortType)) {
            sortedMapAsc.forEach(
                    (k, v) ->
                            writer.printf("%s: %d time(s), %d%%%n", k, v, Math.round((double) v * 100 / data.size()))

            );
        } else {
            writer.printf("Total numbers: %d.%n", data.size());
            String sorted = String.join(" ", data.stream().map(Object::toString).toList());
            writer.printf("Sorted data: %s", sorted);
        }
    }


}
