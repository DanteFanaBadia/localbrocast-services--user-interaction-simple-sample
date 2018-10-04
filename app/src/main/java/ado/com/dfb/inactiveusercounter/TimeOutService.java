package ado.com.dfb.inactiveusercounter;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class TimeOutService extends Service {


    private static final String TAG = TimeOutService.class.getSimpleName();

    private final static Long TIME_OUT_MINUTES = 1L;
    private final IBinder mBinder;
    private boolean isSessionExpired;
    private Long interactionTime;
    private Long diffMinutes;
    private boolean stopThread = false;
    private boolean isBackground = false;

    public TimeOutService() {
        interactionTime = Calendar.getInstance().getTimeInMillis();
        mBinder = new LocalBinder<>(TimeOutService.this);
        isSessionExpired = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("custom-event-name"));
        sessionTimeOutChecker.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        stopThread = true;
    }

    public Long getDiffMinutes(){
        return diffMinutes;
    }

    public boolean hasSessionExpired() {
        return this.isSessionExpired;
    }

    public void expiredSession() {
        this.isSessionExpired = true;
        if(isForeground()){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void restartSession() {
                this.isSessionExpired = false;
        interactionTime =  Calendar.getInstance().getTimeInMillis();
    }

    private boolean isForeground() {
        return !isBackground;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        isBackground = ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN == level;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(isSessionExpired) return;
            interactionTime = intent.getLongExtra("time", 0L);
        }
    };

     private Thread sessionTimeOutChecker = new Thread(new Runnable() {
        @Override
        public void run() {
            while(!stopThread) {
                try {
                    Thread.sleep(1000);
                    diffMinutes = TimeUnit.MILLISECONDS.toMinutes(
                            Calendar.getInstance().getTimeInMillis() - interactionTime);
                    if(diffMinutes >= TIME_OUT_MINUTES && !isSessionExpired){
                        expiredSession();
                    }
                } catch (InterruptedException e) {}
            }
        }
     });
}
