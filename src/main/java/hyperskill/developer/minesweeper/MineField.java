package hyperskill.developer.minesweeper;

import java.util.*;

class MineField {

    static final String MINE_SYMBOL = "X";
    static final String MARK_SYMBOL = "*";
    static final String SAFE_CELL_SYMBOL = ".";
    static final String FREE_CELL_SYMBOL = "/";

    final String[][] mineField;
    final int fieldSize;

    final List<MinePosition> minePositions = new ArrayList<>();
    final List<MineFieldCoordinate> exploredCoordinates = new ArrayList<>();

    MineField(int fieldSize) {
        this.mineField = new String[fieldSize][fieldSize];
        this.fieldSize = fieldSize;
        init();
    }

    void init() {
        for (var row : mineField) {
            Arrays.fill(row, SAFE_CELL_SYMBOL);
        }
    }

    void setMines(int mineCount) {
        for (var row : mineField) {
            Arrays.fill(row, SAFE_CELL_SYMBOL);
        }
        var random = new Random();
        while (mineCount > 0) {
            int row = random.nextInt(fieldSize);
            int col = random.nextInt(fieldSize);
            if (mineField[row][col].equals(MINE_SYMBOL)) {
                continue;
            }
            minePositions.add(new MinePosition(row, col));
            mineField[row][col] = MINE_SYMBOL;
            mineCount--;
        }
    }

    void printMineField(boolean showMine) {
        int rowNumber = 1;
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (var row : mineField) {
            System.out.printf("%d|", rowNumber);
            Arrays.stream(row).map(x -> Objects.equals(x, MINE_SYMBOL) && !showMine ? SAFE_CELL_SYMBOL : x).forEach(System.out::print);
            System.out.print("|");
            System.out.println();
            rowNumber++;
        }
        System.out.println("-|---------|");
    }

    void markCell(MineFieldCoordinate mineFieldPosition) {
        String currentValue = mineField[mineFieldPosition.x()][mineFieldPosition.y()];
        String symbolToMark = Objects.equals(MARK_SYMBOL, currentValue) ? SAFE_CELL_SYMBOL : MARK_SYMBOL;

        if (Objects.equals(MARK_SYMBOL, currentValue) && Objects.equals(SAFE_CELL_SYMBOL, symbolToMark)) {
            minePositions.stream().filter(x -> x.x() == mineFieldPosition.x() && x.y() == mineFieldPosition.y()).findFirst()
                    .ifPresent(x -> x.setFound(false));
        } else if (Objects.equals(MARK_SYMBOL, symbolToMark)) {
            minePositions.stream().filter(x -> x.x() == mineFieldPosition.x() && x.y() == mineFieldPosition.y()).findFirst()
                    .ifPresent(x -> x.setFound(true));
        }
        mineField[mineFieldPosition.x()][mineFieldPosition.y()] = symbolToMark;
    }

    public boolean thereIsMine(MineFieldCoordinate coordinate) {
        if (minePositions.stream().anyMatch(x -> x.x() == coordinate.x() && x.y() == coordinate.y())) {
            mineField[coordinate.x()][coordinate.y()] = MINE_SYMBOL;
            return true;
        }
        return false;
    }

    public void exploreFreeCell(MineFieldCoordinate coordinate, boolean resetExploredCell) {

        if (resetExploredCell) {
            exploredCoordinates.clear();
        }

        int minesAround = new MinePosition(coordinate.x(), coordinate.y()).countMinesAround(mineField);
        if (minesAround == 0) {
            mineField[coordinate.x()][coordinate.y()] = FREE_CELL_SYMBOL;
            exploredCoordinates.add(coordinate);
            var surroundingCoordinates = coordinate.getSurroundingCoordinates(mineField);
            surroundingCoordinates.forEach(c -> {
                if (!exploredCoordinates.contains(c)) {
                    this.exploreFreeCell(c, false);
                }
            });
        } else {
            mineField[coordinate.x()][coordinate.y()] = String.valueOf(minesAround);
        }
    }

    public boolean playerWins() {
        int markedPositionCount = Arrays.stream(mineField).mapToInt(row -> (int) Arrays.stream(row).filter(x -> Objects.equals(MARK_SYMBOL, x)).count()).sum();
        return minePositions.stream().allMatch(MinePosition::found) && markedPositionCount == minePositions.size();
    }

}
