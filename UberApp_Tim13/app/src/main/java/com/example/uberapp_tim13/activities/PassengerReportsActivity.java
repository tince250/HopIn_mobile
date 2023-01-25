package com.example.uberapp_tim13.activities;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.RideForReportDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.tools.Globals;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerReportsActivity extends AppCompatActivity {

    private BarChart barChart;
    private TextView totalTV;
    private TextView averageTV;
    private Spinner criteriaSpinner;
    private MaterialDatePicker picker;
    private TextView noReportsTV;
    private LinearLayout totalLL;
    private LinearLayout notSingleLL;
    private TextView dateRangeTV;

    private boolean dateChosen = false;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<RideForReportDTO> rides;
    private boolean singleDate = true;

    List<BarEntry> entries = new ArrayList<BarEntry>();
    List<String> labels = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Reports");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_passenger_reports);

        initElements();
        addDatePicker();
        setSpinner();
        dateRangeTV.setText(getDefaultDateText());
        if(Globals.userRole.equals("passenger"))
            getDataPassenger();
        else
            getDataDriver();
    }

    private String getDefaultDateText() {
        String month = LocalDate.now().getMonth().toString().toLowerCase(Locale.ROOT);
        month = month.substring(0, 1).toUpperCase() + month.substring(1, 3);

        return LocalDate.now().getDayOfMonth() + " " + month;
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
                        if(Globals.userRole.equals("passenger"))
                            getDataPassenger();
                        else
                            getDataDriver();                    }
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

    private void initElements() {
        barChart = findViewById(R.id.reportsBarChart);
        totalTV = findViewById(R.id.calculatedTotalTV);
        averageTV = findViewById(R.id.calculatedAverageTV);
        criteriaSpinner = findViewById(R.id.spinnerPasengerReportsFilter);
        noReportsTV = findViewById(R.id.noReportsTV);
        totalLL = findViewById(R.id.totalLL);
        notSingleLL = findViewById(R.id.notSingleDateLL);
        dateRangeTV = (TextView) findViewById(R.id.dateRangeTV);
    }

    private void getDataPassenger() {
        start = LocalDateTime.now();
        end = LocalDateTime.now();

        if (dateChosen) {
            Pair<Long, Long> sel = (Pair<Long, Long>) picker.getSelection();
            start = getDateFromPickerSelection(sel.first);
            end = getDateFromPickerSelection(sel.second);
        }

        Call<List<RideForReportDTO>> call = RestUtils.passengerAPI.getAllRidesBetweenDates(AuthService.tokenDTO.getAccessToken(), Globals.user.getId(), convertDate(start), convertDate(end));
        call.enqueue(new Callback<List<RideForReportDTO>>() {
            @Override
            public void onResponse(Call<List<RideForReportDTO>> call, Response<List<RideForReportDTO>> response) {
                if (response.isSuccessful()) {
                    rides = response.body();
                    fillEntries(rides);
                } else {
                    Toast.makeText(PassengerReportsActivity.this, "An error happened while trying to fetch rides.", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<List<RideForReportDTO>> call, Throwable t) {
                Log.d("EVOME", t.toString());
                Toast.makeText(PassengerReportsActivity.this, "An error happened while trying to fetch rides :(", Toast.LENGTH_LONG);
            }
        });
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
                    Toast.makeText(PassengerReportsActivity.this, "An error happened while trying to fetch rides.", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<List<RideForReportDTO>> call, Throwable t) {
                Log.d("EVOME", t.toString());
                Toast.makeText(PassengerReportsActivity.this, "An error happened while trying to fetch rides :(", Toast.LENGTH_LONG);
            }
        });
    }

    private LocalDateTime getDateFromPickerSelection(Long selection) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(selection),TimeZone.getDefault().toZoneId());
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
            totalLL.setVisibility(View.GONE);
            notSingleLL.setVisibility(View.GONE);
            noReportsTV.setVisibility(View.VISIBLE);
            return;
        }
        noReportsTV.setVisibility(View.GONE);

        int i = 0;
        String criteria = criteriaSpinner.getSelectedItem().toString();

        Log.d("KRITERIJUM", criteria);
        Log.d("KRITERIJUM", rides.stream().collect(groupingBy(ride -> LocalDateTime.parse(ride.getStartTime()).toLocalDate())).toString());
        Map<Object, List<RideForReportDTO>> map = rides.stream().collect(groupingBy(ride ->  LocalDateTime.parse(ride.getStartTime()).toLocalDate()));

        if (map.keySet().size() > 1)
            map = fillMissingDates(map);

        Log.d("DODAVANJE", map.toString());

        entries = new ArrayList<>();
        labels = new ArrayList<>();

        String sufix = "";

        switch (criteria) {
            case "Distance traveled":
                for (Object date : map.keySet().stream().sorted().collect(toList()))
                {
                    List<RideForReportDTO> ridesVals = map.get(date);
                    entries.add(new BarEntry((float) i++, (float) ridesVals.stream().mapToDouble(RideForReportDTO::getDistance).sum()));
                    labels.add(formatLabel(date.toString()));
                }
                sufix = "km";
                break;
            case "Money spent":
                for (Object date : map.keySet().stream().sorted().collect(toList()))
                {
                    List<RideForReportDTO> ridesVals = map.get(date);
                    entries.add(new BarEntry((float) i++, (float) ridesVals.stream().mapToDouble(RideForReportDTO::getTotalCost).sum()));
                    labels.add(formatLabel(date.toString()));
                }
                sufix = "RSD";
                break;
            case "Money earnd":
                for (Object date : map.keySet().stream().sorted().collect(toList()))
                {
                    List<RideForReportDTO> ridesVals = map.get(date);
                    entries.add(new BarEntry((float) i++, (float) ridesVals.stream().mapToDouble(RideForReportDTO::getTotalCost).sum()));
                    labels.add(formatLabel(date.toString()));
                }
                sufix = "RSD";
                break;
            case "Number of rides":
                for (Object date : map.keySet().stream().sorted().collect(toList()))
                {
                    List<RideForReportDTO> ridesVals = map.get(date);
                    entries.add(new BarEntry((float) i++, (float) ridesVals.size()));
                    labels.add(formatLabel(date.toString()));
                }
                break;
            default:
                break;
        }


        fillSums(sufix);
        if (map.keySet().size() > 1) {
            initBarChart();
            notSingleLL.setVisibility(View.VISIBLE);
            totalLL.setVisibility(View.VISIBLE);
        }
        else {
            notSingleLL.setVisibility(View.GONE);
            totalLL.setVisibility(View.VISIBLE);
        }

    }

    private String formatLabel(String dateString) {
        String[] tokens = dateString.split("-");
        return tokens[2] + "." + tokens[1] + "." + tokens[0] + ".";
    }

    private Map<Object, List<RideForReportDTO>> fillMissingDates(Map<Object, List<RideForReportDTO>> map) {
        Map<Object, List<RideForReportDTO>> newMap = map;
        List<LocalDate> keys = map.keySet().stream().map(x -> (LocalDate)x).collect(toList());
        keys = keys.stream().sorted().collect(toList());

        LocalDate currDate = start.toLocalDate();
        int done = 0;
        int i = 0;
        while (currDate.isBefore(ChronoLocalDate.from(end)) || currDate.isEqual(ChronoLocalDate.from(end)))  {
            if (keys.contains(currDate)) {
                done++;
            } else {
                newMap.put(currDate, new ArrayList<>());
            }
            currDate = currDate.plusDays(1);
        }

        return newMap;
    }

    private double calculateTotal() {
        double sum = entries.stream().mapToDouble(BarEntry::getY).sum();
        return Math.round(sum);
    }

    private double calculateAverage() {
        return Math.round(calculateTotal()/entries.size());
    }

    private void fillSums(String sufix) {
        double total = this.calculateTotal();
        double average = this.calculateAverage();

        totalTV.setText(total + sufix);
        averageTV.setText(average + sufix);

        if (sufix.equals("")) {
            totalTV.setText((int)total + sufix);
            averageTV.setText((int)average + sufix);
        }

    }

    private void initBarChart() {

        BarDataSet dataSet = new BarDataSet(entries, "Rides in date range");
        dataSet.setColors(ColorTemplate.rgb("#337D98"));
        dataSet.setValueTextSize(12f);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        barChart.getLegend().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        barChart.setClickable(false);

        YAxisRenderer yAxis = barChart.getRendererRightYAxis();

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        if (!singleDate)
            xAxis.setLabelRotationAngle(90);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.size());

        barChart.animateY(1000);
        barChart.invalidate();
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter;
        if(Globals.userRole.equals("passenger")) {
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.passenger_reports_filter));
        } else {
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.driver_reports_filter));
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        criteriaSpinner.setAdapter(adapter);

        criteriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (dateChosen || rides != null) {
                    fillEntries(rides);
                }
                else {
                    if(Globals.userRole.equals("passenger"))
                        getDataPassenger();
                    else
                        getDataDriver();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        criteriaSpinner.setSelection(1);
    }
}