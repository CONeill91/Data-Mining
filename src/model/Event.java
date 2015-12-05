package model;

import util.CsvImporter;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by conor on 27/11/15.
 */

public class Event {

    private static final Logger LOGGER = Logger.getLogger(Event.class.getName());


    private String id;
    private String name;
    private Promoter promoter;
    private Date date;
    private String day;
    private Venue venue;


    private int attending;
    private int malesAttending;
    private int femalesAttending;
    private int nonesAttending;

    private String price_1;
    private String price_2;
    private String price_3;
    private String price_4;
    private int averagePrice;
    private double demograpicMale;

    public double getDemograpicMale() {
        return demograpicMale;
    }

    public void setDemograpicMale(double demograpicMale) {
        this.demograpicMale = demograpicMale;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", promoter=" + promoter +
                ", date=" + date +
                ", day='" + day + '\'' +
                ", venue=" + venue +
                ", attending=" + attending +
                ", malesAttending=" + malesAttending +
                ", femalesAttending=" + femalesAttending +
                ", price_1='" + price_1 + '\'' +
                ", price_2='" + price_2 + '\'' +
                ", price_3='" + price_3 + '\'' +
                ", price_4='" + price_4 + '\'' +
                '}';
    }


    public int getAveragePrice() {
        return averagePrice;
    }

    // Bin Prices - Data Cleaning
    public void setAveragePrice() {
        int total = 0;
        int count = 0;
        if(!getPrice_1().equals("n/a")){
            total += Integer.parseInt(getPrice_1());
            count++;
        }
        if(!getPrice_2().equals("n/a")){
            total += Integer.parseInt(getPrice_2());
            count++;
        }
        if(!getPrice_3().equals("n/a")){
            total += Integer.parseInt(getPrice_3());
            count++;
        }
        if(!getPrice_4().equals("n/a")){
            total += Integer.parseInt(getPrice_4());
            count++;
        }
        if(count == 0){
            this.averagePrice = 0;
        }
        else{
            this.averagePrice = total / count;
        }

    }

    public int getAttending() {
        return attending;
    }

    public void setAttending(int attending) {
        this.attending = attending;
    }


    public String getPrice_3() {
        return price_3;
    }

    public void setPrice_3(String price_3) {
        this.price_3 = price_3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Promoter getPromoter() {
        return promoter;
    }

    public void setPromoter(Promoter promoter) {
        this.promoter = promoter;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getPrice_1() {
        return price_1;
    }

    public void setPrice_1(String price_1) {
        this.price_1 = price_1;
    }

    public String getPrice_2() {
        return price_2;
    }

    public void setPrice_2(String price_2) {
        this.price_2 = price_2;
    }

    public String getPrice_4() {
        return price_4;
    }

    public void setPrice_4(String price_4) {
        this.price_4 = price_4;
    }

    public int getFemalesAttending() {
        return femalesAttending;
    }

    public void incFemalesAttending() {
        this.femalesAttending += 1;
    }

    public int getMalesAttending() {
        return malesAttending;
    }

    public void incMalesAttending() {
        this.malesAttending += 1;
    }

    public int getNonesAttending() {
        return nonesAttending;
    }

    public void incNonesAttending() {
        this.nonesAttending += 1;
    }


}
