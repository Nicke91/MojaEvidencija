package com.example.nicke.mojaevidencija.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.nicke.mojaevidencija.Model.Reminder;
import com.example.nicke.mojaevidencija.Model.Shopping;
import com.example.nicke.mojaevidencija.Model.Task;
import com.example.nicke.mojaevidencija.R;

import java.util.List;

/**
 * Created on 2/7/2018.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolderAdapter>{

    private List<Task> taskList;
    private OnTaskClickListener onTaskClickListener;

    public TaskAdapter(List<Task> taskList, OnTaskClickListener onTaskClickListener) {
        this.taskList = taskList;
        this.onTaskClickListener = onTaskClickListener;
    }

    @Override
    public MyViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_task_item, parent, false);

        return new MyViewHolderAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderAdapter holder, int position) {
        final Task task = taskList.get(position);

      holder.title.setText(task.getTitle());
      if (task.getReminder() != null) {
          Reminder reminder = task.getReminder();


          if (reminder.getDate() != null && !reminder.getDate().trim().equals("")) {
              holder.date.setText(reminder.getDate());
          }
          if (reminder.getTime() != null && !reminder.getTime().trim().equals("")) {
              holder.time.setText(reminder.getTime());
          }
      }

      if (task.getShoppingList() != null && task.getShoppingList().size() > 0) {
          Shopping shopping = task.getShoppingList().get(0);

          if (shopping.getTotal() != null && Integer.parseInt(shopping.getTotal()) > 0) {
              holder.total.setText(shopping.getTotal());
          }
      }

     /* holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            onTaskClickListener.onTaskClick(task);
          }
      });*/
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolderAdapter extends RecyclerView.ViewHolder {

        private TextView title, date, time, total;

        public MyViewHolderAdapter(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.cardTask_title_textView);
            date = itemView.findViewById(R.id.cardTask_date_textView);
            time = itemView.findViewById(R.id.cardTask_time_textView);
            total = itemView.findViewById(R.id.cardTask_total_textView);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    onTaskClickListener.onTaskClick(taskList.get(position));
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    onTaskClickListener.onTaskClick(taskList.get(position));
                }
            });
        }
    }

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }
}
