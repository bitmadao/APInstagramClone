package com.udemy.apinstagramclone;

import android.app.Application;

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
                                                   if(e == null && objects.size() > 1) {
                                                       FancyToast.makeText(getApplicationContext(),
                                                               "Alert! there is a turd in the punchbowl!\nInstallation count is " +
                                                                       objects.size()
                                                               ,
                                                               FancyToast.LENGTH_LONG,
                                                               FancyToast.WARNING,
                                                               true
                                                       ).show();
                                                   }

                                               }
                                           }
        );

    }
}
