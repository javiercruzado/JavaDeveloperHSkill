package hyperskill.developer.sortingtool;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

class WordSorter extends Sorter<String> implements SorterStrategy {
    @Override
    public void sortData(String sortType, String inputFile, String outputFile) {
        List<String> words = super.readData(inputFile);
        words.sort(String::compareTo);
        super.printData(words, sortType, outputFile);
    }

    @Override
    List<String> readWithScanner(Scanner scanner) {
        List<String> words = new ArrayList<>();
        while (scanner.hasNext()) {
            String word = scanner.next();
            words.add(word);
        }
        return words;
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
        System.out.printf("Total words: %d.%n", data.size());
        if ("byCount".equalsIgnoreCase(sortType)) {
            sortedMapAsc.forEach(
                    (k, v) -> System.out.printf("%s: %d time(s), %d%%%n", k, v, Math.round((double) v * 100 / data.size()))
            );
        } else {
            String sorted = String.join(" ", data.stream().map(Object::toString).toList());
            System.out.printf("Sorted data: %s", sorted);
        }
    }

    @Override
    void printSortedDataToFile(List<String> data, String sortType, Map<String, Long> sortedMapAsc, PrintWriter writer) {
        writer.printf("Total words: %d.%n", data.size());
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
