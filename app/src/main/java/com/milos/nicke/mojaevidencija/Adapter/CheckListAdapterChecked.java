package com.milos.nicke.mojaevidencija.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.milos.nicke.mojaevidencija.Model.CheckItem;
import com.example.nicke.mojaevidencija.R;

import java.util.List;

/**
 * Created on 3/14/2018.
 */

public class CheckListAdapterChecked extends RecyclerView.Adapter<CheckListAdapterChecked.MyViewHolder> {

    List<CheckItem> checkItemList;
    ToDoCheckedListener listener;

    public CheckListAdapterChecked(List<CheckItem> checkItemList, ToDoCheckedListener listener) {
        this.checkItemList = checkItemList;
        this.listener = listener;
    }

    @Override
    public CheckListAdapterChecked.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_list_checked_item, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CheckListAdapterChecked.MyViewHolder holder, int position) {
        CheckItem checkItem = checkItemList.get(position);

        holder.checkedCheckBox.setChecked(true);
        holder.checkedText.setText(checkItem.getContent());
    }

    @Override
    public int getItemCount() {
        return checkItemList.size();
    }

    public List<CheckItem> getCheckedList() {
        return checkItemList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkedCheckBox;
        private TextView checkedText;
        private ImageView checkedRemove;

        public MyViewHolder(View itemView) {
            super(itemView);

            checkedCheckBox = itemView.findViewById(R.id.checkList_checked_checkBox);
            checkedText = itemView.findViewById(R.id.checkList_checked_et);
            checkedRemove = itemView.findViewById(R.id.checkList_checked_remove);



            checkedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (!b) {
                        checkItemList.get(getAdapterPosition()).setChecked(0);
                        listener.onItemUnchecked(getAdapterPosition());
                    }
                }
            });

            checkedRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCheckedItemRemoved(getAdapterPosition());
                }
            });
        }
    }

    public interface ToDoCheckedListener {
        void onItemUnchecked(int position);
        void onCheckedItemRemoved(int position);
    }
}
