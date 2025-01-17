package hyperskill.developer.battleshipgame;

import java.util.Arrays;
import java.util.List;

record Coordinate(String row, int column) {

    int getRowIndex() {
        var ids = BattleShipMap.ROW_IDS;
        List<String> list = Arrays.asList(ids);
        return list.indexOf(this.row);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isAvailable(BattleShipMap battleShipMap) {

        int rowMin = getRowIndex() > 0 ? getRowIndex() - 1 : 0;
        int rowMax = getRowIndex() < BattleShipGame.BATTLE_SIZE - 1 ? getRowIndex() + 1 : BattleShipGame.BATTLE_SIZE - 1;

        int colBaseZero = column - 1;
        int colMin = colBaseZero > 0 ? colBaseZero - 1 : 0;
        int colMax = colBaseZero < BattleShipGame.BATTLE_SIZE - 1 ? colBaseZero + 1 : BattleShipGame.BATTLE_SIZE - 1;

        for (int i = rowMin; i <= rowMax; i++) {
            for (int j = colMin; j <= colMax; j++) {
                if (!battleShipMap.getBattleMap()[i][j].equals(BattleShipMap.FOG_OF_WAR)) {
                    return false;
                }
            }
        }

        return true;
    }

    int[] getCoordinateIndexes() {
        return new int[]{getRowIndex(), column - 1};
    }
}
