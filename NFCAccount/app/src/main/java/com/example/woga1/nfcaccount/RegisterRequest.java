package com.example.woga1.nfcaccount;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cjw94 on 2017-09-01.
 */


public class RegisterRequest extends StringRequest
{
    final static private String URL = "http://cj1ne.cafe24.com/Register.php";
    private Map<String, String> parameters;

    public RegisterRequest(String buyerID, String buyerPW, String buyerName, String buyerPhone, int buyerBirth, String buyerBank, String buyerBankAccount, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("buyerID", buyerID);
        parameters.put("buyerPW", buyerPW);
        parameters.put("buyerPhone", buyerPhone);
        parameters.put("buyerName", buyerName);
        parameters.put("buyerBirth", buyerBirth+"");
        parameters.put("buyerBank", buyerBank);
        parameters.put("buyerBankAccount", buyerBankAccount);
    }

    @Override
    public Map<String, String> getParams()
    {
        return parameters;
    }
}