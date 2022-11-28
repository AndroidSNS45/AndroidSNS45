package com.example.android_sns_45

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SigninActivity : AppCompatActivity() {
    private lateinit var mFirebaseAuth //파이어베이스 인증
            : FirebaseAuth
    private lateinit var mDatabaseRef //실시간 데이터 베이스
            : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("sns45")
        val mEtEmail : EditText = findViewById(R.id.et_email)
        val mEtPwd : EditText = findViewById(R.id.et_password)
        val mEtNickname : EditText = findViewById(R.id.et_nickname);
        val mBtnRegister : Button = findViewById(R.id.btn_register)
        mBtnRegister.setOnClickListener(View.OnClickListener {
            //회원가입 시작
            val strEmail = mEtEmail.text.toString() //회원가입 입력필드에 입력한 값을 가져온다.
            val strPwd = mEtPwd.text.toString()
            val strNick = mEtNickname.text.toString()
            mFirebaseAuth!!.createUserWithEmailAndPassword(strEmail, strPwd)
                .addOnCompleteListener(this@SigninActivity) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = mFirebaseAuth //로그인 성공
                        val account = User(strEmail,strPwd,strNick)
                        //database에 삽입
                        mDatabaseRef.child("UserAccount").child(firebaseUser.uid.toString())
                            .setValue(account)
                        val intent = Intent(this@SigninActivity, LoginpanelActivity::class.java)
                        intent.putExtra("emailId", strEmail)
                        intent.putExtra("password", strPwd)
                        intent.putExtra("id", strNick)
                        startActivity(intent)
                        Toast.makeText(this@SigninActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@SigninActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        })
    }
}