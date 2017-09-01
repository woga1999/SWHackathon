package com.example.woga1.nfcaccount;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.List;

import static com.skp.Tmap.TMapView.TILETYPE_HDTILE;

public class ProductPurchaseContent extends AppCompatActivity {

    TMapView tmapview = null;
    TMapGpsManager tmapgps = null;
    LocationManager mLocationManager;
    RelativeLayout mapView = null;
    public Location nowPlace = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_purchase_content);
        tmapgps = new TMapGpsManager(this);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_productbar);

        mapView = (RelativeLayout) findViewById(R.id.mapview);
        Location location = nowLocation();
        location = getLastKnownLocation();
        tmapview = new TMapView(this);
//        TMapPoint startPoint = new TMapPoint(location.getLatitude(), location.getLongitude());
        TMapPoint startPoint = new TMapPoint(37.512702,127.119563);

//        tmapview.setLocationPoint(location.getLongitude(), location.getLatitude());
        tmapview.setLocationPoint(37.512702,127.119563);
        tmapview.setTileType(TILETYPE_HDTILE);
        tmapview.setSKPMapApiKey("cad2cc9b-a3d5-3c32-8709-23279b7247f9");
        tmapview.setZoomLevel(14);
        tmapview.setIconVisibility(true);
        tmapview.setTrackingMode(true);
        tmapview.setCompassMode(true);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        mapView.addView(tmapview);
    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }

        return bestLocation;
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
//                Criteria criteria = new Criteria();
//                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
//                String provider = lm.getBestProvider(criteria, true);
//                myLocation = lm.getLastKnownLocation(provider);
//                Log.d("myLocation: ", String.valueOf(myLocation.getLatitude()));
            }
        }
        return myLocation;
    }

    public String returnDetailAdress() //위도,경도 받아서 상세주소 문자열로 반환
    {
        String location = null;

        return location;
    }

    public Location returnLatLon() //상세주소 문자열을 위도,경도 Location로 반환
    {
        Location detailAdress = null;

        return detailAdress;
    }

    public void execute(double Latitude, double Longitude)  {
        //위도 경도를 받아서 지도상에 띄움

        tmapview = new TMapView(this);
        TMapPoint startPoint = new TMapPoint(Latitude, Longitude);

        tmapview.setLocationPoint(Longitude, Latitude);
        tmapview.setTileType(TILETYPE_HDTILE);
        tmapview.setSKPMapApiKey("cad2cc9b-a3d5-3c32-8709-23279b7247f9");
        tmapview.setZoomLevel(14);
        tmapview.setIconVisibility(true);
        tmapview.setTrackingMode(true);
        tmapview.setCompassMode(true);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);


        mapView.addView(tmapview);
    }
}
