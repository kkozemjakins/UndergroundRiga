package com.undergroundriga

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import android.database.Cursor

const val DATABASE_NAME = "HiddenRiga"
const val TABLE_NAME = "Users"
const val COL_USERNAME = "username"
const val COL_PASSWORD = "password"
const val COL_ID = "id"

const val TABLE_NAME_MAPS = "MapsPlaces"
const val COL_PLACESID = "PlacesId"
const val COL_PLACENAME = "PlaceName"
const val COL_DESCRIPTION = "Description"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " VARCHAR(12), " +
                COL_PASSWORD + " VARCHAR(15));"

        val createTablePlaces = "CREATE TABLE " + TABLE_NAME_MAPS + "(" +
                COL_PLACESID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PLACENAME + " TEXT, " +
                COL_DESCRIPTION + " TEXT;"


        db?.execSQL(createTable)
        db?.execSQL(createTablePlaces)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(user: User){

        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_USERNAME,user.username)
        cv.put(COL_PASSWORD,user.password)

        var result = db.insert(TABLE_NAME,null, cv)

        if(result == -1.toLong()){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()}


    }

    fun insertDataPlaces(places: Places){

        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_PLACENAME,places.PlaceName)
        cv.put(COL_DESCRIPTION,places.Description)

        var result = db.insert(TABLE_NAME_MAPS,null, cv)

        if(result == -1.toLong()){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()}


    }

    fun readDataMapsPlaces() : MutableList<Places>{
        var list : MutableList<Places> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME_MAPS
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()){
            do {
                var places = Places()
                places.PlacesId = result.getString(result.getColumnIndex(COL_PLACESID)).toInt()
                places.PlaceName = result.getString(result.getColumnIndex(COL_PLACENAME))
                places.Description = result.getString(result.getColumnIndex(COL_DESCRIPTION))
                list.add(places)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list

    }

    fun readDataUsers() : MutableList<User>{
        var list : MutableList<User> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()){
            do {
                var user = User()
                user.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                user.username = result.getString(result.getColumnIndex(COL_USERNAME))
                user.password = result.getString(result.getColumnIndex(COL_PASSWORD))
                list.add(user)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list

    }





}