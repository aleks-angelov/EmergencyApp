package edu.aubg.inf397.emergencyapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpDetailsScreen extends AppCompatActivity {

    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_details_screen);

        Intent intent = getIntent();
        int id = intent.getIntExtra(HomeScreen.ITEMID, -1);
        ImageView photo = (ImageView) findViewById(R.id.contactPhoto);
        switch (id) {
            case 1:
                setTitle("Emotional Crisis");
                photo.setImageResource(R.drawable.zhulieta_kuzmanska);
                break;
            case 2:
                setTitle("Medical Distress");
                photo.setImageResource(R.drawable.ventsislav_daskalov);
                break;
            case 3:
                setTitle("Threat of Violence");
                photo.setImageResource(R.drawable.ilko_vangelov);
                break;
            case 4:
                setTitle("Weapon on Campus");
                photo.setImageResource(R.drawable.ilko_vangelov);
                break;
        }

        ArrayList<String> contactDetail = ExpandableListDataPump.getInfo(id);
        TextView name = (TextView) findViewById(R.id.contactName);
        name.append(contactDetail.get(0));
        TextView assistance = (TextView) findViewById(R.id.providedAssistance);
        assistance.append(contactDetail.get(1));
        TextView location = (TextView) findViewById(R.id.officeLocation);
        location.append(contactDetail.get(2));
        TextView hours = (TextView) findViewById(R.id.workingHours);
        hours.append(contactDetail.get(3));
        number = contactDetail.get(4);

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        HashMap<String, List<String>> expandableListDetail = ExpandableListDataPump.getData(id);
        List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        ExpandableListAdapter expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.expandGroup(0);
        expandableListView.expandGroup(1);
    }

    public void phoneContact(View view) {
        Context applicationContext = getApplicationContext();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        } else {
            Toast.makeText(applicationContext, "Please grant Phone permission.", Toast.LENGTH_LONG).show();
            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myAppSettings);
        }
    }
}
