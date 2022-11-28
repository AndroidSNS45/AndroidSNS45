package com.example.android_sns_45


import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class LoginpanelActivity() : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private lateinit var mFirebaseAuth //파이어베이스 인증
            : FirebaseAuth
    private lateinit var mDatabaseRef //실시간 데이터 베이스
            : DatabaseReference
    private var mEtEmail: EditText? = null
    private var mEtPwd //로그인 입력 필드
            : EditText? = null
    private var btn_google //구글 로그인 버튼
            : SignInButton? = null
    private var auth //파이어베이스 인증
            : FirebaseAuth? = null
    private var callbackManager : CallbackManager? =null
    private val googleApiClient: GoogleApiClient? = null
    private var googleSignInClient: GoogleSignInClient? = null

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

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("sns45")
        //
        login_button.setOnClickListener{

            facebooklogin()
        }
        var googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        auth = FirebaseAuth.getInstance()
        btn_google = findViewById(R.id.google_sign_in_button)
        btn_google?.setOnClickListener(
            View.OnClickListener
            //구글 로그인 버튼 클릭시
            {
                val intent = googleSignInClient!!.signInIntent
                startActivityForResult(intent, REQ_SIGN_GOOGLE)
            })
        //printHashKey()
        //callbackManager = callbackManager.Factory.create()
    }
    //Wc79i5GWCk0diSOnEuRkFyXh+ck= //해시키

    fun printHashKey() {
        try {
            val info: PackageInfo = packageManager
                .getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))
                Log.i("TAG", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("TAG", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("TAG", "printHashKey()", e)
        }
    }
    fun facebooklogin(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"))

        LoginManager.getInstance()
            .registerCallback(callbackManager,object : FacebookCallback<LoginResult>{
                override fun onCancel() {

                }

                override fun onError(error: FacebookException) {

                }

                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result?.accessToken)
                }

            })
    }
    fun handleFacebookAccessToken(token :AccessToken?){
        var credential = FacebookAuthProvider.getCredential(token?.token!!)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener {
                    task ->
                if(task.isSuccessful){
                    //login
                    moveMainpage(task.result?.user)
                }else{

                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun moveMainpage(user:FirebaseUser?){
        if(user != null){
            startActivity(Intent(this,LoginpanelActivity::class.java))
        }
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) { //구글 로그인 인증을 요청했을때 결과값을 되돌려받는곳
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode,resultCode, data)
        if (requestCode == REQ_SIGN_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) { //인증결과가 성공적이면
                val account = task.result //acount가 구글로그인 정보를 담고있음(닉네임,프로필사진)
                resultLogin(account) //로그인 결과값 출력 수행하라는 메소드
            }
        }
    }

    private fun resultLogin(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful) { //로그인 성공시
                        Toast.makeText(this@LoginpanelActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                        // Intent intent = new Intent(LoginpanelActivity.this,MainActivity.class);
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        /* intent.putExtra("nickname",account.getDisplayName());
                            intent.putExtra("photourl",String.valueOf(account.getPhotoUrl()));// 프로필 사진, 이름 받기*/
                        startActivity(intent)
                        //finish();//현재 액티비티 파괴
                    } else {
                        Toast.makeText(this@LoginpanelActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    companion object {
        private val REQ_SIGN_GOOGLE = 100 //구글 로그인 결과 코드
    }
}