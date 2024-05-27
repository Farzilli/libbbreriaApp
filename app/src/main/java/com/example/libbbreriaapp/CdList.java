package com.example.libbbreriaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class CdList extends AppCompatActivity implements IManageComponents, IManageIntents {
    private Context context;
    private RecyclerView recyclerView;
    private Button userBtn, cartBtn, libriBtn, cdBtn, findBtn;
    private ProductAdapter adapter;
    private ArrayList<Product> productList;
    private DataFetcher df;
    private int cardClickId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.hideBar(this);
        setContentView(R.layout.activity_cd_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.setComponents();
        this.setListeners();

        this.df.get(Config.ProductApi.CD, "data", new DataFetcher.DataListener() {
            @Override
            public void onDataFetched(JSONArray data) {
                try {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        productList.add(new Product(
                                obj.getInt("id"),
                                obj.getString("type"),
                                obj.getString("imgurl"),
                                obj.getString("title"),
                                obj.getString("description"),
                                obj.getDouble("price"),
                                obj.getInt("qty")
                        ));
                    }

                    adapter = new ProductAdapter(productList, (int id) -> {
                        cardClickId = id;
                        Intent intent = new Intent(context, ProductInfo.class);
                        setIntentParams(intent);
                        changeIntent(intent);
                    });
                    recyclerView.setAdapter(adapter);
                } catch (Exception err){
                    Log.e("ERROR", err.getMessage());

                    Intent intent = new Intent(context, ErrorPage.class);
                    intent.putExtra("error", err.getMessage());
                    startActivity(intent);
                }
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

    @Override
    public void setComponents() {
        this.context = this;

        this.userBtn = this.findViewById(R.id.userButton);
        this.cartBtn = this.findViewById(R.id.cartButton);
        this.libriBtn = this.findViewById(R.id.libriButton);
        this.cdBtn = this.findViewById(R.id.cdButton);
        this.findBtn = this.findViewById(R.id.searchButton);

        this.productList = new ArrayList<>();
        this.df = new DataFetcher(this);
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
}