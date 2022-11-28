package com.example.android_sns_45

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.widget.EditText
import android.os.Bundle
import com.example.android_sns_45.R
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.util.Log
import android.widget.Button
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.example.android_sns_45.MainActivity
import com.google.firebase.database.DatabaseError
import android.widget.Toast
import com.example.android_sns_45.SigninActivity

class LoginpanelActivity : AppCompatActivity() {
    private lateinit var mFirebaseAuth //파이어베이스 인증
            : FirebaseAuth
    private lateinit var mDatabaseRef //실시간 데이터 베이스
            : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mFirebaseAuth = FirebaseAuth.getInstance()
        val user_id = intent.getStringExtra("emailId")

        val mEtEmail : EditText = findViewById(R.id.emailedittext)
        val mEtPwd : EditText = findViewById(R.id.pwdedittext)

        val btn_login = findViewById<Button>(R.id.loginbutton)
        btn_login.setOnClickListener {
            //로그인 요청
            Log.d("tag1", mEtEmail.text.toString().equals(user_id).toString())

            val strEmail = mEtEmail.text.toString() //회원가입 입력필드에 입력한 값을 가져온다.
            val strPwd = mEtPwd.text.toString()
            mFirebaseAuth?.signInWithEmailAndPassword(strEmail, strPwd)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //로그인 성공시 MainActivity로 화면 전환
                        mDatabaseRef = FirebaseDatabase.getInstance().getReference("sns45").child("UserAccount").child(mFirebaseAuth.uid.toString())
                        mDatabaseRef.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                intent.putExtra("emailId", snapshot.child("emailId").value.toString())
                                intent.putExtra("id", snapshot.child("id").value.toString())
                                intent.putExtra("password", snapshot.child("password").value.toString())
                                startActivity(intent)
                                finish()
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                    } else {
                        Toast.makeText(this@LoginpanelActivity, "로그인실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        val btn_register = findViewById<Button>(R.id.assignbutton)
        btn_register.setOnClickListener { //회원가입 화면이동
            val intent = Intent(this@LoginpanelActivity, SigninActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}