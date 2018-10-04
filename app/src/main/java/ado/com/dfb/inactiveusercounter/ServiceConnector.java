package ado.com.dfb.inactiveusercounter;

import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ServiceConnector<T extends Service> implements ServiceConnection {

    private boolean isBound;
    private T mService;

    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
        mService = ((LocalBinder <T>)service).getService();
        isBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) { isBound = false; }

    public T getService() {
        return mService;
    }

    public boolean isBound() {
        return isBound;
    }
}
