package com.hayyaalassalah.faizanahmad.wifihacker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Faizan Ahmad on 1/1/2017.
 */
public class ConnectivityBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("RECEIVEDDDDDDDDDDDD");
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {
            // Do your work.
            System.out.println("CONNECTEDDDDDDDDDDDDDDDDDDD");
            // e.g. To check the Network Name or other info:

        }
    }
}
