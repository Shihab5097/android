package com.example.myfinance;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myfinance.dao.CategoryDAO;
import com.example.myfinance.dao.ExpenseDAO;
import com.example.myfinance.model.Category;
import com.example.myfinance.model.Expense;
import java.util.List;

public class CategoryBreakdownActivity extends AppCompatActivity {
    private Spinner spMonth, spCategory;
    private TableLayout table;
    private ExpenseDAO expDAO;
    private CategoryDAO catDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_breakdown);

        spMonth    = findViewById(R.id.spMonth);
        spCategory = findViewById(R.id.spCategory);
        table      = findViewById(R.id.tableBreak);

        expDAO = new ExpenseDAO(this);
        catDAO = new CategoryDAO(this);

        // month spinner already entries via XML
        loadCategorySpinner();
        refreshTable();

        AdapterView.OnItemSelectedListener sel = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, android.view.View v, int pos, long id) {
                refreshTable();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };
        spMonth.setOnItemSelectedListener(sel);
        spCategory.setOnItemSelectedListener(sel);
    }

    private void loadCategorySpinner() {
        List<Category> list = catDAO.getAll();
        ArrayAdapter<Category> ad = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, list);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(ad);
    }

    private void refreshTable() {
        // clear old rows
        if (table.getChildCount()>1) {
            table.removeViews(1, table.getChildCount()-1);
        }

        String month = spMonth.getSelectedItem().toString();
        Category cat = (Category) spCategory.getSelectedItem();
        if (cat==null) return;

        List<Expense> list = expDAO.getByCategoryAndMonth(cat.getId(), month);
        for (Expense e : list) {
            TableRow row = new TableRow(this);

            TextView tvDate = new TextView(this);
            tvDate.setText(e.getDate());
            tvDate.setLayoutParams(new TableRow.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            tvDate.setPadding(8,16,8,16);
            row.addView(tvDate);

            TextView tvAmt = new TextView(this);
            tvAmt.setText(String.format("%.2f", e.getAmount()));
            tvAmt.setGravity(Gravity.END);
            tvAmt.setLayoutParams(new TableRow.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            tvAmt.setPadding(8,16,8,16);
            row.addView(tvAmt);

            table.addView(row);
        }
    }
}
