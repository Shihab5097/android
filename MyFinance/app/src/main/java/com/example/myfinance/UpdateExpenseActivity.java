package com.example.myfinance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfinance.dao.CategoryDAO;
import com.example.myfinance.dao.ExpenseDAO;
import com.example.myfinance.model.Category;
import com.example.myfinance.model.Expense;

import java.util.Calendar;
import java.util.List;

public class UpdateExpenseActivity extends AppCompatActivity {
    private TableLayout table;
    private ExpenseDAO expDAO;
    private CategoryDAO catDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_expense);

        table  = findViewById(R.id.tableUpdate);
        expDAO = new ExpenseDAO(this);
        catDAO = new CategoryDAO(this);

        refreshTable();
    }

    private void refreshTable() {
        // header row রেখে বাকিটা ক্লিয়ার
        int count = table.getChildCount();
        if (count > 1) {
            table.removeViews(1, count - 1);
        }

        List<Expense> all = expDAO.getAll();
        for (Expense e : all) {
            TableRow row = new TableRow(this);

            // Date
            TextView tvDate = new TextView(this);
            tvDate.setText(e.getDate());
            tvDate.setLayoutParams(new TableRow.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            tvDate.setPadding(8,16,8,16);
            row.addView(tvDate);

            // Category Name
            Category c = catDAO.getById(e.getCategoryId());
            TextView tvCat = new TextView(this);
            tvCat.setText(c != null ? c.getName() : "");
            tvCat.setLayoutParams(new TableRow.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            tvCat.setPadding(8,16,8,16);
            row.addView(tvCat);

            // Amount
            TextView tvAmt = new TextView(this);
            tvAmt.setText(String.format("%.2f", e.getAmount()));
            tvAmt.setLayoutParams(new TableRow.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            tvAmt.setGravity(android.view.Gravity.END);
            tvAmt.setPadding(8,16,8,16);
            row.addView(tvAmt);

            // ক্লিক করলে Edit/Delete ডায়ালগ
            row.setOnClickListener(v -> showEditDeleteDialog(e));

            table.addView(row);
        }
    }

    private void showEditDeleteDialog(Expense e) {
        String[] opts = {"Edit", "Delete"};
        new AlertDialog.Builder(this)
                .setTitle("Expense ID: " + e.getId())
                .setItems(opts, (dialog, which) -> {
                    if (which == 0) {
                        showEditDialog(e);
                    } else {
                        expDAO.deleteExpense(e.getId());
                        refreshTable();
                    }
                })
                .show();
    }

    private void showEditDialog(Expense e) {
        // dialog_edit_expense.xml ইনফ্লেট
        View dlgView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_edit_expense, null, false);

        EditText etAmt     = dlgView.findViewById(R.id.etAmt);
        Spinner spMonth    = dlgView.findViewById(R.id.spMonth);
        Spinner spCategory = dlgView.findViewById(R.id.spCategory);

        // مقدار পূরণ
        etAmt.setText(String.valueOf(e.getAmount()));

        // Month spinner
        ArrayAdapter<CharSequence> mAd = ArrayAdapter.createFromResource(
                this, R.array.months, android.R.layout.simple_spinner_item);
        mAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(mAd);
        // আগের মান সিলেক্ট
        int monthPos = mAd.getPosition(e.getMonth());
        spMonth.setSelection(monthPos);

        // Category spinner
        List<Category> cats = catDAO.getAll();
        ArrayAdapter<Category> cAd = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, cats);
        cAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(cAd);
        // আগের ক্যাটেগরি সিলেক্ট
        for (int i = 0; i < cats.size(); i++) {
            if (cats.get(i).getId() == e.getCategoryId()) {
                spCategory.setSelection(i);
                break;
            }
        }

        // AlertDialog_Show
        new AlertDialog.Builder(this)
                .setTitle("Edit Expense")
                .setView(dlgView)
                .setPositiveButton("OK", (d, id) -> {
                    // আপডেট মান সংগ্রহ
                    String selMonth = spMonth.getSelectedItem().toString();
                    Category selCat = (Category) spCategory.getSelectedItem();
                    double amt = Double.parseDouble(etAmt.getText().toString().trim());

                    e.setMonth(selMonth);
                    e.setCategoryId(selCat.getId());
                    e.setAmount(amt);
                    expDAO.updateExpense(e);

                    refreshTable();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
