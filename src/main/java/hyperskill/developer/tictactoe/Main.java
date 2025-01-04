package hyperskill.developer.tictactoe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

public class Main {

    private static final char MOVE_X = 'X';
    private static final char MOVE_O = 'O';
    private static final char UNDERSCORE = '_';
    private static final char EMPTY = ' ';

    private static final char[][] gameState = new char[3][3];

    private static final Scanner scanner = new Scanner(System.in);
    private static final String[] players = new String[2];

    public static final String PLAYER_USER = "user";
    public static final String PLAYER_EASY = "easy";
    public static final String PLAYER_MEDIUM = "medium";
    public static final String PLAYER_HARD = "hard";

    private static boolean DEBUG = true;

    public static void main(String[] args) {

        DEBUG = args.length > 0 && Boolean.parseBoolean(args[0]);

        do {
            System.out.println("Input command:");
            scanner.reset();
            String menu = scanner.nextLine();
            String[] menuOptions = menu.split(" ");
            if (menuOptions.length == 1 & "exit".equals(menuOptions[0])) {
                System.exit(0);
            } else if (menuOptions.length == 3 && "start".equals(menuOptions[0])
                    && (Stream.of(PLAYER_USER, PLAYER_EASY, PLAYER_MEDIUM, PLAYER_HARD).anyMatch(s -> s.equals(menuOptions[1])))
                    && (Stream.of(PLAYER_USER, PLAYER_EASY, PLAYER_MEDIUM, PLAYER_HARD).anyMatch(string -> string.equals(menuOptions[2])))
            ) {
                players[0] = menuOptions[1];
                players[1] = menuOptions[2];
            } else {
                System.out.println("Bad parameters!");
                continue;
            }

            // write your code here
            if (setInitialState()) {
                while (true) {
                    boolean gameFinished;

                    if (PLAYER_USER.equals(players[0])) {
                        requestUserMovement(MOVE_X);
                    } else {
                        executeMachineMovement(players[0], MOVE_X);
                    }

                    gameFinished = checkResults();
                    if (gameFinished) {
                        break;
                    }

                    if (PLAYER_USER.equals(players[1])) {
                        requestUserMovement(MOVE_O);
                    } else {
                        executeMachineMovement(players[1], MOVE_O);
                    }

                    gameFinished = checkResults();
                    if (gameFinished) {
                        break;
                    }
                }
            } else {
                break;
            }
        } while (true);
        scanner.close();
    }

