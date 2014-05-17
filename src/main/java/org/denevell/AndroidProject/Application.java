package org.denevell.AndroidProject;

import com.squareup.otto.Bus;

public class Application extends android.app.Application {
    protected static final String TAG = "AndroidProject Application class";
    private static Bus sEventBus;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Bus getEventBus() {
        if(sEventBus==null) {
            sEventBus = new com.squareup.otto.Bus();
        }
        return sEventBus;
    }

}
