package com.milos.nicke.mojaevidencija.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.example.nicke.mojaevidencija.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created on 2/28/2018.
 */

public class DateFragmentDialog extends android.support.v4.app.DialogFragment {

    private OnDatePickedListener onDatePickedListener;
    private DatePicker datePicker;

    public interface OnDatePickedListener {
        void onDatePicked(Date date);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_pick_date, null);

        onDatePickedListener = (OnDatePickedListener) getTargetFragment();

        datePicker = view.findViewById(R.id.remainder_dialog_date_picker);
        if (Build.VERSION.SDK_INT < 21) {
            datePicker.setCalendarViewShown(false);
        } else {
            datePicker.setCalendarViewShown(true);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(getActivity().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        onDatePickedListener.onDatePicked(calendar.getTime());
                        dismiss();
                    }
                }).create();

    }
}
