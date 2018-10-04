package ado.com.dfb.inactiveusercounter;

import android.app.Service;
import android.os.Binder;

public class LocalBinder<T2 extends Service> extends Binder {
    private T2 service;

    public LocalBinder(T2 service) {
        this.service = service;
    }

    public T2 getService() {
        return service;
    }
}

