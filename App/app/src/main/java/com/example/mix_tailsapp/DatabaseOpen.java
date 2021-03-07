package com.example.mix_tailsapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.mix_tailsapp.Database.Cocktails;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;
/**
 * Create a database class for the app
 * authors: Annie, Vasily, Miguel
 * @version 1: Getting the database opened (Miguel)
 * @version 2: Adding onUpgrade method (Miguel)
 * @version 2.1: Adding (3) functions to get cocktail name from database (Annie)
 */
public class DatabaseOpen extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "Drinks.db";
    private static final int DATABASE_VERSION = 2;


    public  DatabaseOpen(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    /*
    Writing functions for searchBar in DrinkRecommendationPage Activity (Annie)
     */
    // Create a function to get all cocktails
    public List<Cocktails> getCocktails() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] selectSql = {"id", "name", "spirit", "taste", "size", "strength"};
        String tableName = "cocktails";
        queryBuilder.setTables(tableName);
        Cursor cursor = queryBuilder.query(db, selectSql, null, null, null, null, null);
        List<Cocktails> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Cocktails cocktails = new Cocktails();
                cocktails.setId(cursor.getInt(cursor.getColumnIndex("id")));
                cocktails.setName(cursor.getString(cursor.getColumnIndex("name")));
                cocktails.setSpirit(cursor.getString(cursor.getColumnIndex("spirit")));
                cocktails.setTaste(cursor.getString(cursor.getColumnIndex("taste")));
                cocktails.setSize(cursor.getString(cursor.getColumnIndex("size")));
                cocktails.setStrength(cursor.getString(cursor.getColumnIndex("strength")));
                //cocktails.setIngredients(cursor.getString(cursor.getColumnIndex("ingredients")));
                result.add(cocktails);
            }  while (cursor.moveToNext())   ;
        }
        return result;
    }

    //Create a function to get all cocktails' names
    public List<String> getDrinkName() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] selectSql = {"name"};
        String tableName = "cocktails";

        queryBuilder.setTables(tableName);
        Cursor cursor = queryBuilder.query(db, selectSql, null, null, null, null, null);
        List<String> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(cursor.getColumnIndex("name")));
            }  while (cursor.moveToNext())   ;
        }
        return result;
    }

    //Create a function to get cocktail by name
    public List<Cocktails> getDrinkByName(String name) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] selectSql = {"id", "name", "spirit", "taste", "size", "strength"};
        String tableName = "cocktails";
        queryBuilder.setTables(tableName);
        /*
        get extract name:
        Cursor cursor = queryBuilder.query(db, selectSql, "name = ?", new String[] {name}, null, null, null);
         */

        //  SELECT * FROM Cocktails WHERE name LIKE %pattern%" ==
        Cursor cursor = queryBuilder.query(db, selectSql, "name LIKE ?", new String[] {"%"+name+"%"}, null, null, null);
        List<Cocktails> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Cocktails cocktails = new Cocktails();
                cocktails.setId(cursor.getInt(cursor.getColumnIndex("id")));
                cocktails.setName(cursor.getString(cursor.getColumnIndex("name")));
                cocktails.setSpirit(cursor.getString(cursor.getColumnIndex("spirit")));
                cocktails.setTaste(cursor.getString(cursor.getColumnIndex("taste")));
                cocktails.setSize(cursor.getString(cursor.getColumnIndex("size")));
                cocktails.setStrength(cursor.getString(cursor.getColumnIndex("strength")));
                //cocktails.setIngredients(cursor.getString(cursor.getColumnIndex("ingredients")));
                result.add(cocktails);
            }  while (cursor.moveToNext())   ;
        }
        return result;
    }
}
