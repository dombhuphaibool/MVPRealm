package com.bandonleon.mvprealm.manager.realm;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bandonleon.mvprealm.manager.DataManager;
import com.bandonleon.mvprealm.manager.NetworkManager;
import com.bandonleon.mvprealm.manager.NetworkManager.NextPersonResponseHandler;
import com.bandonleon.mvprealm.manager.NetworkManager.RemovePersonResponseHandler;
import com.bandonleon.mvprealm.model.People;
import com.bandonleon.mvprealm.model.Person;
import com.bandonleon.mvprealm.model.realm.RealmPeople;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.realm.Realm;

/**
 * Created by dom on 11/5/16.
 */

public class RealmDataManager implements DataManager {

    private NetworkManager mNetworkManager;
    private People mPeople;
    private Map<Integer, Integer> mRemovalPendingMap;
    private int mNextPersonIndex;

    public RealmDataManager(@NonNull Context context, @NonNull NetworkManager networkManager) {
        mNetworkManager = networkManager;
        initRealm(context);
        initPeople();
        mRemovalPendingMap = new HashMap<>();
    }

    private void initRealm(@NonNull Context context) {
        Realm.init(context);
        /*
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1) // Must be bumped when the schema changes
                .migration(new RealmMigration() {
                    @Override
                    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                        // DynamicRealm exposes an editable schema
                        RealmSchema schema = realm.getSchema();

                        if (oldVersion == 0) {
                            schema.create("RealmPerson")
                                    .addField("mId", long.class, FieldAttribute.PRIMARY_KEY)
                                    .addField("mFirstName", String.class)
                                    .addField("mLastName", String.class)
                                    .addField("mAge", int.class)
                                    .addField("mPhoneNumber", String.class);
                            oldVersion++;
                        }
                    }
                }) // Migration to run instead of throwing an exception
                .build();
        */
        /*
        RealmConfiguration config = new RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
         */
    }

    private void initPeople() {
        mPeople = new RealmPeople();
        mNextPersonIndex = mPeople.getCount();
    }

    public People getPeople() {
        return mPeople;
    }

    public void addPerson() {
        mNetworkManager.getNextPerson(mNextPersonIndex++, new NextPersonResponseHandler() {
            @Override
            public void onSuccess(@NonNull Person nextPerson) {
                mPeople.add(nextPerson);
            }

            @Override
            public void onFailure(String message) {
                mPeople.onAddError(message);
            }
        });
    }

    public void removePerson() {
        // @TODO: We should return something to indicate that a request in progress
        // (show transient state), then update when it comes back
        final int indexToRemove = (mPeople.getCount() - 1) - mRemovalPendingMap.size();
        mRemovalPendingMap.put(indexToRemove, indexToRemove);
        mNetworkManager.removePerson(indexToRemove, new RemovePersonResponseHandler() {
            @Override
            public void onSuccess() {
                for (Entry<Integer, Integer> entry : mRemovalPendingMap.entrySet()) {
                    if (entry.getKey() > indexToRemove) {
                        entry.setValue(entry.getValue() - 1);
                    }
                }
                Integer removeIndex = mRemovalPendingMap.get(indexToRemove);
                if (removeIndex != null) {
                    mRemovalPendingMap.remove(indexToRemove);
                    mPeople.remove(removeIndex);
                } else {
                    // Fatal error, clear and reset all pending removal
                    mRemovalPendingMap.clear();
                    onFailure("Invalid removal index " + indexToRemove);
                }
            }

            @Override
            public void onFailure(String message) {
                mPeople.onRemoveError(message);
            }
        });
    }
}
