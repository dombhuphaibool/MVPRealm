package com.bandonleon.mvprealm;

import android.app.Application;

import com.bandonleon.mvprealm.manager.NetworkManager;
import com.bandonleon.mvprealm.manager.realm.RealmDataManager;
import com.bandonleon.mvprealm.manager.mock.MockNetworkManager;

/**
 * Created by dom on 11/4/16.
 */

public class MVPRealmApplication extends Application {

    private static MVPRealmApplication sAppInstance;

    public static MVPRealmApplication getInstance() {
        if (sAppInstance == null) {
            throw new IllegalStateException("MVPRealmApplication.onCreate() has not been called yet");
        }
        return sAppInstance;
    }

    private RealmDataManager mDataManager;
    private NetworkManager mNetworkManager;

    @Override
    public void onCreate() {
        sAppInstance = this;
        super.onCreate();

        mNetworkManager = new MockNetworkManager(this);
        mDataManager = new RealmDataManager(this, mNetworkManager);
    }

    public RealmDataManager getDataManager() {
        return mDataManager;
    }
}
