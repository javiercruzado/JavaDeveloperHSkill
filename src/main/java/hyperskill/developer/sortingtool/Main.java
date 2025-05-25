package hyperskill.developer.sortingtool;

import java.util.*;
import java.util.function.Function;

public class Main {

    private static final List<String> VALID_ARGUMENTS_OPTION = List.of(
            "-dataType", "-sortingType", "-inputFile", "-outputFile");

    public static void main(final String[] args) {

        List<String> argumentsAsList = Arrays.asList(args);

        argumentsAsList.forEach(x -> {
            if (x.startsWith("-") && !VALID_ARGUMENTS_OPTION.contains(x)) {
                System.out.printf("\"%s\" is not a valid parameter. It will be skipped.", x);
            }
        });

        Function<String, Boolean> isAnArgumentOption = (str) ->
                str.startsWith("-") && VALID_ARGUMENTS_OPTION.stream().map(String::toLowerCase).toList().contains(str.toLowerCase());

        String dataType = "word";
        int indexDataType = argumentsAsList.indexOf("-dataType");
        if (indexDataType != -1) {
            if (indexDataType + 1 < argumentsAsList.size()) {
                dataType = argumentsAsList.get(indexDataType + 1);
                if (isAnArgumentOption.apply(dataType)) {
                    System.out.println("No data type defined!");
                    return;
                }
            } else if (indexDataType + 1 == argumentsAsList.size()) {
                System.out.println("No data type defined!");
                return;
            }
        }

        String sortingType = "natural";
        int indexSortingType = argumentsAsList.indexOf("-sortingType");
        if (indexSortingType != -1) {
            if (indexSortingType + 1 < argumentsAsList.size()) {
                sortingType = argumentsAsList.get(indexSortingType + 1);
                if (isAnArgumentOption.apply(sortingType)) {
                    System.out.println("No sorting type defined!");
                    return;
                }
            } else if (indexSortingType + 1 == argumentsAsList.size()) {
                System.out.println("No sorting type defined!");
                return;
            }
        }

        String inputFile = "";
        int indexInputFile = argumentsAsList.indexOf("-inputFile");
        if (indexInputFile != -1) {
            if (indexInputFile + 1 < argumentsAsList.size()) {
                inputFile = argumentsAsList.get(indexInputFile + 1);
                if (isAnArgumentOption.apply(inputFile)) {
                    System.out.println("No input file defined!");
                    return;
                }
            } else if (indexInputFile + 1 == argumentsAsList.size()) {
                System.out.println("No input file defined!");
                return;
            }
        }

        String outputFile = "";
        int indexOutputFile = argumentsAsList.indexOf("-outputFile");
        if (indexOutputFile != -1) {
            if (indexOutputFile + 1 < argumentsAsList.size()) {
                outputFile = argumentsAsList.get(indexOutputFile + 1);
                if (isAnArgumentOption.apply(outputFile)) {
                    System.out.println("No output file defined!");
                    return;
                }
            } else if (indexOutputFile + 1 == argumentsAsList.size()) {
                System.out.println("No output file defined!");
                return;
            }
        }

        var sorter = new SorterImpl(dataType, sortingType, inputFile, outputFile);
        sorter.sortData();
    }
}

