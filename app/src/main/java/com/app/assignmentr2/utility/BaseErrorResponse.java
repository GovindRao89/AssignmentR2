
package com.app.assignmentr2.utility;

/**
 * Created by Govind on 28-02-2016.
 */
public class BaseErrorResponse {

    private String mErrorCode = "";
    private String mErrorDescription = "";

    public String getmErrorCode() {
        return mErrorCode;
    }

    public void setmErrorCode(String mErrorCode) {
        this.mErrorCode = mErrorCode;
    }

    public String getmErrorDescription() {
        return mErrorDescription;
    }

    public void setmErrorDescription(String mErrorDescription) {
        this.mErrorDescription = mErrorDescription;
    }
}
