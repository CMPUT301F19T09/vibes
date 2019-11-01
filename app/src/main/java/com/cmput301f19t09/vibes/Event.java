package com.cmput301f19t09.vibes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * TODO
 *
 * @see something?
 */
public class Event {
    protected LocalDate date; // format yyyy-MM-dd
    protected LocalTime time; // format HH:mm
    private final static String TIME_FORMAT = "HH:mm";
    protected String description;

    public Event(LocalDate date, LocalTime time, String description) {
        this.date = date;
        this.time = time;
        this.description = description;
    }

    /**
     * Getter for the date attribute.
     *
     * @return  date
     *      LocalDate object representing the date an event occurred.
     *      Has the format yyyy-MM-dd.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date attribute. Date is used to indicate in part when an event
     * occurred. An Event can have only one date.
     *
     * @param   date
     *      Date of when an Event occurred. Must have the format yyyy-MM-dd or will not be accepted.
     * @return
     *      True if date was set correctly, otherwise False.
     */
    public Boolean setDate(String date) {
        try {
            LocalDate formattedDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

            // if we get here then format of date was correct
            this.date = formattedDate;
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Getter for the time attribute.
     *
     * @return  time
     *      LocalTime object representing the time an event occurred.
     *      Has the format HH:mm.
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Sets the time attribute. Time is used to indicate in part when an event
     * occurred. An Event can have only one date.
     *
     * @param   time
     *      Time of when an Event occurred. Must have the format HH:mm or will not be accepted.
     * @return
     *      True if time was set correctly, otherwise False.
     */
    public boolean setTime(String time) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TIME_FORMAT);
            LocalTime formattedTime = LocalTime.parse(time, dtf);

            // if we get here then format of time was correct
            this.time = formattedTime;
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Getter for the description attribute.
     *
     * @return  description
     *      Description describes the details of an Event.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description attribute. Description is used to provide details
     * which gives context to an event.
     *
     * @param   description
     *      Description must be a String and should provide details which give
     *      context to an Event.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
