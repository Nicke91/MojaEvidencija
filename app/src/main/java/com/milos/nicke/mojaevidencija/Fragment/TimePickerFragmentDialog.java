package com.milos.nicke.mojaevidencija.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.example.nicke.mojaevidencija.R;
import com.milos.nicke.mojaevidencija.Util.Utill;

/**
 * Created on 3/1/2018.
 */

public class TimePickerFragmentDialog extends android.support.v4.app.DialogFragment {

    private OnTimePickedListener onTimePickedListener;
    private TimePicker timePicker;

    private int startingHour, startingMinute;

    public interface OnTimePickedListener {
        void onTimePicked(String time);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_pick_time, null);

        onTimePickedListener = (OnTimePickedListener) getTargetFragment();

        timePicker = view.findViewById(R.id.remainder_dialog_time_picker);

        if (Build.VERSION.SDK_INT < 23) {
            timePicker.setCurrentHour(startingHour);
            timePicker.setCurrentMinute(startingMinute);
        } else {
            timePicker.setHour(startingHour);
            timePicker.setMinute(startingMinute);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(getActivity().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int hour;
                        int minute;

                        if (Build.VERSION.SDK_INT < 23) {
                            hour = timePicker.getCurrentHour();
                            minute = timePicker.getCurrentMinute();
                        } else {
                            hour = timePicker.getHour();
                            minute = timePicker.getMinute();
                        }

                        String time = Utill.getFormattedTime(hour, minute);

                        onTimePickedListener.onTimePicked(time);
                        dismiss();
                    }
                }).create();
    }

    public void setStartingTime(String time) {
        String[] timeArray = time.split(":");
        startingHour = Integer.parseInt(timeArray[0]);
        startingMinute = Integer.parseInt(timeArray[1]);
    }
}
