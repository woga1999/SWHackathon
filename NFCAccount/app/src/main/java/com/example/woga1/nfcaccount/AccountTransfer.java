package com.example.woga1.nfcaccount;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AccountTransfer extends Activity {

    private final String TOSS_PACKAGE_NAME = "viva.republica.toss";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_account_transfer);
        Button noButton = (Button)findViewById(R.id.noButton);
        Button finishButton = (Button)findViewById(R.id.finishButton);
        EditText transferMoney = (EditText)findViewById(R.id.money);
        transferMoney.setPadding(30, 0, 0, 0);
        transferMoney.append("원");
        transferMoney.setSelection(transferMoney.getLeft());

        Intent intent = getIntent();
        String accountInformation  = intent.getExtras().getString("accountInformation");



        noButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(AccountTransfer.this, MenuActivity.class);
                startActivityForResult(intent, 1);
            }

        });

        finishButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Text 값 받아서 로그 남기기
                String text = transferMoney.getText().toString();
                text = text + " " + accountInformation;

                if(text.length() != 1)
                {
                    ClipData clip = ClipData.newPlainText("text", text);

                    ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(clip);
                    launchForPayment(getApplicationContext());

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
                    String str_date = df.format(new Date());

                    str_date = str_date.replace('-', '.');
                    str_date = str_date.substring(0,str_date.length()-3);


                }

            }

        });
    };

    public boolean isTossInstalled(Context context) {
        return context.getPackageManager().getLaunchIntentForPackage(TOSS_PACKAGE_NAME) != null;
    }
    public void launchForPayment(Context context) {
        if (isTossInstalled(context)) {
            //Toss 앱의 설치 유무를 확인하고, 앱이 설치되어 있다면 토스의 main activity 를 실행하는 Intent를 반환합니다.
            Intent tossLauncherIntent = context.getPackageManager().getLaunchIntentForPackage(TOSS_PACKAGE_NAME);
            context.startActivity(tossLauncherIntent);
        }
        else
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + TOSS_PACKAGE_NAME));
            context.startActivity(intent);
        }
    }



}