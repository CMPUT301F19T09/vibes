package com.example.vibes;

import java.time.LocalDateTime;
import java.util.Date;

public class Event {
    LocalDateTime dateTime;
    String description;

    public Event(LocalDateTime dateTime, String description) {
        this.dateTime = dateTime;
        this.description = description;
    }

    /**
     * Getter for the dateTime attribute.
     *
     * @return  dateTime
     *      LocalDateTime object representing the date and time an event occurred
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Sets the dateTime attribute. DateTime is used to indicate when an event
     * occurred. An Event can have only one dateTime.
     *
     * @param   dateTime
     *      when an Event occurred.
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Getter for the description attribute.
     *
     * @return  description
     *      description describes the details of an Event
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description attribute. Description is used to provide details
     * which gives context to an event.
     *
     * @param   description
     *      description must be a String and should provide details which give
     *      context to an Event
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
