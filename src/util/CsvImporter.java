package util;

/**
 * Created by danny on 28/11/2015.
 */

import com.opencsv.CSVReader;
import model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Logger;

public class CsvImporter {
    private static final Logger LOGGER = Logger.getLogger(CsvImporter.class.getName());

    public ArrayList<Event> importEvents() throws FileNotFoundException {
        ArrayList<Event> returnList = new ArrayList<Event>();
        CSVReader reader = new CSVReader(new FileReader("tables/combined_for_java.csv"));
        String[] nextLine;
        try {
            while ((nextLine = reader.readNext()) != null) {
                Event tmp = new Event();

                //remove any whitespace left over from scraping
                tmp.setId(nextLine[0].replaceAll("\\s", "") );
                tmp.setDay(nextLine[1]);


                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                tmp.setDate(format.parse(nextLine[2]));

                tmp.setVenue(new Venue(nextLine[3]));
                tmp.setPromoter(new Promoter((nextLine[4])));


                tmp.setAttending(Integer.parseInt(nextLine[5]));

                tmp.setName(nextLine[6]);
                tmp.setPrice_1(nextLine[7]);
                tmp.setPrice_2(nextLine[8]);
                tmp.setPrice_3(nextLine[9]);
                tmp.setPrice_4(nextLine[10]);

                returnList.add(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnList;
    }


    public ArrayList<Person> importPeople() throws FileNotFoundException {
        ArrayList<Person> returnList = new ArrayList<Person>();
        CSVReader reader = new CSVReader(new FileReader("tables/persons.csv"));
        String[] nextLine;
        try {
            while ((nextLine = reader.readNext()) != null) {
                Person tmp = new Person();

                //skip database primary key value, no need for it in java model
                tmp.setUser_id(nextLine[1]);
                tmp.setFirst_name(nextLine[2]);
                tmp.setLast_name(nextLine[3]);
                tmp.setGender(nextLine[4]);
                tmp.setPrecision(nextLine[5]);

                returnList.add(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnList;
    }

    public ArrayList<Guest> importGuestList() throws FileNotFoundException {
        ArrayList<Guest> returnList = new ArrayList<Guest>();
        CSVReader reader = new CSVReader(new FileReader("tables/guest_list.csv"));
        String[] nextLine;
        try {
            while ((nextLine = reader.readNext()) != null) {
                Guest tmp = new Guest();

                //skip database primary key value, no need for it in java model
                tmp.setEventId(nextLine[1].replaceAll("\\s",""));
                tmp.setUserId(nextLine[2]);
                tmp.setName(nextLine[3]);


                returnList.add(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnList;
    }



}

