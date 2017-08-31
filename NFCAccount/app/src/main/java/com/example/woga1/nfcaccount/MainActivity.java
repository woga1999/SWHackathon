package com.example.woga1.nfcaccount;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
public class MainActivity extends Activity {
    private static final String TAG = "stickynotes";
    private boolean mResumed = false;
    private boolean mWriteMode = false;
    NfcAdapter mNfcAdapter;
    EditText mNote;
    Intent mIntent;
    PendingIntent mNfcPendingIntent;
    IntentFilter[] mWriteTagFilters;
    IntentFilter[] mNdefExchangeFilters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
        findViewById(R.id.write_tag).setOnClickListener(mTagWriter);
        mNote = (EditText) findViewById(R.id.note);
        mNote.addTextChangedListener(mTextWatcher);
        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (MalformedMimeTypeException e) {
            mNdefExchangeFilters = new IntentFilter[] { ndefDetected };
        }
        IntentFilter tagDetected = new IntentFilter(
                NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] { tagDetected };
    }
    @Override
    protected void onResume() {
        super.onResume();
        mResumed = true;
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.contentEquals(getIntent().getAction())) {
            NdefMessage[] messages = getNdefMessages(getIntent());
            byte[] payload = messages[0].getRecords()[0].getPayload();
            setNotBody(new String(payload));
            setIntent(new Intent());
        }
        enableNdefExchageMode();
    }
    private NdefMessage[] getNdefMessages(Intent intent) {
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i<rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
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
    private void setNotBody(String string) {
        Editable text = mNote.getText();
        text.clear();
        text.append(string);
    }
    private void enableNdefExchageMode() {
        mNfcAdapter.enableForegroundNdefPush(MainActivity.this, getNoteAsNdef());
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
        if (mWriteMode&&NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            writeTag(getNoteAsNdef(), detectedTag);
        }
    }
    boolean writeTag(NdefMessage message, Tag detectedTag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(detectedTag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    toast("읽기 전용 태그.");
                    Log.e("test","1");
                    return false;
                }
                if (ndef.getMaxSize()<size) {
                    toast("태그 용량은 " + ndef.getMaxSize() + " 바이트이고, 메시지 크기는 " + size + " 바이트이다.");
                    Log.e("test","2");
                    return false;
                }
                ndef.writeNdefMessage(message);
                toast("태그에 기록했음.");
                Log.e("test","3");
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(detectedTag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        toast("태그를 포맷을 했고 메시지를 썼음.");
                        Log.e("test","4");
                        return true;
                    } catch (IOException e) {
                        toast("태그 포맷을 실패했음.");
                        Log.e("test","5");
                        return false;
                    }
                } else {
                    toast("태그가 NDEF를 지원하지 않음.");
                    Log.e("test","6");
                    return false;
                }
            }
        } catch (Exception e) {
            toast("태그에 쓰기 실패.");
            Log.e("test","7");
        }
        return false;
    }
    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    private void PromptForContent(final NdefMessage msg) {
        new AlertDialog.Builder(this).setTitle("내용을 변경하겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String body = new String(msg.getRecords()[0].getPayload());
                        setNotBody(body);
                    }
                }) .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int which) {
            }
        }).show();
    }
    private View.OnClickListener mTagWriter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            disableNdefExchangeMode();
            enableTagWriteMode();
            new AlertDialog.Builder(MainActivity.this).setTitle("태그에 터치하세요") .setOnCancelListener(
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            disableTagWriteMode();
                            enableNdefExchangeMode();
                        }
                    }).create().show();
        }
    };
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable arg0) {
            if (mResumed) {
                mNfcAdapter.enableForegroundNdefPush(MainActivity.this,
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
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                "text/plain".getBytes(), new byte[] {}, textBytes);
        return new NdefMessage(new NdefRecord[] { textRecord });
    }
    protected void enableTagWriteMode() {
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(
                NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] { tagDetected };
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent,
                mWriteTagFilters, null);
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
        mNfcAdapter.enableForegroundNdefPush(MainActivity.this, getNoteAsNdef());
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent,mWriteTagFilters, null);
    }
}