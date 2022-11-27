package com.example.android_sns_45;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginpanelActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터 베이스
    private EditText mEtEmail, mEtPwd; //로그인 입력 필드
    private SignInButton btn_google; //구글 로그인 버튼
    private FirebaseAuth auth; //파이어베이스 인증
    private GoogleApiClient googleApiClient;
    private GoogleSignInClient googleSignInClient;
    private static final int REQ_SIGN_GOOGLE = 100; //구글 로그인 결과 코드


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth =FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("sns45");

        mEtEmail = findViewById(R.id.emailedittext);
        mEtPwd = findViewById(R.id.pwdedittext);
        //
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.deafault_web_client_id))
                .requestEmail()
                .build();

        /*googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();*/
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        auth= FirebaseAuth.getInstance();

        btn_google =findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() { //구글 로그인 버튼 클릭시
            @Override
            public void onClick(View view) {
                Intent intent =googleSignInClient.getSignInIntent();
                startActivityForResult(intent,REQ_SIGN_GOOGLE);

            }
        });




        Button btn_login = findViewById(R.id.loginbutton);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //로그인 요청
                String strEmail = mEtEmail.getText().toString(); //회원가입 입력필드에 입력한 값을 가져온다.
                String strPwd = mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(LoginpanelActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            //로그인 성공시 MainActivity로 화면 전환
                            Intent intent = new Intent(LoginpanelActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish(); //현재 액티비티 파괴
                        }else{
                            Toast.makeText(LoginpanelActivity.this,"로그인실패",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });




        Button btn_register = findViewById(R.id.assignbutton);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 화면이동
                Intent intent = new Intent(LoginpanelActivity.this,SigninActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //구글 로그인 인증을 요청했을때 결과값을 되돌려받는곳
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_SIGN_GOOGLE){
            Task<GoogleSignInAccount>task =GoogleSignIn.getSignedInAccountFromIntent(data);
            if(task.isSuccessful()){ //인증결과가 성공적이면
                GoogleSignInAccount account = task.getResult(); //acount가 구글로그인 정보를 담고있음(닉네임,프로필사진)
                resultLogin(account); //로그인 결과값 출력 수행하라는 메소드

            }
        }
    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {//로그인 성공시
                            Toast.makeText(LoginpanelActivity.this,"로그인 성공",Toast.LENGTH_SHORT).show();
                           // Intent intent = new Intent(LoginpanelActivity.this,MainActivity.class);
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                           /* intent.putExtra("nickname",account.getDisplayName());
                            intent.putExtra("photourl",String.valueOf(account.getPhotoUrl()));// 프로필 사진, 이름 받기*/
                            startActivity(intent);
                            //finish();//현재 액티비티 파괴
                        }

                        else{Toast.makeText(LoginpanelActivity.this,"로그인 실패",Toast.LENGTH_SHORT).show();


                        }

                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
