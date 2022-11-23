package com.example.android_sns_45

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.posting_list_item.view.* //xml파일에 있는 id에 바로 접근 가능하게해줌


class PostingViewHolder(v : View) : RecyclerView.ViewHolder(v) {

    var view : View = v

    fun bind(item: PostingData){
        view.mName.text = item.name
        view.mNum.text = item.num
        view.mName2.text = item.name
    }
}