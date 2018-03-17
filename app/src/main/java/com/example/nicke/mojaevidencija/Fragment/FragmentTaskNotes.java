package com.example.nicke.mojaevidencija.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nicke.mojaevidencija.Model.Task;
import com.example.nicke.mojaevidencija.R;
import com.example.nicke.mojaevidencija.TaskDetailsActivity;
import com.example.nicke.mojaevidencija.Util.PrefManager;

/**
 * Created on 2/20/2018.
 */

public class FragmentTaskNotes extends FragmentAdapter {

    private boolean isSavePressed;
    private EditText noteEditText;

    public FragmentTaskNotes() {
        fragment = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_notes, container, false);
        isSavePressed = false;
        String note = PrefManager.getNote(getContext());
        noteEditText = view.findViewById(R.id.et_task_note);
        noteEditText.setText(note);
        return view;
    }

    public void collectData() {
        isSavePressed = true;
        PrefManager.setTaskFragment(TaskDetailsActivity.NOTES, getContext());
        setData();
    }

    private void setData() {
        PrefManager.setNote(noteEditText.getText().toString(), getContext());
    }

    @Override
    public void onDestroyView() {
        if (!isSavePressed) {
            setData();
        }
        super.onDestroyView();
    }
}
