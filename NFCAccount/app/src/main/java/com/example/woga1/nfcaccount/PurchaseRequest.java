package com.example.woga1.nfcaccount;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjw94 on 2017-09-01.
 */


public class PurchaseRequest extends StringRequest
{
    final static private String URL = "http://cj1ne.cafe24.com/Perchase.php";
    private Map<String, String> parameters;

    public PurchaseRequest(String TIME, String TYPE, int PRICE, String PLACE, String buyerID, String SELLER, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("TIME", TIME);
        parameters.put("TYPE", TYPE);
        parameters.put("PRICE", PRICE+"");
        parameters.put("PLACE", PLACE);
        parameters.put("buyerID", buyerID);
        parameters.put("SELLER", SELLER);
    }

    @Override
    public Map<String, String> getParams()
    {
        return parameters;
    }
}