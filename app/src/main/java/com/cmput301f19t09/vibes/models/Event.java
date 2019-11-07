package com.cmput301f19t09.vibes.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * TODO
 *
 * @see something?
 */
public class Event implements Serializable {
    protected LocalDate date; // format yyyy-MM-dd; ISO_LOCAL_DATE
    protected static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    protected LocalTime time; // format HH:mm
    protected static DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern(yyyy-MM-dd)
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

    public String getDateString() {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
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

    public String getTimeString() {
        return time.format(timeFormatter);
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
            LocalTime formattedTime = LocalTime.parse(time, timeFormatter);

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

    // returns the combined date and time as a local datetime object
    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.of(date, time);
    }

    // returns the epoch time since dateTime converted to UTC
    public long getEpochUTC() {
        LocalDateTime dateTime = getLocalDateTime();
        // set timezone to utc
        return dateTime.toEpochSecond(ZoneOffset.UTC);
    }
}
