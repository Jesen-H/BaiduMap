package com.hgeson.simplebaidumap.utils;

import com.baidu.mapapi.model.LatLng;

/**
 * @Describe：
 * @Date：2018/9/28
 * @Author：hgeson
 */

public class AddressBean {
    private String city;
    private String name;
    private String address;
    private String phoneNum;
    private String uid;
    private String postcode;
    private String longitude;
    private String latitude;

    public AddressBean(String city, String name, String address, String phoneNum, String uid, String postcode, String longitude, String latitude) {
        this.city = city;
        this.name = name;
        this.address = address;
        this.phoneNum = phoneNum;
        this.uid = uid;
        this.postcode = postcode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
