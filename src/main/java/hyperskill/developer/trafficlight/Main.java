package hyperskill.developer.trafficlight;

import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static hyperskill.developer.trafficlight.Utils.tryParseInt;

public class Main {

    private static LocalTime currentTime;
    private static LocalTime startTime;
    private static long secondsSinceStart = 0;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.printf("Welcome to the traffic management system!%n");

        System.out.print("Input the number of roads: ");
        int numberOfRoads = tryParseInt(scanner.nextLine());
        while (numberOfRoads < 1) {
            System.out.print("Error! Incorrect Input. Try again: ");
            numberOfRoads = tryParseInt(scanner.nextLine());
        }

        System.out.print("Input the interval: ");
        int interval = tryParseInt(scanner.nextLine());
        while (interval < 1) {
            System.out.print("Error! Incorrect Input. Try again: ");
            interval = tryParseInt(scanner.nextLine());
        }

        var trafficLightSystem = new TrafficLightSystem(numberOfRoads, interval);
        var systemMenu = new SystemMenu(trafficLightSystem);

        var queueThread = new Thread(() -> {
            while (true) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000); // Sleep for one second
                    currentTime = LocalTime.now();
                    secondsSinceStart = ChronoUnit.SECONDS.between(startTime, currentTime);
                    if (SystemState.SYSTEM.equals(trafficLightSystem.getSystemState())) {
                        Utils.clearTerminal();
                        trafficLightSystem.printSystemInformation((int) secondsSinceStart);
                    }
                    trafficLightSystem.updateRoadsState();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }, "QueueThread");

        startTime = LocalTime.now();

        trafficLightSystem.setSystemState(SystemState.MENU);
        queueThread.start();

        int menuSelected = 0;
        do {
            if (SystemState.SYSTEM.equals(trafficLightSystem.getSystemState())) {
                scanner.nextLine();
                trafficLightSystem.setSystemState(SystemState.MENU);
                Utils.clearTerminal();
                continue;
            }

            systemMenu.printSystemMenu();

            menuSelected = tryParseInt(scanner.nextLine());
            systemMenu.runAction(menuSelected);

            if (SystemState.MENU.equals(trafficLightSystem.getSystemState())) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    System.in.read();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Utils.clearTerminal();
            }

        } while (menuSelected != 0);

        scanner.close();
    }

}

