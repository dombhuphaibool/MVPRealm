package com.bandonleon.mvprealm.view;

import android.support.annotation.NonNull;

/**
 * Created by dom on 11/5/16.
 */

public interface PersonView {
    void setFirstName(@NonNull String firstName);
    void setLastName(@NonNull String lastName);
    void setAge(@NonNull String age);
    void setPhoneNumber(@NonNull String phoneNumber);
}
