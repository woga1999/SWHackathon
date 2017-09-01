package com.example.woga1.nfcaccount;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.skp.Tmap.TMapGpsManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.woga1.nfcaccount.R.id.logout;
import static com.example.woga1.nfcaccount.R.id.menu_user;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "stickynotes";
    private boolean mResumed = false;
    private boolean mWriteMode = false;
    NfcAdapter mNfcAdapter;
    EditText mNote;
    Intent mIntent;
    PendingIntent mNfcPendingIntent;
    IntentFilter[] mWriteTagFilters;
    IntentFilter[] mNdefExchangeFilters;


    String sellerName;
    String accountNumber;
    String accountBank;
    String shopCategory;
    Date curDate = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    String DateToStr = format.format(curDate);
    Location shopLocation;
    TextView test;
    Editable text;
    TMapGpsManager tmapgps = null;
    final ListviewAdapter listViewAdapter= new ListviewAdapter();

    private final String TOSS_PACKAGE_NAME = "viva.republica.toss";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        tmapgps = new TMapGpsManager(this);
//        final String[] sellerName = {"손장원","최종원"};

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_menubar);

//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sellerName) ;

        ListView listview = (ListView) findViewById(R.id.purchaseList) ;
        listview.setAdapter(listViewAdapter) ;

        listViewAdapter.addItem("강순자","5,300원", "2017.08.31 11:20",1) ;
        listViewAdapter.addItem("안미화","10,000원", "2017.08.30 08:18",1) ;
        listViewAdapter.addItem("최성길","7,000원", "2017.08.28 12:14",1) ;
        listViewAdapter.addItem("육문수","4,500원", "2017.08.24 19:35",1) ;
        listViewAdapter.addItem("현상소","14,000원", "2017.08.19 22:57",1) ;
        listViewAdapter.addItem("박효완","4,500원", "2017.08.17 19:35",1) ;
        listViewAdapter.addItem("정원빈","16,000원", "2017.08.13 16:57",1) ;
        listViewAdapter.addItem("임미정","4,500원", "2017.08.12 13:35",1) ;
        listViewAdapter.addItem("이승철","22,000원", "2017.08.07 23:13",1) ;
        listViewAdapter.addItem("왕제문","14,000원", "2017.08.06 22:40",1) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if(listViewAdapter.getItem(position).equals(""))
                {
                }
                else {
                    startActivity(new Intent(MenuActivity.this, ProductPurchaseContent.class));

                }
            }
        }) ;

        //NFC
        test = (TextView)findViewById(R.id.checkString);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
        mNote = (EditText) findViewById(R.id.note);
        mNote.addTextChangedListener(mTextWatcher);
        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            mNdefExchangeFilters = new IntentFilter[] { ndefDetected };
        }
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] { tagDetected };
        //


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, menu_user, Menu.NONE, "사용자 구매패턴");
        menu.add(Menu.NONE, logout, Menu.NONE, "로그아웃");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case menu_user:
                //사용자 버튼 눌렀을 때
                startActivity(new Intent(MenuActivity.this, CustomChartActivity.class));
                break;
            case logout:
                //로그아웃 버튼
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        mResumed = true;
        enableNdefExchageMode();
    }

    private NdefMessage[] getNdefMessages(Intent intent) {
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i<rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            else {
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNCHANGED,
                        empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };
            }
        } else {
            Log.d(TAG, "알려지지 않은 인텐트.");
            finish();
        }
        return msgs;
    }

    private void setNotBody(String string) {  //읽은건 mNote 에 적혀 있다. // text는 앞에 3글자를 지운것.
        text = mNote.getText();
        text.clear();
        text.append(string);
        text.delete(0,3);
        SeparateInformation(text);
    }

    private void enableNdefExchageMode() {
        mNfcAdapter.enableForegroundNdefPush(MenuActivity.this, getNoteAsNdef());
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mResumed = false;
        mNfcAdapter.disableForegroundNdefPush(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!mWriteMode&&NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] msgs = getNdefMessages(intent);
            PromptForContent(msgs[0]);
        }
    }


    private void PromptForContent(final NdefMessage msg) {
        String body = new String(msg.getRecords()[0].getPayload());
        setNotBody(body);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable arg0) {
            if (mResumed) {
                mNfcAdapter.enableForegroundNdefPush(MenuActivity.this,
                        getNoteAsNdef());
            }
        }
        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    };
    protected NdefMessage getNoteAsNdef() {
        byte[] textBytes = mNote.getText().toString().getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(), new byte[] {}, textBytes);
        return new NdefMessage(new NdefRecord[] { textRecord });
    }
    protected void enableTagWriteMode() {
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] { tagDetected };
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }
    private void disableNdefExchangeMode() {
        mNfcAdapter.disableForegroundNdefPush(this);
        mNfcAdapter.disableForegroundDispatch(this);
    }
    private void disableTagWriteMode() {
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(this);
    }
    private void enableNdefExchangeMode() {
        mNfcAdapter.enableForegroundNdefPush(MenuActivity.this, getNoteAsNdef());
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent,mWriteTagFilters, null);
    }


    private void SeparateInformation(Editable text){   //하나하나 구분해서 출력하는 메소드
        String[] array = text.toString().split("/");
//        test = (TextView)findViewById(R.id.checkString);
        sellerName = array[0];
        accountNumber = array[1];
        accountBank = array[2];
        shopCategory = array[3];

        test.setText(sellerName + " " + accountNumber + " " + accountBank + " " + shopCategory + " " + DateToStr
                + " " );

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String str_date = df.format(new Date());

        str_date = str_date.replace('-', '.');
        str_date = str_date.substring(0,str_date.length()-3);

        listViewAdapter.addItem("신재혁","1,000원", str_date,0) ;

        Intent intent = new Intent(MenuActivity.this, AccountTransfer.class);
        String accountInformation = accountBank + " " +accountNumber;
        intent.putExtra("accountInformation", accountInformation);
        startActivityForResult(intent, 1);




    }

    public Location nowLocation() {
        Location myLocation = null;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {

            tmapgps = new TMapGpsManager(this);
            tmapgps.OpenGps();
            tmapgps.setMinDistance(0);
            tmapgps.setMinTime(100);
            tmapgps.setProvider(tmapgps.GPS_PROVIDER);
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            myLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (myLocation == null) {
                myLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(myLocation != null)
                {
                    Log.d("myLocation: ", String.valueOf(myLocation.getLatitude()));
                }
            }
            else if(myLocation != null){
                Log.d("myLocation: ", String.valueOf(myLocation.getLatitude()));
            }
        }
        return myLocation;
    }
}
