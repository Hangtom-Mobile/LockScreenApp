package com.askhmer.lockscreen.network;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by soklundy on 6/24/2016.
 */
public class MyStringRequestCache extends StringRequest {
    public MyStringRequestCache(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public void deliverError(VolleyError error) {
        if (error instanceof NoConnectionError) {
            Cache.Entry entry = this.getCacheEntry();
            if(entry != null) {
                Response<String> response = parseNetworkResponse(new NetworkResponse(entry.data, entry.responseHeaders));
                deliverResponse(response.result);
                return;
            }
        }
        super.deliverError(error);
    }
}
