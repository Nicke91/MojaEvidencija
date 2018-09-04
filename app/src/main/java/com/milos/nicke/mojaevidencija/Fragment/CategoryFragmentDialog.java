package com.milos.nicke.mojaevidencija.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.milos.nicke.mojaevidencija.Model.Category;
import com.example.nicke.mojaevidencija.R;
import com.milos.nicke.mojaevidencija.Util.AppConfig;

/**
 * Created on 2/7/2018.
 */

public class CategoryFragmentDialog extends DialogFragment {
    private EditText editText;

    public interface CategoryDialogInterface {
        void onFinishCategoryDialog(String name);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_category, null);

        editText = view.findViewById(R.id.dialog_category_editText);
        editText.setHint(AppConfig.getSystemString(view.getContext(), R.string.dialog_hint_new_category));


        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.dialog_title_new_category)
                .setPositiveButton(R.string.ok, null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button okBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String input = editText.getText().toString().trim();

                        if (input.equals("")) {
                            editText.setError(getString(R.string.error_empty_name));
                        } else if (Category.find(Category.class, "name = ?", input.trim()).size() > 0) {
                            editText.setError(getString(R.string.error_category_exists));
                        } else {
                            CategoryDialogInterface activity = (CategoryDialogInterface) getActivity();
                            activity.onFinishCategoryDialog(editText.getText().toString());
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });

        return alertDialog;
    }
}
