package com.example.imageapplication.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "test_db.db";
    public final static int DB_VERSION = 1;
    public static SQLiteDatabase db;
    private final Context context;
    private String DB_PATH;

    private final String TABLE_CART = "tbl_cart";
    private final String CART_ID = "id";
    private final String PRODUCT_IMAGE = "product_image";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        SQLiteDatabase db_Read = null;
        if (dbExist) {
        } else {
            db_Read = this.getReadableDatabase();
            db_Read.close();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying db");
            }
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();

    }

    private void copyDataBase() throws IOException {
        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void close() {
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<ArrayList<Object>> getAllData() {
        ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_CART, new String[]{CART_ID, PRODUCT_IMAGE},
                    null, null, null, null, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cursor.getLong(0));
                    dataList.add(cursor.getString(1));

                    dataArrays.add(dataList);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
        return dataArrays;
    }

    public boolean isDataExist(long id) {
        boolean exist = false;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_CART, new String[]{CART_ID}, CART_ID + "=" + id, null, null, null, null);
            if (cursor.getCount() > 0) {
                exist = true;
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
        return exist;
    }


    public void addData(long id, String product_image) {
        ContentValues values = new ContentValues();
        values.put(CART_ID, id);
        values.put(PRODUCT_IMAGE, product_image);
        try {
            db.insert(TABLE_CART, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }


    public void deleteData(long id) {
        try {
            db.delete(TABLE_CART, CART_ID + "=" + id, null);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }


    public void updateData(long id,  String product_image) {
        ContentValues values = new ContentValues();
        values.put(CART_ID, id);
        values.put(PRODUCT_IMAGE, product_image);
        try {
            db.update(TABLE_CART, values, CART_ID + "=" + id, null);
        } catch (Exception e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }

}