package com.milos.nicke.mojaevidencija.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.milos.nicke.mojaevidencija.Model.Category;
import com.milos.nicke.mojaevidencija.Model.CheckItem;
import com.milos.nicke.mojaevidencija.Model.Reminder;
import com.milos.nicke.mojaevidencija.Model.Shopping;
import com.milos.nicke.mojaevidencija.Model.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2/7/2018.
 */

public class PrefManager {

    private static final String CATEGORY = "category";
    private static final String TASK = "task";
    private static final String NOTE = "note";
    private static final String CHECK_ITEM_LIST = "check_item_list";
    private static final String SHOPPING_LIST = "shopping_list";
    private static final String REMAINDER = "remainder";
    private static final String FRAGMENT = "fragment";

    private static SharedPreferences getSharedPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setCategory(Category category, Context context) {
        String categoryJson = new Gson().toJson(category);
        getSharedPrefs(context).edit().putString(CATEGORY, categoryJson).apply();
    }

    public static Category getCategory(Context context) {
        return new Gson().fromJson(getSharedPrefs(context).getString(CATEGORY, ""),Category.class);
    }

    public static void addTask(Task task, Context context) {
        String taskJson = new Gson().toJson(task);
        getSharedPrefs(context).edit().putString(TASK, taskJson).apply();
    }

    public static Task getTask(Context context) {
        return new Gson().fromJson(getSharedPrefs(context).getString(TASK, ""), Task.class);
    }

    public static void setCheckItemList(List<CheckItem> checkItemList, Context context) {
        String listToJson = new Gson().toJson(checkItemList);
        getSharedPrefs(context).edit().putString(CHECK_ITEM_LIST, listToJson).apply();
    }

    public static List<CheckItem> getCheckItemList(Context context) {
        SharedPreferences sharedPreferences = getSharedPrefs(context);

        if (sharedPreferences.contains(CHECK_ITEM_LIST)) {

            String jsonList = sharedPreferences.getString(CHECK_ITEM_LIST, null);
            Gson gson = new Gson();
            CheckItem[] checkItems = gson.fromJson(jsonList, CheckItem[].class);
            if (checkItems != null) {
                List<CheckItem> checkItemList = Arrays.asList(checkItems);
                return new ArrayList<>(checkItemList);
            } else return null;
        } else {
            return  null;
        }
    }

    public static void addShoppingList(List<Shopping> shoppingTempList, Context context) {
        String listToJson = new Gson().toJson(shoppingTempList);
        getSharedPrefs(context).edit().putString(SHOPPING_LIST, listToJson).apply();
    }

    public static List<Shopping> getShoppingList (Context context) {
        SharedPreferences sharedPreferences = getSharedPrefs(context);

        if (sharedPreferences.contains(SHOPPING_LIST)) {

            String jsonList = sharedPreferences.getString(SHOPPING_LIST, null);
            Gson gson = new Gson();
            Shopping[] shoppingTemps = gson.fromJson(jsonList, Shopping[].class);
            if (shoppingTemps != null) {
                List<Shopping> shoppingTempList = Arrays.asList(shoppingTemps);
                return new ArrayList<>(shoppingTempList);
            } else return null;
        } else {
            return  null;
        }
    }

    public static void setNote(String note, Context context) {
        getSharedPrefs(context).edit().putString(NOTE, note).apply();
    }

    public static String getNote(Context context) {
        return getSharedPrefs(context).getString(NOTE, "");
    }

    public static void setRemainder(Reminder reminder, Context context) {
        String jsonRemainder = new Gson().toJson(reminder);
        getSharedPrefs(context).edit().putString(REMAINDER, jsonRemainder).apply();
    }

    public static Reminder getRemainder(Context context) {
        return new Gson().fromJson(getSharedPrefs(context).getString(REMAINDER, ""), Reminder.class);
    }

    public static void setTaskFragment(int fragment, Context context) {
        getSharedPrefs(context).edit().putInt(FRAGMENT, fragment).apply();
    }

    public static int getTaskFragment(Context context){
        return getSharedPrefs(context).getInt(FRAGMENT, 0);
    }


}
