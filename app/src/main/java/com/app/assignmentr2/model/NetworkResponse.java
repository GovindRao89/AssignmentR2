package com.app.assignmentr2.model;

import com.app.assignmentr2.utility.BaseErrorResponse;

import java.util.List;

/**
 * Created by Govind on 27-02-2016.
 */
public class NetworkResponse extends BaseErrorResponse {

    private String version = "";
    private List<Words> wordsList = null;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Words> getWordsList() {
        return wordsList;
    }

    public void setWordsList(List<Words> wordsList) {
        this.wordsList = wordsList;
    }

    @Override
    public String toString() {
        return "NetworkResponse{" +
                "version='" + version + '\'' +
                ", wordsList=" + wordsList +
                '}';
    }
}
