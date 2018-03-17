package com.example.nicke.mojaevidencija;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicke.mojaevidencija.Adapter.TaskAdapter;
import com.example.nicke.mojaevidencija.Model.Category;
import com.example.nicke.mojaevidencija.Model.Task;
import com.example.nicke.mojaevidencija.Util.AlertDialogHelper;
import com.example.nicke.mojaevidencija.Util.AppConfig;
import com.example.nicke.mojaevidencija.Util.PrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2/7/2018.
 */

public class CategoryDetailsActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener,
        AlertDialogHelper.ActionBarListener {

    private Category category;
    private ActionBar actionBar;

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

    private ImageView ivNoData;
    private TextView tvNoData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_tasks);

        category = PrefManager.getCategory(getApplicationContext());

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(category.getName());
        }

        List<Task> taskList = category.getTaskList();

        if (taskList == null) {
            taskList = new ArrayList<>();
        }

        recyclerView = findViewById(R.id.cardTask_recyclerView);

        RecyclerView.LayoutManager layoutManager;

        if (AppConfig.getSystemString(this, R.string.isTablet).equals("true")) {
            layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        } else {
            layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        }
        recyclerView.setLayoutManager(layoutManager);
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(taskAdapter);

        ivNoData = findViewById(R.id.cardTask_image_no_data_iv);
        tvNoData = findViewById(R.id.cardTask_tv_no_data);

        if (taskList.size() < 1) {
            setNoDataViewVisible();
        } else {
            setNoDataViewGone();
        }
        Button addCardButton = findViewById(R.id.cardTask_add_button);

        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), TaskDetailsActivity.class));
            PrefManager.addTask(null, getApplicationContext());
            }
        });
    }

    @Override
    protected void onResume() {
        List<Task> taskList = category.getTaskList();
        if (taskList != null && taskList.size() > 0) {
            setNoDataViewGone();
        } else {
            setNoDataViewVisible();
            taskList = new ArrayList<>();
        }
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(taskAdapter);

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_edit_name_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit_category:
                changeCategoryName();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeCategoryName() {
        AlertDialogHelper.editCategoryName(CategoryDetailsActivity.this, this);
    }

    @Override
    public void onTaskClick(Task task) {
        PrefManager.addTask(task, getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), TaskDetailsActivity.class);
        startActivity(intent);
    }

    private void setNoDataViewVisible() {
        ivNoData.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.VISIBLE);
    }

    private void setNoDataViewGone() {
        ivNoData.setVisibility(View.GONE);
        tvNoData.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onEditCategoryFinished() {
        category = PrefManager.getCategory(this);
        actionBar.setTitle(category.getName());
    }
}