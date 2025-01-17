package hyperskill.developer.battleshipgame;

import java.util.Arrays;

class BattleShipMap {

    static final String[] ROW_IDS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    static final String FOG_OF_WAR = "~";
    static final String SHIP_SPACE = "O";
    static final String SHIP_HIT_MARK = "X";
    static final String SHIP_HIT_MISSED = "M";

    private final String[][] battleMap;
    private BattleShip[] playerShips;
    private int currentPlayerShipSankCount = 0;

    public void setPlayerShips(BattleShip[] playerShips) {
        this.playerShips = playerShips;
    }

    public String[][] getBattleMap() {
        return battleMap;
    }

    @SuppressWarnings("SameParameterValue")
    BattleShipMap(int size) {
        battleMap = new String[size][size];
        for (String[] strings : battleMap) {
            Arrays.fill(strings, FOG_OF_WAR);
        }
    }

    @SuppressWarnings("SameParameterValue")
    void printMap(boolean fogOfWarFeature, Coordinate shot, boolean successShot) {

        System.out.print("  ");
        int col = 0;
        while (col < ROW_IDS.length) {
            System.out.printf("%s ", ++col);
        }

        int row = 0;
        for (String[] lineMap : battleMap) {
            System.out.println();
            System.out.printf("%s ", ROW_IDS[row]);
            col = 0;
            for (String cell : lineMap) {
                String valueToPrint = cell;
                if (fogOfWarFeature && (shot == null)) {
                    valueToPrint = FOG_OF_WAR;
                } else if (fogOfWarFeature) {
                    var coordinateIndexes = shot.getCoordinateIndexes();
                    if ((coordinateIndexes[0] == row) && (coordinateIndexes[1] == col)) {
                        valueToPrint = successShot ? SHIP_HIT_MARK : SHIP_HIT_MISSED;
                    } else {
                        valueToPrint = FOG_OF_WAR;
                    }
                }
                System.out.printf("%s ", valueToPrint);
                col++;
            }
            row++;
        }
        System.out.println();
    }

    void markMap(BattleShip battleShip) {
        var coordinates = battleShip.getMapCoordinates();
        for (var coordinate : coordinates) {
            battleMap[coordinate.getRowIndex()][coordinate.column() - 1] = SHIP_SPACE;
        }
    }

    boolean spacesAvailableForShip(BattleShip battleShip) {
        return !battleShip.head().isAvailable(this) || !battleShip.back().isAvailable(this);
    }

    boolean executePlayerShot(Coordinate shotCoordinate) {
        String currentValue = battleMap[shotCoordinate.getRowIndex()][shotCoordinate.column() - 1];
        if (SHIP_SPACE.equals(currentValue) || SHIP_HIT_MARK.equals(currentValue)) {
            battleMap[shotCoordinate.getRowIndex()][shotCoordinate.column() - 1] = SHIP_HIT_MARK;
            return true;
        } else {
            battleMap[shotCoordinate.getRowIndex()][shotCoordinate.column() - 1] = SHIP_HIT_MISSED;
            return false;
        }
    }

    int playerShipSankCount() {
        int playerShipSankCount = 0;
        for (var playerShip : playerShips) {
            if (playerShip.theShipIsSank(this)) {
                playerShipSankCount++;
            }
        }
        return playerShipSankCount;
    }

    public boolean newPlayerShipIsSank() {
        if (currentPlayerShipSankCount < playerShipSankCount()) {
            currentPlayerShipSankCount = playerShipSankCount();
            return true;
        }
        return false;
    }
}
