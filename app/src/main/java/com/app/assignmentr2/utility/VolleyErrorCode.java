
package com.app.assignmentr2.utility;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by Govind on 28-02-2016.
 */
public class VolleyErrorCode {
    public static int getErrorCode(VolleyError error) {
        if (error instanceof NetworkError) {
            return AxisWalletConstants.HTTP_NETWORK_ERROR;
        } else if (error instanceof ServerError) {
            return AxisWalletConstants.HTTP_SERVER_ERROR;
        } else if (error instanceof AuthFailureError) {
            return AxisWalletConstants.HTTP_UNAUTHORIZED_ERROR;
        } else if (error instanceof ParseError) {
            return AxisWalletConstants.HTTP_PARSE_ERROR;
        } else if (error instanceof NoConnectionError) {
            return AxisWalletConstants.HTTP_NO_CONNECTION_ERROR;
        } else if (error instanceof TimeoutError) {
            return AxisWalletConstants.HTTP_TIME_OUT_ERROR;
        }
        return 0;
    }
}
