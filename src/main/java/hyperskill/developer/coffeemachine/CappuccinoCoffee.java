package hyperskill.developer.coffeemachine;

class CappuccinoCoffee extends Coffee {

    CappuccinoCoffee() {
        this.beansAmount = 12;
        this.milkAmount = 100;
        this.waterAmount = 200;
        this.price = 6;
    }

    public void prepareCoffee(Balances balance) {
        checkBalancesAndPrepare(balance);
    }
}
