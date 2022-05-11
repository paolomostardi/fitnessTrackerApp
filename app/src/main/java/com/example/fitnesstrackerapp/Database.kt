package com.example.fitnesstrackerapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


//read https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper to understand more

class Database(var context:Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "fitness-tracker"
    }






    inner class Users {
        val TABLE_NAME = "users"
        val ID = "id"
        val USERNAME = "username"
        val EMAIL = "email"
        val FIRST_NAME = "first_name"
        val LAST_NAME = "last_name"
        val PASSWORD = "password"

        fun createTable(db:SQLiteDatabase?) {
            val query = "create table $TABLE_NAME ($ID integer primary key autoincrement, $USERNAME text, $EMAIL text, $FIRST_NAME text, $LAST_NAME text, $PASSWORD text)"
            db?.execSQL(query)
        }

        fun add(username:String, email:String, password:String): Long {
            //user is not required to enter first and last name on account creation

            val values = ContentValues()
            values.put(USERNAME, username)
            values.putNull(FIRST_NAME)
            values.putNull(LAST_NAME)
            values.put(EMAIL, email)
            //encrypt password here!!!
            values.put(PASSWORD, password)

            val success = writableDatabase.insert(TABLE_NAME, null, values)
            writableDatabase.close()
            return success //returns insert ID
        }

        fun existsById(id:Int):Boolean {
            var exists = false
            val stmt = "select * from $TABLE_NAME where $ID = $id"
            val cursor = readableDatabase.rawQuery(stmt, null)
            //returns true if the 'cursor' can move to the first item (aka, a user exists)
            exists = cursor.moveToFirst()
            readableDatabase.close()
            return exists
        }

        fun existsByDetails(usernameOrEmail:String, password: String):Boolean {
            var exists = false
            var stmt = "select * from $TABLE_NAME where ($USERNAME = \"$usernameOrEmail\" and $PASSWORD = \"$password\")"
            var cursor = readableDatabase.rawQuery(stmt, null)
            //returns true if the 'cursor' can move to the first item (aka, a user exists)
            exists = cursor.moveToFirst()
            if (exists) return true

            stmt = "select * from $TABLE_NAME where ($EMAIL = \"$usernameOrEmail\" and $PASSWORD = \"$password\")"
            cursor = readableDatabase.rawQuery(stmt, null)
            exists = cursor.moveToFirst()
            readableDatabase.close()
            return exists
        }
    }





    override fun onCreate(db:SQLiteDatabase?) {
        Users().createTable(db)
    }

    override fun onUpgrade(db:SQLiteDatabase?, oldVersion:Int, newVersion:Int) {
        //handle version differences
    }

}
    /*
    val DATABASE_NAME ="MyDB"
    val TABLE_NAME="Users"
    val COL_NAME = "name"
    val COL_AGE = "age"
    val COL_ID = "id"

    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + TABLE_NAME +" (" +
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR(256)," +
                COL_AGE +" INTEGER)"

        db?.execSQL(createTable)

    }

    /*override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //To change body of created functions use File | Settings | File Templates.
    }*/

    fun insertData(user:User){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_NAME,user.name)
        cv.put(COL_AGE,user.age)
        var result = db.insert(TABLE_NAME,null,cv)
        if(result == -1.toLong())
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
    }

    fun readData() : MutableList<User>{
        var list : MutableList<User> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var user = User()
                user.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                user.name = result.getString(result.getColumnIndex(COL_NAME))
                user.age = result.getString(result.getColumnIndex(COL_AGE)).toInt()
                list.add(user)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun deleteData(){
        val db = this.writableDatabase
        db.delete(TABLE_NAME,null,null)
        db.close()
    }


    fun updateData() {
        val db = this.writableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var cv = ContentValues()
                cv.put(COL_AGE,(result.getInt(result.getColumnIndex(COL_AGE))+1))
                db.update(TABLE_NAME,cv,COL_ID + "=? AND " + COL_NAME + "=?",
                    arrayOf(result.getString(result.getColumnIndex(COL_ID)),
                        result.getString(result.getColumnIndex(COL_NAME))))
            }while (result.moveToNext())
        }

        result.close()
        db.close()
    }


}

*/