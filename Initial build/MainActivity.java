package com.legacy.ct.consolelauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText et;
    private PackageManager manager;
    private List apps;
    private List pkgs;
    private Animation shake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadApps();

        //assign animation
        shake = AnimationUtils.loadAnimation(this,R.anim.shakeanim);
        //set et equal to created view by id
        et = findViewById(R.id.enteredText);

        //listen for user pressing "Enter" on keyboard
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                    //get user input, make first letter upper case
                    String command = et.getText().toString().substring(0,1).toUpperCase()+et.getText().toString().substring(1);
                    //typing "ls" will launch AppDrawerActivity, app name will launch given app
                    //make sure app is installed
                    if(command.equalsIgnoreCase("ls")) {
                        Intent in = new Intent(getApplicationContext(), AppDrawerActivity.class);
                        startActivity(in);
                    } else if (apps.contains(command)) {
                        //get index of app
                        int ix = apps.indexOf(command);
                        //create Intent and launch app
                        Intent in = manager.getLaunchIntentForPackage(pkgs.get(ix).toString());
                        MainActivity.this.startActivity(in);
                    } else {
                        //shake animation
                        et.startAnimation(shake);
                    }
                }
                return false;
            }
        });
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

    //disable back button on this view
    @Override
    public void onBackPressed() {
    }
}
