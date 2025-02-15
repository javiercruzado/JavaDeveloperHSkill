package hyperskill.developer.trafficlight;

import java.util.Objects;

final class Road {

    private final String name;
    private boolean isOpen;
    private int secondsInState;

    Road(String name) {
        this.name = name;
        this.isOpen = false;
        this.secondsInState = 0;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public int getSecondsInState() {
        return secondsInState;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setSecondsInState(int secondsInState) {
        this.secondsInState = secondsInState;
    }

    boolean keepRoadOpen() {
        if (secondsInState > 1) {
            secondsInState--;
            return true;
        }
        isOpen = false;
        return false;
    }

    void openForSeconds(int interval) {
        isOpen = true;
        secondsInState = interval;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Road) obj;
        return Objects.equals(this.name, that.name) &&
                this.isOpen == that.isOpen &&
                this.secondsInState == that.secondsInState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isOpen, secondsInState);
    }

    @Override
    public String toString() {
        return "Road[" +
                "name=" + name + ", " +
                "isOpen=" + isOpen + ", " +
                "secondsInState=" + secondsInState + ']';
    }

}
