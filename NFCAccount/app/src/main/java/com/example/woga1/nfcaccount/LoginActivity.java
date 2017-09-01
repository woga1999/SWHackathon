package com.example.woga1.nfcaccount;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);}
        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final Button btnRegister = (Button) findViewById(R.id.btnRegister);
        final EditText idText = (EditText)findViewById(R.id.idText);
        final EditText pwText = (EditText)findViewById(R.id.pwText);

        //로그인버튼 눌렀을 때
        btnLogin.setOnClickListener(new EditText.OnClickListener(){
            @Override
            public void onClick(View view) {
                final String buyerID = idText.getText().toString();
                final String buyerPW = pwText.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success)
                            {
                                String buyerID = jsonResponse.getString("buyerID");
                                String buyerPassword = jsonResponse.getString("buyerPW");
                                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                intent.putExtra("buyerID", buyerID);
                                intent.putExtra("buyerPW", buyerPW);
                                LoginActivity.this.startActivity(intent);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인에 실패하였습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(buyerID, buyerPW, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

            }

        });

        //회원가입버튼 눌렀을 때
        btnRegister.setOnClickListener(new EditText.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);

            }

        });
    }
}
