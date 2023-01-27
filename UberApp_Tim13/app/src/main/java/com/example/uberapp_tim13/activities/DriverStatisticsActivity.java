package com.example.uberapp_tim13.activities;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.RideForReportDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.tools.Globals;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private boolean isDay = true;
    private boolean isMonth = false;
    private boolean isWeek = false;
    private TextView dayTv;
    private TextView monthTV;
    private TextView weekTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Statistics");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_driver_statistics);

        ((TextView) findViewById(R.id.driverNameTV)).setText("Hey, " + Globals.user.getName());

        initElements();
        isDay = true;
        dayTv.setTextColor(getResources().getColor(R.color.lighter_blue));
        dayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDay = true;
                isMonth = false;
                isWeek = false;
                dayTv.setTextColor(getResources().getColor(R.color.lighter_blue));
                monthTV.setTextColor(getResources().getColor(R.color.black));
                weekTV.setTextColor(getResources().getColor(R.color.black));
                fillEntries(rides);
            }
        });
        monthTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDay = false;
                isMonth = true;
                isWeek = false;
                dayTv.setTextColor(getResources().getColor(R.color.black));
                monthTV.setTextColor(getResources().getColor(R.color.lighter_blue));
                weekTV.setTextColor(getResources().getColor(R.color.black));
                fillEntries(rides);
            }
        });
        weekTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDay = false;
                isMonth = false;
                isWeek = true;
                dayTv.setTextColor(getResources().getColor(R.color.black));
                monthTV.setTextColor(getResources().getColor(R.color.black));
                weekTV.setTextColor(getResources().getColor(R.color.lighter_blue));
                fillEntries(rides);
            }
        });

        addDatePicker();
        setSpinner();
        dateRangeTV.setText(getDefaultDateText());
        getDataDriver();
    }

    private String getDefaultDateText() {
        String month = LocalDate.now().getMonth().toString().toLowerCase(Locale.ROOT);
        month = month.substring(0, 1).toUpperCase() + month.substring(1, 3);

        return LocalDate.now().getDayOfMonth() + " " + month;
    }

    private void initElements() {
        totalTV = findViewById(R.id.totalValueTV);
        averageTV = findViewById(R.id.statTypeValueTV);
        criteriaSpinner = findViewById(R.id.spinnerPasengerReportsFilter);
        dateRangeTV = (TextView) findViewById(R.id.dateRangeTV);
        dayTv = findViewById(R.id.dayTV);
        monthTV = findViewById(R.id.yearTV);
        weekTV = findViewById(R.id.monthTV);
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
                        getDataDriver();
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
                    fillEntries(rides);
                }
                else {
                    getDataDriver();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        criteriaSpinner.setSelection(1);
    }

    private void getDataDriver() {
        start = LocalDateTime.now();
        end = LocalDateTime.now();

        if (dateChosen) {
            Pair<Long, Long> sel = (Pair<Long, Long>) picker.getSelection();
            start = getDateFromPickerSelection(sel.first);
            end = getDateFromPickerSelection(sel.second);
        }

        Call<List<RideForReportDTO>> call = RestUtils.driverAPI.getAllRidesBetweenDates(AuthService.tokenDTO.getAccessToken(), Globals.user.getId(), convertDate(start), convertDate(end));
        call.enqueue(new Callback<List<RideForReportDTO>>() {
            @Override
            public void onResponse(Call<List<RideForReportDTO>> call, Response<List<RideForReportDTO>> response) {
                if (response.isSuccessful()) {
                    rides = response.body();
                    fillEntries(rides);
                } else {
                    Toast.makeText(DriverStatisticsActivity.this, "An error happened while trying to fetch rides.", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<List<RideForReportDTO>> call, Throwable t) {
                Log.d("EVOME", t.toString());
                Toast.makeText(DriverStatisticsActivity.this, "An error happened while trying to fetch rides :(", Toast.LENGTH_LONG);
            }
        });
    }

    private LocalDateTime getDateFromPickerSelection(Long selection) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(selection), TimeZone.getDefault().toZoneId());
    }

    private String convertDate(LocalDateTime date) {
        String day = "" + date.getDayOfMonth();
        if (date.getDayOfMonth() < 10) {
            day = "0" + date.getMonthValue();
        }

        String month = "" + date.getMonthValue();
        if (date.getMonthValue() < 10) {
            month = "0" + date.getMonthValue();
        }
        return date.getYear() + "/" + month + "/" + day;
    }


    private void fillEntries(List<RideForReportDTO> rides) {

        if (rides.size() == 0) {
            return;
        }

        int i = 0;
        String criteria = criteriaSpinner.getSelectedItem().toString();

        Log.d("KRITERIJUM", criteria);
        Log.d("KRITERIJUM", rides.stream().collect(groupingBy(ride -> LocalDateTime.parse(ride.getStartTime()).toLocalDate())).toString());



        String sufix = "";
        String prefix = "";
        double total = 0;
        switch (criteria) {
            case "Distance traveled":
                for (RideForReportDTO ride : rides)
                {
                    total = total + ride.getDistance();
                }
                prefix = "traveled";
                sufix = "km";
                break;
            case "Earnings":
                for (RideForReportDTO ride : rides)
                {
                    total = total + ride.getTotalCost();
                }
                prefix = "earnd";
                sufix = "RSD";
                break;
            case "Rejected rides":
                for (RideForReportDTO ride : rides)
                {
                    if (ride.isRejected())
                        total = total + 1;
                }
                prefix = "rejected";
                sufix = "rides";
                break;
            case "Accepted rides":
                for (RideForReportDTO ride : rides)
                {
                    if (!ride.isRejected())
                        total = total + 1;
                }
                prefix = "accepted";
                sufix = "rides";
                break;
            default:
                break;
        }


        fillSums(sufix, prefix, total);

    }

    private void fillSums(String sufix, String prefix, double total) {
        this.totalTV.setText(Double.toString(total));
        ((TextView)findViewById(R.id.statTypeTV)).setText("You " + prefix);
        int range = end.getDayOfYear() - start.getDayOfYear();
        if (isDay) {
            averageTV.setText(String.format("%.2f", total/range)  + sufix);
        } else if (isWeek) {
            averageTV.setText(String.format("%.2f", total/range*7) + sufix);
        } else {
            averageTV.setText(String.format("%.2f", total/range*30) + sufix);
        }
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
