package com.milos.nicke.mojaevidencija.Model;

import com.orm.SugarRecord;

/**
 * Created on 2/7/2018.
 */

public class Shopping extends SugarRecord<Shopping> {

    private String quantity;
    private String name;
    private String price;
    private String rowTotal;
    private String total;
    private Task task;

    public Shopping(String quantity, String name, String price, String rowTotal, String total, Task task) {
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.rowTotal = rowTotal;
        this.total = total;
        this.task = task;
    }

    public Shopping() {
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRowTotal() {
        return rowTotal;
    }

    public void setRowTotal(String rowTotal) {
        this.rowTotal = rowTotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
