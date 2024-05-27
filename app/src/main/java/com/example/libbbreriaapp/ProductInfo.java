package com.example.libbbreriaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductInfo extends AppCompatActivity implements IManageComponents, IManageIntents{
    private int id;
    private WebView img;
    private TextView title, desc, price;
    private Button addBtn;
    private Product prod;
    private DataFetcher df;
    private PreferencesIO pio;
    private Context context;
    private Button userBtn, cartBtn, libriBtn, cdBtn, findBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.hideBar(this);
        setContentView(R.layout.activity_product_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            Intent myIntent = getIntent();
            this.id = myIntent.getIntExtra("id", -1);
        } catch (Exception err) {
            Intent intent = new Intent(context, ErrorPage.class);
            intent.putExtra("error", err.getMessage());
            startActivity(intent);
        }

        this.setComponents();
        this.setListeners();
        this.fetchProductInfo();
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

        this.img = this.findViewById(R.id.itemimg);
        this.title = this.findViewById(R.id.itemtitle);
        this.desc = this.findViewById(R.id.itemdesc);
        this.price = this.findViewById(R.id.itemprice);
        this.addBtn = this.findViewById(R.id.add);
    }

    @Override
    public void setData() {
        this.img.loadUrl(Config.ToolsApi.IMG + this.prod.getImgurl());
        this.title.setText(this.prod.getTitle());
        this.desc.setText(this.prod.getDescription());
        this.price.setText(this.prod.getPrice() + "€");
    }

    @Override
    public void setListeners() {
        this.userBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.LOGIN));
        this.cartBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.CART_LIST));
        this.libriBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.LIBRI_LIST));
        this.cdBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.CD_LIST));
        this.findBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.FIND_LIST));

        this.addBtn.setOnClickListener((View view)-> {
            if (this.pio.get("session_id", null) == null) {
                this.changeIntent(Config.Pages.LOGIN);
            }
            else {
                this.checkIfAlreadyLogged();
            }
        });
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

    private void checkIfAlreadyLogged(){
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

                if (message.equals("Not Logged")) changeIntent(Config.Pages.LOGIN);
                else if (message.equals("Logged")) {
                    add();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("FETCH ERROR", errorMessage);

                Intent intent = new Intent(context, Config.Pages.ERROR);
                intent.putExtra("error", errorMessage);
                changeIntent(intent);
            }
        });
    }

    private void fetchProductInfo(){
        this.df.get(Config.ProductApi.ID + this.id, "data", new DataFetcher.DataListener() {
            @Override
            public void onDataFetched(JSONArray data) {
                try {
                    JSONObject obj = data.getJSONObject(0);
                    prod = new Product(
                            obj.getInt("id"),
                            obj.getString("type"),
                            obj.getString("imgurl"),
                            obj.getString("title"),
                            obj.getString("description"),
                            obj.getDouble("price"),
                            obj.getInt("qty")
                    );
                } catch (Exception err){
                    Log.e("ERROR", err.getMessage());

                    Intent intent = new Intent(context, ErrorPage.class);
                    intent.putExtra("error", err.getMessage());
                    startActivity(intent);
                }

                setData();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("FETCH ERROR", errorMessage);

                Intent intent = new Intent(context, ErrorPage.class);
                intent.putExtra("error", errorMessage);
                startActivity(intent);
            }
        });
    }

    private void add(){
        Map<String, String> params = new HashMap<>();
        params.put("session_id", this.pio.get("session_id", null));

        this.df.post(Config.CartApi.ADD + id, params, new DataFetcher.DataListener() {
            @Override
            public void onDataFetched(String data) {
                //changeIntent(Config.Pages.LIBRI_LIST);
                vibrate(context, 50);
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