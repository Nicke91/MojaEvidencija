package com.milos.nicke.mojaevidencija.Model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created on 2/7/2018.
 */

public class Task extends SugarRecord<Task> {

    private String title;
    private int fragment;
    private String note;
    private Reminder reminder;
    private Category category;

    public Task(String title, int fragment, String note, Reminder reminder, Category category ) {
        this.title = title;
        this.note = note;
        this.reminder = reminder;
        this.category = category;
    }

    public Task() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFragment() {
        return fragment;
    }

    public void setFragment(int fragment) {
        this.fragment = fragment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<CheckItem> getCheckItemList() {
        return CheckItem.find(CheckItem.class, "task = ?", this.getId().toString());
    }

    public List<Shopping> getShoppingList() {
        return Shopping.find(Shopping.class, "task = ?", this.getId().toString());
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public Reminder getReminder() {
        return reminder;
    }


}
