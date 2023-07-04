package com.example.sbaapp.Quran

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sbaapp.Quran.Entities.List.Surah
import com.example.sbaapp.Quran.Entities.Models.SurahModel
import com.example.sbaapp.Quran.Support.Utils.Adapter
import com.example.sbaapp.Quran.ViewHolders.SurahViewHolder
import com.example.sbaapp.R
import kotlinx.android.synthetic.main.activity_list_surah.*
import android.R.menu
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.Menu





class ListSurahActivity: AppCompatActivity() {

    lateinit var  adapter : Adapter<SurahModel, SurahViewHolder>
    val surah = Surah()
    val listSurah: ArrayList<SurahModel>
        get() = surah.surahList



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_surah)
        //setToolbar()
        setList()
    }

    private fun setToolbar(){
        //setSupportActionBar(toolbarQuran)
        //supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    private fun setList(){
        val manager = LinearLayoutManager(this)
        adapter = object : Adapter<SurahModel, SurahViewHolder>(R.layout.list_surah, SurahViewHolder::class.java,
                SurahModel::class.java, listSurah){
            override fun bindView(holder: SurahViewHolder, tipeData: SurahModel, position: Int) {
                holder.onBind(applicationContext, tipeData)
            }
        }


        list_surah.layoutManager = manager
        list_surah.adapter = adapter


    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
////        menuInflater.inflate(R.menu.search_quran_menu, menu)
////        return true
////    }
}