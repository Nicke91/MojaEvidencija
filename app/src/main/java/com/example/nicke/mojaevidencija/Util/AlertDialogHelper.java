package com.example.nicke.mojaevidencija.Util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nicke.mojaevidencija.Adapter.CategoryAdapter;
import com.example.nicke.mojaevidencija.MainActivity;
import com.example.nicke.mojaevidencija.Model.Category;
import com.example.nicke.mojaevidencija.R;

import java.util.List;

/**
 * Created on 3/10/2018.
 */

public class AlertDialogHelper {

    public static void deleteCategoryDialog(final CategoryAdapter.OnCategoryListener activity, final int position) {

        List<Category> categoryList = Category.listAll(Category.class);
        Category category = categoryList.get(position);

        AlertDialog.Builder dialog = new AlertDialog.Builder((MainActivity) activity);
        dialog.setTitle(String.format(AppConfig.getSystemString((MainActivity)(activity),R.string.dialog_title_category_delete), category.getName()));
        dialog.setMessage(String.format(AppConfig.getSystemString((MainActivity) (activity),R.string.dialog_message_category_are_you_sure), category.getName()));
        dialog.setPositiveButton(((MainActivity)activity).getResources().getString(R.string.dialog_button_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.updateDatabase(position);
            }
        })
                .setNegativeButton(((MainActivity)activity).getResources().getString(R.string.dialog_button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    public static Dialog editCategoryTitle(View view, final ActionBarListener actionBarListener) {
        final Category category = PrefManager.getCategory(view.getContext());
        final EditText editText = view.findViewById(R.id.dialog_category_editText);
        editText.setHint("");
        editText.setText(category.getName());

        final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                .setView(view)
                .setTitle(AppConfig.getSystemString(view.getContext(), R.string.dialog_title_category_change_title))
                .setMessage(AppConfig.getSystemString(view.getContext(), R.string.dialog_message_category_change_title))
                .setPositiveButton(AppConfig.getSystemString(view.getContext(), R.string.ok), null)
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
                            editText.setError(AppConfig.getSystemString(view.getContext(), R.string.error_empty_name));
                            return;
                        }
                        List<Category> categories = Category.find(Category.class, AppConfig.SUGAR_WHERE_NAME, input);

                        if (categories != null && categories.size() > 0) {
                            for (Category category : categories) {
                                if (category.getName().equals(input)) {
                                    editText.setError(AppConfig.getSystemString(view.getContext(), R.string.error_category_exists));
                                    return;
                                }
                            }
                        }
                        Category currentCategory = Category.findById(Category.class, category.getId());

                        if (currentCategory != null) {

                            currentCategory.setName(input);
                            if (category.getDate() != null) {
                                currentCategory.setDate(category.getDate());
                            }
                            currentCategory.save();
                            PrefManager.setCategory(currentCategory, view.getContext());
                            actionBarListener.onEditCategoryFinished();

                        }
                        alertDialog.dismiss();
                    }
                });
            }
        });
        return alertDialog;
    }

    public static void editCategoryName(final Context context, final ActionBarListener actionBarListener) {
        final Category category = PrefManager.getCategory(context);

        final EditText etInput = new EditText(context);
        etInput.setText(category.getName());
        etInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT

        );

        etInput.setLayoutParams(lp);

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setView(etInput);
        alertDialog.setTitle(AppConfig.getSystemString(context, R.string.dialog_title_category_change_title));
        alertDialog.setMessage(AppConfig.getSystemString(context, R.string.dialog_message_category_change_title));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, AppConfig.getSystemString(context, R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String input = etInput.getText().toString().trim();
                if (input.equals("")) {
                    Toast.makeText(context, AppConfig.getSystemString(context, R.string.error_empty_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Category> categories = Category.find(Category.class, AppConfig.SUGAR_WHERE_NAME, input);

                if (categories != null && categories.size() > 0) {
                    for (Category category : categories) {
                        if (category.getName().equals(input)) {
                            Toast.makeText(context, AppConfig.getSystemString(context, R.string.error_category_exists), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                Category currentCategory = Category.findById(Category.class, category.getId());

                if (currentCategory != null) {

                    currentCategory.setName(input);
                    if (category.getDate() != null) {
                        currentCategory.setDate(category.getDate());
                    }
                    currentCategory.save();
                    PrefManager.setCategory(currentCategory, context);
                    actionBarListener.onEditCategoryFinished();

                }
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public static void areYouSureDialog(Context context, final AlarmDeleteListener alarmDeleteListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(AppConfig.getSystemString(context, R.string.dialog_title_delete))
                .setMessage(AppConfig.getSystemString(context, R.string.dialog_message_are_you_sure))
                .setPositiveButton(AppConfig.getSystemString(context, R.string.dialog_button_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alarmDeleteListener.onDeleteConfirmed(true);
                    }
                })
                .setNegativeButton(AppConfig.getSystemString(context, R.string.dialog_button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alarmDeleteListener.onDeleteConfirmed(false);
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void goBackDialog(Context context, final BackListener backListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(AppConfig.getSystemString(context, R.string.dialog_title_button_back))
                .setMessage(AppConfig.getSystemString(context, R.string.dialog_message_button_back_save))
                .setPositiveButton(AppConfig.getSystemString(context, R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backListener.onSaveConfirmed(true);
                    }
                })
                .setNegativeButton(AppConfig.getSystemString(context, R.string.dialog_no_thanks), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backListener.onSaveConfirmed(false);
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface ActionBarListener {
        void onEditCategoryFinished();
    }

    public interface AlarmDeleteListener {
        void onDeleteConfirmed(boolean canDelete);
    }

    public interface BackListener {
        void onSaveConfirmed(boolean canSave);
    }

}
