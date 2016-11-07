package com.bandonleon.mvprealm.model.realm;

import android.support.annotation.NonNull;

import com.bandonleon.mvprealm.model.Person;

import io.realm.RealmObject;

/**
 * Created by dom on 11/4/16.
 */

public class RealmPerson extends RealmObject implements Person {
    /*
    @PrimaryKey
    private long mId;
    */
    private String mFirstName;
    private String mLastName;
    private int mAge;
    private String mPhoneNumber;

    public RealmPerson() {
    }

    public RealmPerson(@NonNull String firstName, @NonNull String lastName, int age, @NonNull String phoneNumber) {
        mFirstName = firstName;
        mLastName = lastName;
        mAge = age;
        mPhoneNumber = phoneNumber;
    }
    /*
    public long getId() {
        return mId;
    }
    */
    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        mAge = age;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }
}
