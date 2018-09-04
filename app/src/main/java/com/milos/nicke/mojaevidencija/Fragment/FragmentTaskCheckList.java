package com.milos.nicke.mojaevidencija.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.milos.nicke.mojaevidencija.Adapter.CheckListAdapterChecked;
import com.milos.nicke.mojaevidencija.Adapter.CheckListAdapterUnchecked;
import com.milos.nicke.mojaevidencija.Model.CheckItem;
import com.milos.nicke.mojaevidencija.Model.Task;
import com.example.nicke.mojaevidencija.R;
import com.milos.nicke.mojaevidencija.TaskDetailsActivity;
import com.milos.nicke.mojaevidencija.Util.PrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 3/14/2018.
 */

public class FragmentTaskCheckList extends FragmentAdapter implements CheckListAdapterUnchecked.ToDoUncheckedListener,
        CheckListAdapterChecked.ToDoCheckedListener {

    private boolean isSavePressed;

    private Task task;

    private List<CheckItem> uncheckedItemList;
    private List<CheckItem> checkedItemList;
    private List<CheckItem> checkItemList;

    private CheckListAdapterUnchecked uncheckedAdapter;
    private CheckListAdapterChecked checkedAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_check_list, container, false);

        isSavePressed = false;

        task = PrefManager.getTask(getContext());

        checkItemList = PrefManager.getCheckItemList(getContext());
        checkedItemList = new ArrayList<>();
        uncheckedItemList = new ArrayList<>();

        if (checkItemList == null) {
            checkItemList = new ArrayList<>();
        } else {
            for (CheckItem item : checkItemList) {
                if (item.getChecked() == 1) {
                    checkedItemList.add(item);
                } else {
                    uncheckedItemList.add(item);
                }
            }
        }

        uncheckedAdapter = new CheckListAdapterUnchecked(uncheckedItemList, this);
        checkedAdapter = new CheckListAdapterChecked(checkedItemList, this);

        RecyclerView rvUnchecked = view.findViewById(R.id.checkList__unchecked_rv);
        RecyclerView rvChecked = view.findViewById(R.id.checkList_checked_rv);

        rvUnchecked.setAdapter(uncheckedAdapter);
        rvChecked.setAdapter(checkedAdapter);
        rvUnchecked.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChecked.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageButton ibAddItem = view.findViewById(R.id.ib_check_list_add);
        Button removeAllBtn = view.findViewById(R.id.checkList_button_delete_all);

        ibAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckedItemList.add(new CheckItem(0, "", task));
                uncheckedAdapter.notifyDataSetChanged();
            }
        });

        removeAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkItemList.clear();
                checkedItemList.clear();
                uncheckedItemList.clear();
                uncheckedAdapter.notifyDataSetChanged();
                checkedAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onItemChecked(int position) {
        checkedItemList.add(uncheckedItemList.get(position));
        checkedAdapter.notifyDataSetChanged();
        onUncheckedItemRemoved(position);
    }

    @Override
    public void onUncheckedItemRemoved(int position) {
        uncheckedItemList.remove(position);
        uncheckedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemUnchecked(int position) {
        uncheckedItemList.add(checkedItemList.get(position));
        uncheckedAdapter.notifyDataSetChanged();
        onCheckedItemRemoved(position);
    }

    @Override
    public void onCheckedItemRemoved(int position) {
        checkedItemList.remove(position);
        checkedAdapter.notifyDataSetChanged();
    }

    @Override
    public void collectData() {
        isSavePressed = true;
        PrefManager.setTaskFragment(TaskDetailsActivity.TO_DO_LIST, getContext());
        setData();
    }

    private void setData() {
        checkItemList.clear();
        checkItemList.addAll(uncheckedItemList);
        checkItemList.addAll(checkedItemList);
        PrefManager.setCheckItemList(checkItemList, getContext());
    }

    @Override
    public void onDestroy() {
        if (!isSavePressed) {
            setData();
        }
        super.onDestroy();
    }
}
