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
const val COL_ROLE = "role"

const val TABLE_NAME_MAPS = "MapsPlaces"
const val COL_PLACESID = "PlacesId"
const val COL_PLACENAME = "PlaceName"
const val COL_DESCRIPTION = "Description"
const val COL_POSX = "PosX"
const val COL_POSY = "PosY"
const val COL_TAG = "Tag"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {


    override fun onCreate(db: SQLiteDatabase?) {
        val createTableUsers = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " VARCHAR(12), " +
                COL_PASSWORD + " VARCHAR(15), " +
                COL_ROLE + " VARCHAR(1));"

        val createTablePlaces = "CREATE TABLE " + TABLE_NAME_MAPS + "(" +
                COL_PLACESID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PLACENAME + " TEXT, " +
                COL_DESCRIPTION  + " TEXT, " +
                COL_TAG  + " TEXT, " +
                COL_POSX  + " TEXT, " +
                COL_POSY + " TEXT);"


        db?.execSQL(createTableUsers)
        db?.execSQL(createTablePlaces)

        /*val admin = User("admin","admin", "1")
        insertData(admin)*/

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(user: User){

        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_USERNAME,user.username)
        cv.put(COL_PASSWORD,user.password)
        cv.put(COL_ROLE,user.role)

        var result = db.insert(TABLE_NAME,null, cv)

        if(result == -1.toLong()){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()}


    }

    fun insertDataPlaces(places: Places){

        val db = this.writableDatabase
        var cvPlaces = ContentValues()
        cvPlaces.put(COL_PLACENAME,places.PlaceName)
        cvPlaces.put(COL_DESCRIPTION,places.Description)
        cvPlaces.put(COL_TAG,places.Tag)
        cvPlaces.put(COL_POSX,places.PosX)
        cvPlaces.put(COL_POSY,places.PosY)

        var result = db.insert(TABLE_NAME_MAPS,null, cvPlaces)

        if(result == -1.toLong()){
            Toast.makeText(context, "Failed" + places.PlaceName+places.Description+
                    places.Tag+places.PosX+places.PosY, Toast.LENGTH_SHORT).show()
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
                places.Tag = result.getString(result.getColumnIndex(COL_TAG))
                places.PosX = result.getString(result.getColumnIndex(COL_POSX))
                places.PosY = result.getString(result.getColumnIndex(COL_POSY))
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
                user.role = result.getString(result.getColumnIndex(COL_ROLE))
                list.add(user)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list

    }

    fun deleteUserData(id: Int) {
        val db = this.writableDatabase
        val whereClause = "$COL_ID = ?"
        val whereArgs = arrayOf(id.toString())
        val result = db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()

        if (result != -1) {
            Toast.makeText(context, "User with ID $id deleted successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to delete user with ID $id", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteMapsData(id: Int) {
        val db = this.writableDatabase
        val whereClause = "$COL_PLACESID = ?"
        val whereArgs = arrayOf(id.toString())
        val result = db.delete(TABLE_NAME_MAPS, whereClause, whereArgs)
        db.close()

        if (result != -1) {
            Toast.makeText(context, "Place with ID $id deleted successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to delete user with ID $id", Toast.LENGTH_SHORT).show()
        }
    }


    fun updateDataUser(){
        val db = this.writableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var cv = ContentValues()
                cv.put(COL_PASSWORD,(result.getInt(result.getColumnIndex(COL_PASSWORD))+1))
                db.update(TABLE_NAME,cv,COL_ID + "=? AND " + COL_USERNAME + "=?",
                    arrayOf(result.getString(result.getColumnIndex(COL_ID)),
                        result.getString(result.getColumnIndex(COL_USERNAME))))
            }while (result.moveToNext())
        }

        result.close()
        db.close()
    }





}