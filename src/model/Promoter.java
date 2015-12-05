package model;

import java.util.Arrays;

/**
 * Created by conor on 27/11/15.
 */
public class Promoter {

    public Promoter(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Promoter{" +
                "name='" + name + '\'' +
                ", eventsPromoted=" + Arrays.toString(eventsPromoted) +
                '}';
    }

    private String name;

    private Event[] eventsPromoted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Event[] getEventsPromoted() {
        return eventsPromoted;
    }

    public void setEventsPromoted(Event[] eventsPromoted) {
        this.eventsPromoted = eventsPromoted;
    }
}
