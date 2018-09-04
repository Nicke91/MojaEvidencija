package com.milos.nicke.mojaevidencija.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.milos.nicke.mojaevidencija.Model.Reminder;
import com.milos.nicke.mojaevidencija.Model.Task;
import com.example.nicke.mojaevidencija.R;
import com.milos.nicke.mojaevidencija.TaskDetailsActivity;
import com.milos.nicke.mojaevidencija.Util.CalendarHelper;
import com.milos.nicke.mojaevidencija.Util.PrefManager;
import com.milos.nicke.mojaevidencija.Util.ReminderScheduler;
import com.milos.nicke.mojaevidencija.Util.Utill;

import java.util.Date;

/**
 * Created on 2/20/2018.
 */

public class FragmentTaskReminder extends FragmentAdapter implements View.OnClickListener,
        DateFragmentDialog.OnDatePickedListener, TimePickerFragmentDialog.OnTimePickedListener {

    public static final int REQUEST_CODE_DATE_DIALOG = 0;
    public static final int REQUEST_CODE_TIME_DIALOG = 1;

    private static final int SWITCH_UNCHECKED = 0;
    private static final int SWITCH_CHECKED = 1;

    private boolean isSavePressed;

    private Reminder thisReminder;

    private LinearLayout layoutRemainderHolder;
    private RelativeLayout datesLayout, timeLayout, repeatLayout;

    private EditText etDescription;

    private ImageView repeatDropDown;
    private TextView tvDateValue, tvTimeValue;

    private Spinner spRepeatValue;

    public FragmentTaskReminder() {
        fragment = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_reminder, container, false);

        String[] SP_REPEAT_ITEMS = getContext().getResources().getStringArray(R.array.spinner_task_reminder_repeat);

        isSavePressed = false;

        ArrayAdapter<String> repeatAdapter = new ArrayAdapter<>(
                getContext(),
                R.layout.layout_spinner_repeat,
                SP_REPEAT_ITEMS
        );
        thisReminder = PrefManager.getRemainder(getContext());

        if (thisReminder == null) {
            thisReminder = new Reminder();
            thisReminder.setIsRemainderChecked(SWITCH_UNCHECKED);
            PrefManager.setRemainder(thisReminder, getContext());
        }

        repeatAdapter.setDropDownViewResource(R.layout.layout_spinner_repeat);
        spRepeatValue = view.findViewById(R.id.remainder_spinner_values_repeat);
        spRepeatValue.setAdapter(repeatAdapter);

        Switch showContentLayoutBtn = view.findViewById(R.id.btn_remainder_show_date_switch);
        layoutRemainderHolder = view.findViewById(R.id.remainder_layout_content_holder);

        etDescription = view.findViewById(R.id.reminder_et_description);

        datesLayout = view.findViewById(R.id.remainder_layout_values_date);
        timeLayout = view.findViewById(R.id.remainder_layout_values_time);
        repeatLayout = view.findViewById(R.id.remainder_layout_values_repeat);
        repeatDropDown = view.findViewById(R.id.remainder_iv_dropdown_repeat);

        tvDateValue = view.findViewById(R.id.remainder_tv_values_date);
        tvTimeValue = view.findViewById(R.id.remainder_tv_values_time);

        if (thisReminder.getIsRemainderChecked() == SWITCH_CHECKED) {
            showContentLayoutBtn.setChecked(true);
            layoutRemainderHolder.setVisibility(View.VISIBLE);

            String description = thisReminder.getDescription();
            String date = thisReminder.getDate();
            String time = thisReminder.getTime();
            String repeat = thisReminder.getRepeat();

            if (description != null) {
                etDescription.setText(thisReminder.getDescription());
            }

            if (date != null) {
                tvDateValue.setText(thisReminder.getDate());
            } else {
                tvDateValue.setText(Utill.getFormattedDate(CalendarHelper.getCurrentDate()));
            }

            if (time != null) {
                tvTimeValue.setText(thisReminder.getTime());
            } else {
                tvTimeValue.setText(Utill.getFormattedCurrentTime());
            }

            if (repeat != null && !repeat.equals(ReminderScheduler.NO_REPEAT)) {
                repeatLayout.setVisibility(View.VISIBLE);
                repeatDropDown.setImageResource(R.mipmap.ic_dropdown_subtract);

                for (int i = 0; i < SP_REPEAT_ITEMS.length; i++) {
                    if (repeat.equals(SP_REPEAT_ITEMS[i])) {
                        spRepeatValue.setSelection(i);
                        break;
                    }
                }
            }
        } else {
            thisReminder.setIsRemainderChecked(SWITCH_UNCHECKED);
            showContentLayoutBtn.setChecked(false);
            layoutRemainderHolder.setVisibility(View.GONE);
        }

        //setSwitchListener
        showContentLayoutBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    thisReminder.setIsRemainderChecked(SWITCH_CHECKED);
                    layoutRemainderHolder.setVisibility(View.VISIBLE);
                    datesLayout.setVisibility(View.VISIBLE);
                    tvDateValue.setVisibility(View.VISIBLE);
                    tvDateValue.setText(Utill.getFormattedDate(CalendarHelper.getCurrentDate()));
                    timeLayout.setVisibility(View.VISIBLE);
                    tvTimeValue.setText(Utill.getFormattedCurrentTime());
                } else {
                    layoutRemainderHolder.setVisibility(View.GONE);
                }
            }
        });

        //setClickListeners()
        repeatDropDown.setOnClickListener(this);

        tvDateValue.setOnClickListener(this);
        tvTimeValue.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remainder_iv_dropdown_repeat:
                setDropdownArrows(repeatDropDown, repeatLayout);
                break;
            case R.id.remainder_tv_values_date:
                DateFragmentDialog dateFragmentDialog = new DateFragmentDialog();
                dateFragmentDialog.setTargetFragment(this, REQUEST_CODE_DATE_DIALOG);
                dateFragmentDialog.show(getFragmentManager(), "FragmentTaskReminder.DateDialog");
                break;
            case R.id.remainder_tv_values_time:
                TimePickerFragmentDialog timePickerFragmentDialog = new TimePickerFragmentDialog();
                timePickerFragmentDialog.setStartingTime(tvTimeValue.getText().toString());
                timePickerFragmentDialog.setTargetFragment(this, REQUEST_CODE_TIME_DIALOG);
                timePickerFragmentDialog.show(getFragmentManager(), "FragmentTaskReminder.TimeDialog");
                break;
        }
    }

    private void setDropdownArrows(ImageView dropdown, RelativeLayout layout) {
        if (layout.getVisibility() != View.VISIBLE) {
            layout.setVisibility(View.VISIBLE);
            dropdown.setImageResource(R.mipmap.ic_dropdown_subtract);
        } else {
            layout.setVisibility(View.GONE);
            dropdown.setImageResource(R.mipmap.ic_dropdown_expand);
        }
    }

    @Override
    public void onDatePicked(Date date) {
        if (date != null) {

            String dateString = Utill.getFormattedDate(date);

            thisReminder.setDate(dateString);
            tvDateValue.setText(dateString);
        }
    }

    @Override
    public void onTimePicked(String time) {
        if (time != null) {
            thisReminder.setTime(time);
            tvTimeValue.setText(time);
        }
    }

    @Override
    public void collectData() {
        isSavePressed = true;
        PrefManager.setTaskFragment(TaskDetailsActivity.REMAINDER, getContext());
        setData();
    }

    private void setData() {
        if (layoutRemainderHolder.getVisibility() == View.VISIBLE) {
            String description = etDescription.getText().toString();
            String date = tvDateValue.getText().toString();
            String time = tvTimeValue.getText().toString();
            String spItem = ReminderScheduler.NO_REPEAT;

            if (repeatLayout.getVisibility() == View.VISIBLE) {
                spItem = spRepeatValue.getSelectedItem().toString();
            }

            thisReminder.setIsRemainderChecked(SWITCH_CHECKED);
            thisReminder.setDescription(description);
            thisReminder.setDate(date);
            thisReminder.setTime(time);
            thisReminder.setRepeat(spItem);
            PrefManager.setRemainder(thisReminder, getContext());

        } else {
            Task task = PrefManager.getTask(getContext());

            if (task != null) {

                if (task.getId() != null) {
                    if (thisReminder != null) {
                        ReminderScheduler.cancelReminder(task, getContext());
                    }
                }
            }

            thisReminder = null;
            PrefManager.setRemainder(null, getContext());
        }
    }

    @Override
    public void onDestroyView() {
        if (!isSavePressed) {
            setData();
        }
        super.onDestroyView();
    }
}
