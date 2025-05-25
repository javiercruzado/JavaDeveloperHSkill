package hyperskill.developer.sortingtool;

class SorterImpl {
    private final String dataType;
    private final String sortType;
    private final String inputFile;
    private final String outputFile;

    SorterImpl(String dataType, String sortType, String inputFile, String outputFile) {
        this.dataType = dataType;
        this.sortType = sortType;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void sortData() {
        SorterStrategy sorter;
        switch (dataType) {
            case "long" -> sorter = new NumberSorter();
            case "line" -> sorter = new LineSorter();
            case "word" -> sorter = new WordSorter();
            case "sortIntegers" -> sorter = new IntegerSorter();
            default -> throw new IllegalStateException("Unexpected value: " + dataType);
        }
        sorter.sortData(sortType, inputFile, outputFile);
    }
}
