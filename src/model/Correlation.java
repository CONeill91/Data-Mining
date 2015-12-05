package model;

/**
 * Created by conor on 05/12/15.
 */
public class Correlation {
    private String eventA;
    private String eventB;
    private int numEventA;
    private int numEventB;

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

    public int getNumEventA() {
        return numEventA;
    }

    public void setNumEventA(int numEventA) {
        this.numEventA = numEventA;
    }

    public int getNumEventB() {
        return numEventB;
    }

    public void setNumEventB(int numEventB) {
        this.numEventB = numEventB;
    }
}
