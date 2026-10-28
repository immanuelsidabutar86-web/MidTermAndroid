package com.example.midterm;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "StudentDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE students (" +
                "student_id TEXT PRIMARY KEY, " +
                "student_name TEXT, " +
                "student_age INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS students");
        onCreate(db);
    }

    public boolean addStudent(String name, String id, int age) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("student_name", name);
        values.put("student_id", id);
        values.put("student_age", age);

        long result = db.insert("students", null, values);
        return result != -1;
    }

    public boolean editStudent(String name, String id, int age) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("student_name", name);
        values.put("student_age", age);

        int result = db.update(
                "students", values, "student_id=?",
                new String[]{id});

        return result > 0;
    }

    public boolean deleteStudent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                "students", "student_id=?",
                new String[]{id});

        return result > 0;
    }

    public Cursor viewStudents() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM students ORDER BY student_name",
                null);
    }
}
