package hyperskill.developer.battleshipgame;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class BattleShipGame {

    static final int BATTLE_SIZE = 10;
    static final int BATTLESHIP_BY_USER = 5;
    static private final Player[] gamePlayers = {
            new Player("Player 1", new BattleShipMap(BATTLE_SIZE)),
            new Player("Player 2", new BattleShipMap(BATTLE_SIZE))};

    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        gamePlayers[0].setPlayerRival(gamePlayers[1]);
        gamePlayers[1].setPlayerRival(gamePlayers[0]);

        for (var player : gamePlayers) {
            System.out.printf("%n%s, place your ships on the game field%n%n", player.getName());
            initPlayerShips(player);
            System.out.printf("%nPress Enter and pass the move to another player%n...%n");
            scanner.nextLine();
        }

        boolean gameIsOver = false;
        do {
            for (var player : gamePlayers) {
                printMapForPlayer(player);
                System.out.printf("%n%s, it's your turn:%n", player.getName());

                boolean coordinateIsOk;
                var rivalMap = player.getPlayerRival().getBattleShipMap();
                do {
                    coordinateIsOk = true;
                    String shotCoordinateStr = scanner.nextLine();
                    try {
                        Coordinate shotCoordinate = readCoordinate(shotCoordinateStr);
                        if (rivalMap.executePlayerShot(shotCoordinate)) {
                            System.out.printf("%nYou hit a ship!%n%n");
                            if (rivalMap.newPlayerShipIsSank()) {
                                System.out.printf("%nYou sank a ship! Specify a new target:%n%n");
                            }
                            if(rivalMap.playerShipSankCount()==BATTLESHIP_BY_USER){
                                System.out.printf("%nYou sank the last ship. You won. Congratulations!%n%n");
                            }
                        } else {
                            System.out.printf("%nYou missed!%n");
                        }
                    } catch (InvalidCoordinate e) {
                        coordinateIsOk = false;
                        System.out.printf("%nError! You entered the wrong coordinates! Try again:");
                    }
                } while (!coordinateIsOk);

                gameIsOver = rivalMap.playerShipSankCount() == BATTLESHIP_BY_USER;
                if (gameIsOver) {
                    continue;
                }
                System.out.printf("%nPress Enter and pass the move to another player%n...%n");
                scanner.nextLine();

            }
        } while (!gameIsOver);
        scanner.close();
    }

    private static void printMapForPlayer(Player player) {
        player.getBattleShipMap().printMap(true, null, false);
        System.out.println("---------------------");
        player.getBattleShipMap().printMap(false, null, false);
    }

    private static void initPlayerShips(Player player) {

        BattleShipsTypes[] playerShips = {BattleShipsTypes.AIRCRAFT_CARRIER,
                BattleShipsTypes.BATTLESHIP,
                BattleShipsTypes.SUBMARINE,
                BattleShipsTypes.CRUISER,
                BattleShipsTypes.DESTROYER};

        var battleMap = player.getBattleShipMap();
        battleMap.printMap(false, null, false);

        int pIndex = 0;
        BattleShip[] playerBattleShips = new BattleShip[BattleShipGame.BATTLESHIP_BY_USER];
        for (var ship : playerShips) {
            boolean added = false;
            System.out.printf("%nEnter the coordinates of the %s (%d cells):%n%n", ship.getDescription(), ship.getLength());
            do {
                String shipInfo = scanner.nextLine();
                var battleShip = createBattleShip(shipInfo);
                if (battleShip != null) {
                    if (battleShip.getLength() != ship.getLength()) {
                        System.out.printf("Error! Wrong length of the %s! Try again:%n%n", ship.getDescription());
                        continue;
                    }
                    if (battleMap.spacesAvailableForShip(battleShip)) {
                        System.out.printf("Error! You placed it too close to another one. Try again:%n%n");
                        continue;
                    }
                    added = true;
                    playerBattleShips[pIndex] = battleShip;
                    pIndex++;
                    battleMap.markMap(battleShip);
                    battleMap.printMap(false, null, false);
                }
            } while (!added);
        }
        battleMap.setPlayerShips(playerBattleShips);
    }

    private static BattleShip createBattleShip(String shipInfo) {
        shipInfo = shipInfo.trim();
        String[] coordinatesFromInput = shipInfo.split(" ");
        if (coordinatesFromInput.length != 2) {
            System.out.printf("Error!%n");
        }

        try {
            var head = readCoordinate(coordinatesFromInput[0]);
            var back = readCoordinate(coordinatesFromInput[1]);

            if (validateShipCoordinates(head, back)) {
                return new BattleShip(head, back);
            } else {
                System.out.printf("Error! Wrong ship location! Try again:%n%n");
            }

        } catch (InvalidCoordinate e) {
            System.out.printf("Error! Wrong ship location! Try again:%n%n");
        }
        return null;
    }

    private static Coordinate readCoordinate(String coordinateStr) throws InvalidCoordinate {
        if (coordinateStr.length() != 2 && coordinateStr.length() != 3) {
            throw new InvalidCoordinate("Invalid Length");
        }
        String row = coordinateStr.substring(0, 1);
        String columnStr = coordinateStr.substring(1, 2);
        if (coordinateStr.length() == 3) {
            columnStr = coordinateStr.substring(1, 3);
        }

        if (Arrays.stream(BattleShipMap.ROW_IDS).noneMatch(x -> x.equals(row))) {
            throw new InvalidCoordinate("Invalid Row");
        }

        int column;
        try {
            column = Integer.parseInt(columnStr);
        } catch (NumberFormatException e) {
            column = -1;
        }
        if (column < 1 || column > 10) {
            throw new InvalidCoordinate("Invalid Column");
        }

        return new Coordinate(row, column);
    }

    private static boolean validateShipCoordinates(Coordinate head, Coordinate back) {

        boolean sameRow = Objects.equals(head.row(), back.row());
        boolean sameColum = head.column() == back.column();
        if (sameRow) {
            return 1 <= head.column() && head.column() <= BATTLE_SIZE &&
                    1 <= back.column() && back.column() <= BATTLE_SIZE;
        } else if (sameColum) {
            return 1 <= head.getRowIndex() + 1 && head.getRowIndex() + 1 <= BATTLE_SIZE &&
                    1 <= back.getRowIndex() + 1 && back.getRowIndex() + 1 <= BATTLE_SIZE;
        } else {
            return false;
        }
    }

}

