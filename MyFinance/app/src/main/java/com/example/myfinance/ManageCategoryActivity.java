package com.example.myfinance;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfinance.dao.CategoryDAO;
import com.example.myfinance.model.Category;

import java.util.List;

public class ManageCategoryActivity extends AppCompatActivity {
    private EditText etCatName;
    private Button btnAddCat;
    private ListView lvCats;
    private CategoryDAO catDAO;
    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        etCatName = findViewById(R.id.etCatName);
        btnAddCat = findViewById(R.id.btnAddCat);
        lvCats    = findViewById(R.id.lvCats);
        catDAO    = new CategoryDAO(this);

        loadCategories();

        btnAddCat.setOnClickListener(v -> {
            String name = etCatName.getText().toString().trim();
            if (!name.isEmpty()) {
                // String → Category 객체로 래핑
                Category newCat = new Category();
                newCat.setName(name);
                catDAO.addCategory(newCat);
                etCatName.setText("");
                loadCategories();
            } else {
                Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show();
            }
        });

        lvCats.setOnItemClickListener((parent, view, pos, id) -> {
            Category cat = categories.get(pos);
            showEditDeleteDialog(cat);
        });
    }

    private void loadCategories() {
        categories = catDAO.getAll();
        String[] names = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            names[i] = categories.get(i).getName();
        }
        lvCats.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                names
        ));
    }

    private void showEditDeleteDialog(Category cat) {
        String[] opts = {"Edit", "Delete"};
        new AlertDialog.Builder(this)
                .setTitle(cat.getName())
                .setItems(opts, (dialog, which) -> {
                    if (which == 0) {
                        editCategory(cat);
                    } else {
                        catDAO.deleteCategory(cat.getId());
                        loadCategories();
                    }
                })
                .show();
    }

    private void editCategory(Category cat) {
        final EditText input = new EditText(this);
        input.setText(cat.getName());
        new AlertDialog.Builder(this)
                .setTitle("Edit Category")
                .setView(input)
                .setPositiveButton("OK", (d,i) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        cat.setName(newName);
                        catDAO.updateCategory(cat);
                        loadCategories();
                    } else {
                        Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
