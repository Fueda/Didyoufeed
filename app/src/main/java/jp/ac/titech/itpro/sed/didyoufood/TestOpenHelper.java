package jp.ac.titech.itpro.sed.didyoufood;
import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class TestOpenHelper extends SQLiteOpenHelper {

    // データーベースのバージョン
    public static final int DATABASE_VERSION = 1;
    // データーベース名
    public static final String DATABASE_NAME = "TestDB.db";
    private static final String TABLE_NAME = "testdb";
    private static final String _ID = "_id";
    private static final String COLUMN_NAME_TITLE = "amount";
    private static final String COLUMN_NAME_SUBTITLE1 = "hour";
    private static final String COLUMN_NAME_SUBTITLE2 = "minute";
    private static final String COLUMN_NAME_SUBTITLE3 = "year";
    private static final String COLUMN_NAME_SUBTITLE4 = "month";
    private static final String COLUMN_NAME_SUBTITLE5 = "day";
    private static final String COLUMN_NAME_SUBTITLE6 = "person";
    private static final String COLUMN_NAME_SUBTITLE7 = "pet";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_SUBTITLE1 + " INTEGER," +
                    COLUMN_NAME_SUBTITLE2 + " INTEGER," +
                    COLUMN_NAME_SUBTITLE3 + " INTEGER," +
                    COLUMN_NAME_SUBTITLE4 + " INTEGER," +
                    COLUMN_NAME_SUBTITLE5 + " INTEGER," +
                    COLUMN_NAME_SUBTITLE6 + " TEXT," +
                    COLUMN_NAME_SUBTITLE7 + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    TestOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // テーブル作成
        db.execSQL(
                SQL_CREATE_ENTRIES
        );
        Log.d("debug", "onCreate(SQLiteDatabase db)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        // アップデートの判別、古いバージョンは削除して新規作成
        db.execSQL(
                SQL_DELETE_ENTRIES
        );
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db,
                            int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}