package com.example.myfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfinance.dao.CategoryDAO;
import com.example.myfinance.dao.ExpenseDAO;
import com.example.myfinance.model.Category;
import com.example.myfinance.model.Expense;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {
    private Spinner spMonth, spYear;
    private TableLayout tableReport;
    private ExpenseDAO expDAO;
    private CategoryDAO catDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // find views
        spMonth     = findViewById(R.id.spMonth);
        spYear      = findViewById(R.id.spYear);
        tableReport = findViewById(R.id.tableReport);

        // init DAOs
        expDAO = new ExpenseDAO(this);
        catDAO = new CategoryDAO(this);

        setupSpinners();
        setupButtons();
        refreshReport();

        // listen for changes
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int pos, long id) {
                refreshReport();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };
        spMonth.setOnItemSelectedListener(listener);
        spYear.setOnItemSelectedListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // whenever returning here, update table
        refreshReport();
    }

    private void setupSpinners() {
        // month spinner
        String[] months = getResources().getStringArray(R.array.months);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, months
        );
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(mAdapter);
        spMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH));

        // year spinner (current down to current -5)
        List<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = currentYear; y >= currentYear - 5; y--) {
            years.add(String.valueOf(y));
        }
        ArrayAdapter<String> yAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, years
        );
        yAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(yAdapter);
        spYear.setSelection(0);
    }

    private void setupButtons() {
        findViewById(R.id.btnManageCategories).setOnClickListener(v ->
                startActivity(new Intent(this, ManageCategoryActivity.class))
        );
        findViewById(R.id.btnAddExpense).setOnClickListener(v ->
                startActivity(new Intent(this, AddExpenseActivity.class))
        );
        findViewById(R.id.btnCategoryBreakdown).setOnClickListener(v ->
                startActivity(new Intent(this, CategoryBreakdownActivity.class))
        );
        findViewById(R.id.btnUpdateExpense).setOnClickListener(v ->
                startActivity(new Intent(this, UpdateExpenseActivity.class))
        );
    }

    private void refreshReport() {
        String month = spMonth.getSelectedItem().toString();
        int year     = Integer.parseInt(spYear.getSelectedItem().toString());

        // fetch filtered expenses
        List<Expense> all = expDAO.getByMonthAndYear(month, year);

        // sort by date
        Collections.sort(all, Comparator.comparing(Expense::getDate));

        // clear previous rows (keep header)
        if (tableReport.getChildCount() > 1) {
            tableReport.removeViews(1, tableReport.getChildCount() - 1);
        }

        double total = 0;
        // add one row per expense
        for (Expense e : all) {
            TableRow row = new TableRow(this);

            // date column
            TextView tvDate = new TextView(this);
            tvDate.setText(e.getDate());
            tvDate.setLayoutParams(new TableRow.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
            ));
            tvDate.setPadding(8, 16, 8, 16);
            row.addView(tvDate);

            // category column (lookup name)
            Category c = catDAO.getById(e.getCategoryId());
            String catName = (c != null ? c.getName() : "Unknown");
            TextView tvCat = new TextView(this);
            tvCat.setText(catName);
            tvCat.setLayoutParams(new TableRow.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
            ));
            tvCat.setPadding(8, 16, 8, 16);
            row.addView(tvCat);

            // amount column
            TextView tvAmt = new TextView(this);
            tvAmt.setText(String.format(Locale.getDefault(), "%.2f", e.getAmount()));
            tvAmt.setGravity(Gravity.END);
            tvAmt.setLayoutParams(new TableRow.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
            ));
            tvAmt.setPadding(8, 16, 8, 16);
            row.addView(tvAmt);

            tableReport.addView(row);
            total += e.getAmount();
        }

        // add total row
        TableRow sumRow = new TableRow(this);

        TextView tvLabel = new TextView(this);
        tvLabel.setText("Total");
        tvLabel.setLayoutParams(new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f
        ));
        tvLabel.setPadding(8, 16, 8, 16);
        sumRow.addView(tvLabel);

        TextView tvSum = new TextView(this);
        tvSum.setText(String.format(Locale.getDefault(), "%.2f", total));
        tvSum.setGravity(Gravity.END);
        tvSum.setLayoutParams(new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
        ));
        tvSum.setPadding(8, 16, 8, 16);
        sumRow.addView(tvSum);

        tableReport.addView(sumRow);
    }
}
