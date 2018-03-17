package com.example.nicke.mojaevidencija.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.nicke.mojaevidencija.Model.Shopping;
import com.example.nicke.mojaevidencija.R;
import com.example.nicke.mojaevidencija.TaskDetailsActivity;
import com.example.nicke.mojaevidencija.Util.CalculatorHelper;
import com.example.nicke.mojaevidencija.Util.PrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2/20/2018.
 */

public class FragmentTaskShoppingList extends FragmentAdapter implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        TextWatcher {

    private Context context;

    private boolean isSavePressed;
    private String[] SP_QUANTITY_ITEM;
    private ArrayAdapter<String> quantityAdapter;

    private List<View> viewList;
    private List<Shopping> shoppingList;

    private TableLayout shoppingListLayout;

    private Spinner quantitySpinner;
    private EditText titleET;
    private EditText priceET;
    private TextView rowTotalTV;
    private TextView totalTV;

    public FragmentTaskShoppingList() {
        fragment = this;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_shopping_list, container, false);

        isSavePressed = false;
        SP_QUANTITY_ITEM = getResources().getStringArray(R.array.spinner_task_list_quantity);

        quantityAdapter = new ArrayAdapter<>(
                getContext(),
                R.layout.layout_spinner_quantity,
                SP_QUANTITY_ITEM
        );

        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        viewList = new ArrayList<>();

        shoppingList = PrefManager.getShoppingList(getContext());

        ImageButton addListImageBtn = view.findViewById(R.id.ib_shopping_list_add);
        shoppingListLayout = view.findViewById(R.id.layout_table_shopping_list_holder);
        totalTV = view.findViewById(R.id.tv_shopping_list_total);

        setShoppingList();
        addListImageBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_shopping_list_add:
                addViews();
                break;
            case R.id.iv_shopping_list_delete:
                shoppingListLayout.removeView((View) view.getParent().getParent());
                viewList.remove(view.getParent().getParent());
                shoppingList.clear();
                updateTotal();
                break;
        }
    }

    private void setShoppingList() {
        if (shoppingList != null && shoppingList.size() > 0) {

            //editedTask (has list)
            for (int i = 0; i < shoppingList.size(); i++) {
                addViews();
            }
            //editedTask (if tasks added) altering fragments
            if (viewList != null && viewList.size() > 0) {
                for (int i = 0; i < viewList.size(); i++) {
                    quantitySpinner = viewList.get(i).findViewById(R.id.spinner_shopping_list_quantity_select);
                    titleET = viewList.get(i).findViewById(R.id.et_shopping_list_name);
                    priceET = viewList.get(i).findViewById(R.id.et_shopping_list_price);
                    rowTotalTV = viewList.get(i).findViewById(R.id.tv_shopping_list_row_total);

                    int getSpinnerSelection = Integer.parseInt(shoppingList.get(i).getQuantity());

                    quantitySpinner.setSelection(getSpinnerSelection - 1);
                    titleET.setText(shoppingList.get(i).getName());
                    priceET.setText(shoppingList.get(i).getPrice());
                    rowTotalTV.setText(shoppingList.get(i).getRowTotal());
                }
                updateTotal();
            }
        } else {
            //newTask/editedTask (with no list)
            shoppingList = new ArrayList<>();
        }
    }

    private void addViews() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.layout_shopping_list_inputs, null);
        shoppingListLayout.addView(rowView, shoppingListLayout.getChildCount());

        quantitySpinner = rowView.findViewById(R.id.spinner_shopping_list_quantity_select);
        titleET = rowView.findViewById(R.id.et_shopping_list_name);
        priceET = rowView.findViewById(R.id.et_shopping_list_price);
        rowTotalTV = rowView.findViewById(R.id.tv_shopping_list_row_total);


        viewList.add(rowView);

        quantitySpinner.setOnItemSelectedListener(this);

        priceET.addTextChangedListener(this);

        ImageView deleteShoppingList = rowView.findViewById(R.id.iv_shopping_list_delete);
        deleteShoppingList.setOnClickListener(this);
    }

    private void updateRowTotal() {

        if (viewList != null && viewList.size() > 0) {

            for (View view : viewList) {
                priceET = view.findViewById(R.id.et_shopping_list_price);
                quantitySpinner = view.findViewById(R.id.spinner_shopping_list_quantity_select);

                int rowTotal = CalculatorHelper.calculateRowTotal(priceET.getText().toString(), quantitySpinner.getSelectedItem().toString());

                rowTotalTV = view.findViewById(R.id.tv_shopping_list_row_total);
                rowTotalTV.setText(String.valueOf(rowTotal));
            }
        }
        updateTotal();
    }

    private void updateTotal() {
        int total = 0;

        if (viewList != null && viewList.size() > 0) {
            for (View view : viewList) {
                priceET = view.findViewById(R.id.et_shopping_list_price);
                quantitySpinner = view.findViewById(R.id.spinner_shopping_list_quantity_select);

                int rowTotal = CalculatorHelper.calculateRowTotal(priceET.getText().toString(), quantitySpinner.getSelectedItem().toString());
                total += rowTotal;
            }

            totalTV.setText(String.valueOf(total));
        } else {
            totalTV.setText(String.valueOf(total));
        }
    }

    private void setData() {
        shoppingList.clear();
        if (viewList != null && viewList.size() > 0) {
            for (View view : viewList) {
                quantitySpinner = view.findViewById(R.id.spinner_shopping_list_quantity_select);
                titleET = view.findViewById(R.id.et_shopping_list_name);
                priceET = view.findViewById(R.id.et_shopping_list_price);
                rowTotalTV = view.findViewById(R.id.tv_shopping_list_row_total);

                Shopping shopping = new Shopping();

                shopping.setQuantity(quantitySpinner.getSelectedItem().toString());
                shopping.setName(titleET.getText().toString());
                shopping.setPrice(priceET.getText().toString());
                shopping.setRowTotal(rowTotalTV.getText().toString());
                shopping.setTotal(totalTV.getText().toString());

                shoppingList.add(shopping);
            }
        }

        PrefManager.addShoppingList(shoppingList, getContext());
        viewList.clear();
    }

    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        updateRowTotal();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //TextWatcher
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        updateRowTotal();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    //FragmentAdapter
    @Override
    public void collectData() {
        isSavePressed = true;
        PrefManager.setTaskFragment(TaskDetailsActivity.SHOPPING_LIST, getContext());
        setData();
    }

    @Override
    public void onDestroyView() {
        if (!isSavePressed) {
            setData();
        }
        super.onDestroyView();
    }
}
