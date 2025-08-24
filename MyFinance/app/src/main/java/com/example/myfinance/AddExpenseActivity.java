package com.example.myfinance;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myfinance.dao.CategoryDAO;
import com.example.myfinance.dao.ExpenseDAO;
import com.example.myfinance.model.Category;
import com.example.myfinance.model.Expense;
import java.util.Calendar;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {
    private Spinner    spMonth, spCategory;
    private EditText   etDate, etAmount;
    private Button     btnAdd;
    private TableLayout table;
    private ExpenseDAO expDAO;
    private CategoryDAO catDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        spMonth    = findViewById(R.id.spMonth);
        etDate     = findViewById(R.id.etDate);
        spCategory = findViewById(R.id.spCategory);
        etAmount   = findViewById(R.id.etAmount);
        btnAdd     = findViewById(R.id.btnAdd);
        table      = findViewById(R.id.tableExpenses);

        expDAO = new ExpenseDAO(this);
        catDAO = new CategoryDAO(this);

        setupDatePicker();
        loadCategorySpinner();
        refreshTable();  // প্রথমে খালি অথবা পূর্বের ডেটা

        btnAdd.setOnClickListener(v -> {
            String month = spMonth.getSelectedItem().toString();
            String date  = etDate.getText().toString().trim();
            String amtS  = etAmount.getText().toString().trim();
            Category cat = (Category) spCategory.getSelectedItem();

            if (date.isEmpty()) {
                Toast.makeText(this, "Please choose a date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (amtS.isEmpty()) {
                Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
                return;
            }

            Expense e = new Expense();
            e.setMonth(month);
            e.setDate(date);
            e.setCategoryId(cat.getId());
            e.setAmount(Double.parseDouble(amtS));
            expDAO.addExpense(e);

            etDate.setText("");
            etAmount.setText("");
            refreshTable();
        });
    }

    private void setupDatePicker() {
        etDate.setFocusable(false);
        etDate.setClickable(true);
        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (dp, y, m, d) -> {
                        String s = String.format("%04d-%02d-%02d", y, m+1, d);
                        etDate.setText(s);
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    private void loadCategorySpinner() {
        List<Category> list = catDAO.getAll();
        ArrayAdapter<Category> ad = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                list
        );
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(ad);
    }

    private void refreshTable() {
        // existing rows মুছে ফেলো (header row রেখে)
        int childCount = table.getChildCount();
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }

        // DB থেকে আনা সব exp
        List<Expense> all = expDAO.getAll();  // অথবা getByMonth(spMonth.getSelectedItem()...)
        for (Expense e : all) {
            TableRow row = new TableRow(this);

            TextView tvDate = new TextView(this);
            tvDate.setText(e.getDate());
            tvDate.setLayoutParams(new TableRow.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            tvDate.setPadding(8,16,8,16);
            row.addView(tvDate);

            Category c = catDAO.getById(e.getCategoryId());
            TextView tvCat = new TextView(this);
            tvCat.setText(c!=null ? c.getName() : "");
            tvCat.setLayoutParams(new TableRow.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            tvCat.setPadding(8,16,8,16);
            row.addView(tvCat);

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
