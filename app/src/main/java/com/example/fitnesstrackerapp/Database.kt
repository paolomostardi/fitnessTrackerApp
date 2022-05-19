package com.example.fitnesstrackerapp

import android.annotation.SuppressLint
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
        val POINTS = "points"

        fun createTable(db:SQLiteDatabase?) {
            val query = "create table $TABLE_NAME ($ID integer primary key autoincrement, $USERNAME text, $EMAIL text, $FIRST_NAME text, $LAST_NAME text, $PASSWORD text,$POINTS integer DEFAULT 0)"
            db?.execSQL(query)
        }

        fun add(username:String, email:String, password:String): Long {
            //user is not required to enter first and last name on account creation
            if (Users().existsByUsername(username))
                return -1
            if (Users().existsByEmail(email))
                return -1
            val values = ContentValues()
            values.put(USERNAME, username)
            values.putNull(FIRST_NAME)
            values.putNull(LAST_NAME)
            values.put(EMAIL, email)
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

        fun existsByUsername(username:String):Boolean {
            var exists = false
            val stmt = "select * from $TABLE_NAME where $USERNAME = \"$username\" "
            val cursor = readableDatabase.rawQuery(stmt, null)
            exists = cursor.moveToFirst()
            if (exists) return true
            return false
        }

        fun existsByEmail(email:String):Boolean {
            var exists = false
            val stmt = "select * from $TABLE_NAME where $EMAIL = \"$email\" "
            val cursor = readableDatabase.rawQuery(stmt, null)
            exists = cursor.moveToFirst()
            if (exists) return true
            return false
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



        @SuppressLint("Range")
        fun usernameContainsString(username:String): MutableList<String>{

            var stmt = "select * from $TABLE_NAME where ($USERNAME LIKE \"%$username%\" )"
            var cursor = readableDatabase.rawQuery(stmt, null)
            val stringList = mutableListOf<String>()
            cursor.moveToFirst()

            println("searching stuff -----------------------------------------------------------")
            if (cursor.count > 0) {
                do {
                    var element = cursor.getString(cursor.getColumnIndex(USERNAME))
                    stringList.add(element)
                    println("found some one $element")
                } while (cursor.moveToNext())
            }
            else{
                println("no values found")
            }
            return stringList
        }
        fun addDummyData(){
            Users().add("username","email@gmail.com","password")
            Users().add("username1","email1@gmail.com","password")
            Users().add("username2","email2@gmail.com","password")
            Users().add("username3","email3@gmail.com","password")
            Users().add("username4","email4@gmail.com","password")
            Users().add("username5","email5@gmail.com","password")
            Users().add("paolomostardi","email6@gmail.com","password")

            Users().setPoints("username",100)
            Users().setPoints("username1",101)
            Users().setPoints("username2",102)
            Users().setPoints("username3",99)
            Users().setPoints("username4",104)
            Users().setPoints("username5",105)
            returnUsersByPoints()
        }

        private fun setPoints(username: String,points: Int){
            var stmt = "UPDATE $TABLE_NAME set $POINTS = 110 "
            val contentValues = ContentValues()
            contentValues.put(POINTS,points)
            val whereClause = "$USERNAME=?"
            val whereArgs = arrayOf(username)
            writableDatabase.update(TABLE_NAME,contentValues,whereClause,whereArgs)

        }
        @SuppressLint("Range")
        fun getPositionByUsername(username: String): Int{
            var stmt = "select * from $TABLE_NAME ORDER BY $POINTS DESC"
            var cursor = readableDatabase.rawQuery(stmt, null)
            val stringList = mutableListOf<String>()
            var counter = 0
            if (!existsByUsername(username))
                return -1
            cursor.moveToFirst()
                do {
                    var element = cursor.getString(cursor.getColumnIndex(USERNAME))
                    if (element == username)
                        break
                    counter++
                } while (cursor.moveToNext())

            return counter + 1
        }

        @SuppressLint("Range")
        fun getPointsByUsername(username: String): Int{
            var exists = false
            val stmt = "select * from $TABLE_NAME where $USERNAME = \"$username\" "
            val cursor = readableDatabase.rawQuery(stmt, null)
            exists = cursor.moveToFirst()
            if(exists && cursor.getColumnIndex(POINTS) > -1)
                return cursor.getInt(cursor.getColumnIndex(POINTS))
            return -1
        }

        fun addPoints(username: String,points: Int){
            val pointsToPut = getPointsByUsername(username)
            if (pointsToPut < 0 )
                return
            val contentValues = ContentValues()
            contentValues.put(POINTS, points + pointsToPut)
            val whereClause = "$USERNAME=?"
            val whereArgs = arrayOf(username)
            writableDatabase.update(TABLE_NAME,contentValues,whereClause,whereArgs)
        }
        @SuppressLint("Range")
        fun returnUsersByPoints(): Pair<MutableList<Int>,MutableList<String>>{
            var stmt = "select * from $TABLE_NAME ORDER BY $POINTS DESC"
            var cursor = readableDatabase.rawQuery(stmt, null)
            val stringList = mutableListOf<String>()
            val integerList = mutableListOf<Int>()
            cursor.moveToFirst()
            if (cursor.count > 0) {
                do {
                    var element = cursor.getString(cursor.getColumnIndex(USERNAME))
                    stringList.add(element)
                    var element2 = cursor.getInt(cursor.getColumnIndex(POINTS))
                    integerList.add(element2)
                    println("found some one $element , points: $element2")
                } while (cursor.moveToNext())
            }
            else{
                println("no values found")
            }
            return Pair(integerList,stringList)
        }
        fun deleteUser(username: String) : Int {
            println("deleting user $username")
            return writableDatabase.delete(TABLE_NAME,USERNAME + "= \"$username\"",null)
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