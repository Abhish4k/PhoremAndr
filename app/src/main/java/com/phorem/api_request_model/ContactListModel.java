package com.phorem.api_request_model;

import android.graphics.Bitmap;
import android.net.Uri;

public class ContactListModel {
    String name,  number;
    Bitmap image;
    Uri uri;

    public ContactListModel(String name, Bitmap image, String number, Uri uri) {
        this.name = name;
        this.image = image;
        this.number = number;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
