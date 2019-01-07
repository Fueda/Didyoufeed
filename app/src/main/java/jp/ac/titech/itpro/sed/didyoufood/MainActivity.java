package jp.ac.titech.itpro.sed.didyoufood;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {

    CalendarView mCalendarView;
    TextView textView;

    private TextView textView2;
    private EditText editTextKey, editTextValue1, editTextValue2;
    private TestOpenHelper helper;
    private SQLiteDatabase db;

    public int yearDB;
    public int monthDB;
    public int dayDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //今日の日付をTextViewで表示する
        String date = new SimpleDateFormat("y年M月d日", Locale.getDefault()).format(new Date());
        final String year  = new SimpleDateFormat("y", Locale.getDefault()).format(new Date());
        final String month = new SimpleDateFormat("M", Locale.getDefault()).format(new Date());
        final String day   = new SimpleDateFormat("d", Locale.getDefault()).format(new Date());
        yearDB = Integer.parseInt(year);
        monthDB = Integer.parseInt(month);
        dayDB = Integer.parseInt(day);
        display(date);

        CalendarView mCalendarView = (CalendarView)findViewById(R.id.calendar);
        //カレンダーで指定した日の日付をTextViewで表示する
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //String date = new SimpleDateFormat("yyyy年MM月dayOfMonth日", Locale.getDefault()).format(new Date());
                String date = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                yearDB = year;
                monthDB = month + 1;
                dayDB = dayOfMonth;
                display(date);
                readData(yearDB,monthDB,dayDB);
            }
        });


        //SQLite
        editTextKey = findViewById(R.id.edit_text_key);
        editTextValue1 = findViewById(R.id.edit_text_value1);
        editTextValue2 = findViewById(R.id.edit_text_value2);

        textView = findViewById(R.id.text_view);
        //データベースの中身を表示する
        readData(yearDB,monthDB,dayDB);

        Button insertButton = findViewById(R.id.button_insert);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(helper == null){
                    helper = new TestOpenHelper(getApplicationContext());
                }

                if(db == null){
                    db = helper.getWritableDatabase();
                }

                String key = editTextKey.getText().toString();
                //value1は時．value2は分．
                String value1 = editTextValue1.getText().toString();
                String value2 = editTextValue2.getText().toString();

                if(key.length()<1 || value1.length()<1 || value2.length()<1){}else {
                    editTextKey.getEditableText().clear();
                    editTextValue1.getEditableText().clear();
                    editTextValue2.getEditableText().clear();
                    insertData(db, key, Integer.valueOf(value1), Integer.valueOf(value2), yearDB, monthDB, dayDB);
                    readData(yearDB,monthDB,dayDB);
                }
            }
        });

        /* 「READ」ボタンの機能
        Button readButton = findViewById(R.id.button_read);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData(yearDB,monthDB,dayDB);
            }
        });
        */

    }
    //テキストを表示するためのメソッドdisplay()
    public void display(String text){
        TextView textView = (TextView)findViewById(R.id.textDate);
        textView.setText("" + text + "の記録");
    }

    //SQLite
    private void readData(int yearDB, int monthDB, int dayDB){
        if(helper == null){
            helper = new TestOpenHelper(getApplicationContext());
        }

        if(db == null){
            db = helper.getReadableDatabase();
        }
        Log.d("debug","**********Cursor");

        Cursor cursor = db.query(
                "testdb",
                new String[] { "amount", "hour", "minute", "year" , "month" , "day" },
                "hour < 24 AND minute < 60 AND year = " + yearDB + " AND month = " + monthDB + " AND day = " + dayDB ,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        StringBuilder sbuilder = new StringBuilder();

        for (int i = 0; i < cursor.getCount(); i++) {
            sbuilder.append(cursor.getInt(1));
            sbuilder.append("時");
            sbuilder.append(cursor.getInt(2));
            sbuilder.append("分");
            sbuilder.append(":  ");
            sbuilder.append(cursor.getString(0));
            sbuilder.append("\n");
            cursor.moveToNext();
        }

        // 忘れずに！
        cursor.close();

        Log.d("debug","**********"+sbuilder.toString());
        textView.setText(sbuilder.toString());
    }

    private void insertData(SQLiteDatabase db, String amount, int hour, int minute, int year, int month, int day){

        ContentValues values = new ContentValues();
        values.put("amount", amount);
        values.put("hour", hour);
        values.put("minute", minute);
        values.put("year", year);
        values.put("month", month);
        values.put("day", day);
        values.put("person", "飼い主A");
        values.put("pet", "ペットA");

        db.insert("testdb", null, values);
    }

}
