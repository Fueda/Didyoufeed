package jp.ac.titech.itpro.sed.didyoufood;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.RadioButton;
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
import java.util.Calendar;
import java.text.ParseException;
import android.widget.RadioGroup;
import android.app.Activity;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends AppCompatActivity {

    CalendarView mCalendarView;
    TextView textView;

    private TextView textView2;
    private EditText editTextKey, editTextValue1, editTextValue2, editTextPerson;
    private TestOpenHelper helper;
    private SQLiteDatabase db;

    public int yearDB;
    public int monthDB;
    public int dayDB;
    public String personDB = "";

    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //現在時刻のCalendarオブジェクトを取得する
        //Calendar cl = Calendar.getInstance();
        final Calendar[] c1 = {Calendar.getInstance()};

        //ラジオボタン
        radioGroup = (RadioGroup) findViewById(R.id.RadioGroupSubject);
        /*
        public void checkButton(View v){
            int radioId = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(radioId);
        }
        */

        /*
        // ラジオグループのオブジェクトを取得
        RadioGroup rg = (RadioGroup)findViewById(R.id.radiogroup);

        // ラジオグループのチェック状態変更イベントを登録
        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            // チェック状態変更時に呼び出されるメソッド
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // チェック状態時の処理を記述
                // チェックされたラジオボタンオブジェクトを取得
                RadioButton radioButton = (RadioButton)findViewById(checkedId);
            }
        });
        */

        editTextPerson = findViewById(R.id.edit_text_person);

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    // 選択されているラジオボタンの取得
                    RadioButton radioButton = (RadioButton) findViewById(checkedId);
                    // ラジオボタンのテキストを取得
                    personDB = radioButton.getText().toString();
                    editTextPerson.setText(personDB, TextView.BufferType.NORMAL);
                } else {
                    // 何も選択されていない場合の処理
                }
            }
        });
        /*
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            final CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        Toast.makeText(getApplication(), "チェックが付いた", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplication(), "チェックが外れた", Toast.LENGTH_LONG).show();
                    }
                }
            });
            */
        /*
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    // 選択されているラジオボタンの取得
                    RadioButton radioButton = (RadioButton) findViewById(checkedId);

                    // ラジオボタンのテキストを取得
                    String text = radioButton.getText().toString();

                    Log.v("checked", text);
                } else {
                    // 何も選択されていない場合の処理
                }
            }
        });
        */

        //今日の日付をTextViewで表示する
        final String date = new SimpleDateFormat("y年M月d日", Locale.getDefault()).format(new Date());
        final String year  = new SimpleDateFormat("y", Locale.getDefault()).format(new Date());
        final String month = new SimpleDateFormat("M", Locale.getDefault()).format(new Date());
        final String day   = new SimpleDateFormat("d", Locale.getDefault()).format(new Date());
        yearDB = Integer.parseInt(year);
        monthDB = Integer.parseInt(month);
        dayDB = Integer.parseInt(day);
        display(date);

        final CalendarView mCalendarView = (CalendarView)findViewById(R.id.calendar);
        //カレンダーで指定した日の日付をTextViewで表示する
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //String date = new SimpleDateFormat("yyyy年MM月dayOfMonth日", Locale.getDefault()).format(new Date());
                String date = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                yearDB = year;
                monthDB = month + 1;
                dayDB = dayOfMonth;
                c1[0].set(Calendar.YEAR, year);
                c1[0].set(Calendar.MONTH, month);
                c1[0].set(Calendar.DATE, dayOfMonth);
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

                if(key.length()<1 || value1.length()<1 || value2.length()<1 ||  personDB.length()<1){}else {
                    editTextKey.getEditableText().clear();
                    editTextValue1.getEditableText().clear();
                    editTextValue2.getEditableText().clear();
                    insertData(db, key, Integer.valueOf(value1), Integer.valueOf(value2), yearDB, monthDB, dayDB, personDB);
                    readData(yearDB,monthDB,dayDB);
                }
            }
        });

        //「現在時刻」ボタンの機能
        Button ButtonDefault = findViewById(R.id.button_default);
        ButtonDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = new SimpleDateFormat("y年M月d日", Locale.getDefault()).format(new Date());
                final String hour  = new SimpleDateFormat("H", Locale.getDefault()).format(new Date());
                final String minute  = new SimpleDateFormat("m", Locale.getDefault()).format(new Date());
                editTextValue1.setText(hour, TextView.BufferType.NORMAL);
                editTextValue2.setText(minute, TextView.BufferType.NORMAL);
                editTextKey.setText("普通", TextView.BufferType.NORMAL);
            }
        });
        //「前日」ボタンの機能
        Button ButtonYesterday = findViewById(R.id.button_yesterday);
        ButtonYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1[0].add(Calendar.DATE, -1);
                String date = c1[0].get(Calendar.YEAR) + "年" + String.valueOf(c1[0].get(Calendar.MONTH)+1) + "月" + c1[0].get(Calendar.DATE) + "日";
                display(date);
                readData(c1[0].get(Calendar.YEAR), c1[0].get(Calendar.MONTH)+1, c1[0].get(Calendar.DATE));
            }
        });
        //「翌日」ボタンの機能
        Button ButtonTomorrow = findViewById(R.id.button_tomorrow);
        ButtonTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1[0].add(Calendar.DATE, 1);
                String date = c1[0].get(Calendar.YEAR) + "年" + String.valueOf(c1[0].get(Calendar.MONTH)+1) + "月" + c1[0].get(Calendar.DATE) + "日";
                display(date);
                readData(c1[0].get(Calendar.YEAR), c1[0].get(Calendar.MONTH)+1, c1[0].get(Calendar.DATE));
            }
        });
        //「今日にジャンプ」ボタンの機能
        Button ButtonToday = findViewById(R.id.button_today);
        ButtonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1[0] = Calendar.getInstance();
                String date = c1[0].get(Calendar.YEAR) + "年" + String.valueOf(c1[0].get(Calendar.MONTH)+1) + "月" + c1[0].get(Calendar.DATE) + "日";
                display(date);
                readData(c1[0].get(Calendar.YEAR), c1[0].get(Calendar.MONTH)+1, c1[0].get(Calendar.DATE));
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
                new String[] { "amount", "hour", "minute", "year" , "month" , "day" , "person" , "pet"},
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
            sbuilder.append(String.format("%02d",cursor.getInt(1)));
            sbuilder.append("時");
            sbuilder.append(String.format("%02d",cursor.getInt(2)));
            sbuilder.append("分");
            sbuilder.append(":  ");
            sbuilder.append(String.format("%5s",cursor.getString(0)));
            sbuilder.append("  ");
            sbuilder.append(String.format("%s",cursor.getString(6)));
            sbuilder.append("\n");
            cursor.moveToNext();
        }

        // 忘れずに！
        cursor.close();

        Log.d("debug","**********"+sbuilder.toString());
        textView.setText(sbuilder.toString());
    }

    private void insertData(SQLiteDatabase db, String amount, int hour, int minute, int year, int month, int day, String person){

        ContentValues values = new ContentValues();
        values.put("amount", amount);
        values.put("hour", hour);
        values.put("minute", minute);
        values.put("year", year);
        values.put("month", month);
        values.put("day", day);
        values.put("person", person);
        values.put("pet", "ペットA");

        db.insert("testdb", null, values);
    }


}
