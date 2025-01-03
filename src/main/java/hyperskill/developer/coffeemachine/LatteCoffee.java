package hyperskill.developer.coffeemachine;

class LatteCoffee extends Coffee {

    LatteCoffee() {
        this.beansAmount = 20;
        this.milkAmount = 75;
        this.waterAmount = 350;
        this.price = 7;
    }

    public void prepareCoffee(Balances balance) {
        checkBalancesAndPrepare(balance);
    }
}
