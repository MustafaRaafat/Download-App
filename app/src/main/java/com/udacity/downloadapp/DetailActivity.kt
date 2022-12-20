package com.udacity.downloadapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udacity.R
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setSupportActionBar(binding.toolbar)
        binding.contentDetail.nameDownload.text = intent.getStringExtra("name")
        binding.contentDetail.statusDownload.text = intent.getStringExtra("status")

    }

    fun goToMain(view: View) {
        if (view.id == binding.fab.id) {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
