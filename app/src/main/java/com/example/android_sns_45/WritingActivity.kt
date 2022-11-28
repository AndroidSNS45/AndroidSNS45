package com.example.android_sns_45

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import coil.load
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_writing.*

class WritingActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference


//    private val permissionList = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
//    private val checkPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
//        result.forEach {
//            if(!it.value) {
//                Toast.makeText(applicationContext, "권한 동의 필요!", Toast.LENGTH_SHORT).show()
//                finish()
//            }
//        }
//    }
//    private val readImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        imageView2.load(uri)
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        auth = FirebaseAuth.getInstance()

        val post : Button = findViewById(R.id.btn_post)
        val content : EditText = findViewById(R.id.et_content)
        val back : ImageView = findViewById(R.id.iv_back)
        val imageUpload : Button = findViewById(R.id.imageUpload)

//        checkPermission.launch(permissionList)
//        imageUpload.setOnClickListener {
//            readImage.launch("image/*")
//            Log.d("tag1",readImage.)
//        }
        imageUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }
        post.setOnClickListener {
            if(content.text.toString().trim() == "") {
                Toast.makeText(applicationContext, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                val writerUid = auth.currentUser!!.uid
                val email : String? = intent.getStringExtra("emailId")
                val id : String? = intent.getStringExtra("id")
                val tmp : String = content.text.toString()
                val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
                val time : String = sdf.format(Date())
                val imageUrl : String? = ""
                val postingID = id + "@" + Date()
//                Toast.makeText(applicationContext, Date().time.toString(), Toast.LENGTH_SHORT).show()
//                val commentNum = "0"
//                val who : String? = intent.getStringExtra("who")

                database = FirebaseDatabase.getInstance().getReference("Postings")
                val posting = PostingData(writerUid,email,id,tmp,time, System.currentTimeMillis().toString(),imageUrl)
                database.child(postingID).setValue(posting).addOnSuccessListener {
                    Toast.makeText(applicationContext, "글을 업로드 하였습니다", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, "업로드에 실패 했습니다", Toast.LENGTH_SHORT).show()
                }

            }
        }

        back.setOnClickListener {
            finish()
        }
    }

    private val activityResult : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        if(it.resultCode == RESULT_OK && it.data != null){
            val uri = it.data!!.data

            Glide.with(this)
                .load(uri)
                .into(imageView2)
        }
    }
}