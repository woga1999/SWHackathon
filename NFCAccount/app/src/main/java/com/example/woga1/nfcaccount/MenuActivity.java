package com.example.woga1.nfcaccount;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

//        final String[] sellerName = {"손장원","최종원"};

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_menubar);

//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sellerName) ;

        ListView listview = (ListView) findViewById(R.id.purchaseList) ;
        final ListviewAdapter listViewAdapter = new ListviewAdapter();

        listViewAdapter.addItem("신재혁치킨","3600원", "서울시 광진구 군자동 세종대학교") ;
        listViewAdapter.addItem("최종원떡볶이","3600원", "서울시 관악구 낙성대 종원이네") ;
        listview.setAdapter(listViewAdapter) ;
//        listViewAdapter.addItem("박효완스","3600원", "서울시 광진구 뚝섬유원지역") ;



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
//                if(sellerName[position].equals(""))
                Toast.makeText(getApplicationContext(),listViewAdapter.getItem(position).toString(),Toast.LENGTH_SHORT).show();
                if(listViewAdapter.getItem(position).equals(""))
                {
                    Toast.makeText(getApplicationContext(),"클릭할게 없다",Toast.LENGTH_SHORT).show();
                    Log.e("position", "빈칸");
                }
                else {
                    Toast.makeText(getApplicationContext(),"클릭할게 있다",Toast.LENGTH_SHORT).show();
                    Log.e("position", "빈칸아님");
                    startActivity(new Intent(MenuActivity.this, ProductPurchaseContent.class));
                }

            }
        }) ;
    }
}
