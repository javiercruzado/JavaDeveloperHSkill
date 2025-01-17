package hyperskill.developer.battleshipgame;

class Player {
    private final String name;
    private final BattleShipMap battleShipMap;
    private Player playerRival;

    Player(String name, BattleShipMap battleShipMap) {
        this.name = name;
        this.battleShipMap = battleShipMap;
    }

    public String getName() {
        return name;
    }

    public BattleShipMap getBattleShipMap() {
        return battleShipMap;
    }

    public Player getPlayerRival() {
        return playerRival;
    }

    public void setPlayerRival(Player playerRival) {
        this.playerRival = playerRival;
    }
}
