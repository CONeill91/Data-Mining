package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by conor on 27/11/15.
 */
public class Person {

    private static final Logger LOGGER = Logger.getLogger( Person.class.getName() );

    private String first_name;
    private String last_name;
    private String user_id;
    private String gender;
    private String precision;
    private int averageSpend;
    private Set<String> idOfEventsAttended = new HashSet<String>();
    private ArrayList<Event> pastEventAttendance = new ArrayList<Event>();
    private HashMap<String,Integer> eventDays;
    private HashMap<String,Integer> venueMap;
    private HashMap<String,Integer> promoterMap;
    private HashMap<Correlation,Integer> promoterCorrelationMap;

    @Override
    public String toString() {
        return "Person{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", gender='" + gender + '\'' +
                ", precision='" + precision + '\'' +
                ", idOfEventsAttended=" + idOfEventsAttended +
                ", pastEventAttendance=" + pastEventAttendance +
                '}' + ", AverageSpend = " + averageSpend ;
    }

    public Set<String> getIdOfEventsAttended() {
        return idOfEventsAttended;
    }

    public void addEventId(String id){
        idOfEventsAttended.add(id);
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public ArrayList<Event> getPastEventAttendance() {
        return pastEventAttendance;
    }

    public void attendedEvent(Event e) {
        pastEventAttendance.add(e);
    }

    public HashMap<String, Integer> getEventDays() {
        return eventDays;
    }

    public void setEventDays(HashMap<String,Integer> days){
        this.eventDays = days;
    }

    public HashMap<String, Integer> getVenueMap() {
        return venueMap;
    }

    public void setVenueMap(HashMap<String, Integer> venueMap) {
        this.venueMap = venueMap;
    }

    public HashMap<String, Integer> getPromoterMap() {
        return promoterMap;
    }

    public void setPromoterMap(HashMap<String, Integer> promoterMap) {
        this.promoterMap = promoterMap;
    }

    public HashMap<String,Integer> calculatePromoterMaps() {
        // Init Map
        HashMap<String,Integer> promoterMap = new HashMap<String, Integer>();
        ArrayList<Event> events = getPastEventAttendance();
        for(Event event : events){
            String promoter = event.getPromoter().getName();
            if(!promoterMap.containsKey(promoter)) {
                promoterMap.put(promoter,1);
            }
            else{
                promoterMap.put(promoter,promoterMap.get(promoter) + 1);
            }
        }
        return promoterMap;
    }
    
    public int getAverageSpend() {
        return averageSpend;
    }

    public void setAverageSpend() {
        int total = 0;
        for(Event event : pastEventAttendance) {
            total += event.getAveragePrice();
        }
        if(pastEventAttendance.size() != 0){
            this.averageSpend = total / pastEventAttendance.size();
        }
        else{
            this.averageSpend = 0;
        }

    }

    public HashMap<String,Integer> calculateVenueMaps() {
        // Init Map
        HashMap<String,Integer> venueMap = new HashMap<String, Integer>();
        ArrayList<Event> events = getPastEventAttendance();
        for(Event event : events){
            String venue = event.getVenue().getName();
            if(!venueMap.containsKey(venue)) {
                venueMap.put(venue,1);
            }
            else{
                venueMap.put(venue,venueMap.get(venue) + 1);
            }
        }
        return venueMap;
    }

    public HashMap<String,Integer> calculateEventDays() {
        // Init Map
        HashMap<String,Integer> days = new HashMap<String, Integer>();
        ArrayList<Event> events = getPastEventAttendance();
        for(Event event : events){
            String day = event.getDay();
            if(!days.containsKey(day)) {
                days.put(day,1);
            }
            else{
                days.put(day,days.get(day) + 1);
            }
        }
        return days;
    }



    public HashMap<Correlation,Integer> calculatePromoterCorrelationMaps() {
        // Init Map
        HashMap<Correlation,Integer> correlationMap = new HashMap<Correlation,Integer>();
        HashMap<String,Integer> promoterMap = this.getPromoterMap();
        HashSet<String> promoters = new HashSet<String>(promoterMap.keySet());
        for(String prom : promoters){
            for(String prom1 : promoters){
                Correlation temp = new Correlation();
                temp.setEventA(prom);
                temp.setEventB(prom1);
                if(temp.getEventA().equals(temp.getEventB())){
                    continue;
                }
                correlationMap.put(temp,promoterMap.get(temp.getEventA()) / promoterMap.get(temp.getEventB()));
            }
        }
        return correlationMap;
    }

    
}

