/**
 * Created by conor on 27/11/15.
 * Predication main running class.
 */

import model.*;
import sun.rmi.runtime.Log;
import util.CsvImporter;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class Prediction {

    private static final Logger LOGGER = Logger.getLogger( Prediction.class.getName() );

    public static final double DATE_WEIGHT = 5;
    public static final double DAY_WEIGHT = 20;
    public static final double PROMOTOR_WEIGHT = 30;
    public static final double PRICE_WEIGHT = 20;
    public static final double VENUE_WEIGHT = 15;
    public static final double GENDER_WEIGHT = 10;

    public static void main(String[] args)  {

        CsvImporter importer = new CsvImporter();
        try {
            ArrayList<Event> listOfEvents = importer.importEvents();
            ArrayList<Person> listOfPeople = importer.importPeople();
            ArrayList<Guest> guestList = importer.importGuestList();
            HashMap<String, Person> peopleMap = new HashMap<String, Person>();
            HashMap<String, Event> eventMap = new HashMap<String, Event>();
/*
            // Change listOfEvents to 2015 only events for testing.
            Iterator<Event> it = listOfEvents.iterator();
            while(it.hasNext()){
                Event event = it.next();
                Calendar cal = Calendar.getInstance();
                cal.setTime(event.getDate());
                if(cal.get(Calendar.YEAR) != 2015){
                    it.remove();
                }

            }*/

            for (Person person : listOfPeople) {
                peopleMap.put(person.getUser_id(), person);
            }

            for (Event event : listOfEvents) {
                eventMap.put(event.getId(), event);
                event.getVenue().setAverageCapacity();
                event.setAveragePrice();
            }

            for (Guest guest : guestList) {
                if (peopleMap.containsKey(guest.getUserId())) {

                    peopleMap.get(guest.getUserId()).addEventId(guest.getEventId());
                }
            }

            //for each person in map, adds corresponding events to their arraylist of pastEventAttendance
            for (Map.Entry<String, Person> person : peopleMap.entrySet()) {
                for (String eventID : peopleMap.get(person.getKey()).getIdOfEventsAttended()) {
                    if (eventMap.containsKey(eventID)) {
                        peopleMap.get(person.getKey()).attendedEvent(eventMap.get(eventID));
                    }
                }

            }


            // They don't contribute anything but zeroes
            removePeopleWithNoAttendance(listOfPeople);
            removeEventsWithNoAttendance(listOfEvents);
            calculateDemographic(listOfPeople,listOfEvents);
            calculateGenderSplit(listOfEvents);



            for (Event event : listOfEvents) {
                event.setAttending(event.getMalesAttending() + event.getFemalesAttending());
                event.getVenue().setEventsInVenue(event.getVenue().calculateEventsInVenue(listOfEvents));
            }



            // Set remaining Person Values
            for(Person person : listOfPeople){
                person.setAverageSpend();
                person.setEventDays(person.calculateEventDays());
                person.setVenueMap(person.calculateVenueMaps());
                person.setPromoterMap(person.calculatePromoterMaps());
                person.setPromoterCorrelationMap(person.calculatePromoterCorrelationMaps());
            }



            // Test Algorithm on 2015 data.
            //testAlgorithm(listOfPeople,listOfEvents);

        }
        catch(FileNotFoundException e){
            LOGGER.info("Error importing data from CSVs");
            e.printStackTrace();
        }
    }

    //
    public static void testAlgorithm(ArrayList<Person> persons, ArrayList<Event> events) {
        ArrayList<Person> testPeople = new ArrayList<Person>();
        for (int i = 0; i < 10; i++) {
            int randomNum = (int) (Math.random() * persons.size() - 1);
            testPeople.add(persons.get(randomNum));
        }
        for(Event event: events){
            LOGGER.info(event.getName());
            for(Person person: testPeople){
                LOGGER.info(calculatePredictionScore(person,event,persons));
            }
        }
    }


    // Set gender demographic values
    public static void calculateDemographic(ArrayList<Person> persons, ArrayList<Event> events){
        for(Event event: events){
            for(Person person: persons){
                ArrayList<Event> personEvents = person.getPastEventAttendance();
                if(personEvents.contains(event)){
                    if(person.getGender().equals("male")){
                        event.incMalesAttending();
                    }if(person.getGender().equals("female")){
                        event.incFemalesAttending();
                    }if(person.getGender().equals("None")){
                        event.incNonesAttending();
                    }
                }
            }
        }

    }

    // Calculate the gender split for each event
    public static void calculateGenderSplit(ArrayList<Event> events) {
        HashSet<Venue> venues = new HashSet<Venue>();
        for(Event event: events){
            venues.add(event.getVenue());
        }

        for(Venue venue: venues){
            //LOGGER.info(venue.getName());
            ArrayList<Event>venueEvents = venue.getEventsInVenue();
            int totalMales = 0;
            int totalFemales = 0;

            for(Event event : venueEvents){
                totalMales += event.getMalesAttending();
                totalFemales += event.getFemalesAttending();
            }
            double splitdiff = Math.abs(totalFemales + totalMales);
            venue.setSplit(splitdiff);
        }
    }


    // Remove users which have 0 events attended.
    public static void removePeopleWithNoAttendance(ArrayList<Person> persons){
        Iterator<Person> iter = persons.iterator();

        while (iter.hasNext()) {
            Person str = iter.next();

            if (str.getPastEventAttendance().size() == 0)
                iter.remove();
        }
    }
    // Remove events with 0 attendance.
    public static void removeEventsWithNoAttendance(ArrayList<Event> events){
        Iterator<Event> iter = events.iterator();

        while (iter.hasNext()) {
            Event str = iter.next();

            if (str.getAttending() == 0)
                iter.remove();
        }
    }






    // ** Prediction Methods **
    /*
     *  Each person object has a correlation map containing all pairs of promoters of events they have attended &
     *  the correlation between them. The higher the correlation between previous events and the promoter of the
     *  predicted event, the more likely a person is to attend.
     */

    public static double promotorPrediction(ArrayList<Person> persons,Person person,Event futureEvent){
        HashMap<Correlation,Integer> masterCorrelationMap = new HashMap<Correlation, Integer>();
        // Populate master correlation map
        for(Person p: persons){
            HashMap<Correlation,Integer> personPromoterCorrelationMap = p.getPromoterCorrelationMap();
            Iterator it = personPromoterCorrelationMap.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                if(!masterCorrelationMap.containsKey(pair.getKey())){
                    masterCorrelationMap.put((Correlation)pair.getKey(),(Integer)pair.getValue());
                }
                masterCorrelationMap.put((Correlation)pair.getKey(),masterCorrelationMap.get(pair.getKey()) + (Integer)pair.getValue());
            }

        }

        Iterator it =masterCorrelationMap.entrySet().iterator();
        int total = 0;
        int count = 0;
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            Correlation correlation = (Correlation)pair.getKey();
            if (correlation.getEventB().equals(futureEvent.getPromoter().getName())){
                count++;
                total += (Integer)pair.getValue();
            }
        }
        double averageCorrelation = total / count;
        if(averageCorrelation == 1){
            return PROMOTOR_WEIGHT;
        }
        else if (averageCorrelation == 2){
            return PROMOTOR_WEIGHT * 0.80;
        }
        else if(averageCorrelation == 3){
            return PROMOTOR_WEIGHT * 0.50;
        }
        else if(averageCorrelation == 4){
            return PROMOTOR_WEIGHT * 0.30;
        }
        else if(averageCorrelation == 5){
            return PROMOTOR_WEIGHT * 0.10;
        }

        return 0;
    }


    /*
    * The closer the demographic of the event is to the average demographic of a persons's previous events,
    * the more likely they are to attend
     */
    public static double genderPrediction(Person person, Event futureEvent) {
       double splitDifference= futureEvent.getVenue().getSplit();
        if(splitDifference == 0){
            return GENDER_WEIGHT;
        }
        else if (splitDifference <= 10){
            return GENDER_WEIGHT * 0.80;
        }
        else if (splitDifference <= 20){
            return GENDER_WEIGHT * 0.60;
        }
        else if (splitDifference <= 30){
            return GENDER_WEIGHT * 0.50;
        }
        else if (splitDifference <= 40){
            return GENDER_WEIGHT * 0.40;
        }
        return 0;


    }





    // If the event falls on the day before a BH a person is more likely to attend.
    public static double datePrediction(Event event){
        ArrayList<Date> bankHolidays = initBankHolidayList();
        for(Date date : bankHolidays){
            if(date.equals(event.getDate())){
                return DATE_WEIGHT;
            }
        }
        return 0;
    }

    // The more a person frequents a venue the more likely they are to attend an event in that venue.
    public static double venuePrediction(Person person, Event futureEvent){
        String futureEventVenue = futureEvent.getVenue().getName();
        HashMap<String,Integer> personVenueMap = person.getVenueMap();
        int numTimesAtVenue = 0;
        if (personVenueMap.containsKey(futureEventVenue)){
             numTimesAtVenue = personVenueMap.get(futureEventVenue);
        }

        if(numTimesAtVenue >= 15){
            return VENUE_WEIGHT;
        }
        else if (numTimesAtVenue < 15 && numTimesAtVenue >= 10){
            return VENUE_WEIGHT * 0.80;
        }
        else if(numTimesAtVenue < 10 && numTimesAtVenue >= 7){
            return VENUE_WEIGHT * 0.50;
        }
        else if(numTimesAtVenue < 7 && numTimesAtVenue >= 3){
            return VENUE_WEIGHT * 0.30;
        }

        return 0;
    }

    // The closer the event price is to a person's average spend, the more likely they are to attend.
    public static double pricePrediction(Person person,Event event){
        if(event.getAveragePrice() <= person.getAverageSpend()){
            return PRICE_WEIGHT;
        }
        else if(event.getAveragePrice() - person.getAverageSpend() <= 2.50){
            return PRICE_WEIGHT * 0.80;
        }
        else if(event.getAveragePrice() - person.getAverageSpend() <= 5 && event.getAveragePrice() - person.getAverageSpend() >= 2.50){
                return PRICE_WEIGHT * 0.50;
        }
        return 0;
    }

    // If a person normally goes out on a certain day, they are more likely to go out again on that day.
    public static double dayPrediction(Person person,Event futureEvent){
        String dayofFutureEvent = futureEvent.getDay();
        HashMap<String,Integer> dayMap = person.getEventDays();
        int numTimesOutThatDay = 0;
        if(dayMap.containsKey(dayofFutureEvent)){
             numTimesOutThatDay = dayMap.get(dayofFutureEvent);
        }
        if(numTimesOutThatDay >= 15){
           return DAY_WEIGHT;
        }
        else if (numTimesOutThatDay < 15 && numTimesOutThatDay >= 10){
            return DAY_WEIGHT * 0.80;
        }
        else if(numTimesOutThatDay < 10 && numTimesOutThatDay >= 7){
            return DAY_WEIGHT * 0.50;
        }
        else if(numTimesOutThatDay < 7 && numTimesOutThatDay >= 3){
            return DAY_WEIGHT * 0.30;
        }
        return 0;
    }


    // Call all sub-algorithms & return a percentage.
    public static String calculatePredictionScore(Person person, Event futureEvent,ArrayList<Person> persons){
        boolean attended = false;
        for (Event event : person.getPastEventAttendance()){
            if(event.getId().equals(futureEvent.getId())){
                attended = true;
                break;
            }
        }
        return  promotorPrediction(persons,person,futureEvent) + dayPrediction(person,futureEvent) + datePrediction(futureEvent) + pricePrediction(person,futureEvent)
                + venuePrediction(person,futureEvent) + genderPrediction(person,futureEvent) + "% chance that "+ person.getFirst_name() +" will attend the event"
                + "\t {Did the person attended event?} " + attended;

    }

    // Method which initializes list with all date values of BH's in 2016,2015,2014,2013;
    private static ArrayList<Date> initBankHolidayList() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        ArrayList<Date> bankholidays = new ArrayList<Date>();
        String [] bhDates13 =  {"2012/12/31","2013/3/16","2013/3/28","2013/3/31","2013/5/5","2013/6/2","2013/9/4","2013/10/27","2013/12/23"};
        String [] bhDates14 =  {"2013/12/31","2014/3/16","2014/4/17","2014/4/20","2014/5/4","2014/6/1","2014/9/3","2014/10/26","2014/12/23"};
        String [] bhDates15 = {"2014/12/31","2015/3/16","2015/4/2","2015/4/5","2015/5/3","2015/5/31","2015/9/2","2015/10/25","2015/12/23"};
        String [] bhDates16 = {"2015/12/31","2016/3/16","2016/3/24","2016/3/27","2016/4/30","2016/6/5","2016/7/31","2016/10/30","2016/12/23"};
        try {
            for(String date : bhDates13){
                bankholidays.add(format.parse(date));
            }
            for(String date : bhDates14){
                bankholidays.add(format.parse(date));
            }
            for(String date : bhDates15){
                bankholidays.add(format.parse(date));
            }
            for(String date : bhDates16){
                bankholidays.add(format.parse(date));
            }
        }
        catch (ParseException p){
            LOGGER.info("Error parsing BH dates");
        }
        return bankholidays;
    }




}


