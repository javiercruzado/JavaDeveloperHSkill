package hyperskill.developer.trafficlight;

import java.util.ArrayDeque;
import java.util.Scanner;

class TrafficLightSystem {

    static final Scanner scanner = new Scanner(System.in);
    private final int numberOfRoads;
    private final int interval;
    final ArrayDeque<Road> circularQueue;

    public TrafficLightSystem(int numberOfRoads, int interval) {
        this.systemState = SystemState.NOSTARTED;
        this.numberOfRoads = numberOfRoads;
        this.interval = interval;
        circularQueue = new ArrayDeque<>(numberOfRoads);
    }

    private SystemState systemState;

    public SystemState getSystemState() {
        return systemState;
    }

    public void setSystemState(SystemState systemState) {
        this.systemState = systemState;
    }

    public void addRoad() {
        if (circularQueue.size() == this.numberOfRoads) {
            System.out.println("Queue is full");
            return;
        }
        System.out.print("Input road name: ");
        String roadName = scanner.nextLine();
        circularQueue.offer(new Road(roadName));
        System.out.printf("%s Added!", roadName);
    }

    public void deleteRoad() {
        if (circularQueue.isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }
        var roadDeleted = circularQueue.removeFirst();
        System.out.printf("%s deleted!", roadDeleted.getName());
        if (roadDeleted.isOpen() && circularQueue.peekFirst() != null) {
            Road nextRoad = circularQueue.peekFirst();
            nextRoad.openForSeconds(interval);
        }
    }

    void updateRoadsState() {
        if (circularQueue.isEmpty()) {
            return;
        }
        if (circularQueue.size() == 1) {
            var road = circularQueue.getFirst();
            if (!road.keepRoadOpen()) {
                road.openForSeconds(interval);
            }
            return;
        }
        ArrayDeque<Road> closedRoadsL = new ArrayDeque<>();
        ArrayDeque<Road> closedRoadsR = new ArrayDeque<>();
        boolean nextOfferFirst = false;
        boolean openNext = false;
        int openFor = 0;
        for (Road road : circularQueue) {
            if (openNext) {
                road.setOpen(true);
                road.setSecondsInState(interval);
                openFor = interval;
                openNext = false;
                continue;
            }
            if (road.isOpen()) {
                boolean keepOpen = road.keepRoadOpen();
                if (!keepOpen) {
                    closedRoadsL.offerLast(road);
                    openNext = true;
                }
                openFor = road.getSecondsInState();
                nextOfferFirst = true;
            } else {
                if (nextOfferFirst) {
                    closedRoadsR.offerLast(road);
                } else {
                    closedRoadsL.offerLast(road);
                }
            }
        }

        //there were not more elements
        if (openNext) {
            var road = circularQueue.getFirst();
            road.openForSeconds(interval);
            closedRoadsL.remove(road);
        }

        int idx = 0;
        while (!closedRoadsR.isEmpty()) {
            var road = closedRoadsR.removeFirst();
            road.setSecondsInState(openFor + interval * idx);
            idx++;
        }
        while (!closedRoadsL.isEmpty()) {
            var road = closedRoadsL.removeFirst();
            road.setSecondsInState(openFor + interval * idx);
            idx++;
        }
    }

    void printSystemInformation(int secondsSinceSystemStart) {
        if (SystemState.SYSTEM.equals(this.systemState)) {
            System.out.printf("! %ds. have passed since system startup !%n", secondsSinceSystemStart);
            System.out.printf("! Number of roads: %d !%n", this.numberOfRoads);
            System.out.printf("! Interval: %d !%n", this.interval);
            System.out.println();
            circularQueue.forEach(r -> {
                if (r.isOpen()) {
                    System.out.printf("%s will be \u001B[32mopen for %ds.\u001B[0m%n", r.getName(), r.getSecondsInState());
                } else {
                    System.out.printf("%s will be \u001B[31mclosed for %ds.\u001B[0m%n", r.getName(), r.getSecondsInState());
                }
            });
            System.out.println();
            System.out.printf("! Press \"Enter\" to open menu !%n");
        }
    }

}
