package com.milos.nicke.mojaevidencija.SqlDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.milos.nicke.mojaevidencija.Model.Category;
import com.milos.nicke.mojaevidencija.Model.Shopping;
import com.milos.nicke.mojaevidencija.Model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2/7/2018.
 */

public class SQLDatabaseManager extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Evidence.db";

    private static final String TABLE_CATEGORY = "categories";
    private static final String CATEGORY_ID = "id";
    private static final String CATEGORY_NAME = "name";
    private static final String CATEGORY_DATE = "date";

    private static final String TABLE_TASK = "tasks";
    private static final String TASK_ID = "task_id";
    private static final String TASK_NAME = "task_name";
    private static final String TASK_NOTE = "task_note";
    private static final String TASK_CATEGORY = "task_category";

    private static final String TABLE_SHOPPING_LIST = "shoppings";
    private static final String LIST_QUANTITY = "list_quantity";
    private static final String LIST_NAME = "list_name";
    private static final String LIST_PRICE = "list_price";
    private static final String LIST_ROW_TOTAL = "list_row_total";
    private static final String LIST_TOTAL = "list_total";
    private static final String LIST_TASK = "list_task";

    private static final String TABLE_REMAINDER = "remainders";
    private static final String REMAINDER_CHECKED = "remainder_checked";
    private static final String REMAINDER_DATE = "remainder_date";
    private static final String REMAINDER_TIME = "remainder_time";
    private static final String REMAINDER_REPEAT = "remainder_repeat";
    private static final String REMAINDER_TASK = "remainder_task";


    private SQLiteDatabase db;

    public SQLDatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, null);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String categoryQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY + "(id integer PRIMARY KEY autoincrement, name VARCHAR, date VARCHAR);";
        String taskQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_TASK + "(task_id integer PRIMARY KEY autoincrement," +
                " task_name VARCHAR," +
                " task_note VARCHAR," +
                " task_category VARCHAR);";
        String listQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_SHOPPING_LIST + "(list_quantity VARCHAR," +
                " list_name VARCHAR," +
                " list_price VARCHAR," +
                " list_row_total VARCHAR," +
                " list_total VARCHAR," +
                " list_task VARCHAR);";
        String remainderQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_REMAINDER + "(remainder_checked VARCHAR," +
                " remainder_date VARCHAR," +
                " remainder_time VARCHAR," +
                " remainder_repeat VARCHAR," +
                " remainder_task VARCHAR);";
        sqLiteDatabase.execSQL(categoryQuery);
        sqLiteDatabase.execSQL(taskQuery);
        sqLiteDatabase.execSQL(listQuery);
        sqLiteDatabase.execSQL(remainderQuery);
        this.db = sqLiteDatabase;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST);

        onCreate(db);
    }

    /*public List<Category> getCategories() {
        List<Category> categoryList = new ArrayList<>();

        db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORY, null);

        int getId = cursor.getColumnIndex(CATEGORY_ID);
        int getName = cursor.getColumnIndex(CATEGORY_NAME);
        int getDate = cursor.getColumnIndex(CATEGORY_DATE);

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(getId));
                category.setName(cursor.getString(getName));
                category.setDate(cursor.getString(getDate));

                categoryList.add(category);
            } while (cursor.moveToNext());
        } else {
            return null;
        }
        cursor.close();

        return categoryList;
    }*/

    public void addCategory(Category category) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME, category.getName());
        contentValues.put(CATEGORY_DATE, category.getDate());

        db.insert(TABLE_CATEGORY, null, contentValues);
    }

    public void removeCategory(String name) {
        db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_CATEGORY + " WHERE TRIM(name)= '" + name.trim() +"'" + ";");
        db.execSQL("DELETE FROM " + TABLE_TASK + " WHERE TRIM(task_category)= '" + name.trim() + "'" + ";");
    }

    public String searchCategoryName(String name) {
        String checkedCategoryName;

        db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + CATEGORY_NAME + " FROM " + TABLE_CATEGORY, null);
        int getCategoryName = cursor.getColumnIndex(CATEGORY_NAME);

        cursor.moveToFirst();

        if((cursor.getCount() > 0)) {
            do {
                checkedCategoryName = cursor.getString(getCategoryName);

                if(checkedCategoryName.equals(name)) {
                    break;
                }
            }while (cursor.moveToNext());
        } else {
            checkedCategoryName = "";
        }
        cursor.close();
        return checkedCategoryName;
    }


    public void addTask(Task task, Category category) {

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_NAME, task.getTitle());
        contentValues.put(TASK_NOTE, task.getNote());
        contentValues.put(TASK_CATEGORY, category.getName());
        db.insert(TABLE_TASK, null, contentValues);

    }

    public void updateTask(Task task, Category category, String oldName) {
        db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_NAME, task.getTitle());
        contentValues.put(TASK_NOTE, task.getNote());
        contentValues.put(TASK_CATEGORY, category.getName());

        db.update(TABLE_TASK, contentValues,"task_name= '"+ oldName+ "'", null);
    }

    public List<Task> getTasks(Category category) {
        List<Task> taskList = new ArrayList<>();

        db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TASK + " WHERE TRIM(task_category)= '" + category.getName().trim() + "'", null);

        int getName = cursor.getColumnIndex(TASK_NAME);

        int getNote = cursor.getColumnIndex(TASK_NOTE);

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {
                Task task = new Task();
                task.setTitle(cursor.getString(getName));
                task.setNote(cursor.getString(getNote));

                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return taskList;
    }

    public void removeTask(String name) {
        db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_TASK + " WHERE TRIM(task_name)= '" + name.trim() +"'" + ";");
    }

    public boolean searchTaskName(String name) {
        boolean isFound = false;

        db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + TASK_NAME + " FROM " + TABLE_TASK, null);

        int getTaskName = cursor.getColumnIndex(TASK_NAME);

        cursor.moveToFirst();

        if (cursor.getCount() > 0 ) {
            do {
                String checkName = cursor.getString(getTaskName);

                if (checkName.trim().equals(name.trim())) {
                    isFound = true;
                    break;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return isFound;
    }

    public void setShopping(Shopping shopping, Task task) {
        db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(LIST_QUANTITY, shopping.getQuantity());
        contentValues.put(LIST_NAME, shopping.getName());
        contentValues.put(LIST_PRICE, shopping.getPrice());
        contentValues.put(LIST_ROW_TOTAL, shopping.getRowTotal());
        contentValues.put(LIST_TOTAL, shopping.getTotal());
        contentValues.put(LIST_TASK, task.getTitle());

        db.insert(TABLE_SHOPPING_LIST, null, contentValues);
    }

    public List<Shopping> getShopingList(Task task) {

        List<Shopping> shoppingList = new ArrayList<>();

        db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SHOPPING_LIST + " WHERE TRIM(list_task)= '" + task.getTitle().trim() + "'", null);

        int getQuantity = cursor.getColumnIndex(LIST_QUANTITY);
        int getName = cursor.getColumnIndex(LIST_NAME);
        int getPrice = cursor.getColumnIndex(LIST_PRICE);
        int getRowTotal = cursor.getColumnIndex(LIST_ROW_TOTAL);
        int getTotal = cursor.getColumnIndex(LIST_TOTAL);

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {

                Shopping shopping = new Shopping();

                shopping.setQuantity(cursor.getString(getQuantity));
                shopping.setName(cursor.getString(getName));
                shopping.setPrice(cursor.getString(getPrice));
                shopping.setRowTotal(cursor.getString(getRowTotal));
                shopping.setTotal(cursor.getString(getTotal));

                shoppingList.add(shopping);

            }while (cursor.moveToNext());
        }

        cursor.close();
        return shoppingList;
    }

    public void updateShopping(Shopping shopping, Task task, String oldName) {

    }

}
