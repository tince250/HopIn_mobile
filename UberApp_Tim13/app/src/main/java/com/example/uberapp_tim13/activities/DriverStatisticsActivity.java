package com.example.uberapp_tim13.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.RideForReportDTO;
import com.example.uberapp_tim13.tools.Globals;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.LocalDateTime;
import java.util.List;

public class DriverStatisticsActivity extends AppCompatActivity {

    private TextView averageTV;
    private TextView totalTV;
    private Spinner criteriaSpinner;
    private MaterialDatePicker picker;
    private TextView dateRangeTV;

    private boolean dateChosen = false;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<RideForReportDTO> rides;
    private boolean singleDate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Statistics");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_driver_statistics);

        ((TextView) findViewById(R.id.driverNameTV)).setText("Hey, " + Globals.user.getName());

        initElements();
        addDatePicker();

    }

    private void initElements() {
        totalTV = findViewById(R.id.totalValueTV);
        averageTV = findViewById(R.id.statTypeValueTV);
        criteriaSpinner = findViewById(R.id.spinnerPasengerReportsFilter);
        dateRangeTV = (TextView) findViewById(R.id.dateRangeTV);
    }

    private void addDatePicker() {
        Button button = (Button) findViewById(R.id.dateRangeBtn);

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointBackward.now());
        builder.setTheme(R.style.CustomThemeOverlay_MaterialCalendar_Fullscreen);
        builder.setCalendarConstraints(constraintsBuilder.build());
        picker = builder.build();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.show(getSupportFragmentManager(), "Material_Range");
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {

                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Log.d("PICKER", "PICKER");
                        setDateRangeText();
                        dateChosen = true;
//                        if(Globals.userRole.equals("passenger"))
//                            getDataPassenger();
//                        else
//                            getDataDriver();
                    }
                });
            }
        });
    }

    private void setDateRangeText() {
        Pair<Long, Long> sel = (Pair<Long, Long>)picker.getSelection();
        if (sel.first.equals(sel.second)) {
            singleDate = true;
            String[] tokens = picker.getHeaderText().split(" ");
            dateRangeTV.setText(tokens[0] + " " + tokens[1]);
        } else {
            singleDate = false;
            dateRangeTV.setText(picker.getHeaderText());
        }
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.statistic_options));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        criteriaSpinner.setAdapter(adapter);

        criteriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (dateChosen || rides != null) {
//                    fillEntries(rides);
                }
                else {
//                    if(Globals.userRole.equals("passenger"))
//                        getDataPassenger();
//                    else
//                        getDataDriver();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        criteriaSpinner.setSelection(1);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
