package com.bandonleon.mvprealm.manager.mock;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.bandonleon.mvprealm.manager.NetworkManager;
import com.bandonleon.mvprealm.model.Person;
import com.bandonleon.mvprealm.model.mock.MockPeople;
import com.bandonleon.mvprealm.model.realm.RealmPerson;

import java.util.Random;

/**
 * Created by dom on 11/5/16.
 */

public class MockNetworkManager implements NetworkManager {

    // Simulated delays are in milliseconds
    private static final int ADD_PERSON_NETWORK_DELAY = 750;
    private static final int REMOVE_PERSON_NETWORK_DELAY = 550;

    private Handler mMainHandler;
    private MockPeople mMockPeople;

    public MockNetworkManager(@NonNull Context context) {
        mMainHandler = new Handler(Looper.getMainLooper());
        initMockModel();
    }

    private void initMockModel() {
        mMockPeople = new MockPeople();
        mMockPeople.add(new RealmPerson("Jose", "Cuervos", 35, "415-466-1234"));
        mMockPeople.add(new RealmPerson("Maria", "Santana", 28, "206-555-4321"));
        mMockPeople.add(new RealmPerson("Osvaldo", "Rugierro", 45, "303-444-3784"));
        mMockPeople.add(new RealmPerson("Rebecca", "Santos", 33, "209-498-7233"));
        mMockPeople.add(new RealmPerson("Rodolfo", "Borrego", 52, "516-525-9098"));
        mMockPeople.add(new RealmPerson("Cindy", "Torres", 26, "650-333-4567"));

        mMockPeople.add(new RealmPerson("Silvia", "Rodrigues", 35, "415-888-5432"));
        mMockPeople.add(new RealmPerson("Carlos", "Dominguez", 28, "209-225-9095"));
        mMockPeople.add(new RealmPerson("Mercedes", "Plaza", 45, "303-242-1985"));
        mMockPeople.add(new RealmPerson("Juanita", "Gonzalez", 33, "516-498-8893"));
        mMockPeople.add(new RealmPerson("Ricardo", "Bollero", 52, "715-525-9098"));
        mMockPeople.add(new RealmPerson("Cecilia", "Blanca", 26, "650-258-4567"));
    }

    public void getNextPerson(final int index, @NonNull final NextPersonResponseHandler responseHandler) {
        new SleepWorkerThread(ADD_PERSON_NETWORK_DELAY, new Runnable() {
            @Override
            public void run() {
                final Person nextPerson = mMockPeople.get(index % mMockPeople.getCount());
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        responseHandler.onSuccess(nextPerson);
                    }
                });
            }
        }).start();
    }

    public void removePerson(final int index, @NonNull final RemovePersonResponseHandler responseHandler) {
        new SleepWorkerThread(REMOVE_PERSON_NETWORK_DELAY, new Runnable() {
            @Override
            public void run() {
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (index >= 0) {
                            responseHandler.onSuccess();
                        } else {
                            responseHandler.onFailure("Removal index must be greater than zero.");
                        }
                    }
                });
            }
        }).start();
    }

    private static class SleepWorkerThread extends Thread {
        private static final Random sRandom = new Random();

        long mSleepMs;

        public SleepWorkerThread(long sleepMs, @NonNull Runnable runnable) {
            super(runnable);
            long randomMs = Math.abs(sRandom.nextLong()) % 1000;
            mSleepMs = sleepMs + randomMs;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(mSleepMs);
            } catch (InterruptedException ex) {
                // Ignore exception
            }
            super.run();
        }
    }
}
