package model;

import util.CsvImporter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by conor on 27/11/15.
 */
public class Venue {
    private String name;
    private int averageCapacity;
    private double split;

    public ArrayList<Event> getEventsInVenue() {
        return eventsInVenue;
    }

    public void setEventsInVenue(ArrayList<Event> eventsInVenue) {
        this.eventsInVenue = eventsInVenue;
    }

    public ArrayList<Event> calculateEventsInVenue(ArrayList<Event> events) {
       ArrayList<Event> e = new ArrayList<Event>();

        for (Event event: events){
            if(event.getVenue().getName().equals(this.getName())){
                e.add(event);
            }
        }

        return e;
    }

    ArrayList<Event> eventsInVenue;

    public double getSplit() {
        return split;
    }

    public void setSplit(double split) {
        this.split = split;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "name='" + name + '\'' +
                ", averageCapacity=" + averageCapacity +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Venue(String name) {
        this.name = name;
    }

    public int getAverageCapacity() {
        return averageCapacity;
    }

    //todo write function to calculate average capacity for each venue
    public void setAverageCapacity() throws FileNotFoundException{
        CsvImporter importer = new CsvImporter();
        ArrayList<Event> events = importer.importEvents();
        int total = 0;
        int count = 0;
        for(Event event: events){
            if(event.getVenue().getName().equals(this.getName())){
                total += event.getAttending();
                count++;
            }
        }
        this.averageCapacity = total/count;
    }



}
