package com.example.postaldz;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private EditText searchEditText;
    private ListView resultsListView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Open the database
        database = openOrCreateDatabase("postaldz.db", MODE_PRIVATE, null);

        searchEditText = findViewById(R.id.searchEditText);
        resultsListView = findViewById(R.id.resultsListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        resultsListView.setAdapter(adapter);

        // Add text change listener to searchEditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used in this example
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // When text changes in searchEditText, perform search
                String searchText = s.toString();
                searchInDatabase(searchText);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used in this example
            }
        });

        // Set click listener on ListView to show details when an item is clicked
        resultsListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = adapter.getItem(position);
            showDetails(selectedItem);
        });
    }

    private void searchInDatabase(String searchText) {
        // Perform search in the database using SQL query
        // You can use Cursor to retrieve results
        Cursor cursor = database.rawQuery("SELECT * FROM algeria_postcodes WHERE " +
                "field1 LIKE '%" + searchText + "%' OR " +
                "field2 LIKE '%" + searchText + "%' OR " +
                "field3 LIKE '%" + searchText + "%' OR " +
                "field4 LIKE '%" + searchText + "%' OR " +
                "field5 LIKE '%" + searchText + "%' OR " +
                "field6 LIKE '%" + searchText + "%' OR " +
                "field7 LIKE '%" + searchText + "%' OR " +
                "field8 LIKE '%" + searchText + "%' OR " +
                "field9 LIKE '%" + searchText + "%' OR " +
                "field10 LIKE '%" + searchText + "%' OR " +
                "field11 LIKE '%" + searchText + "%' OR " +
                "field12 LIKE '%" + searchText + "%' OR " +
                "field13 LIKE '%" + searchText + "%'", null);

        // Retrieve results and display them in ListView
        List<String> searchResults = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String result = cursor.getString(cursor.getColumnIndex("field8"));
                searchResults.add(result);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Update ArrayAdapter with new results and call notifyDataSetChanged()
        adapter.clear();
        adapter.addAll(searchResults);
        adapter.notifyDataSetChanged();
    }


    private void showDetails(String selectedItem) {
        // Perform a query to retrieve all fields for the selected item
        Cursor cursor = database.rawQuery("SELECT * FROM algeria_postcodes WHERE field8 = '" + selectedItem + "'", null);

        if (cursor != null && cursor.moveToFirst()) {
            StringBuilder details = new StringBuilder();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String columnName = cursor.getColumnName(i);
                String value = cursor.getString(i);
                details.append(columnName).append(": ").append(value).append("\n");
            }
            cursor.close();

            // Show details in a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Details")
                    .setMessage(details.toString())
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }
}
