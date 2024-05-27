package com.example.libbbreriaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogOut extends AppCompatActivity implements IManageComponents, IManageIntents{
    private Context context;
    private Button userBtn, cartBtn, libriBtn, cdBtn, findBtn;
    private Button logout;
    private TextView name;
    private String username = "";
    private DataFetcher df;
    private PreferencesIO pio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_out);
        this.hideBar(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.setComponents();
        this.setListeners();

        this.username = this.pio.get("session_id", null);
        //todo: chiedere nome utente

        this.setData();
    }

    @Override
    public void setComponents() {
        this.context = this;
        this.df = new DataFetcher(this.context);
        this.pio = new PreferencesIO(this.context, "libbbreria");

        this.userBtn = this.findViewById(R.id.userButton);
        this.cartBtn = this.findViewById(R.id.cartButton);
        this.libriBtn = this.findViewById(R.id.libriButton);
        this.cdBtn = this.findViewById(R.id.cdButton);
        this.findBtn = this.findViewById(R.id.searchButton);

        this.name = this.findViewById(R.id.name);
        this.logout = this.findViewById(R.id.logout);
    }

    @Override
    public void setListeners() {
        this.userBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.LOGIN));
        this.cartBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.CART_LIST));
        this.libriBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.LIBRI_LIST));
        this.cdBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.CD_LIST));
        this.findBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.FIND_LIST));

        this.logout.setOnClickListener((View v) -> this.logout());
    }

    @Override
    public void setData() {
        this.name.setText(this.username);
    }

    @Override
    public void changeIntent(@NonNull Class intent) {
        this.vibrate(this.context, 50);
        this.startActivity(new Intent(this.context, intent));
        this.finish();
    }

    @Override
    public void changeIntent(@NonNull Intent intent) {
        this.vibrate(this.context, 50);
        this.startActivity(intent);
        this.finish();
    }

    private void logout(){
        this.vibrate(this.context, 50);

        Map<String, String> params = new HashMap<>();
        params.put("session_id", pio.get("session_id", null));

        this.df.post(Config.UserApi.LOGOUT, params, new DataFetcher.DataListener() {
            @Override
            public void onDataFetched(String data) {
                pio.remove("session_id");
                changeIntent(Config.Pages.LOGIN);
            }

            @Override
            public void onError(String errorMessage) {
                Intent intent = new Intent(context, Config.Pages.ERROR);
                intent.putExtra("error", errorMessage);
                changeIntent(intent);
            }
        });
    }
}