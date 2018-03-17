package com.example.nicke.mojaevidencija.Model;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

/**
 * Created on 2/7/2018.
 */

public class Category extends SugarRecord<Category> {

    private String name;
    private String date;

    public Category() {
    }

    public Category(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Task> getTaskList() {
        return Task.find(Task.class, "category = ?", String.valueOf(this.getId()));
    }
}
