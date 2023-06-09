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
    Button getPriceButton;
    Button deleteButton;
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
        deleteButton = findViewById(R.id.deleteButton);
        getPriceButton = findViewById(R.id.getPriceButton);

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control.open();
                String company = spinner.getSelectedItem().toString();
                control.close();
                resultView.setText(company);
            }
        });

        getPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control.open();
                String price = priceEdit.getText().toString();
                control.close();
                resultView.setText(price);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                control.open();
                control.delete(name);
                control.close();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                String price = priceEdit.getText().toString();
                String company = (String) spinner.getSelectedItem(); //((TextView) spinner.getSelectedItem()).getText().toString();
                control.open();
                boolean itWorked = control.insert(name, company);
                boolean itStillWorked = control.insert(price, company);
                control.close();
                if(itWorked && itStillWorked){
                    Toast.makeText(getApplicationContext(), "Added "+name+" "+company+" "+price, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "FAILED "+name+" "+company+" "+price, Toast.LENGTH_SHORT).show();
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
        getNames = control.getAllNamesArray();
        control.close();
        CustomAdapter adapter = new CustomAdapter(getNames);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAdapter.ViewHolder viewHolder = (CustomAdapter.ViewHolder) view.getTag();
                TextView textView = viewHolder.getTextView();
                String name = textView.getText().toString();
                control.open();
                String company = control.getCompany(name);
                String price = control.getPrice(name);
                control.close();
                resultView.setText(name+": "+company);
                resultView.setText(name+": "+price);
            }
        });
        recycle.setAdapter(adapter);
        Toast.makeText(this,adapter.getItemCount()+ "", Toast.LENGTH_SHORT).show();

    }
}