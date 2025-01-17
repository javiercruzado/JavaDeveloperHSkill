package hyperskill.developer.battleshipgame;

enum BattleShipsTypes {
    AIRCRAFT_CARRIER, BATTLESHIP, SUBMARINE, CRUISER, DESTROYER;

    int getLength() {
        return switch (this) {
            case AIRCRAFT_CARRIER -> 5;
            case BATTLESHIP -> 4;
            case SUBMARINE, CRUISER -> 3;
            case DESTROYER -> 2;
        };
    }

    String getDescription() {
        return switch (this) {
            case AIRCRAFT_CARRIER -> "Aircraft Carrier";
            case BATTLESHIP -> "Battleship";
            case SUBMARINE -> "Submarine";
            case CRUISER -> "Cruiser";
            case DESTROYER -> "Destroyer";
        };
    }
}
