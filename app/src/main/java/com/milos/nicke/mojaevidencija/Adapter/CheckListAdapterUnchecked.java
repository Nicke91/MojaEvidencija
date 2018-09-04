package com.milos.nicke.mojaevidencija.Adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.milos.nicke.mojaevidencija.Model.CheckItem;
import com.example.nicke.mojaevidencija.R;
import com.milos.nicke.mojaevidencija.Util.AppConfig;

import java.util.List;

/**
 * Created on 3/14/2018.
 */

public class CheckListAdapterUnchecked extends RecyclerView.Adapter<CheckListAdapterUnchecked.MyViewHolder> {

    private List<CheckItem> checkItemList;
    private ToDoUncheckedListener listener;

    public CheckListAdapterUnchecked(List<CheckItem> checkItemList, ToDoUncheckedListener listener) {
        this.checkItemList = checkItemList;
        this.listener = listener;
    }

    @Override
    public CheckListAdapterUnchecked.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_list_unchecked_item, null);
        return new MyViewHolder(view, checkItemList);
    }

    @Override
    public void onBindViewHolder(CheckListAdapterUnchecked.MyViewHolder holder, int position) {
        CheckItem checkItem = checkItemList.get(position);

        holder.editText.setText(checkItem.getContent());
        holder.editText.setError(null);
        checkItem.setChecked(0);
        holder.checkBox.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return checkItemList.size();
    }

    public List<CheckItem> getUncheckedList() {

        return checkItemList;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private List<CheckItem> checkItemList;

        private CheckBox checkBox;
        private EditText editText;
        private ImageView remove;
        private String text;

        public MyViewHolder(final View itemView, final List<CheckItem> checkItemList) {
            super(itemView);

            this.checkItemList = checkItemList;
            checkBox = itemView.findViewById(R.id.checkList_checkBox);
            editText = itemView.findViewById(R.id.checkList_et);
            remove = itemView.findViewById(R.id.checkList_remove);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                    if (isChecked) {
                        if (editText.getText().toString().trim().equals("")) {
                            editText.setError(AppConfig.getSystemString(itemView.getContext(), R.string.error_no_empty_field));
                            checkBox.setChecked(false);
                        } else {
                            checkItemList.get(getAdapterPosition()).setChecked(1);
                            listener.onItemChecked(getAdapterPosition());
                        }
                    }
                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onUncheckedItemRemoved(getAdapterPosition());
                }
            });

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    text = charSequence.toString();
                    checkItemList.get(getAdapterPosition()).setContent(text);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    public interface ToDoUncheckedListener {
        void onItemChecked(int position);
        void onUncheckedItemRemoved(int position);
    }
}
