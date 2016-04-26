package com.freego.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.freego.R;

public class HostEditActivity extends Activity {

    private int flag;

    private String[] genders;

    private String[] person_number;

    private String[] period;

    private String[] startDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_edit);

        TextView textView =(TextView)findViewById(R.id.hostedit_hotel_name);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Helvetica LT Light.ttf");
        textView.setTypeface(typeFace);

        genders = getResources().getStringArray(R.array.genders_array);
        Spinner genderSpinner = (Spinner) findViewById(R.id.hostedit_gender_spinner);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genders);
        genderSpinner.setAdapter(genderAdapter);

        person_number = getResources().getStringArray(R.array.person_number_array);
        Spinner personNumberSpinner = (Spinner) findViewById(R.id.hostedit_person_number_spinner);
        ArrayAdapter<String> personNumberAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, person_number);
        personNumberSpinner.setAdapter(personNumberAdapter);

        period = getResources().getStringArray(R.array.period_array);
        Spinner periodSpinner = (Spinner) findViewById(R.id.hostedit_period_spinner);
        ArrayAdapter<String> periodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, period);
        periodSpinner.setAdapter(periodAdapter);

        startDate = getResources().getStringArray(R.array.start_date_array);
        Spinner startSpinner = (Spinner) findViewById(R.id.hostedit_start_spinner);
        ArrayAdapter<String> startAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, startDate);
        startSpinner.setAdapter(startAdapter);
    }
}
