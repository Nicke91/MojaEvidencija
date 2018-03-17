package com.example.nicke.mojaevidencija.Model;

import com.orm.SugarRecord;

/**
 * Created on 2/20/2018.
 */

public class Reminder extends SugarRecord<Reminder> {

    private int isRemainderChecked;
    private String description;
    private String date;
    private String time;
    private String repeat;

    public Reminder(int isRemainderChecked, String description, String date, String time, String repeat) {
        this.isRemainderChecked = isRemainderChecked;
        this.description = description;
        this.date = date;
        this.time = time;
        this.repeat = repeat;
    }

    public Reminder() {
    }

    public int getIsRemainderChecked() {
        return isRemainderChecked;
    }

    public void setIsRemainderChecked(int isRemainderChecked) {
        this.isRemainderChecked = isRemainderChecked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }
}
