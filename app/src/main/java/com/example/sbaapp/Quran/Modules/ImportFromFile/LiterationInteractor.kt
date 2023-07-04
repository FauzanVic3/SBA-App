package com.example.sbaapp.Quran.Modules.ImportFromFile

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.os.AsyncTask.execute
import com.example.sbaapp.Quran.Support.Data.LocalData
import com.example.sbaapp.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class LiterationInteractor (var context: Context, var literationInterface: Literation): AsyncTask<Void, Void, Void>(){

    lateinit var db: SQLiteDatabase
    val dbHelper = LocalData(context)

    fun setData(){
        db = dbHelper.writableDatabase
        execute()
    }

    override fun doInBackground(vararg params: Void?): Void? {
        //insertFromFile(R.raw.original, R.raw.translate)
        insertFromFile(R.raw.quran_uthmani, R.raw.translate)
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        literationInterface.successInputDatabase()
    }

    fun insertFromFile(resourceIdOriginal: Int, resourceIdTranslation: Int) {
        try {
            val insertsStream = context.resources.openRawResource(resourceIdOriginal)
            val insertReader = BufferedReader(InputStreamReader(insertsStream))

            val insertsStream2 = context.resources.openRawResource(resourceIdTranslation)
            val insertReader2 = BufferedReader(InputStreamReader(insertsStream2))

            db.beginTransaction()

            while (insertReader.ready()) {
                val insertStmt = insertReader.readLine()
                db.execSQL(insertStmt)
            }

            while (insertReader2.ready()) {
                val insertStmt = insertReader2.readLine()
                db.execSQL(insertStmt)
            }

            db.setTransactionSuccessful()
            db.endTransaction()

            insertReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}