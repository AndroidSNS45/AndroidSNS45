package com.example.android_sns_45

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android_sns_45.databinding.ActivityMyinfoBinding

public class MyinfoActivity: AppCompatActivity() {
    private val imageView = ImageView(parent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMyinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = MyinfoAdapter(imageView)
        binding.postimageRecyclerView.adapter = adapter
        binding.postimageRecyclerView.layoutManager = GridLayoutManager(this, 3)

    }



}