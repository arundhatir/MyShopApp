package com.android.myshopapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.myshopapp.model.ProductsData;

import java.util.LinkedList;
import java.util.List;


public class ProductsDataDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "products.db";
    public static final String FILE_DIR = "myshop";
    private static final int DATABASE_VERSION = 1 ;
    public static final String TABLE_NAME = "Products";
    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_PRODUCT_ID = "prodId";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_STATUS = "status";


    public ProductsDataDBHelper(Context context) {
        super(context,  DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_PRODUCT_ID + " INTEGER NOT NULL, " +
                COLUMN_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                COLUMN_PRODUCT_STATUS + " TEXT NOT NULL);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
    /**create record**/
    public void saveProduct(ProductsData productsData) {

        if(!hasObject(String.valueOf(productsData.getId()))) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_PRODUCT_ID, productsData.getId());
            values.put(COLUMN_USER_ID, productsData.getUserid());
            values.put(COLUMN_PRODUCT_NAME, productsData.getTitle());
            values.put(COLUMN_PRODUCT_STATUS, productsData.isCompleted());

            // insert
            db.insert(TABLE_NAME, null, values);
            db.close();
        }
    }

    /**update record**/
    public void updateProduct(String id, boolean status) {

        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_PRODUCT_STATUS,status);

            // update
            db.update(TABLE_NAME,  values, COLUMN_PRODUCT_ID+"= ?",new String[]{id});
            db.close();

    }

    /**Query records, give options to filter results**/
    public List<ProductsData> productsList(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_NAME;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_NAME + " ORDER BY "+ filter;
        }

        List<ProductsData> productsLinkedList = new LinkedList<ProductsData>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ProductsData prodData = null;

        if (cursor.moveToFirst()) {
            do {

                long dbid = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                int prodid = cursor.getInt(cursor.getColumnIndex(String.valueOf(COLUMN_PRODUCT_ID)));
                int userid = cursor.getInt(cursor.getColumnIndex(String.valueOf(COLUMN_USER_ID)));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
                String status = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_STATUS));
                boolean completed ;
                if(status.equals("1"))
                {
                    completed = true;
                }
                else completed = false;
                prodData = new ProductsData(prodid,userid,title,completed);
                prodData.setDbid(dbid);
                productsLinkedList.add(prodData);
            } while (cursor.moveToNext());
        }


        return productsLinkedList;
    }
    public boolean hasObject(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PRODUCT_ID + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {id});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;
            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }


        }

        cursor.close();
        db.close();
        return hasObject;
    }

}