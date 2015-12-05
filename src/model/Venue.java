package model;

import util.CsvImporter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by conor on 27/11/15.
 */
public class Venue {
    private String name;
    private int averageCapacity;


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
