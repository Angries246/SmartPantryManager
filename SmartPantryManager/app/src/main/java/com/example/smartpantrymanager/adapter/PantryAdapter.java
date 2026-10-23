package com.example.smartpantrymanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.util.DateUtils;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    private List<PantryItem> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    public PantryAdapter(List<PantryItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryItem item = items.get(position);
        holder.nameText.setText(item.getName());
        holder.categoryText.setText(item.getCategory() + " • " + item.getQuantity() + " " + item.getUnit());
        holder.expiryText.setText(DateUtils.formatDate(item.getExpiryDate()));
        holder.statusText.setText(DateUtils.getExpiryStatus(item.getExpiryDate()));
        holder.statusText.setTextColor(DateUtils.getExpiryColor(item.getExpiryDate()));

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.deleteText.setOnClickListener(v -> listener.onDeleteClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<PantryItem> newList) {
        this.items = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, categoryText, expiryText, statusText, deleteText;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textName);
            categoryText = itemView.findViewById(R.id.textCategory);
            expiryText = itemView.findViewById(R.id.textExpiry);
            statusText = itemView.findViewById(R.id.textStatus);
            deleteText = itemView.findViewById(R.id.textDelete);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
