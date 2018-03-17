package com.example.nicke.mojaevidencija;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.nicke.mojaevidencija.Adapter.CategoryAdapter;
import com.example.nicke.mojaevidencija.AppHelpers.DateFormatter;
import com.example.nicke.mojaevidencija.Fragment.CategoryFragmentDialog;
import com.example.nicke.mojaevidencija.Model.Category;
import com.example.nicke.mojaevidencija.Model.Task;
import com.example.nicke.mojaevidencija.Util.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryFragmentDialog.CategoryDialogInterface, CategoryAdapter.OnCategoryListener {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    private LinearLayout layoutNoCategory;

    private FloatingActionButton fab;
    private FloatingActionButton fab_no_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryFragmentDialog categoryFragmentDialog = new CategoryFragmentDialog();
                categoryFragmentDialog.show(getSupportFragmentManager(), "MainActivity.CategoryDialog");
            }
        });

        fab_no_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryFragmentDialog categoryFragmentDialog = new CategoryFragmentDialog();
                categoryFragmentDialog.show(getSupportFragmentManager(), "MainActivity.CategoryDialog");
            }
        });
    }

    @Override
    protected void onResume() {
        if (categoryList != null) {
            categoryList = Category.listAll(Category.class);
            categoryAdapter = new CategoryAdapter(categoryList, this);
            recyclerView.setAdapter(categoryAdapter);
           noDataCheck(categoryList);
        } else {
            setNoCategoryViewVisible();
        }
        super.onResume();
    }

    private void init() {
        recyclerView = findViewById(R.id.category_recyclerView);
        fab = findViewById(R.id.fab);
        fab_no_category = findViewById(R.id.fab_no_data);

        layoutNoCategory = findViewById(R.id.layout_no_category);

        categoryList = Category.listAll(Category.class);

        if (categoryList == null || categoryList.size() < 1 ) {
            categoryList = new ArrayList<>();
            setNoCategoryViewVisible();
        } else {
            setNoCategoryViewGone();
        }

        categoryAdapter = new CategoryAdapter(categoryList, MainActivity.this);
        recyclerView.setAdapter(categoryAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public void onFinishCategoryDialog(String name) {

            Category category = new Category();

            category.setName(name);
            category.setDate(DateFormatter.getFormmatedDate());

            category.save();

            categoryList = Category.listAll(Category.class);
            categoryAdapter.notifyDataSetChanged();

            noDataCheck(categoryList);

            PrefManager.setCategory(category, getApplicationContext());

            Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
            startActivity(intent);

    }
    //CategoryAdapter
    @Override
    public void onCategoryClickListener(int position) {

        Category category = Category.findById(Category.class, categoryList.get(position).getId());

        PrefManager.setCategory(category, this);

        Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
        startActivity(intent);

    }
    //CategoryAdapter
    @Override
    public void updateDatabase(int position) {
        Category category = Category.findById(Category.class, categoryList.get(position).getId());
        if (category != null) {
            deleteCategory(category);
            categoryList = Category.listAll(Category.class);
            categoryAdapter = new CategoryAdapter(categoryList, this);
            recyclerView.setAdapter(categoryAdapter);

            noDataCheck(categoryList);
            PrefManager.setCategory(null,this);
        }
    }

    private void noDataCheck(List<Category> categoryList) {

        if (categoryList != null && categoryList.size() > 0) {
            setNoCategoryViewGone();
        } else {
            setNoCategoryViewVisible();
        }
    }

    private void setNoCategoryViewVisible() {

        layoutNoCategory.setVisibility(View.VISIBLE);
        fab.setVisibility(View.GONE);
    }

    private void setNoCategoryViewGone() {

        layoutNoCategory.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
    }

    private void deleteCategory(Category category) {
        List<Task> tempTaskList = category.getTaskList();

        if (tempTaskList != null && tempTaskList.size() > 0) {
            for (Task task : tempTaskList) {
                task.delete();
            }
        }
        category.delete();
    }

}
