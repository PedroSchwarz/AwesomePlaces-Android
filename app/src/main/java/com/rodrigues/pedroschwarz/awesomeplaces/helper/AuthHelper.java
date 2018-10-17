package com.rodrigues.pedroschwarz.awesomeplaces.helper;

import com.google.firebase.auth.FirebaseAuth;

public class AuthHelper {

    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    public static String getUserUid() {
        return getAuth().getCurrentUser().getUid();
    }

}
