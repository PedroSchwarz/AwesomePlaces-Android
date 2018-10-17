package com.rodrigues.pedroschwarz.awesomeplaces.helper;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseHelper {

    public static FirebaseFirestore getRootRef() {
        return FirebaseFirestore.getInstance();
    }

    public static CollectionReference getUsersRef() {
        return getRootRef().collection("Users");
    }

}
