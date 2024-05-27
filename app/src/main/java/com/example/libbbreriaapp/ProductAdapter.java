package com.example.libbbreriaapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<Product> products;
    private ProductAdapter.ClickCallback click;

    public ProductAdapter(ArrayList<Product> products, final ProductAdapter.ClickCallback click) {
        this.products = products;
        this.click = click;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.img.loadUrl(Config.ToolsApi.IMG + product.getImgurl());
        holder.title.setText(product.getTitle());
        holder.price.setText(product.getPrice() + "€");

        holder.cardView.setOnClickListener((View view) -> {
            click.onClick(product.getId());
        });

        holder.img.setOnTouchListener((View v, MotionEvent event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) click.onClick(product.getId());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        WebView img;
        TextView title;
        TextView price;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.img = itemView.findViewById(R.id.img);
            this.title = itemView.findViewById(R.id.title);
            this.price = itemView.findViewById(R.id.price);
            this.cardView = (CardView) itemView;
        }
    }

    public interface ClickCallback {
        void onClick(int id);
    }
}
