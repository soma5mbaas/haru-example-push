package com.haru.pushtest;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.haru.push.Push;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends ActionBarActivity implements LocationSource, LocationListener {

    private GoogleMap mGoogleMap;
    private FragmentManager mFragmentManager;
    private LocationManager mLocationManager;
    private OnLocationChangedListener mMapLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        Button sendMsgBtn = (Button) findViewById(R.id.pushMsgSend),
                sendNotiBtn = (Button) findViewById(R.id.pushNotiSend);

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Push push = new Push.MessageBuilder()
                        .setChannel("testChannel")
                        .setMessage("안녕하세요?\n" +
                                new SimpleDateFormat().format(new Date()) +
                                "에 보낸 테스트 메세지입니다!")
                        .build();
                push.sendInBackground();
            }
        });

        sendNotiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Push push = new Push.NotificationBuilder()
                        .setTitle("안녕하세요?")
                        .setChannel("testChannel")
                        .setMessage("앱에서도 알람을 보낼 수 있어요!")
                        .build();
                push.sendInBackground();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        setUpLocationTracking();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 구글맵을 셋업한다.
     */
    private void setUpMapIfNeeded() {
        if (mGoogleMap == null) {
            mGoogleMap = ((SupportMapFragment)mFragmentManager.findFragmentById(R.id.mapView))
                    .getMap();

            // Zoom, auto tracking enabled
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setLocationSource(this);
        }
    }

    private void setUpLocationTracking() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

        if(mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        if(mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    protected void onPause() {
        super.onDestroy();
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mMapLocationListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mMapLocationListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mMapLocationListener != null) {
            mMapLocationListener.onLocationChanged(location);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
