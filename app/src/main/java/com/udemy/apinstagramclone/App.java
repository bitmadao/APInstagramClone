package com.udemy.apinstagramclone;

import android.app.Application;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()

        );

        ParseQuery<ParseObject> installationCount = ParseQuery.getQuery("Installation");

        installationCount.findInBackground(
                new FindCallback<ParseObject>() {
                                               @Override
                                               public void done(List<ParseObject> objects, ParseException e) {
                                                   Log.i("TurdBowl", "Performing punchbowl check.");
                                                   if(e == null && objects.size() > 1) {
                                                       Log.i("TurdBowl",
                                                               "Alert! there is a turd in the punchbowl!\nInstallation count is " +
                                                               objects.size()
                                                       );
                                                       FancyToast.makeText(getApplicationContext(),
                                                               "Alert! there is a turd in the punchbowl!\nInstallation count is " +
                                                                       objects.size()
                                                               ,
                                                               FancyToast.LENGTH_LONG,
                                                               FancyToast.WARNING,
                                                               true
                                                       ).show();
                                                   } else if (e != null){
                                                       Log.i("TurdBowl", e.getMessage());
                                                   } else {
                                                       Log.i("TurdBowl", "Nothing to report. objects.size(): " + objects.size());
                                                   }

                                                   Log.i("TurdBowl", "End of punchbowl check!");

                                               }
                                           }
        );

    }
}
