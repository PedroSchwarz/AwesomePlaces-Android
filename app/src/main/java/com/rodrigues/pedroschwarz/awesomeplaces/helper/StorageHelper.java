package com.rodrigues.pedroschwarz.awesomeplaces.helper;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StorageHelper {

    public static StorageReference getRootStorage() {
        return FirebaseStorage.getInstance().getReference();
    }

    public static StorageReference getPathStorage(String path) {
        return getRootStorage().child(path);
    }
}
