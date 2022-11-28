package com.example.android_sns_45

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


private const val TAG_POSTING = "posting_fragment"
private const val TAG_HOME = "home_fragment"
private const val TAG_MY_INFO = "my_info_fragment"

class MainActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var database : DatabaseReference
    private lateinit var array : ArrayList<PostingData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val emailId = intent.getStringExtra("emailId")
        val id = intent.getStringExtra("id")
        val navigationview: BottomNavigationView = findViewById(R.id.navigationView)
        navigationview.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.positngFragment -> {
                    val intent1 = Intent(this,WritingActivity::class.java)
                    intent1.putExtra("emailId",emailId)
                    intent1.putExtra("id",id)
                    Log.e("MainActivity","emailId = $emailId id= $id")
                    startActivity(intent1)
                    Toast.makeText(applicationContext, "글쓰기", Toast.LENGTH_SHORT).show()
                }
                R.id.homeFragment -> setFragment(TAG_HOME, PostingFragment())
                R.id.myPageFragment -> setFragment(TAG_MY_INFO, MyInfoFragment())
            }
            true
        }
        auth = FirebaseAuth.getInstance()
        array = ArrayList()
        database = FirebaseDatabase.getInstance().getReference("Postings")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                array.clear()
                for(data in snapshot.children) {
                    if(data.child("writerUid").value as String? == auth.currentUser!!.uid) {
                        val email = data.child("emailId").value as String?
                        val content = data.child("content").value as String?
                        val id = data.child("id").value as String?
                        val time = data.child("time").value as String?
                        val epoch = data.child("epoch").value as String?
                        val writerUid = data.child("writerUid").value as String

                        array.add(PostingData(writerUid,email,id,content,time,epoch))
                    }
                }
                if(array.size > 1) {
                    array.sortWith(Comparator { p0, p1 -> p0!!.epoch!!.toLong().compareTo(p1!!.epoch!!.toLong()) * -1})
                }
                postRecyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "게시글을 불러 오지못했습니다", Toast.LENGTH_SHORT).show()
            }
        })

        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        val adapter = PostingListAdapter(array)
        postRecyclerView.adapter = adapter

    }
    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val posting = manager.findFragmentByTag(TAG_POSTING)
        val home = manager.findFragmentByTag(TAG_HOME)
        val myInfo = manager.findFragmentByTag(TAG_MY_INFO)

        if (posting != null){
            fragTransaction.hide(posting)
        }

        if (home != null){
            fragTransaction.hide(home)
        }

        if (myInfo != null) {
            fragTransaction.hide(myInfo)
        }

        if (tag == TAG_POSTING) {
            if (posting!=null){
                fragTransaction.show(posting)
                postRecyclerView.visibility
            }
        }
        else if (tag == TAG_HOME) {
            if (home != null) {
                fragTransaction.show(home)
            }
        }

        else if (tag == TAG_MY_INFO){
            if (myInfo != null){
                fragTransaction.show(myInfo)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }
}