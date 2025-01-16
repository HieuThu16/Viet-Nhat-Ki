package com.example.vitnhtk;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView tvSelectedDate;
    private EditText etPositive, etNegative, etProgress, etLearning;
    private Button btnSave, btnSelectDate;
    private DatabaseHelper dbHelper;

    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        etPositive = findViewById(R.id.etPositive);
        etNegative = findViewById(R.id.etNegative);
        etProgress = findViewById(R.id.etProgress);
        etLearning = findViewById(R.id.etLearned);
        btnSave = findViewById(R.id.btnSave);
        btnSelectDate = findViewById(R.id.btnSelectDate);

        dbHelper = new DatabaseHelper(this);

        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        selectedDate = String.format("%02d/%02d/%04d", calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        tvSelectedDate.setText("Ngày: " + selectedDate);

        loadDiary(selectedDate);

        // Thay đổi ngày
        btnSelectDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        tvSelectedDate.setText("Ngày: " + selectedDate);
                        loadDiary(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Lưu nhật ký
        btnSave.setOnClickListener(v -> {
            String positive = etPositive.getText().toString();
            String negative = etNegative.getText().toString();
            String progress = etProgress.getText().toString();
            String learning = etLearning.getText().toString();

            boolean isSaved = dbHelper.saveDiary(selectedDate, positive, negative, progress, learning);

            if (isSaved) {
                Toast.makeText(this, "Nhật ký đã được lưu!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lỗi khi lưu nhật ký.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDiary(String date) {
        Cursor cursor = dbHelper.getDiaryByDate(date);
        if (cursor.moveToFirst()) {
            etPositive.setText(cursor.getString(cursor.getColumnIndex("positive")));
            etNegative.setText(cursor.getString(cursor.getColumnIndex("negative")));
            etProgress.setText(cursor.getString(cursor.getColumnIndex("progress")));
            etLearning.setText(cursor.getString(cursor.getColumnIndex("learning")));
        } else {
            etPositive.setText("");
            etNegative.setText("");
            etProgress.setText("");
            etLearning.setText("");
        }
        cursor.close();
    }
}
