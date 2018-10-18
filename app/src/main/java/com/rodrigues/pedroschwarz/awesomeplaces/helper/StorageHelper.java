package com.rodrigues.pedroschwarz.awesomeplaces.helper;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class StorageHelper {

    public static StorageReference getRootStorage() {
        return FirebaseStorage.getInstance().getReference();
    }

    public static StorageReference getPathStorage(String path) {
        return getRootStorage().child(path);
    }

    public static String getUserImagePath() {
        return "profile_images/" + AuthHelper.getUserUid() + ".jpg";
    }

    public static String getPlaceImagePath() {
        return "places_images/" + AuthHelper.getUserUid() + "-" + Calendar.getInstance().getTimeInMillis() + ".jpg";
    }
}
