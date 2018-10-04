package ado.com.dfb.inactiveusercounter;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;

import java.util.Calendar;


public class MainActivity extends BaseAuthActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        Intent intent = new Intent("custom-event-name");
        intent.putExtra("time",  Calendar.getInstance().getTimeInMillis());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
