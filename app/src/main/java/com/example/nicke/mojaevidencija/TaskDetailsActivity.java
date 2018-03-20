package com.example.nicke.mojaevidencija;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.nicke.mojaevidencija.Fragment.FragmentAdapter;
import com.example.nicke.mojaevidencija.Fragment.FragmentTaskCheckList;
import com.example.nicke.mojaevidencija.Fragment.FragmentTaskNotes;
import com.example.nicke.mojaevidencija.Fragment.FragmentTaskReminder;
import com.example.nicke.mojaevidencija.Fragment.FragmentTaskShoppingList;
import com.example.nicke.mojaevidencija.Model.Category;
import com.example.nicke.mojaevidencija.Model.CheckItem;
import com.example.nicke.mojaevidencija.Model.Reminder;
import com.example.nicke.mojaevidencija.Model.Shopping;
import com.example.nicke.mojaevidencija.Model.Task;
import com.example.nicke.mojaevidencija.Util.AlertDialogHelper;
import com.example.nicke.mojaevidencija.Util.AppConfig;
import com.example.nicke.mojaevidencija.Util.NotificationHelper;
import com.example.nicke.mojaevidencija.Util.PrefManager;
import com.example.nicke.mojaevidencija.Util.ReminderScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2/15/2018.
 */

public class TaskDetailsActivity extends AppCompatActivity implements AlertDialogHelper.AlarmDeleteListener {

    public static final int NOTES = 0;
    public static final int TO_DO_LIST = 1;
    public static final int SHOPPING_LIST = 2;
    public static final int REMAINDER = 3;

    private Category category;
    private Task currentTask;
    private List<CheckItem> checkItemList;
    private List<Shopping> shoppingList;
    private Reminder reminder;

    private FragmentAdapter currentFragment;

    private EditText titleEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Spinner typeSpinner = findViewById(R.id.spinner_type);

