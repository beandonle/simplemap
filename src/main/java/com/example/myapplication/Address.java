package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;

public class Address {
    private String mAddress;
    private LatLng mLatLng;

    public Address(String address, LatLng latlng)
    {
        mAddress = address;
        mLatLng = latlng;
    }

    public String getAddress()
    {
        return mAddress;
    }

    public void setAddress(String address)
    {
        mAddress = address;
    }

    public LatLng getLatLng()
    {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng)
    {
        mLatLng = latLng;
    }

}
