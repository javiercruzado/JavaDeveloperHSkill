package hyperskill.developer.coffeemachine;

final class Balances {

    private int money;
    private int amountOfWater;
    private int amountOfMilk;
    private int amountOfCoffeeBeans;
    private int disposableCups;

    Balances(int money, int amountOfWater, int amountOfMilk, int amountOfCoffeeBeans, int disposableCups) {
        this.money = money;
        this.amountOfWater = amountOfWater;
        this.amountOfMilk = amountOfMilk;
        this.amountOfCoffeeBeans = amountOfCoffeeBeans;
        this.disposableCups = disposableCups;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getAmountOfWater() {
        return amountOfWater;
    }

    public void setAmountOfWater(int amountOfWater) {
        this.amountOfWater = amountOfWater;
    }

    public int getAmountOfMilk() {
        return amountOfMilk;
    }

    public void setAmountOfMilk(int amountOfMilk) {
        this.amountOfMilk = amountOfMilk;
    }

    public int getAmountOfCoffeeBeans() {
        return amountOfCoffeeBeans;
    }

    public void setAmountOfCoffeeBeans(int amountOfCoffeeBeans) {
        this.amountOfCoffeeBeans = amountOfCoffeeBeans;
    }

    public int getDisposableCups() {
        return disposableCups;
    }

    public void setDisposableCups(int disposableCups) {
        this.disposableCups = disposableCups;
    }

    public void updateBalances(int coffeeBeansAmount, int milkAmount, int waterAmount, int price, int numberOfCups) {
        this.amountOfCoffeeBeans += coffeeBeansAmount;
        this.amountOfMilk += milkAmount;
        this.amountOfWater += waterAmount;
        this.money += price;
        this.disposableCups += numberOfCups;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Balances) obj;
        return this.money == that.money &&
                this.amountOfWater == that.amountOfWater &&
                this.amountOfMilk == that.amountOfMilk &&
                this.amountOfCoffeeBeans == that.amountOfCoffeeBeans &&
                this.disposableCups == that.disposableCups;
    }

    @Override
    public String toString() {
        return "Stock[" +
                "money=" + money + ", " +
                "amountOfWater=" + amountOfWater + ", " +
                "amountOfMilk=" + amountOfMilk + ", " +
                "amountOfCoffeeBeans=" + amountOfCoffeeBeans + ", " +
                "disposableCups=" + disposableCups + ']';
    }

}
