package hyperskill.developer.minesweeper;

enum Commands {
    MINE, FREE;

    String getDescription() {
        return switch (this) {
            case FREE -> "free";
            case MINE -> "mine";
        };
    }
}
