package dd.wan.dailylife

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import dd.wan.dailylife.model.Note
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.P)
class SQLHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        val sdf = SimpleDateFormat("E MMM dd yyyy", Locale.getDefault())
        private const val DB_NAME = "Diary"
        private const val DB_VERSION = 1
        private const val TB_DIARY = "tbl_diary"
        private const val DATE = "date"
        private const val CONTENT = "content"
        private const val TITLE = "title"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        var query = "Create table $TB_DIARY($DATE TEXT,$TITLE TEXT,$CONTENT TEXT)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TB_DIARY")
        onCreate(db)
    }

    fun checkInsert(note: Note):Long {
        var list = getAllDB()
        for (item in list) {
            if (sdf.format(note.date).equals(sdf.format(item.date))) {
                return(updateDB(note).toLong())
            }
        }
        return(insertDB(note))
    }

    fun deleteDB(date:Date) :Int {
        val db = this.writableDatabase
        var success = db.delete(TB_DIARY, "$DATE = ?", arrayOf(sdf.format(date)))
        db.close()
        return success
    }
    fun updateDB(note: Note): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DATE, sdf.format(note.date))
        contentValues.put(TITLE, note.title)
        contentValues.put(CONTENT, note.string)
        var success = db.update(TB_DIARY, contentValues, "$DATE = ?", arrayOf(sdf.format(note.date)))
        db.close()
        return success
    }

    fun insertDB(note: Note): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DATE, sdf.format(note.date))
        contentValues.put(TITLE, note.title)
        contentValues.put(CONTENT, note.string)
        var success = db.insert(TB_DIARY, null, contentValues)
        db.close()
        return success
    }

    fun getAllDB(): ArrayList<Note> {
        var list = ArrayList<Note>()
        val query = "SELECT * FROM $TB_DIARY"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(query, null)
        } catch (e: Exception) {
            db.execSQL(query)
            return list
        }
        if (cursor.moveToFirst()) {
            do {
                var date = sdf.parse(cursor.getString(cursor.getColumnIndex(DATE)))
                var text = cursor.getString(cursor.getColumnIndex(CONTENT))
                var title = cursor.getString(cursor.getColumnIndex(TITLE))
                list.add(Note(date, title, text))
            } while (cursor.moveToNext())
        }
        return list
    }
}