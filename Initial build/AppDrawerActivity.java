package com.legacy.ct.consolelauncher;

import android.widget.AdapterView;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AppDrawerActivity extends AppCompatActivity {

    private PackageManager manager;
    private ListView list;
    private List apps;
    private List pkgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_drawer);
        //user instances
        loadApps();
        populateAppDrawer();
        addClickListener();
    }

    //populate 'apps' List with application names and icons
    private void loadApps() {
        //used instances
        manager = getPackageManager();
        apps = new ArrayList<>();
        pkgs = new ArrayList<>();
        //create Intent and set as launcher type
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        //get package and app name and icon from each app found and add to list
        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for(ResolveInfo ri : availableActivities) {
             pkgs.add(ri.activityInfo.packageName);
             //app.icon = ri.loadIcon(manager);
             apps.add(ri.loadLabel(manager).toString());
        }
    }

    //populate AppDrawer ListView with 'apps'
    private void populateAppDrawer() {
        //build Adapter
        ArrayAdapter<List> adptr = new ArrayAdapter<List>(this.getApplicationContext(), R.layout.views, apps);
        //assign ListView and apply Adapter
        list = (ListView) findViewById(R.id.appList);
        list.setAdapter(adptr);
    }

    //listen for click on ListView
    private void addClickListener(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                Intent i;
                i = manager.getLaunchIntentForPackage(pkgs.get(pos).toString());
                AppDrawerActivity.this.startActivity(i);
            }
        });
    }
}