package com.example.android_sns_45

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.posting_list_item.view.* //xml파일에 있는 id에 바로 접근 가능하게해줌


class PostingViewHolder(v : View) : RecyclerView.ViewHolder(v) {


    var id = v.findViewById<TextView>(R.id.mEmail)
    var content = v.findViewById<TextView>(R.id.mContent)
    var id2 = v.findViewById<TextView>(R.id.mEmail2)
    //fun bind(item: PostingData){
      //  view.mEmail.text = item.email
      //  view.mContent.text = item.content
      //  view.mEmail2.text = item.email
    //}
}