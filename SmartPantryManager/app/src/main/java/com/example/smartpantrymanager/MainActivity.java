package com.example.smartpantrymanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.adapter.PantryAdapter;
import com.example.smartpantrymanager.database.PantryDatabase;
import com.example.smartpantrymanager.model.PantryItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PantryAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private PantryAdapter adapter;
    private PantryDatabase database;
    private List<PantryItem> itemList = new ArrayList<>();
    private long selectedExpiryDate = 0;

    private Button btnAll, btnExpiring, btnExpired, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = PantryDatabase.getInstance(this);

        recyclerView = findViewById(R.id.recyclerView);
        btnAll = findViewById(R.id.btnAll);
        btnExpiring = findViewById(R.id.btnExpiring);
        btnExpired = findViewById(R.id.btnExpired);
        btnAdd = findViewById(R.id.btnAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(itemList, this);
        recyclerView.setAdapter(adapter);

        loadAllItems();

        btnAll.setOnClickListener(v -> loadAllItems());
        btnExpiring.setOnClickListener(v -> loadExpiringSoon());
        btnExpired.setOnClickListener(v -> loadExpired());
        btnAdd.setOnClickListener(v -> showAddDialog(null));
    }

    private void loadAllItems() {
        itemList = database.pantryDao().getAllItems();
        adapter.updateList(itemList);
    }

    private void loadExpiringSoon() {
        long now = System.currentTimeMillis();
        long threeDays = now + (3 * 24 * 60 * 60 * 1000L);
        itemList = database.pantryDao().getExpiringSoonItems(now, threeDays);
        adapter.updateList(itemList);
        if (itemList.isEmpty()) Toast.makeText(this, "No items expiring soon!", Toast.LENGTH_SHORT).show();
    }

    private void loadExpired() {
        long now = System.currentTimeMillis();
        itemList = database.pantryDao().getExpiredItems(now);
        adapter.updateList(itemList);
        if (itemList.isEmpty()) Toast.makeText(this, "No expired items!", Toast.LENGTH_SHORT).show();
    }

    private void showAddDialog(PantryItem editItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);
        builder.setView(view);

        EditText editName = view.findViewById(R.id.editName);
        EditText editCategory = view.findViewById(R.id.editCategory);
        EditText editQuantity = view.findViewById(R.id.editQuantity);
        EditText editUnit = view.findViewById(R.id.editUnit);
        EditText editNotes = view.findViewById(R.id.editNotes);
        Button btnPickDate = view.findViewById(R.id.btnPickDate);
        TextView textSelectedDate = view.findViewById(R.id.textSelectedDate);

        selectedExpiryDate = 0;

        if (editItem != null) {
            editName.setText(editItem.getName());
            editCategory.setText(editItem.getCategory());
            editQuantity.setText(String.valueOf(editItem.getQuantity()));
            editUnit.setText(editItem.getUnit());
            editNotes.setText(editItem.getNotes());
            selectedExpiryDate = editItem.getExpiryDate();
            textSelectedDate.setText(new java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(new java.util.Date(selectedExpiryDate)));
        }

        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(this, (view1, year, month, day) -> {
                Calendar selected = Calendar.getInstance();
                selected.set(year, month, day);
                selectedExpiryDate = selected.getTimeInMillis();
                textSelectedDate.setText(day + "/" + (month + 1) + "/" + year);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });

        builder.setPositiveButton(editItem == null ? "Add" : "Update", (dialog, which) -> {
            String name = editName.getText().toString().trim();
            String category = editCategory.getText().toString().trim();
            String qtyStr = editQuantity.getText().toString().trim();
            String unit = editUnit.getText().toString().trim();
            String notes = editNotes.getText().toString().trim();

            if (name.isEmpty() || category.isEmpty() || qtyStr.isEmpty() || selectedExpiryDate == 0) {
                Toast.makeText(this, "Please fill all fields and pick date!", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(qtyStr);

            if (editItem == null) {
                PantryItem newItem = new PantryItem(name, category, quantity, unit, selectedExpiryDate, System.currentTimeMillis(), notes);
                database.pantryDao().insert(newItem);
                Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show();
            } else {
                editItem.setName(name);
                editItem.setCategory(category);
                editItem.setQuantity(quantity);
                editItem.setUnit(unit);
                editItem.setExpiryDate(selectedExpiryDate);
                editItem.setNotes(notes);
                database.pantryDao().update(editItem);
                Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
            }
            loadAllItems();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    public void onItemClick(PantryItem item) {
        showAddDialog(item);
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete?")
                .setMessage("Delete " + item.getName() + "?")
                .setPositiveButton("Delete", (d, w) -> {
                    database.pantryDao().delete(item);
                    loadAllItems();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}