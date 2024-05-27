package com.example.libbbreriaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements IManageComponents, IManageIntents {
    private Context context;
    private Button userBtn, cartBtn, libriBtn, cdBtn, findBtn;
    private TextView err;
    private String errorMsg;
    private EditText email;
    private EditText password;
    private Button submit;
    private DataFetcher df;
    private PreferencesIO pio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        this.hideBar(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.setComponents();
        this.checkIfAlreadyLogged();
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

        this.err = this.findViewById(R.id.errMsg);
        this.email = this.findViewById(R.id.email);
        this.password = this.findViewById(R.id.password);
        this.submit = this.findViewById(R.id.submit);
    }

    @Override
    public void setListeners() {
        this.userBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.LOGIN));
        this.cartBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.CART_LIST));
        this.libriBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.LIBRI_LIST));
        this.cdBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.CD_LIST));
        this.findBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.FIND_LIST));

        this.submit.setOnClickListener((View v) -> this.login());
    }

    @Override
    public void setData() {
        this.err.setText(this.errorMsg);
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

    private void login() {
        this.vibrate(this.context, 50);
        String emailStr = this.email.getText().toString().trim();
        String passwordStr = this.password.getText().toString().trim();

        if (!emailStr.isEmpty() && !passwordStr.isEmpty()) {
            Map<String, String> params = new HashMap<>();
            params.put("login", "login");
            params.put("email", emailStr);
            params.put("password", passwordStr);

            this.df.post(Config.UserApi.LOGIN, params, new DataFetcher.DataListener() {
                @Override
                public void onDataFetched(String data) {
                    Log.i("LOGINonDataFetched", data);
                    try {
                        JSONObject JsonData = new JSONObject(data);
                        if (JsonData.get("status").equals("success")) pio.set("session_id", JsonData.get("session_id").toString());
                        changeIntent(Config.Pages.LOGOUT);
                    } catch (Exception err) {
                        Intent intent = new Intent(context, Config.Pages.ERROR);
                        intent.putExtra("error", err.getMessage());
                        changeIntent(intent);
                    }
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

    private void checkIfAlreadyLogged(){
        if (this.pio.get("session_id", null) != null) {
            Map<String, String> params = new HashMap<>();
            params.put("session_id", this.pio.get("session_id", null));

            this.df.post(Config.UserApi.ISLOGGED, params, new DataFetcher.DataListener() {
                @Override
                public void onDataFetched(String data) {
                    Log.i("ISLOGGED", data.toString());
                    String message = "";

                    try {
                        JSONObject jsonData = new JSONObject(data);
                        message = jsonData.get("message").toString();
                    } catch (Exception error) {
                        Log.e("JSON PARSING", error.getMessage());

                        Intent intent = new Intent(context, Config.Pages.ERROR);
                        intent.putExtra("error", error.getMessage());
                        changeIntent(intent);
                    }

                    if (message.equals("Not Logged")) setListeners();
                    else if (message.equals("Logged")) changeIntent(Config.Pages.LOGOUT);
                }

                @Override
                public void onError(String errorMessage) {
                    Log.e("FETCH ERROR", errorMessage);

                    Intent intent = new Intent(context, Config.Pages.ERROR);
                    intent.putExtra("error", errorMessage);
                    changeIntent(intent);
                }
            });
        } else {
            setListeners();
        }
    }
}