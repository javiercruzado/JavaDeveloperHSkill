package hyperskill.developer.trafficlight;

import java.util.Arrays;
import java.util.List;

class SystemMenu {
    static final List<String> menuItems = Arrays.asList("1. Add road", "2. Delete road", "3. Open system", "0. Quit");
    private final TrafficLightSystem trafficLightSystem;

    SystemMenu(TrafficLightSystem trafficLightSystem) {
        this.trafficLightSystem = trafficLightSystem;
    }

    void printSystemMenu() {
        System.out.printf("Menu:%n");
        for (var menu : menuItems) {
            System.out.printf("%s%n", menu);
        }
    }

    void runAction(int menuSelected) {
        switch (menuSelected) {
            case 1 -> trafficLightSystem.addRoad();
            case 2 -> trafficLightSystem.deleteRoad();
            case 3 -> {
                System.out.println("Open system");
                Utils.clearTerminal();
                trafficLightSystem.setSystemState(SystemState.SYSTEM);
            }
            case 0 -> {
                System.out.println("Bye!");
                System.exit(0);
            }
            default -> System.out.println("Incorrect option");
        }
    }

}
