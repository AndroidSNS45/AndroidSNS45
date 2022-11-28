package com.example.android_sns_45

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.Display
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.android_sns_45.databinding.ActivityMyinfoBinding

class MyinfoAdapter(private val viewModel: ViewModel) : RecyclerView.Adapter<MyinfoAdapter.ViewHolder>(){
    inner class ViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView){
        fun setContents(pos:Int) {
            val k = pos
        }
    }
    override fun getItemCount(): Int {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var width = getDisplayWidth(parent.context) / 3
        var imageView = ImageView(parent.context)
        imageView.layoutParams = LinearLayoutCompat.LayoutParams(width,width)

        return ViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContents(position)
    }

    fun getDisplayWidth(ctx: Context): Int {
        val outMetrics = DisplayMetrics()
        val display: Display
        @Suppress("DEPRECATION")
        display = (ctx as Activity).windowManager.defaultDisplay
        @Suppress("DEPRECATION")
        display.getMetrics(outMetrics)
        val size = Point()
        @Suppress("DEPRECATION")
        display.getRealSize(size)
        return size.x
    }
}