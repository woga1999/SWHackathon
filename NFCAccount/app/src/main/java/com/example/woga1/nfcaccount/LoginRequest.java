package com.example.woga1.nfcaccount;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjw94 on 2017-09-01.
 */


public class LoginRequest extends StringRequest
{
    final static private String URL = "http://cj1ne.cafe24.com/Login.php";
    private Map<String, String> parameters;

    public LoginRequest(String buyerID, String buyerPW,  Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("buyerID", buyerID);
        parameters.put("buyerPW", buyerPW);
    }

    @Override
    public Map<String, String> getParams()
    {
        return parameters;
    }
}
