package com.example.android_sns_45

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PostingListAdapter (val itemList : List<PostingData>) :
    RecyclerView.Adapter<PostingViewHolder>() {
    override fun getItemCount(): Int {
        return itemList.size // adapter에게 데이터 갯수 반환
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostingViewHolder {
        val inflatedView = LayoutInflater.from(parent.context) //아이템 정보 가져오기
            .inflate(R.layout.posting_list_item, parent, false)
        
        return PostingViewHolder(inflatedView); //Adapter에 사용할 viewholder 설정
    }

    override fun onBindViewHolder(holder: PostingViewHolder, position: Int) {
        val item = itemList[position]
        holder.apply { //viewholder에 apply함수 사용하여 각각의 데이터를 적용시켜줌
            bind(item)
        }
    }

}