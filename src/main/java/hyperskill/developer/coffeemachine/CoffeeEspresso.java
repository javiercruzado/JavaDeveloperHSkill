package hyperskill.developer.coffeemachine;

class CoffeeEspresso extends Coffee {

    CoffeeEspresso() {
        this.beansAmount = 16;
        this.milkAmount = 0;
        this.waterAmount = 250;
        this.price = 4;
    }

    public void prepareCoffee(Balances balance) {
        checkBalancesAndPrepare(balance);
    }
}
