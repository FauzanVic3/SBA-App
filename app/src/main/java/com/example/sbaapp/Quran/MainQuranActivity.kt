package com.example.sbaapp.Quran

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.sbaapp.Quran.Modules.Database.DatabaseHelper
import com.example.sbaapp.Quran.Modules.ImportFromFile.Literation
import com.example.sbaapp.Quran.Modules.ImportFromFile.LiterationInteractor
import com.example.sbaapp.R

class MainQuranActivity : AppCompatActivity(), Literation {

    val interactor = LiterationInteractor(this, this)
    val databaseHelper = DatabaseHelper(this)

    override fun successInputDatabase() {
        println("Hello Entering MainQuranActivity")
        openNextActivity()
    }

    override fun failedInputDatabase() {
        databaseHelper.clearTable()
        interactor.setData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quran_main)

        if (databaseHelper.isDataAvailable()){
            println("Hello Entering MainQuranActivity")
            openNextActivity()
        }else{
            databaseHelper.clearTable()
            interactor.setData()
        }
    }


    fun openNextActivity(){
        val intent = Intent(this, ListSurahActivity::class.java)
        startActivity(intent)
        finish()
    }


}