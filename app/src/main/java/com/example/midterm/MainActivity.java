package com.example.midterm;
import android.app.Activity;
import android.os.Bundle;
import android.database.Cursor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    EditText etName, etId, etAge;
    TextView tvResult;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etId = findViewById(R.id.etId);
        etAge = findViewById(R.id.etAge);
        tvResult = findViewById(R.id.tvResult);

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnView = findViewById(R.id.btnView);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);

        dbHelper = new DBHelper(this);

        btnAdd.setOnClickListener(v -> {
            if (!validInput()) return;

            boolean success = dbHelper.addStudent(
                    etName.getText().toString().trim(),
                    etId.getText().toString().trim(),
                    Integer.parseInt(etAge.getText().toString())
            );

            message(success ? "Student added" :
                    "Failed! Student ID may already exist");
        });

        btnView.setOnClickListener(v -> {
            Cursor cursor = dbHelper.viewStudents();
            StringBuilder result = new StringBuilder();

            try {
                if (cursor.moveToFirst()) {
                    do {
                        result.append("Name: ")
                                .append(cursor.getString(1))
                                .append("\nID: ")
                                .append(cursor.getString(0))
                                .append("\nAge: ")
                                .append(cursor.getInt(2))
                                .append("\n\n");
                    } while (cursor.moveToNext());
                } else {
                    result.append("No student records found");
                }
            } finally {
                cursor.close();
            }

            tvResult.setText(result.toString());
        });

        btnEdit.setOnClickListener(v -> {
            if (!validInput()) return;

            boolean success = dbHelper.editStudent(
                    etName.getText().toString().trim(),
                    etId.getText().toString().trim(),
                    Integer.parseInt(etAge.getText().toString())
            );

            message(success ? "Student updated" :
                    "Student ID not found");
        });

        btnDelete.setOnClickListener(v -> {
            String id = etId.getText().toString().trim();

            if (id.isEmpty()) {
                etId.setError("Enter Student ID");
                return;
            }

            boolean success = dbHelper.deleteStudent(id);

            message(success ? "Student deleted" :
                    "Student ID not found");
        });
    }

    private boolean validInput() {
        String name = etName.getText().toString().trim();
        String id = etId.getText().toString().trim();
        String ageText = etAge.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Enter student name");
            return false;
        }

        if (id.isEmpty()) {
            etId.setError("Enter student ID");
            return false;
        }

        if (ageText.isEmpty()) {
            etAge.setError("Enter student age");
            return false;
        }

        int age = Integer.parseInt(ageText);

        if (age < 17 || age > 50) {
            etAge.setError("Age must be 17-50");
            return false;
        }

        return true;
    }

    private void message(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
