package hyperskill.developer.minesweeper;

import java.util.ArrayList;
import java.util.List;

record MineFieldCoordinate(int x, int y) {

    List<MineFieldCoordinate> getSurroundingCoordinates(String[][] mineField) {
        var list = new ArrayList<MineFieldCoordinate>();
        int xLowLimit = x == 0 ? 0 : x - 1;
        int xHighLimit = x == mineField.length - 1 ? mineField.length - 1 : x + 1;
        for (int i = xLowLimit; i <= xHighLimit; i++) {
            int yLowLimit = y == 0 ? 0 : y - 1;
            int yHighLimit = y == mineField[i].length - 1 ? mineField[i].length - 1 : y + 1;
            for (int j = yLowLimit; j <= yHighLimit; j++) {
                if (x == i && y == j) {
                    continue;
                }
                list.add(new MineFieldCoordinate(i, j));
            }
        }
        return list;
    }

    @Override
    public String toString() {
        return "MineFieldCoordinate[" +
                "x=" + x + ", " +
                "y=" + y + ']';
    }

}
