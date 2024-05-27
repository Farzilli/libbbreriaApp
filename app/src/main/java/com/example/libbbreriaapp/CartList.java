package com.example.libbbreriaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartList extends AppCompatActivity implements IManageComponents, IManageIntents {
    private Context context;
    private RecyclerView recyclerView;
    private Button userBtn, cartBtn, libriBtn, cdBtn, findBtn;
    private CartProductAdapter adapter;
    private ArrayList<CartProduct> productList;
    private DataFetcher df;
    private PreferencesIO pio;
    private int cardClickId;
    private TextView totalPrice;
    private Button buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.hideBar(this);
        setContentView(R.layout.activity_cart_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.setComponents();

        if (this.pio.get("session_id", null) == null) {
            this.changeIntent(Config.Pages.LOGIN);
        }
        else {
            this.checkIfAlreadyLogged();
        }
    }

    @Override
    public void setComponents() {
        this.context = this;
        this.df = new DataFetcher(this);
        this.pio = new PreferencesIO(this.context, "libbbreria");

        this.userBtn = this.findViewById(R.id.userButton);
        this.cartBtn = this.findViewById(R.id.cartButton);
        this.libriBtn = this.findViewById(R.id.libriButton);
        this.cdBtn = this.findViewById(R.id.cdButton);
        this.findBtn = this.findViewById(R.id.searchButton);

        this.totalPrice = this.findViewById(R.id.totprice);
        this.buy = this.findViewById(R.id.buy);

        this.productList = new ArrayList<CartProduct>();
        this.recyclerView = findViewById(R.id.rv);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void setListeners() {
        this.userBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.LOGIN));
        this.cartBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.CART_LIST));
        this.libriBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.LIBRI_LIST));
        this.cdBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.CD_LIST));
        this.findBtn.setOnClickListener((View view) -> this.changeIntent(Config.Pages.FIND_LIST));

        this.buy.setOnClickListener((View view) -> buy());
    }

    @Override
    public void setIntentParams(@NonNull Intent intent) {
        intent.putExtra("id", this.cardClickId);
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

    @Override
    public void setData() {
        this.loadProducts();
    }

    private void loadProducts(){
        this.productList.clear();

        Map<String, String> params = new HashMap<>();
        params.put("session_id", this.pio.get("session_id", null));

        this.df.post(Config.CartApi.CART, params, new DataFetcher.DataListener() {
            @Override
            public void onDataFetched(String data) {
                try {
                    JSONObject jodata = new JSONObject(data);
                    JSONArray jsonData = jodata.getJSONArray("data");
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject obj = jsonData.getJSONObject(i);
                        productList.add(new CartProduct(
                                obj.getInt("id"),
                                obj.getString("imgurl"),
                                obj.getString("title"),
                                obj.getDouble("price"),
                                obj.getInt("qty")
                        ));
                    }

                    double tot = 0.0;
                    for (CartProduct prod : productList) tot += prod.calcTot();
                    totalPrice.setText("Totale: " + String.format("%.2f", tot) + "€");

                    adapter = new CartProductAdapter(productList, new CartProductAdapter.ClickCallback() {
                        @Override
                        public void onAddClick(int id) {
                            addClick(id);
                        }

                        @Override
                        public void onRmClick(int id) {
                            rmClick(id);
                        }

                        @Override
                        public void onDelClick(int id) {
                            delClick(id);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                }catch (Exception err){
                    Log.e("ERROR_PARSING", err.getMessage());

                    Intent intent = new Intent(context, Config.Pages.ERROR);
                    intent.putExtra("error", err.getMessage());
                    changeIntent(intent);
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

    private void addClick(int id) {
        cardClickId = id;
        vibrate(context, 50);
        makeRequest(Config.CartApi.ADD, id);
    }

    private void rmClick(int id) {
        cardClickId = id;
        vibrate(context, 50);
        makeRequest(Config.CartApi.RM, id);
    }

    private void delClick(int id) {
        cardClickId = id;
        vibrate(context, 50);
        makeRequest(Config.CartApi.DEL, id);
    }

    private void makeRequest(String url, int id){
        Map<String, String> params = new HashMap<>();
        params.put("session_id", this.pio.get("session_id", null));

        this.df.post(url + id, params, new DataFetcher.DataListener() {
            @Override
            public void onDataFetched(String data) {
                setData();
            }

            @Override
            public void onError(String errorMessage) {
                Intent intent = new Intent(context, Config.Pages.ERROR);
                intent.putExtra("error", errorMessage);
                changeIntent(intent);
            }
        });
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
                        setListeners();
                        setData();
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

    private void buy(){
        this.vibrate(this.context, 50);
        Map<String, String> params = new HashMap<>();
        params.put("session_id", this.pio.get("session_id", null));
        this.df.post(Config.CartApi.BUY, params, new DataFetcher.DataListener() {
            @Override
            public void onDataFetched(String data) {
                Log.i("CARTDATA", data);
                setData();
            }

            @Override
            public void onError(String errorMessage) {
                Log.i("CARTERROR", errorMessage);
            }
        });
    }
}