package com.milos.nicke.mojaevidencija.Model;

import com.orm.SugarRecord;

/**
 * Created on 3/14/2018.
 */

public class CheckItem extends SugarRecord<CheckItem>{
    private int checked;
    private String content;
    private Task task;

    public CheckItem() {
    }

    public CheckItem(int checked, String content, Task task) {
        this.checked = checked;
        this.content = content;
        this.task = task;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
