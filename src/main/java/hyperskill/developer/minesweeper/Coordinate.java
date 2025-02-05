package hyperskill.developer.minesweeper;

record Coordinate(int x, int y) {
    MineFieldCoordinate getMineFieldCoordinate() {
        return new MineFieldCoordinate(y - 1, x - 1);
    }
}