    private static boolean setInitialState() {
        String initialState = new String(
                new char[]{UNDERSCORE, UNDERSCORE, UNDERSCORE,
                        UNDERSCORE, UNDERSCORE, UNDERSCORE,
                        UNDERSCORE, UNDERSCORE, UNDERSCORE});

        if (initialState.length() != 9) {
            System.out.println("Invalid input length");
            return false;
        }
        var validChars = initialState.chars().allMatch(x -> x == MOVE_X || x == MOVE_O || x == UNDERSCORE);
        if (!validChars) {
            System.out.println("Invalid input");
            return false;
        }
        var initialStateArray = initialState.toCharArray();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameState[i][j] = initialStateArray[i * 3 + j] == UNDERSCORE ? EMPTY : initialStateArray[i * 3 + j];
            }
        }
        printGameTable();
        return true;
    }

    private static void printGameTable() {
        System.out.println("---------");
        for (char[] chars : gameState) {
            System.out.printf("| %c %c %c |%n", chars[0], chars[1], chars[2]);
        }
        System.out.println("---------");
    }

    private static void requestUserMovement(@SuppressWarnings("SameParameterValue") char nextMovement) {
        do {
            System.out.print("Enter the coordinates: > ");
            try {
                int xPosition = scanner.nextInt();
                int yPosition = scanner.nextInt();

                if (xPosition > 3 || yPosition > 3) {
                    System.out.println("Coordinates should be from 1 to 3!");
                } else if (gameState[xPosition - 1][yPosition - 1] != EMPTY) {
                    System.out.println("This cell is occupied! Choose another one!");
                } else {
                    gameState[xPosition - 1][yPosition - 1] = nextMovement;
                    break;
                }
            } catch (InputMismatchException exception) {
                System.out.println("You should enter numbers!");
                scanner.nextLine();
            }
        } while (true);
        printGameTable();
    }

    private static void executeMachineMovement(String player, char nextMovement) {
        System.out.printf("Making move level \"%s\"%n", player);

        Random random = new Random();
        do {
            int xPosition;
            int yPosition;
            if (PLAYER_MEDIUM.equals(player)) {
                for (int i = 0; i < gameState.length; i++) {

                    int spaceFree = 0;
                    int winningCount = 0;
                    int opponentCount = 0;
                    int freeCount = 0;
                    for (int j = 0; j < gameState.length; j++) {
                        if (gameState[i][j] == EMPTY) {
                            freeCount++;
                            spaceFree = j;
                        } else if (gameState[i][j] == nextMovement) {
                            winningCount++;
                        } else {
                            opponentCount++;
                        }
                        // Winning move
                        if (freeCount == 1 && winningCount == 2) {
                            xPosition = i;
                            yPosition = spaceFree;
                            gameState[xPosition][yPosition] = nextMovement;
                            break;
                        }
                        //Fallback move
                        if (freeCount == 1 && opponentCount == 2) {
                            xPosition = i;
                            yPosition = spaceFree;
                            gameState[xPosition][yPosition] = nextMovement;
                            break;
                        }
                    }
                }
            } else if (PLAYER_HARD.equals(player)) {
                for (int i = 0; i < gameState.length; i++) {
                    gameStateMiniMax[i] = Arrays.copyOf(gameState[i], gameState[i].length);
                }
                if (DEBUG) {
                    saveMinimaxTracking(String.format("call minimax with depth=2;player=%s%n", PLAYER_HARD));
                }

                var minimaxMovement = minimax(2, PLAYER_HARD);
                if (DEBUG) {
                    saveMinimaxTracking(String.format("set minimax movement: [score, move x, move y]%n"));
                    saveMinimaxTracking(String.format(Arrays.toString(minimaxMovement) + "%n"));
                }
                gameState[minimaxMovement[1] - 1][minimaxMovement[2] - 1] = nextMovement;
                break;
            }

            xPosition = random.nextInt(3) + 1;
            yPosition = random.nextInt(3) + 1;


            if (gameState[xPosition - 1][yPosition - 1] == EMPTY) {
                gameState[xPosition - 1][yPosition - 1] = nextMovement;
                break;
            }
        } while (true);
        printGameTable();
    }

    private static boolean checkResults() {

        for (char[] row : gameState) {
            if (row[0] == MOVE_X && row[1] == MOVE_X && row[2] == MOVE_X) {
                System.out.println("X wins");
                return true;
            }
            if (row[0] == MOVE_O && row[1] == MOVE_O && row[2] == MOVE_O) {
                System.out.println("O wins");
                return true;
            }
        }

        for (int j = 0; j < gameState[0].length; j++) {
            if (gameState[0][j] == MOVE_X && gameState[1][j] == MOVE_X && gameState[2][j] == MOVE_X) {
                System.out.println("X wins");
                return true;
            }
            if (gameState[0][j] == MOVE_O && gameState[1][j] == MOVE_O && gameState[2][j] == MOVE_O) {
                System.out.println("O wins");
                return true;
            }
        }

        if (gameState[0][0] == MOVE_X && gameState[1][1] == MOVE_X && gameState[2][2] == MOVE_X) {
            System.out.println("X wins");
            return true;
        }
        if (gameState[0][0] == MOVE_O && gameState[1][1] == MOVE_O && gameState[2][2] == MOVE_O) {
            System.out.println("O wins");
            return true;
        }

        if (gameState[0][2] == MOVE_X && gameState[1][1] == MOVE_X && gameState[2][0] == MOVE_X) {
            System.out.println("X wins");
            return true;
        }

        if (gameState[0][2] == MOVE_O && gameState[1][1] == MOVE_O && gameState[2][0] == MOVE_O) {
            System.out.println("O wins");
            return true;
        }

        boolean emptySpacesAvailable = areEmptySpacesAvailable();

        if (!emptySpacesAvailable) {
            System.out.println("Draw");
            return true;
        }

        return false;
    }

    private static boolean areEmptySpacesAvailable() {
        boolean emptySpacesAvailable = false;
        for (char[] row : gameState) {
            if (row[0] == EMPTY || row[1] == EMPTY || row[2] == EMPTY) {
                emptySpacesAvailable = true;
                break;
            }
        }
        return emptySpacesAvailable;
    }

    private static void printGameTableMiniMax() {
        saveMinimaxTracking(String.format("---------%n"));
        for (char[] chars : gameStateMiniMax) {
            saveMinimaxTracking(String.format("| %c %c %c |%n", chars[0], chars[1], chars[2]));
        }
        saveMinimaxTracking(String.format("---------%n"));
    }

    private static final char[][] gameStateMiniMax = new char[3][3];

    private static int[] minimax(int depth, String player) {
        // Generate possible next moves in a List of int[2] of {row, col}.
        if (DEBUG) {
            saveMinimaxTracking(String.format("depth=%d;player=%s%n", depth, player));
        }

        char playerMovement;
        String rival;
        if (Objects.equals(players[0], player)) {
            playerMovement = MOVE_X;
            rival = players[1];
        } else {
            playerMovement = MOVE_O;
            rival = players[0];
        }

        List<int[]> nextMoves = generateMoves();
        // mySeed is maximizing; while oppSeed is minimizing
        int bestScore = (Objects.equals(player, PLAYER_HARD)) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            // Game over or depth reached, evaluate score
            char computerMovement = PLAYER_HARD.equals(players[0]) ? MOVE_X : MOVE_O;
            char rivalMovement= PLAYER_HARD.equals(players[0]) ? MOVE_O : MOVE_X;
            bestScore = evaluate(computerMovement, rivalMovement);
            if (DEBUG) {
                saveMinimaxTracking(String.format("Game over or depth reached, evaluate score%n"));
                saveMinimaxTracking(String.format("bestScore %d%n", bestScore));
                printGameTableMiniMax();
            }
        } else {
            for (int[] move : nextMoves) {
                // Try this move for the current "player"
                gameStateMiniMax[move[0] - 1][move[1] - 1] = playerMovement;
                if (Objects.equals(player, PLAYER_HARD)) {  // mySeed (computer) is maximizing player
                    currentScore = minimax(depth - 1, rival)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else {  // oppSeed is minimizing player
                    currentScore = minimax(depth - 1, rival)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }
                // Undo move
                gameStateMiniMax[move[0] - 1][move[1] - 1] = EMPTY;
            }
        }
        return new int[]{bestScore, bestRow, bestCol};
    }
    /** The heuristic evaluation function for the current board
     @Return +1000, +10, +1 for EACH 3-, 2-, 1-in-a-line for computer.
     -100, -10, -1 for EACH 3-, 2-, 1-in-a-line for opponent.
     0 otherwise   */
    private static int evaluate(char computerMovement, char rivalMovement) {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLine(0, 0, 0, 1, 0, 2, computerMovement, rivalMovement);  // row 0
        score += evaluateLine(1, 0, 1, 1, 1, 2, computerMovement, rivalMovement);  // row 1
        score += evaluateLine(2, 0, 2, 1, 2, 2, computerMovement, rivalMovement);  // row 2
        score += evaluateLine(0, 0, 1, 0, 2, 0, computerMovement, rivalMovement);  // col 0
        score += evaluateLine(0, 1, 1, 1, 2, 1, computerMovement, rivalMovement);  // col 1
        score += evaluateLine(0, 2, 1, 2, 2, 2, computerMovement, rivalMovement);  // col 2
        score += evaluateLine(0, 0, 1, 1, 2, 2, computerMovement, rivalMovement);  // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0, computerMovement, rivalMovement);  // alternate diagonal
        return score;
    }

    /** The heuristic evaluation function for the given line of 3 cells
     @Return +900, +30, +1 for 3-, 2-, 1-in-a-line for computer.
     -100, -10, -1 for 3-, 2-, 1-in-a-line for opponent.
     0 otherwise */
    private static int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3, char computerMovement, char rivalMovement) {
        int score = 0;

        // First cell
        if (gameStateMiniMax[row1][col1] == computerMovement) {
            score = 1;
        } else if (gameStateMiniMax[row1][col1] == rivalMovement) {
            score = -1;
        }

        // Second cell
        if (gameStateMiniMax[row2][col2] == computerMovement) {
            if (score == 1) {   // cell1 is mySeed
                score = 30;
            } else if (score == -1) {  // cell1 is oppSeed
                return 0;
            } else {  // cell1 is empty
                score = 1;
            }
        } else if (gameStateMiniMax[row2][col2] == rivalMovement) {
            if (score == -1) { // cell1 is oppSeed
                score = -10;
            } else if (score == 1) { // cell1 is mySeed
                return 0;
            } else {  // cell1 is empty
                score = -1;
            }
        }

        // Third cell
        if (gameStateMiniMax[row3][col3] == computerMovement) {
            if (score > 0) {  // cell1 and/or cell2 is mySeed
                score *= 30;
            } else if (score < 0) {  // cell1 and/or cell2 is oppSeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = 1;
            }
        } else if (gameStateMiniMax[row3][col3] == rivalMovement) {
            if (score < 0) {  // cell1 and/or cell2 is oppSeed
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 is mySeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }

    /**
     * Find all valid next moves.
     */
    private static List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<>(); // allocate List

        // Search for empty cells and add to the List
        for (int row = 0; row < gameStateMiniMax.length; ++row) {
            for (int col = 0; col < gameStateMiniMax[row].length; ++col) {
                if (gameStateMiniMax[row][col] == EMPTY) {
                    nextMoves.add(new int[]{row + 1, col + 1});
                }
            }
        }
        return nextMoves;
    }

    public static void saveMinimaxTracking(String logInfo) {
        File directory = new File("miniMaxTracking.txt");
        Path path = Paths.get(directory.toURI());

        byte[] strToBytes = logInfo.getBytes();

        try {
            Files.write(path, strToBytes, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
