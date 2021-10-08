package com.phantom.automath

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

const val DATABASE_NAME = "DATABASE"
const val TABLE_NAME = "ALGEBRA_TABLE"

class AlgebraTable{
	companion object {
		const val primary_id = "id"
		const val expression = "expression"
		const val description = "description"
	}
}

data class Algebra(val id: Int, val expression: String, val description: String = "")

class AlgebraDatabaseHandler(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
	override fun onCreate(db: SQLiteDatabase?){
		val createTable = "CREATE TABLE $TABLE_NAME (${AlgebraTable.primary_id} INTEGER PRIMARY KEY AUTOINCREMENT, ${AlgebraTable.expression} VARCHAR(256), ${AlgebraTable.description} VARCHAR(256))"
		db?.execSQL(createTable)
	}

	override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
		TODO("Not yet implemented")
	}

	fun insertData(expression: String, description: String){
		val database = this.writableDatabase
		val contentValues = ContentValues()
		contentValues.put(AlgebraTable.expression, expression)
		contentValues.put(AlgebraTable.description, description)
		val result = database.insert(TABLE_NAME, null, contentValues)
		if(result == (0).toLong())
			Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
		else
			Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
	}

	@SuppressLint("Range")
	fun readData(): MutableList<Algebra> {
		val list: MutableList<Algebra> = ArrayList()
		val db = this.readableDatabase
		val query = "Select * from $TABLE_NAME"
		val result = db.rawQuery(query, null)
		if (result.moveToFirst()) {
			do {
				val algebra = Algebra(
					result.getInt(result.getColumnIndex(AlgebraTable.primary_id)),
					result.getString(result.getColumnIndex(AlgebraTable.expression)),
					result.getString(result.getColumnIndex(AlgebraTable.description))
				)
//                user.id = result.getString(result.getColumnIndex(AlgebraTable.primary_id)).toInt()

				list.add(algebra)
			}
			while (result.moveToNext())
		}
		return list
	}

	fun getDataCursor(): Cursor {
		val db = this.readableDatabase
		val query = "Select * from $TABLE_NAME"
		val result = db.rawQuery(query, null)
		result.moveToFirst()
		return result
	}

	fun clear_table() {
		val database = this.writableDatabase
		database.delete(TABLE_NAME, null, null)
	}

	fun deleteById(id: Int){
		this.writableDatabase.delete(TABLE_NAME, "${AlgebraTable.primary_id} = ?", arrayOf(id.toString()))
	}
}