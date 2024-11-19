package com.example.eventlotterysystem;
import java.util.Date;

/**
 * Object for registration periods, event periods
 */
public class TimePeriod {
    private Date start;
    private Date end;

    public TimePeriod (Date start, Date end) {
        if (start.after(end)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        if (start.after(this.end)) {
            throw new IllegalArgumentException("Start date must precede End date");
        }
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        if (this.start.after(end)) {
            throw new IllegalArgumentException("End date must follow Start date");
        }
        this.end = end;
    }

    public boolean isInPeriod (Date date) {
        return !date.before(start) && !date.after(end);
    }
}
