package edu.aubg.inf397.emergencyapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public final static String ITEMID = "edu.aubg.inf397.emergencyapp.ID";
    private GoogleApiClient mGoogleApiClient;
    private String mLatitude, mLongitude;

    private ProgressBar holdProgressBar;
    private CountDownTimer holdTimer;
    private Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        applicationContext = getApplicationContext();
        holdProgressBar = (ProgressBar) findViewById(R.id.holdProgressBar);

        holdTimer = new CountDownTimer(2000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                holdProgressBar.incrementProgressBy(5);
            }

            @Override
            public void onFinish() {
                holdProgressBar.incrementProgressBy(5);
                LocationManager locationManager = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    SmsManager smsManager = SmsManager.getDefault();
                    if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        smsManager.sendTextMessage("+359988968509", null, "Help! I am an AUBG student in an emergency situation!\nMy location is: http://maps.google.com/?q=" + mLatitude + "," + mLongitude, null, null);
                        Toast.makeText(applicationContext, "SMS sent! Keep calm!", Toast.LENGTH_LONG).show();
                    } else {
                        holdProgressBar.setProgress(0);
                        this.cancel();
                        Toast.makeText(applicationContext, "Please grant SMS permission.", Toast.LENGTH_LONG).show();
                        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myAppSettings);
                    }
                } else {
                    holdProgressBar.setProgress(0);
                    this.cancel();
                    Toast.makeText(applicationContext, "Please turn Location on.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }
        };

        ImageButton emergencyButton = (ImageButton) findViewById(R.id.emergencyButton);
        emergencyButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        holdTimer.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        holdTimer.cancel();
                        holdProgressBar.setProgress(0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                mLatitude = String.valueOf(mLastLocation.getLatitude());
                mLongitude = String.valueOf(mLastLocation.getLongitude());
            }
        } else {
            holdProgressBar.setProgress(0);
            holdTimer.cancel();
            Toast.makeText(applicationContext, "Please grant Location permission.", Toast.LENGTH_LONG).show();
            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myAppSettings);
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d("Suspension Cause: ", Integer.toString(cause));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.d("Connection Result: ", result.toString());
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            LocationManager locationManager = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Intent mapIntent = new Intent(this, EmergencyMapScreen.class);
                startActivity(mapIntent);
            } else {
                Toast.makeText(applicationContext, "Please turn Location on.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        } else {
            switch (id) {
                case R.id.nav_emotional:
                    id = 1;
                    break;
                case R.id.nav_medical:
                    id = 2;
                    break;
                case R.id.nav_threat:
                    id = 3;
                    break;
                case R.id.nav_weapon:
                    id = 4;
                    break;
            }

            Intent intent = new Intent(this, HelpDetailsScreen.class);
            intent.putExtra(ITEMID, id);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

}
