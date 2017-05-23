package lds.historialpager;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by Tano on 27/4/2017.
 * No Fue Magia
 */

public class Aplicacion extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
