package hyperskill.developer.coffeemachine;

import java.util.Scanner;

public class CoffeeMachine {

    private static int cupsNumberBeforeCleaning = 10;

    private static final String ACTION_BUY = "buy";
    private static final String ACTION_FILL = "fill";
    private static final String ACTION_TAKE = "take";
    private static final String ACTION_REMAINING = "remaining";
    private static final String ACTION_EXIT = "exit";
    private static final String ACTION_CLEAN = "clean";

    private static Scanner sc;

    private static Balances currentBalance;

    public static void main(String[] args) {

        currentBalance = new Balances(550, 400, 540, 120, 9);

        sc = new Scanner(System.in);

        String action;
        do {
            printTextForUserInput("Write action (buy, fill, take, clean, remaining, exit): ");

            action = sc.next();

            if (cupsNumberBeforeCleaning == 0 && !ACTION_CLEAN.equals(action)) {
                System.out.printf("I need cleaning!%n");
                continue;
            }

            switch (action) {
                case ACTION_BUY:
                    buyCoffee();
                    cupsNumberBeforeCleaning--;
                    break;
                case ACTION_FILL:
                    fillStocks();
                    break;
                case ACTION_TAKE:
                    takeMoney();
                    break;
                case ACTION_REMAINING:
                    remainingStock();
                    break;
                case ACTION_CLEAN:
                    cleanMachine();
                    break;
                default:
                    break;
            }
        }
        while (!action.equals(ACTION_EXIT));

        sc.close();
    }

    private static void cleanMachine() {
        System.out.println("I have been cleaned!\n");
        cupsNumberBeforeCleaning = 10;
    }

    private static void remainingStock() {
        printStockInfo();
    }

    private static void printStockInfo() {
        System.out.printf("%nThe coffee machine has:%n" +
                        "%d ml of water%n" +
                        "%d ml of milk%n" +
                        "%d g of coffee beans%n" +
                        "%d disposable cups%n" +
                        "$%d of money%n%n"
                , currentBalance.getAmountOfWater(),
                currentBalance.getAmountOfMilk(),
                currentBalance.getAmountOfCoffeeBeans(),
                currentBalance.getDisposableCups(),
                currentBalance.getMoney());
    }

    private static void buyCoffee() {
        printTextForUserInput("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
        String next = sc.next();
        if ("back".equals(next)) {
            return;
        }

        int coffeeType = Integer.parseInt(next);

        switch (coffeeType) {
            case 1:
                new CoffeeEspresso().prepareCoffee(currentBalance);
                break;
            case 2:
                new LatteCoffee().prepareCoffee(currentBalance);
                break;
            case 3:
                new CappuccinoCoffee().prepareCoffee(currentBalance);
                break;
            default:
                break;
        }
    }

    private static void fillStocks() {
        printTextForUserInput("Write how many ml of water you want to add: ");
        int waterAmount = sc.nextInt();

        printTextForUserInput("Write how many ml of milk you want to add: ");
        int milkAmount = sc.nextInt();

        printTextForUserInput("Write how many grams of coffee beans you want to add: ");
        int coffeeBeansAmount = sc.nextInt();

        printTextForUserInput("Write how many disposable cups you want to add: ");
        int cupsAmount = sc.nextInt();
        updateBalances(coffeeBeansAmount, milkAmount, waterAmount, 0, cupsAmount);
    }

    private static void takeMoney() {
        System.out.printf("I gave you $%d%n", currentBalance.getMoney());
        updateBalances(0, 0, 0, -currentBalance.getMoney(), 0);
    }

    private static void printTextForUserInput(String promptText) {
        System.out.println(promptText);
        System.out.print("> ");
    }

    private static void updateBalances(int coffeeBeansAmount, int milkAmount, int waterAmount, int price, int numberOfCups) {
        currentBalance.setAmountOfCoffeeBeans(currentBalance.getAmountOfCoffeeBeans() + coffeeBeansAmount);
        currentBalance.setAmountOfMilk(currentBalance.getAmountOfMilk() + milkAmount);
        currentBalance.setAmountOfWater(currentBalance.getAmountOfWater() + waterAmount);
        currentBalance.setMoney(currentBalance.getMoney() + price);
        currentBalance.setDisposableCups(currentBalance.getDisposableCups() + numberOfCups);
    }
}

