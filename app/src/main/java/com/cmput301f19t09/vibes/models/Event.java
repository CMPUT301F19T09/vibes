package com.cmput301f19t09.vibes.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents an event in time. Models something that happened which has a date,
 * time, and description. Implements serializable to be passable in intents.
 */
public class Event implements Serializable {
    protected static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    protected LocalDate date; // format yyyy-MM-dd; ISO_LOCAL_DATE
    protected LocalTime time; // format HH:mm
    protected String description;

    /**
     * Creates a new Event object by accepting a date, time, and description.
     *
     * @param date        Date of when the event occurred; should have the format ISO_LOCAL_DATE (yyyy-MM-dd).
     * @param time        Time of when the event occurred; should have the format HH:mm.
     * @param description Describes the circumstances surrounding an event.
     */
    public Event(LocalDate date, LocalTime time, String description) {
        this.date = date;
        this.time = time;
        this.description = description;
    }

    /**
     * Getter for the date attribute.
     *
     * @return LocalDate object representing the date an event occurred.
     * Has the format yyyy-MM-dd.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Getter for the date attribute returned as an ISO_LOCAL_DATE formatted string.
     * Used for displaying the date.
     *
     * @return String representation of a LocalDate object representing the date an event occurred.
     * Has the format yyyy-MM-dd.
     */
    public String getDateString() {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Sets the date attribute. Date is used to indicate in part when an event
     * occurred. An Event can have only one date.
     *
     * @param date Date of when an Event occurred. Must have the format yyyy-MM-dd or will not be accepted.
     * @return True if date was set correctly, otherwise False.
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
     * @return LocalTime object representing the time an event occurred.
     * Has the format HH:mm.
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Sets the time attribute. Time is used to indicate in part when an event
     * occurred. An Event can have only one date.
     *
     * @param time Time of when an Event occurred.
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * Getter for the time attribute returned as an HH:mm formatted string.
     * Used for displaying the time.
     *
     * @return String representation of a LocalTime object representing the time an event occurred.
     * Has the format HH:mm.
     */
    public String getTimeString() {
        return time.format(timeFormatter);
    }

    /**
     * Sets the time attribute. Time is used to indicate in part when an event
     * occurred. An Event can have only one date.
     *
     * @param time Time of when an Event occurred. Must have the format HH:mm or will not be accepted.
     * @return True if time was set correctly, otherwise False.
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
     * @return Description which describes the details of an Event.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description attribute. Description is used to provide details
     * which gives context to an event.
     *
     * @param description Description must be a String and should provide details which give
     *                    context to an Event.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Combines the date and time attributes of the Event into a LocalDateTime object
     * Used for comparison of Events based on the time they occurred.
     *
     * @return LocalDateTime object which is the combination of the LocalDate date and
     * LocalTime time attributes.
     */
    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.of(date, time);
    }

    /**
     * Converts the combined date and time to its UTC epoch representation.
     * An epoch is a an absolute time reference; the time since Midnight on the
     * 1st of January 1970. Used for conversion into an acceptable format to store
     * in firebase.
     *
     * @return Long UTC epoch representation of the combined date and time attribute.
     */
    public long getEpochUTC() {
        LocalDateTime dateTime = getLocalDateTime();
        // set timezone to utc
        return dateTime.toEpochSecond(ZoneOffset.UTC);
    }
}
