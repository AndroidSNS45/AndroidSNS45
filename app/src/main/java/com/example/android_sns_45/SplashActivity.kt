package com.example.android_sns_45

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 3000 // 1 sec 스플래시 유지시간
    private lateinit var auth : FirebaseAuth // 파이어 베이스 형식
    private lateinit var databaseReference: DatabaseReference
    public lateinit var  context_main : Context
    public var userInfoArr = arrayOfNulls<String>(3)

    //데이터 가져오기 형식 보기 쉽게 영단어로 표현
    private var email = 0
    private var password = 1
    private var id = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        context_main = this
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("sns45").child("UserAccount").child(auth.uid.toString())
        if(auth.uid== null){
            Handler().postDelayed({
                val intent = Intent(this@SplashActivity, LoginpanelActivity::class.java)
                startActivity(intent)
                finish()
            }, SPLASH_TIME_OUT)// 핸들러 사용
        }
        else{
            Handler().postDelayed({
                val intent = Intent(this@SplashActivity, LoginpanelActivity::class.java)
                startActivity(intent)
                finish()
            }, SPLASH_TIME_OUT)// 핸들러 사용
        }

    }

    fun nextActivity(){
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.putExtra("emailId", userInfoArr[email])
            intent.putExtra("id", userInfoArr[id])
            intent.putExtra("password", userInfoArr[password])
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }

    public fun getData(i : Int): String? {
        return userInfoArr[i]
    }
}