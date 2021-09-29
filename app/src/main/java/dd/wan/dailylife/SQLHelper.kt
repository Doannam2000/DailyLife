package dd.wan.dailylife

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputLayout
import dd.wan.dailylife.model.note
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.P)
class SQLHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_NAME = "Diary"
        private const val DB_VERSION = 1
        private const val TB_Diary = "tbl_diary"
        private const val DATE = "date"
        private const val CONTENT = "content"
        private const val TITLE = "title"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("Create table " + TB_Diary + "(" + DATE + "TEXT," +TITLE+"TEXT,"+ CONTENT + "TEXT" + ")")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TB_Diary")
        onCreate(db)
    }

    fun insertDB(date: Calendar,title:String, text: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DATE, date.toString())
        contentValues.put(TITLE, title)
        contentValues.put(CONTENT, text)
        db.insert(TB_Diary, null, contentValues)
        db.close()
    }

    fun getAllDB(): ArrayList<note> {
        var list = ArrayList<note>()
        val query = "SELECT * FROM $TB_Diary"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(query, null)
        } catch (e: Exception) {
            db.execSQL(query)
            return list
        }
        if(cursor.moveToFirst())
        {
            do {
                var date = cursor.getString(cursor.getColumnIndex(DATE)) as Calendar
                var text = cursor.getString(cursor.getColumnIndex(CONTENT))
                var title = cursor.getString(cursor.getColumnIndex(TITLE))
                list.add(note(date,title,text))
            }while (cursor.moveToNext())
        }
        return list
    }
}