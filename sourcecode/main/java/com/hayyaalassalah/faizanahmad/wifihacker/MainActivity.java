package com.hayyaalassalah.faizanahmad.wifihacker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends Activity {

    TextView mainText;
    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    StringBuilder sb = new StringBuilder();
    ListView listview;
    ArrayAdapter adapter;
    ArrayList<String> ssids = new ArrayList<String>();
    ArrayList<String> capabilities = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Lato-Light.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        setContentView(R.layout.activity_main);


        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> networkList = wifi.getScanResults();
        for (ScanResult network : networkList)
        {
            ssids.add(network.SSID.toString());
            capabilities.add(network.capabilities);
            String Capabilities =  network.capabilities;
            System.out.println(network.SSID + " capabilities : " + Capabilities);

        }


        WifiManager wifiManager2 = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager2.isWifiEnabled() == false) {
            wifiManager2.setWifiEnabled(true);
        }

        listview = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter(getApplicationContext(),R.layout.text_view,ssids);
        listview.setAdapter(adapter);

        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), CrackActivity.class);
                System.out.println(ssids.get(position));
                intent.putExtra("ssid", ssids.get(position));
                intent.putExtra("detail", capabilities.get(position));
               // unregisterReceiver(receiverWifi);
                startActivity(intent);
            }
        });

    }

    public String getWifiName(Context context) {
        String ssid = "none";
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState()) == NetworkInfo.DetailedState.CONNECTED) {
            ssid = wifiInfo.getSSID();
        }
        return ssid;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Refresh");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
       // mainWifi.startScan();
        //mainText.setText("Starting Scan");
        return super.onMenuItemSelected(featureId, item);
    }

    protected void onPause() {
        //unregisterReceiver(receiverWifi);
        super.onPause();
    }

    public void scan(View view){

        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> networkList = wifi.getScanResults();
        for (ScanResult network : networkList)
        {
            if(ssids.contains(network.SSID.toString()) == false) {
                ssids.add(network.SSID.toString());
                capabilities.add(network.capabilities);
            }
            String Capabilities =  network.capabilities;
            System.out.println(network.SSID + " capabilities : " + Capabilities);

        }
        listview.invalidate();
        adapter.notifyDataSetChanged();


    }



    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {

            wifiList = mainWifi.getScanResults();

            for (int i = 0; i < wifiList.size(); i++) {
                if(ssids.contains((wifiList.get(i)).SSID.toString()) == false) {
                    ssids.add((wifiList.get(i)).SSID.toString());

                }
            }
            System.out.println(ssids);
            adapter.notifyDataSetChanged();
            listview.invalidate();
        }
    }

    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if (wifiInfo.getNetworkId() == -1) {
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        } else {
            return false; // Wi-Fi adapter is OFF
        }
    }

    public void instructions(View view){
        Intent intent = new Intent(this,InstructionsClass.class);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



}


