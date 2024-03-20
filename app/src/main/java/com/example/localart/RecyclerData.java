package com.example.localart;

import static okhttp3.internal.Internal.instance;

import com.google.android.gms.common.data.DataHolder;

import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;

import okhttp3.Address;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RouteDatabase;
import okhttp3.internal.connection.StreamAllocation;

public class RecyclerData {

    private String title;
    private int imgid;
    private static RecyclerData instance;
    private String totalAmount;

    public RecyclerData() {

    }

    public static synchronized RecyclerData getInstance() {
        if (instance == null) {
            instance = new RecyclerData();
        }
        return instance;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public RecyclerData(String title, int imgid) {
        this.title = title;
        this.imgid = imgid;
    }
}
