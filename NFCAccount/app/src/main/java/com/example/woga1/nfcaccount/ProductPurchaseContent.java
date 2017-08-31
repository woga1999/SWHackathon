package com.example.woga1.nfcaccount;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class ProductPurchaseContent extends Activity {

    RelativeLayout mapView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_purchase_content);
        mapView = (RelativeLayout) findViewById(R.id.mapview);
    }
}
