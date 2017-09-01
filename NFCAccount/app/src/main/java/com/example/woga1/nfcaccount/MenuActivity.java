package com.example.woga1.nfcaccount;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.skp.Tmap.TMapGpsManager;

public class MenuActivity extends AppCompatActivity {

    TMapGpsManager tmapgps = null;
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
        final ListviewAdapter listViewAdapter = new ListviewAdapter();

        listViewAdapter.addItem("신재혁치킨","3,600원", "17.09.01 00:20") ;
        listViewAdapter.addItem("최종원떡볶이","3,600원", "17.08.30 20:35") ;
        listview.setAdapter(listViewAdapter) ;
        listViewAdapter.addItem("박효완탕수육","4,200원", "17.08.29 22:14") ;
        listViewAdapter.addItem("감태균탕수육","5,200원", "17.08.28 22:14") ;
        listViewAdapter.addItem("육문수탕수육","6,200원", "17.08.27 22:14") ;

//        Log.e("location", String.valueOf(nowLocation().getLatitude()));
//        Log.e("location", String.valueOf(nowLocation().getLongitude()));


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
