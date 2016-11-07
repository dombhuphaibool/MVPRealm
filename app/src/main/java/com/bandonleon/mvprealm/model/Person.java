package com.bandonleon.mvprealm.model;

/**
 * Created by dom on 11/6/16.
 */

public interface Person {

    String getFirstName();
    void setFirstName(String firstName);

    String getLastName();
    void setLastName(String lastName);

    int getAge();
    void setAge(int age);

    String getPhoneNumber();
    void setPhoneNumber(String phoneNumber);
}
