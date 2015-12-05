package model;

/**
 * Created by conor on 05/12/15.
 */
public class Correlation {
    private String eventA;
    private String eventB;

    @Override
    public String toString() {
        return "eventA='" + eventA + '\'' +
                ", eventB='" + eventB;
    }

    public String getEventA() {
        return eventA;
    }

    public void setEventA(String eventA) {
        this.eventA = eventA;
    }

    public String getEventB() {
        return eventB;
    }

    public void setEventB(String eventB) {
        this.eventB = eventB;
    }
}
