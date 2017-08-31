package com.example.woga1.nfcaccount;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btnRequestRegister = (Button) findViewById(R.id.btnRequestRegister);
        Button btnCancelRegister = (Button) findViewById(R.id.btnCancelRegister);
        ImageButton backImageButton = (ImageButton) findViewById(R.id.backimageButton);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_registerbar);


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
            }

        });

        //회원등록 취소 버튼 눌렀을 때
        btnCancelRegister.setOnClickListener(new EditText.OnClickListener(){
            @Override
            public void onClick(View view) {
            }

        });
    }
}
