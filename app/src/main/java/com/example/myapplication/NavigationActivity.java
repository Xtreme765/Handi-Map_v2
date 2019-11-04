package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class NavigationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Spinner locationSpinner = findViewById(R.id.locationSpinnerA);
        Spinner locationSpinner2 = findViewById(R.id.locationSpinnerB);
        locationSpinner.setOnItemSelectedListener(this);
        locationSpinner2.setOnItemSelectedListener(this);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spinner_items, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.spinner_items2, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        locationSpinner2.setAdapter(adapter2);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
        Toast.makeText( this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView){
    }
}
