package hyperskill.developer.battleshipgame;

import java.util.Objects;

import static java.lang.Math.abs;

record BattleShip(Coordinate head, Coordinate back) {

    int getLength() {
        boolean sameRow = Objects.equals(head.row(), back.row());
        boolean sameColum = Objects.equals(head.column(), back.column());
        int length = 0;
        if (sameRow) {
            length = head.column() - back.column();
        } else if (sameColum) {
            length = head.getRowIndex() - back.getRowIndex();
        }
        return abs(length) + 1;
    }

    Coordinate[] getMapCoordinates() {
        Coordinate[] coordinates;
        boolean sameRow = Objects.equals(head.row(), back.row());
        boolean sameColum = Objects.equals(head.column(), back.column());
        int coordinatesDiff = 0;
        if (sameRow) {
            coordinatesDiff = head.column() - back.column();
        } else if (sameColum) {
            coordinatesDiff = head.getRowIndex() - back.getRowIndex();
        }

        int length = abs(coordinatesDiff) + 1;
        coordinates = new Coordinate[length];
        int index = 0;
        coordinates[index] = head;
        index++;

        do {
            coordinates[index] = new Coordinate(
                    sameRow ? head.row() : BattleShipMap.ROW_IDS[head.getRowIndex() + (coordinatesDiff > 0 ? -index : index)],
                    sameRow ? (head.column() + (coordinatesDiff > 0 ? -index : index)) : head.column()
            );
            index++;
        } while (index < abs(coordinatesDiff));
        coordinates[abs(coordinatesDiff)] = back;

        return coordinates;
    }

    boolean theShipIsSank(BattleShipMap battleShipMap) {
        boolean isSank = true;
        var coordinates = getMapCoordinates();
        var map = battleShipMap.getBattleMap();
        for (Coordinate c : coordinates) {
            var indexes = c.getCoordinateIndexes();
            if (!Objects.equals(map[indexes[0]][indexes[1]], BattleShipMap.SHIP_HIT_MARK)) {
                return false;
            }
        }
        return isSank;
    }
}
