package com.rodrigues.pedroschwarz.awesomeplaces.helper;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DatabaseHelper {

    public static FirebaseFirestore getRootRef() {
        return FirebaseFirestore.getInstance();
    }

    public static CollectionReference getUsersRef() {
        return getRootRef().collection("Users");
    }

    public static CollectionReference getPlacesRef() {
        return getRootRef().collection("Places");
    }

    public static DocumentReference getPlaceRef(String placeId) {
        return getPlacesRef().document(placeId);
    }

    public static CollectionReference getCommentsRef() {
        return getRootRef().collection("Comments");
    }
}
