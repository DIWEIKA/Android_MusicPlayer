package com.example4job1.musicplayer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example4job1.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText unameEdt=(EditText)findViewById(R.id.edt_username);
        final EditText pwdEdt=(EditText)findViewById(R.id.edt_password);
        SharedPreferences pref = getSharedPreferences("Userinfo",MODE_PRIVATE);
        unameEdt.setText(pref.getString("uname",""));
        Button quitBtn=(Button)findViewById(R.id.btn_quit);
        Button loginBtn=(Button)findViewById(R.id.btn_login);
        final CheckBox savenameChk=(CheckBox)findViewById(R.id.chk_savename);

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unameEdt.getText().toString()==null||unameEdt.getText().toString().equals("") || !pwdEdt.getText().toString().equals("123456")){
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences.Editor editor= getSharedPreferences("Userinfo",MODE_PRIVATE).edit();
                    if(savenameChk.isChecked() == true){
                        editor.putString("uname",unameEdt.getText().toString());
                    }
                    else {
                        editor.putString("uname","");
                    }
                    editor.apply();
                    Intent intent=new Intent(LoginActivity.this,ListActivity.class);
                    intent.putExtra("uname",unameEdt.getText().toString());
                    startActivity(intent);
                }
            }
        });



    }
}
