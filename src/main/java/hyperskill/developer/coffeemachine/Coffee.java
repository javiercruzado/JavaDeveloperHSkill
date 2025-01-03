package hyperskill.developer.coffeemachine;

class Coffee {
    protected int price;
    protected int beansAmount;
    protected int milkAmount;
    protected int waterAmount;

    protected void checkBalancesAndPrepare(Balances balance) {
        boolean thereIsBalance = true;
        if (beansAmount > balance.getAmountOfCoffeeBeans()) {
            System.out.println("Sorry, not enough coffee!");
            thereIsBalance = false;
        }
        if (milkAmount > balance.getAmountOfMilk()) {
            System.out.println("Sorry, not enough milk!");
            thereIsBalance = false;
        }

        if (waterAmount > balance.getAmountOfWater()) {
            System.out.println("Sorry, not enough water!");
            thereIsBalance = false;
        }

        if (balance.getDisposableCups() < 1) {
            System.out.println("Sorry, not enough cups!");
            thereIsBalance = false;
        }
        if (thereIsBalance) {
            System.out.println("I have enough resources, making you a coffee!");
            balance.updateBalances(-beansAmount, -milkAmount, -waterAmount, price, -1);
        }
    }
}
