package com.app.assignmentr2.model;

import android.graphics.Bitmap;

/**
 * Created by Govind on 27-02-2016.
 */
public class Words {

    private String id = "";
    private String ratio = "";
    private String word = "";
    private String meaning = "";
    private String variant = "";
    private String uri;
    private byte[] bitmap;


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public byte[] getBitmap() {
        return bitmap;
    }

    public void setBitmap(byte[] bitmap) {
        this.bitmap = bitmap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    @Override
    public String toString() {
        return "Words{" +
                "id='" + id + '\'' +
                ", ratio='" + ratio + '\'' +
                ", word='" + word + '\'' +
                ", meaning='" + meaning + '\'' +
                ", variant='" + variant + '\'' +
                ", uri=" + uri +
                '}';
    }
}