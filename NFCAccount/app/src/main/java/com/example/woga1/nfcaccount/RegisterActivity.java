package com.example.woga1.nfcaccount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btnRequestRegister = (Button) findViewById(R.id.btnRequestRegister);
//        Button btnCancelRegister = (Button) findViewById(R.id.btnCancelRegister);
        ImageButton backImageButton = (ImageButton) findViewById(R.id.backimageButton);

        final EditText idText = (EditText) findViewById(R.id.setID);
        final EditText pwText = (EditText) findViewById(R.id.setPW);
        final EditText nameText = (EditText) findViewById(R.id.setName);
        final EditText phoneText = (EditText) findViewById(R.id.setPhone);
        final EditText birthText = (EditText) findViewById(R.id.setBirth);
        final EditText bankText = (EditText) findViewById(R.id.setBankName);
        final EditText bankAccountText = (EditText) findViewById(R.id.setAccount);

//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.custom_registerbar);


        //뒤로가기 버튼 눌렀을 때
//        backImageButton.setOnClickListener(new EditText.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//            }
//
//        });

        //회원등록 버튼 눌렀을 때
        btnRequestRegister.setOnClickListener(new EditText.OnClickListener(){
            @Override
            public void onClick(View view) {
                String buyerID = idText.getText().toString();
                String buyerPW = pwText.getText().toString();
                String buyerName = nameText.getText().toString();
                String buyerPhone = phoneText.getText().toString();
                int buyerBirth = Integer.parseInt(birthText.getText().toString());
                String buyerBank = bankText.getText().toString();
                String buyerBankAccount = bankAccountText.getText().toString();

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
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("회원 등록에 성공하였습니다.")
                                        .setPositiveButton("확인", null)
                                        .create()
                                        .show();

                                Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
                                RegisterActivity.this.startActivity(intent);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("회원 등록에 실패하였습니다.")
                                        .setNegativeButton("확인", null)
                                        .create()
                                        .show();
                            }
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(buyerID, buyerPW, buyerName, buyerPhone, buyerBirth, buyerBank, buyerBankAccount, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }



        });

//        //회원등록 취소 버튼 눌렀을 때
//        btnCancelRegister.setOnClickListener(new EditText.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//            }
//
//        });
    }
}
