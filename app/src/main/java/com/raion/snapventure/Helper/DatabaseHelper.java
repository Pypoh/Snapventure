package com.raion.snapventure.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.raion.snapventure.Data.DataUserStage;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "stages.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DataUserStage.CREATE_TABLE);
//        insertDataStage(0, "Easy 1", "Room", 0,0);
//        insertDataStage(1, "Easy 2", "Room", 0,0);
//        insertDataStage(2, "Easy 3", "Room", 0,0);
//        insertDataStage(3, "Medium 1", "Room", 0,0);
//        insertDataStage(4, "Medium 2", "Room", 0,0);
//        insertDataStage(5, "Medium 3", "Room", 0,0);
//        insertDataStage(6, "Hard 1", "Room", 0,0);
//        insertDataStage(7, "Hard 2", "Room", 0,0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataUserStage.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insertDataStage(int id, String difficulty, String stage, int status, int point_status, String riddleEN, String riddleID, String answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataUserStage.COLUMN_ID, id);
        values.put(DataUserStage.COLUMN_DIFF, difficulty);
        values.put(DataUserStage.COLUMN_STAGE, stage);
        values.put(DataUserStage.COLUMN_STATUS, status);
        values.put(DataUserStage.COLUMN_POINT_STATUS, point_status);
        values.put(DataUserStage.COLUMN_RIDDLE_EN, riddleEN);
        values.put(DataUserStage.COLUMN_RIDDLE_ID, riddleID);
        values.put(DataUserStage.COLUMN_ANSWER, answer);

        long id_insert = db.insert(DataUserStage.TABLE_NAME, null, values);
        return id_insert;
    }

    public int updateDataStageStatus(DataUserStage dataUserStage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataUserStage.COLUMN_STATUS, dataUserStage.getStage());
        return db.update(DataUserStage.TABLE_NAME, values, DataUserStage.COLUMN_ID + "=?",
                new String[]{String.valueOf(dataUserStage.getId())});

    }

    public int updateDataStagePoint(DataUserStage dataUserStage, int point) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataUserStage.COLUMN_POINT_STATUS, point);
        return db.update(DataUserStage.TABLE_NAME, values, DataUserStage.COLUMN_ID + "=?",
                new String[]{String.valueOf(dataUserStage.getId())});
    }

    public DataUserStage getStageDataByStage(String stage) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DataUserStage.TABLE_NAME, new String[]{DataUserStage.COLUMN_ID,
                        DataUserStage.COLUMN_DIFF, DataUserStage.COLUMN_STAGE, DataUserStage.COLUMN_STATUS, DataUserStage.COLUMN_POINT_STATUS},
                DataUserStage.COLUMN_STAGE + "=?", new String[]{stage}, null, null, null, null
        );
        if (cursor != null) {
            cursor.moveToFirst();
        }

        assert cursor != null;
        DataUserStage dataUserStage = new DataUserStage(
                cursor.getInt(cursor.getColumnIndex(DataUserStage.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(DataUserStage.COLUMN_DIFF)),
                cursor.getString(cursor.getColumnIndex(DataUserStage.COLUMN_STAGE)),
                cursor.getString(cursor.getColumnIndex(DataUserStage.COLUMN_RIDDLE_EN)),
                cursor.getString(cursor.getColumnIndex(DataUserStage.COLUMN_RIDDLE_ID)),
                cursor.getString(cursor.getColumnIndex(DataUserStage.COLUMN_ANSWER)),
                cursor.getInt(cursor.getColumnIndex(DataUserStage.COLUMN_STATUS)),
                cursor.getInt(cursor.getColumnIndex(DataUserStage.COLUMN_POINT_STATUS))
        );
        cursor.close();
        return dataUserStage;
    }

    public List<DataUserStage> getAllData() {
        List<DataUserStage> dataUserStages = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + DataUserStage.TABLE_NAME + " ORDER BY " + DataUserStage.COLUMN_ID + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DataUserStage dataUserStage = new DataUserStage();
                dataUserStage.setId(cursor.getInt(cursor.getColumnIndex(DataUserStage.COLUMN_ID)));
                dataUserStage.setDifficulty(cursor.getString(cursor.getColumnIndex(DataUserStage.COLUMN_DIFF)));
                dataUserStage.setStage(cursor.getString(cursor.getColumnIndex(DataUserStage.COLUMN_STAGE)));
                dataUserStage.setStatus(cursor.getInt(cursor.getColumnIndex(DataUserStage.COLUMN_STATUS)));
                dataUserStage.setPoint_status(cursor.getInt(cursor.getColumnIndex(DataUserStage.COLUMN_POINT_STATUS)));
                dataUserStage.setRiddleEn(cursor.getString(cursor.getColumnIndex(DataUserStage.COLUMN_RIDDLE_EN)));
                dataUserStage.setRiddleId(cursor.getString(cursor.getColumnIndex(DataUserStage.COLUMN_RIDDLE_ID)));
                dataUserStage.setAnswer(cursor.getString(cursor.getColumnIndex(DataUserStage.COLUMN_ANSWER)));
                dataUserStages.add(dataUserStage);
            } while (cursor.moveToNext());
        }
        db.close();
        return dataUserStages;
    }
}
