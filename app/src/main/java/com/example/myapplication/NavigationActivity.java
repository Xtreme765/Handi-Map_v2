package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class NavigationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner locationSpinner;
    String spinnerText;
    Spinner locationSpinner2;
    String spinnerText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        locationSpinner = findViewById(R.id.locationSpinnerA);
        spinnerText = locationSpinner.getSelectedItem().toString();
        locationSpinner.setOnItemSelectedListener(this);

        locationSpinner2 = findViewById(R.id.locationSpinnerB);
        spinnerText2 = locationSpinner2.getSelectedItem().toString();
        locationSpinner2.setOnItemSelectedListener(this);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spinner_items, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.spinner_items2, R.layout.spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        locationSpinner2.setAdapter(adapter2);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
        //Toast.makeText( this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        //String itemSelected = adapterView.getSelectedItem().toString();
        spinnerText = locationSpinner.getSelectedItem().toString();
        spinnerText2 = locationSpinner2.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView){
    }

    /** Called when the user taps the Back button */
    public void routingMessage(View view) {
        // Code for sending Starting Location and Destination
        String sLocation = spinnerText;
        String eDestination = spinnerText2;
        Toast.makeText( this, "Routing " + sLocation + " to " + eDestination, Toast.LENGTH_SHORT).show();

        // Goes back to map screen
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("startKey", sLocation);
        intent.putExtra("endKey", eDestination);
        startActivity(intent);
    }

}
