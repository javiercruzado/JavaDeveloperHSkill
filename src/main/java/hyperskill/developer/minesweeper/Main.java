package hyperskill.developer.minesweeper;

import java.util.*;

public class Main {

    private static final int MINE_FIELD_SIZE = 9;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.printf("How many mines do you want on the field?%n");
        int numberOfMines = scanner.nextInt();

        var mineField = new MineField(MINE_FIELD_SIZE);
        mineField.setMines(numberOfMines);
        mineField.printMineField(false);

        boolean play = true;
        do {
            System.out.printf("Set/unset mines marks or claim a cell as free:%n");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            String command = scanner.nextLine().trim();

            if (x < 1 || x > MINE_FIELD_SIZE || y < 1 || y > MINE_FIELD_SIZE) {
                System.out.printf("Invalid coordinate!%n");
                continue;
            }

            if (Commands.MINE.getDescription().equalsIgnoreCase(command)) {
                mineField.markCell(new Coordinate(x, y).getMineFieldCoordinate());
            } else if (Commands.FREE.getDescription().equalsIgnoreCase(command)) {
                if (mineField.thereIsMine(new Coordinate(x, y).getMineFieldCoordinate())) {
                    System.out.println("You stepped on a mine and failed!");
                    mineField.printMineField(true);
                    play = false;
                    continue;
                }
                mineField.exploreFreeCell(new Coordinate(x, y).getMineFieldCoordinate(), true);
            }

            mineField.printMineField(false);
            if (mineField.playerWins()) {
                System.out.printf("Congratulations! You found all the mines!%n");
                play = false;
            }
        } while (play);

        scanner.close();
    }
}


