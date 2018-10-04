package ado.com.dfb.inactiveusercounter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class AppInactiveUserCounter extends Application implements
        Application.ActivityLifecycleCallbacks   {

    private static final String TAG = AppInactiveUserCounter.class.getSimpleName();

    private ServiceConnector<TimeOutService> conn;

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, TimeOutService.class));
        registerActivityLifecycleCallbacks(this);
        conn = new ServiceConnector<>();
    }


    @Override
    public void onActivityStarted(Activity activity) {
        Intent intent = new Intent(activity, TimeOutService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    public void restartSession(){
        this.conn.getService().restartSession();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (!conn.isBound()) return;
        TimeOutService service = conn.getService();
        if(service.hasSessionExpired() && activity instanceof BaseAuthActivity) {
            Toast.makeText(activity, String.format("Session expired",service.getDiffMinutes()), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


    @Override public void onActivityDestroyed(Activity activity) { stopService(new Intent(activity, TimeOutService.class)); }
    @Override public void onActivityStopped(Activity activity) {}
    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
    @Override public void onActivityPaused(Activity activity) {}
    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
}