package hyperskill.developer.cinemaroommanager;

import java.util.Arrays;
import java.util.Scanner;

public class CinemaManager {

    private static final int SHOW_THE_SEATS = 1;
    private static final int BUY_A_TICKET = 2;
    private static final int STATISTICS = 3;
    private static final int EXIT = 0;

    private static String[][] seats;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Enter the number of rows:");
        int rows = scanner.nextInt();

        System.out.println("Enter the number of seats in each row:");
        int seatsByRow = scanner.nextInt();

        initSeats(rows, seatsByRow);

        int menuItem;
        do {
            System.out.println("1. Show the seats");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");

            menuItem = scanner.nextInt();

            if (menuItem == EXIT) {
                break;
            }

            switch (menuItem) {
                case SHOW_THE_SEATS:
                    printCinemaSeats();
                    break;
                case BUY_A_TICKET:
                    buyTicket();
                    break;
                case STATISTICS:
                    showStatistics();
                    break;
            }

        } while (true);

        scanner.close();
    }

    private static void showStatistics() {

        int numberOfPurchasedTickets = 0;
        for (var row : seats) {
            numberOfPurchasedTickets += (int) Arrays.stream(row).filter("B"::equals).count();
        }
        double percentage = ((double) (numberOfPurchasedTickets * 100) / (seats.length * seats[0].length));
        percentage = (double) Math.round(percentage * 100) / 100;

        int currentIncome = getIncome(false);
        int totalIncome = getIncome(true);

        System.out.printf("Number of purchased tickets: %d%n" +
                        "Percentage: %.2f%%%n" +
                        "Current income: $%d%n" +
                        "Total income: $%d%n"
                , numberOfPurchasedTickets, percentage, currentIncome, totalIncome);
    }

    private static void initSeats(int rows, int seatsByRow) {
        seats = new String[rows][seatsByRow];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < seatsByRow; c++) {
                seats[r][c] = "S";
            }
        }
    }

    private static void buyTicket() {

        while (true) {
            System.out.println("Enter a row number:");
            int rowNumber = scanner.nextInt();

            System.out.println("Enter a seat number in that row:");
            int seatNumber = scanner.nextInt();

            boolean wrongInput = rowNumber < 1 || seatNumber < 1 ||
                    rowNumber > seats.length || seatNumber > seats[rowNumber - 1].length;
            if (wrongInput) {
                System.out.println("Wrong input!");
                continue;
            }

            boolean ticketAlreadyPurchased = seats[rowNumber - 1][seatNumber - 1].equals("B");
            if (ticketAlreadyPurchased) {
                System.out.println("That ticket has already been purchased!\n");
            } else {
                seats[rowNumber - 1][seatNumber - 1] = "B";
                int ticketPrice = getTicketPrice(rowNumber);
                System.out.printf("Ticket price: $%d%n%n", ticketPrice);
                break;
            }
        }
    }

    private static int getTicketPrice(int rowNumber) {
        int ticketPrice;

        if (seats.length * seats[0].length <= 60) {
            ticketPrice = 10;
        } else {
            int firstRows = seats.length / 2;
            ticketPrice = rowNumber <= firstRows ? 10 : 8;
        }
        return ticketPrice;
    }

    private static int getIncome(boolean showTotalIncoming) {
        int income = 0;
        for (int r = 0; r < seats.length; r++) {
            if (showTotalIncoming) {
                income += seats[r].length * getTicketPrice(r + 1);
            } else {
                for (int c = 0; c < seats[r].length; c++) {
                    if (seats[r][c].equals("B")) {
                        income += getTicketPrice(r+1);
                    }
                }
            }
        }
        return income;
    }

    private static void printCinemaSeats() {
        int rows = seats.length;
        int columns = seats[0].length;
        System.out.println("Cinema:");
        System.out.print(" ");
        int colHeader = 1;
        while (colHeader < columns + 1) {
            System.out.printf(" %d", colHeader);
            colHeader++;
        }

        for (int r = 0; r < rows; r++) {
            System.out.println();
            System.out.print(r + 1);
            for (int c = 0; c < columns; c++) {
                System.out.printf(" %s", seats[r][c]);
            }
        }
        System.out.printf("%n%n");
    }

}
