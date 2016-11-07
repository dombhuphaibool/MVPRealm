package com.bandonleon.mvprealm.model.mock;

import android.support.annotation.NonNull;

import com.bandonleon.mvprealm.model.Person;

/**
 * Created by dom on 11/6/16.
 */

public class MockPerson implements Person {

    private String mFirstName;
    private String mLastName;
    private int mAge;
    private String mPhoneNumber;

    public MockPerson(@NonNull Person person) {
        mFirstName = person.getFirstName();
        mLastName = person.getLastName();
        mAge = person.getAge();
        mPhoneNumber = person.getPhoneNumber();
    }

    public MockPerson(@NonNull String firstName, @NonNull String lastName, int age, @NonNull String phoneNumber) {
        mFirstName = firstName;
        mLastName = lastName;
        mAge = age;
        mPhoneNumber = phoneNumber;
    }

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
