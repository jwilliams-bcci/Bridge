package com.burgess.bridge;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class BridgeHelper {
    public static void setChangeTracker(EditText editText, Double compareTo) {
        Drawable originalBackground = editText.getBackground();
        boolean fieldChanged = true;
        editText.setTag(!fieldChanged);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (!editText.getText().toString().equals(compareTo.toString())) {
                    editText.setBackgroundResource(R.drawable.green_border);
                    editText.setTag(fieldChanged);
                } else {
                    editText.setBackground(originalBackground);
                    editText.setTag(!fieldChanged);
                }
            }
        });
    }

    public static void setChangeTracker(EditText editText, Integer compareTo) {
        Drawable originalBackground = editText.getBackground();
        boolean fieldChanged = true;
        editText.setTag(!fieldChanged);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (!editText.getText().toString().equals(compareTo.toString())) {
                    editText.setBackgroundResource(R.drawable.green_border);
                    editText.setTag(fieldChanged);
                } else {
                    editText.setBackground(originalBackground);
                    editText.setTag(!fieldChanged);
                }
            }
        });
    }

    public static void setChangeTracker(EditText editText, String compareTo) {
        Drawable originalBackground = editText.getBackground();
        boolean fieldChanged = true;
        editText.setTag(!fieldChanged);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (!editText.getText().toString().equals(compareTo)) {
                    editText.setBackgroundResource(R.drawable.green_border);
                    editText.setTag(fieldChanged);
                    } else {
                    editText.setBackground(originalBackground);
                    editText.setTag(!fieldChanged);
                }
            }
        });
    }

    public static void setChangeTracker(Spinner spinner, String compareTo) {
        Drawable originalBackground = spinner.getBackground();
        boolean fieldChanged = true;
        spinner.setTag(!fieldChanged);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getSelectedItem().toString().equals(compareTo)) {
                    adapterView.setBackgroundResource(R.drawable.green_border);
                    adapterView.setTag(fieldChanged);
                } else {
                    adapterView.setBackground(originalBackground);
                    adapterView.setTag(!fieldChanged);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public static void setChangeTracker(CheckBox checkBox, boolean compareTo) {
        Drawable originalBackground = checkBox.getBackground();
        boolean fieldChanged = true;
        checkBox.setTag(!fieldChanged);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!checkBox.isChecked() == compareTo) {
                checkBox.setBackgroundResource(R.drawable.green_border);
                checkBox.setTag(fieldChanged);
            } else {
                checkBox.setBackground(originalBackground);
                checkBox.setTag(!fieldChanged);
            }
        });
    }
}
