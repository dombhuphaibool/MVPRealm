package com.bandonleon.mvprealm.manager;

import com.bandonleon.mvprealm.model.People;

/**
 * Created by dom on 11/6/16.
 */

public interface DataManager {
    People getPeople();
    void addPerson();
    void removePerson();
}
