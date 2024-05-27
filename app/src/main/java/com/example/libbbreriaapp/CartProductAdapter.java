package com.example.libbbreriaapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {

    private ArrayList<CartProduct> products;
    private CartProductAdapter.ClickCallback click;

    public CartProductAdapter(ArrayList<CartProduct> products, final CartProductAdapter.ClickCallback click) {
        this.products = products;
        this.click = click;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartProduct product = products.get(position);
        holder.img.loadUrl(Config.ToolsApi.IMG + product.getImgurl());
        holder.title.setText(product.getTitle());
        holder.price.setText(product.getPrice() + "€");
        holder.qty.setText(product.getQty() + "");

        holder.add.setOnClickListener((View view) -> click.onAddClick(product.getId()));
        holder.rm.setOnClickListener((View view) -> click.onRmClick(product.getId()));
        holder.del.setOnClickListener((View view) -> click.onDelClick(product.getId()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        WebView img;
        TextView title;
        TextView price;
        TextView qty;
        Button add, rm, del;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.img = itemView.findViewById(R.id.img);
            this.title = itemView.findViewById(R.id.title);
            this.price = itemView.findViewById(R.id.price);
            this.qty = itemView.findViewById(R.id.qty);
            this.add = itemView.findViewById(R.id.add);
            this.rm = itemView.findViewById(R.id.rm);
            this.del = itemView.findViewById(R.id.del);
            this.cardView = (CardView) itemView;
        }
    }

    public interface ClickCallback {
        default void onAddClick(int id){};
        default void onRmClick(int id){};
        default void onDelClick(int id){};
    }
}
