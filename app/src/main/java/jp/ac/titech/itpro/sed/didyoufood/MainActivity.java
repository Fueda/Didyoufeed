package jp.ac.titech.itpro.sed.didyoufood;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    CalendarView mCalendarView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        display(date);
        */

        CalendarView mCalendarView = (CalendarView)findViewById(R.id.calendar);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "-" + "0" + (month + 1) + "-" + year;
                display(date);
            }
        });
    }
    public void display(String text){
        TextView textView = (TextView)findViewById(R.id.text);
        textView.setText("" + text);
    }

}
