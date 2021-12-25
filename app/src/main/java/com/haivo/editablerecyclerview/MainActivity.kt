package com.haivo.editablerecyclerview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ActivityUtils
import com.haivo.editablerecyclerview.databinding.ActivityMainBinding
import com.haivo.editablerecyclerview.room.*
import com.haivo.editablerecyclerview.view.AppListController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppListController.inject(binding.root, this)

        binding.tvMoreApp.setOnClickListener {
            startActivity(
                Intent(ActivityUtils.getTopActivity(), MoreAppActivity::class.java)
            )
        }
    }
}