package com.example.nicke.mojaevidencija.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicke.mojaevidencija.CategoryDetailsActivity;
import com.example.nicke.mojaevidencija.MainActivity;
import com.example.nicke.mojaevidencija.Model.Category;
import com.example.nicke.mojaevidencija.R;
import com.example.nicke.mojaevidencija.Util.AlertDialogHelper;
import com.example.nicke.mojaevidencija.Util.AppConfig;
import com.example.nicke.mojaevidencija.Util.PrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2/7/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Category> categoryList;
    private OnCategoryListener activity;



    public CategoryAdapter(List<Category> categoryList, OnCategoryListener activity) {
        this.activity = activity;
        this.categoryList = categoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Category category = categoryList.get(position);

        holder.nameTextView.setText(category.getName());
        holder.dateTextView.setText(category.getDate());

        holder.moreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogHelper.deleteCategoryDialog(activity, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView dateTextView;
        private ImageView moreImageView;

        MyViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.categoryName_textView);
            dateTextView = itemView.findViewById(R.id.categoryDate_textView);
            moreImageView = itemView.findViewById(R.id.categoryMore_imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    activity.onCategoryClickListener(position);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface OnCategoryListener {
        void onCategoryClickListener(int position);
        void updateDatabase(int position);
    }
}