        final String SP_TYPE[] = getResources().getStringArray(R.array.spinner_task_types);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this,
                R.layout.layout_spinner_repeat,
                SP_TYPE
        );

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        onNewIntent(getIntent());

        category = PrefManager.getCategory(this);
        currentTask = PrefManager.getTask(this);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(category.getName());
        }

        titleEditText = findViewById(R.id.et_task_title);

        //editing existing task
        if (currentTask != null) {
            titleEditText.setText(currentTask.getTitle());
            String note = currentTask.getNote();
            checkItemList = currentTask.getCheckItemList();
            shoppingList = currentTask.getShoppingList();
            reminder = currentTask.getReminder();
            PrefManager.setNote(note, this);
            PrefManager.setCheckItemList(checkItemList, this);
            PrefManager.addShoppingList(new ArrayList<>(shoppingList), this);
            PrefManager.setRemainder(reminder, this);

            typeSpinner.setSelection(currentTask.getFragment());

        //creating new task
        } else {
            currentTask = new Task();
            PrefManager.setNote(null, this);
            PrefManager.setCheckItemList(null, this);
            PrefManager.addShoppingList(null, this);
            PrefManager.setRemainder(null, this);
            PrefManager.addTask(currentTask, this);
            //da se ne bi ucitali podaci za pogresan Task
        }

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case NOTES:
                        FragmentTaskNotes fragmentTaskNotes = new FragmentTaskNotes();
                        replaceFragments(fragmentTaskNotes);
                        currentFragment = fragmentTaskNotes;
                        break;
                    case TO_DO_LIST:
                        FragmentTaskCheckList fragmentTaskCheckList = new FragmentTaskCheckList();
                        replaceFragments(fragmentTaskCheckList);
                        currentFragment = fragmentTaskCheckList;
                        break;
                    case SHOPPING_LIST:
                        FragmentTaskShoppingList fragmentTaskShoppingList = new FragmentTaskShoppingList();
                        replaceFragments(fragmentTaskShoppingList);
                        currentFragment = fragmentTaskShoppingList;
                        break;
                    case REMAINDER:
                        FragmentTaskReminder fragmentTaskReminder = new FragmentTaskReminder();
                        replaceFragments(fragmentTaskReminder);
                        currentFragment = fragmentTaskReminder;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {

        Bundle notificationBundle = intent.getExtras();

        if (notificationBundle != null) {
            long taskId = notificationBundle.getLong(NotificationHelper.NOTIFICATION_EXTRA_TASK_ID, 0);
            if (taskId > 0) {
                currentTask = Task.findById(Task.class, taskId);
                if (currentTask != null) {
                    PrefManager.addTask(currentTask, this);
                    PrefManager.setCategory(currentTask.getCategory(), this);
                    currentTask.setFragment(REMAINDER);
                } else {
                    finish();
                }
            }
        }
        super.onNewIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_task:
                saveTask();
                return true;
            case R.id.delete_task:
                AlertDialogHelper.areYouSureDialog(this, this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveTask() {

        String titleInput = titleEditText.getText().toString();

        if (titleInput.trim().equals("")) {
            titleEditText.setError(getString(R.string.error_empty_title));
            return;
        }

        List<Task> searchedTasks = Task.find(Task.class, "title= ? and category = ?",
                titleInput.trim(),
                String.valueOf(category.getId()));

        //ako naslov postoji u bazi, a nije jednak ovom trenutnom naslovu
        if (searchedTasks.size() > 0) {
            if (currentTask.getTitle() != null) {
                if (!currentTask.getTitle().trim().equals(titleInput.trim())) {
                    titleEditText.setError(getString(R.string.error_title_exists));
                    return;
                }
            } else {
                titleEditText.setError(getString(R.string.error_title_exists));
                return;
            }
        }

        //Collect from fragments
        currentFragment.collectData();
        int fragment = PrefManager.getTaskFragment(this);
        checkItemList = PrefManager.getCheckItemList(this);
        shoppingList = PrefManager.getShoppingList(this);
        CheckItem tempCheckItem;
        Shopping tempShopping;
        Reminder tempReminder;
        List<Shopping> tempShoppingList = new ArrayList<>();

        //existing task has ID, but new task does not
        if (currentTask.getId() != null) {
            CheckItem.deleteAll(CheckItem.class, "task = ?", currentTask.getId().toString());
            Shopping.deleteAll(Shopping.class, "task = ?", currentTask.getId().toString());
            //clearing everything for new data
        } else {
            currentTask.setCategory(category);
            currentTask.setTitle(titleInput.trim());
            currentTask.setFragment(fragment);
            currentTask.save();
            //new Task() saved
        }

        if (checkItemList != null) {

            for (CheckItem checkItem : checkItemList) {
                tempCheckItem = new CheckItem();
                tempCheckItem.setChecked(checkItem.getChecked());
                tempCheckItem.setContent(checkItem.getContent());
                tempCheckItem.setTask(currentTask);
                tempCheckItem.save();
            }
        }

        if (shoppingList != null) {

            for (Shopping shopping : shoppingList) {

                tempShopping = new Shopping();

                tempShopping.setQuantity(shopping.getQuantity());
                tempShopping.setName(shopping.getName());
                tempShopping.setPrice(shopping.getPrice());
                tempShopping.setRowTotal(shopping.getRowTotal());
                tempShopping.setTotal(shopping.getTotal());
                tempShopping.setTask(currentTask);
                tempShopping.save();

                tempShoppingList.add(tempShopping);
            }
        }

        Reminder currentReminder = PrefManager.getRemainder(this);
        tempReminder = null;

        if (currentReminder != null) {

            tempReminder = new Reminder();
            if (currentReminder.getId() != null) {
                tempReminder = Reminder.findById(Reminder.class, currentReminder.getId());
            }

            tempReminder.setIsRemainderChecked(currentReminder.getIsRemainderChecked());
            tempReminder.setDescription(currentReminder.getDescription());
            tempReminder.setDate(currentReminder.getDate());
            tempReminder.setTime(currentReminder.getTime());
            tempReminder.setRepeat(currentReminder.getRepeat());
            currentTask.setReminder(tempReminder);
            tempReminder.save();
        }
        //not sure if needed this below code, maybe for ReminderScheduler just in case
        PrefManager.addTask(currentTask, this);

        Task updatingTask = Task.findById(Task.class, currentTask.getId());

        if (updatingTask != null) {

            updatingTask.setFragment(fragment);
            updatingTask.setTitle(titleInput.trim());
            updatingTask.setNote(PrefManager.getNote(this));
            updatingTask.setReminder(tempReminder);

            ReminderScheduler.showInfo = true;
            ReminderScheduler.setReminder(tempReminder, this);
            //not null checked inside setReminder()..

            updatingTask.setCategory(category);
            updatingTask.save();

            PrefManager.addTask(updatingTask, this);
            finish();
        }
    }

    private void deleteTask() {
        if (currentTask.getId() != null) {
            ReminderScheduler.cancelReminder(currentTask, this);

            List<CheckItem> tempCheckItemList = currentTask.getCheckItemList();
            List<Shopping> tempShoppingList = currentTask.getShoppingList();
            Reminder tempReminder = currentTask.getReminder();

            if (tempCheckItemList != null && tempCheckItemList.size() > 0) {
                for (CheckItem checkItem : tempCheckItemList) {
                    checkItem.delete();
                }
            }
            if (tempShoppingList != null && tempShoppingList.size() > 0) {
                for (Shopping shopping : tempShoppingList) {
                    shopping.delete();
                }
            }
            if (tempReminder != null) {
                tempReminder.delete();
            }
            currentTask.delete();
        }
        finish();
    }

    private void replaceFragments(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_content_holder, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onDeleteConfirmed(boolean canDelete) {
        if (canDelete) {
           deleteTask();
        }
    }
}
