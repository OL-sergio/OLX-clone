package exemple.udemy.java.olx.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SettingsFirebase {

    public static DatabaseReference databaseReference;
    public static FirebaseAuth getFirebaseAuth;
    public static StorageReference storageReference;


    public static String getUserID(){
        FirebaseAuth auth = getFirebaseAuth();
        return auth.getCurrentUser().getUid();
    }


    public static DatabaseReference getDatabaseReference(){
        if(databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }


    public static FirebaseAuth getFirebaseAuth(){

        if(getFirebaseAuth == null){
            getFirebaseAuth = FirebaseAuth.getInstance();
        }
        return getFirebaseAuth;
    }


    public static StorageReference getStorageReference(){
        if(storageReference == null){
            storageReference = FirebaseStorage.getInstance().getReference();
        }
    return storageReference;
    }
}
