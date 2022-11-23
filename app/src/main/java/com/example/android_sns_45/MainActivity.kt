package com.example.android_sns_45

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


private const val TAG_POSTING = "posting_fragment"
private const val TAG_HOME = "home_fragment"
private const val TAG_MY_INFO = "my_info_fragment"

class MainActivity : AppCompatActivity() {
    val postingData : List<PostingData> = listOf(
        PostingData("kmj1","1"),
        PostingData("kmj2","2"),
        PostingData("kmj3","3"),
        PostingData("kmj3","3"),
        PostingData("kmj3","3"),
        PostingData("kmj3","3"),
        PostingData("kmj3","3")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        val adapter = PostingListAdapter(postingData)
        postRecyclerView.adapter = adapter

        val navigationview: BottomNavigationView = findViewById(R.id.navigationView)
        navigationview.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.positngFragment -> setFragment(TAG_POSTING, PostingFragment())
                R.id.homeFragment -> setFragment(TAG_HOME, PostingFragment())
                R.id.myPageFragment -> setFragment(TAG_MY_INFO, MyInfoFragment())
            }
            true
        }
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