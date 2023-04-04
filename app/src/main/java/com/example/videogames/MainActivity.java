package com.example.videogames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText nameEdit;
    EditText priceEdit;
    Spinner spinner;
    Button addButton;
    Button getButton;
    DatabaseControl control;
    TextView resultView;
    TextView companyView;
    RecyclerView recycle;
    public String[] getNames;
    public String[] getPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEdit = findViewById(R.id.nameEdit);
        priceEdit = findViewById(R.id.priceEdit);
        spinner = findViewById(R.id.spinner);
        addButton = findViewById(R.id.addButton);
        getButton = findViewById(R.id.getButton);
        resultView = findViewById(R.id.resultView);
        recycle = findViewById(R.id.recycle);
        companyView = findViewById(R.id.companyResult);

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control.open();
                String company = control.getCompany(nameEdit.getText().toString());
                String secondCompany = control.getCompany(priceEdit.getText().toString());
                control.close();
                resultView.setText(company);
                companyView.setText(secondCompany);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                String price = priceEdit.getText().toString();
                String company = ((TextView) spinner.getSelectedItem()).getText().toString();
                control.open();
                boolean itWorked = control.insert(name, company);
                boolean itStillWorked = control.insert(price, company);
                control.close();
                if(itWorked && itStillWorked){
                    Toast.makeText(getApplicationContext(), "Added "+name+" "+price+" "+company, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "FAILED "+name+" "+price+" "+company, Toast.LENGTH_SHORT).show();
                }
            }
        });

        control = new DatabaseControl(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        recycle.setLayoutManager(new LinearLayoutManager(this));
        control.open();
        getNames = control.getAllNamesArray(nameEdit.getText().toString());
        getPrice = control.getAllPricesArray(priceEdit.getText().toString());
        control.close();
        //resultView.setText(getNames);
        //companyView.setText(getCompany);
        recycle.setAdapter(new CustomAdapter(getNames));

    }
}