package com.example.uberapp_tim13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class DriverReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Reports");
        setContentView(R.layout.activity_driver_reports);
        setSpinner();

        Button button = (Button) findViewById(R.id.dateRangeBtn);
        TextView textView = (TextView) findViewById(R.id.dateRangeTV);

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        builder.setTheme(R.style.CustomThemeOverlay_MaterialCalendar_Fullscreen);
        builder.setCalendarConstraints(constraintsBuilder.build());
        MaterialDatePicker picker = builder.build();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.show(getSupportFragmentManager(), "Material_Range");
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        textView.setText(picker.getHeaderText());
                    }
                });
            }
        });
    }

    private void setSpinner() {
        Spinner areaNumSpinner = findViewById(R.id.spinnerDriverReportsFilter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.driver_reports_filter));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaNumSpinner.setAdapter(adapter);
    }
}