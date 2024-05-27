package com.example.libbbreriaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ErrorPage extends AppCompatActivity implements IManageComponents, IManageIntents{
    private Context context;
    private String error;
    private TextView errorDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.hideBar(this);
        setContentView(R.layout.activity_error_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            Intent myIntent = getIntent();
            this.error = myIntent.getStringExtra("error");
        } catch (Exception err) {
            //TODO error page
        }

        this.setComponents();
        this.setData();
    }

    @Override
    public void setData(){
        this.errorDisplay.setText(this.error);
    }

    @Override
    public void setComponents() {
        this.context = this;
        this.errorDisplay = this.findViewById(R.id.error);
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void changeIntent(@NonNull Class intent) {
        this.startActivity(new Intent(this.context, intent));
        this.finish();
    }

    @Override
    public void changeIntent(@NonNull Intent intent) {
        this.startActivity(intent);
        this.finish();
    }
}