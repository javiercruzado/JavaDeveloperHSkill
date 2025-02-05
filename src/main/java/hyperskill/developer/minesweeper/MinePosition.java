package hyperskill.developer.minesweeper;

import java.util.Objects;

final class MinePosition {

    private final int x;
    private final int y;
    private boolean found;

    MinePosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.found = false;
    }

    int countMinesAround(String[][] mineField) {
        int mineCount = 0;
        int xLowLimit = x == 0 ? 0 : x - 1;
        int xHighLimit = x == mineField.length - 1 ? mineField.length - 1 : x + 1;
        for (int i = xLowLimit; i <= xHighLimit; i++) {
            int yLowLimit = y == 0 ? 0 : y - 1;
            int yHighLimit = y == mineField[i].length - 1 ? mineField[i].length - 1 : y + 1;
            for (int j = yLowLimit; j <= yHighLimit; j++) {
                if (x == i && y == j) {
                    continue;
                }
                if (Objects.equals(mineField[i][j], MineField.MINE_SYMBOL)) {
                    mineCount++;
                }
            }
        }
        return mineCount;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public boolean found() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MinePosition) obj;
        return this.x == that.x &&
                this.y == that.y &&
                this.found == that.found;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, found);
    }

    @Override
    public String toString() {
        return "MinePosition[" +
                "x=" + x + ", " +
                "y=" + y + ", " +
                "found=" + found + ']';
    }

}
