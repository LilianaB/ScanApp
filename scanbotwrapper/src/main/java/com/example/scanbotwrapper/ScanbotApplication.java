package com.example.scanbotwrapper;

import android.app.Application;
import net.doo.snap.ScanbotSDKInitializer;

/**
 * Created by Liliana Barrios on 07/10/16.
 */


/**
 * {@link ScanbotSDKInitializer} should be called
 * in {@code Application.onCreate()} method for RoboGuice modules initialization
 */
public class ScanbotApplication extends Application {
    @Override
    public void onCreate() {
        new ScanbotSDKInitializer().initialize(this);
        super.onCreate();
    }
}
