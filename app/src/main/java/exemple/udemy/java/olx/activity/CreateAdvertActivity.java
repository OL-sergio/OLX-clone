package exemple.udemy.java.olx.activity;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santalu.maskara.widget.MaskEditText;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import exemple.udemy.java.olx.R;
import exemple.udemy.java.olx.databinding.ActivityCreateAdvertBinding;
import exemple.udemy.java.olx.helper.SettingsFirebase;
import exemple.udemy.java.olx.model.Advert;
import exemple.udemy.java.olx.utilities.CustomHorizontalProgressDialog;

public class CreateAdvertActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityCreateAdvertBinding binding;

    private EditText createAdvertTitle;
    private EditText createAdvertDescription;
    private CurrencyEditText createAdvertPrice;
    private MaskEditText createAdvertPhoneNumber;
    private Spinner spinnerAdvertCategory;
    private Spinner spinnerAdvertState;
    private Button createAdvert;
    private ImageView imageViewAdvertA;
    private ImageView imageViewAdvertB;
    private ImageView imageViewAdvertC;
    private CustomHorizontalProgressDialog dialog;

    private Advert advert;
    private FirebaseAuth auth;

    private static final int STORAGE_PERMISSION_CODE = 23;

    private final List<byte[]> listOfPhotos = new ArrayList<>();
    private final List<String> listOfPhotosUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAdvertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbarCreateAdvert;
        toolbar.setTitle(R.string.olx);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MyAdvertsActivity.class);
            startActivity(intent);
            finish();
        });

        dialog = new CustomHorizontalProgressDialog(this);




        Objects.requireNonNull(getSupportActionBar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //storageReference = SettingsFirebase.getStorageReference();
        auth = SettingsFirebase.getFirebaseAuth();


        components();
        loadSpinner();

        if (!checkStoragePermissions()) {
            requestForStoragePermissions();

        } else {
            // AndroidPermissions are granted, proceed with your logic

        }
        /*
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getColor(R.color.hot_pink_200));
        */
        createAdvert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                validateDataAdvert(view);
                saveAdvert();
            }
        });


    }

    private void loadSpinner() {
       /* String[] statesStrings = new String[]{
          "SP", "MT"
        };*/

        String[] statesStrings = getResources().getStringArray(R.array.states);
        ArrayAdapter<String> adapterStates = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, statesStrings);
        adapterStates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdvertState.setAdapter(adapterStates);

        String[] categoriesStrings = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, categoriesStrings);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdvertCategory.setAdapter(adapterCategories);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.imageView_create_advert_a) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY));
                galleryActivityResultLauncherImageA.launch(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryActivityResultLauncherImageA.launch(intent);
            }
        }

        if (view.getId() == R.id.imageView_create_advert_b) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT,  MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY));
                galleryActivityResultLauncherImageB.launch(intent);
            }else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryActivityResultLauncherImageB.launch(intent);
            }
        }

        if (view.getId() == R.id.imageView_create_advert_c) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY));
                galleryActivityResultLauncherImageC.launch(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryActivityResultLauncherImageC.launch(intent);
            }
        }

        Log.d(TAG, "listOfPhotos"+ listOfPhotos);
    }

    ActivityResultLauncher<Intent>  galleryActivityResultLauncherImageA = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
               if (result.getResultCode() == Activity.RESULT_OK && result.getData()!= null) {

                    Uri uri = result.getData().getData();
                    Log.d(TAG, "onActivityResult"+ uri);

                    Bitmap imageUrl = null;
                    try {
                        imageUrl = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageUrl, 200, 200, true);

                        Glide.with(this)
                                .load(resizedBitmap)
                                .into(imageViewAdvertA);

                        imageViewAdvertA.setDrawingCacheEnabled(true);
                        imageViewAdvertA.buildDrawingCache();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imageUrl.compress(Bitmap.CompressFormat.JPEG, 65, baos);
                        byte [] dataImage = baos.toByteArray();

                        listOfPhotosToArray(dataImage);


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

    ActivityResultLauncher<Intent>  galleryActivityResultLauncherImageB = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData()!= null) {

                    Uri uri = result.getData().getData();
                    Log.d(TAG, "onActivityResult"+ uri);

                    Bitmap imageUrl = null;

                    try {
                        imageUrl = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageUrl, 400, 400, true);

                        Glide.with(this)
                                .load(resizedBitmap)
                                .into(imageViewAdvertB);

                        imageViewAdvertB.setDrawingCacheEnabled(true);
                        imageViewAdvertB.buildDrawingCache();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imageUrl.compress(Bitmap.CompressFormat.JPEG, 65, baos);
                        byte [] dataImage = baos.toByteArray();

                        listOfPhotosToArray(dataImage);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

    ActivityResultLauncher<Intent>  galleryActivityResultLauncherImageC = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData()!= null) {

                    Uri uri = result.getData().getData();
                    Log.d(TAG, "onActivityResult"+ uri);

                    Bitmap imageUrl = null;
                    try {
                        imageUrl = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageUrl, 400, 400, true);

                        Glide.with(this)
                                .load(resizedBitmap)
                                .into(imageViewAdvertC);

                        imageViewAdvertC.setDrawingCacheEnabled(true);
                        imageViewAdvertC.buildDrawingCache();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imageUrl.compress(Bitmap.CompressFormat.JPEG, 65, baos);
                        byte [] dataImage = baos.toByteArray();
                        listOfPhotosToArray(dataImage);


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });


    private void listOfPhotosToArray(byte[] imageUrl) {
        listOfPhotos.add(imageUrl);
    }



   public boolean checkStoragePermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11 (R) or above
            return Environment.isExternalStorageManager();
        }else {
            //Below android 11
            int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestForStoragePermissions() {
        //Android is 11 (R) or above
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                storageActivityResultLauncher.launch(intent);
            }catch (Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                storageActivityResultLauncher.launch(intent);
            }
        }else{
            //Below android 11
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    STORAGE_PERMISSION_CODE
            );
        }
    }

    private final ActivityResultLauncher<Intent> storageActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>(){

                        @Override
                        public void onActivityResult(ActivityResult o) {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                                //Android is 11 (R) or above
                                if(Environment.isExternalStorageManager()){
                                    //Manage External Storage Permissions Granted
                                    Log.d(TAG, "onActivityResult: Manage External Storage Permissions Granted");
                                }else{
                                    Toast.makeText(CreateAdvertActivity.this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                                    alertValidatePermissions();
                                }
                            }else{
                                //Below android 11

                            }
                        }
                    });


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0){
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(read && write){
                    Toast.makeText(CreateAdvertActivity.this, "Storage Permissions Granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CreateAdvertActivity.this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                    alertValidatePermissions();
                }
            }
        }
    }

    public void validateDataAdvert(View view) {

      advert = advertConfiguration();
      String price = String.valueOf(createAdvertPrice.getRawValue());

        if( listOfPhotos.size() != 0 ){
            if(!advert.getState().isEmpty()) {
                if(!advert.getCategory().isEmpty()) {
                    if (!advert.getTitle().isEmpty()){
                        if (!price.isEmpty() && !price.equals("0")){
                            if (!advert.getPhone().isEmpty() && advert.getPhone().length() >= 10){
                                if (!advert.getDescription().isEmpty()){
                                    saveAdvert();
                                }else {
                                    alertMessage("Intreduza uma descrição!");
                                }
                            }else {
                                alertMessage("Intreduza o numero telemovel!");
                            }
                        }else {
                            alertMessage("Intreduza um preço!");
                        }
                    }else {
                        alertMessage("Intreduza um título!");
                    }
                }else{
                    alertMessage("Selecione pelo menos categoria!");
                }
            }else {
                alertMessage("Selecione pelo menos estado!");
            }
        } else {
            alertMessage("Selecione pelo menos uma foto!");
        }
    }

    private void saveAdvert() {
        // Adds the size of the Array of photos
        for (int counter = 0; counter < listOfPhotos.size(); counter++) {
            byte [] urlImages = listOfPhotos.get(counter);

            showProgressDialog();

            int listSize = listOfPhotos.size();
            Log.d(TAG, "images" + listSize + " " + urlImages);
            saveStorageImages(urlImages, listSize, counter);

        }
    }



   private void saveStorageImages(byte[] urlImages, int listSize, int counter) {
        Log.d(TAG, "images" + Arrays.toString(urlImages));



        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference advertImageReference = storageReference.child("images")
                .child("advert")
                .child(advert.getIdAdvert())
                .child("image" + counter + ".jpg" );

        UploadTask uploadTask = advertImageReference.putBytes(urlImages);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
           @Override
           public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
               if (!task.isSuccessful()) {
                   throw task.getException();
               }

               // Continue with the task to get the download URL
               return advertImageReference.getDownloadUrl();
           }
           }).addOnCompleteListener(new OnCompleteListener<Uri>() {
               @Override
               public void onComplete(@NonNull Task<Uri> task) {
                   if (task.isSuccessful()) {
                       Uri downloadUri = task.getResult();
                       String urlConverter = downloadUri.toString();

                       listOfPhotosUrl.add(urlConverter);

                       if(listSize == listOfPhotosUrl.size()) {
                            advert.setPhotos(listOfPhotosUrl);
                            advert.saveMyAdvert();
                            advert.savePublicAdvert();

                            startActivity(new Intent(getApplicationContext(), MyAdvertsActivity.class));
                            finish();

                       }

                       hideProgressDialog();

                   } else {
                       // Handle failures
                       // ...
                      alertMessage("Falha ao realizar salvar imagens!");
                   }
               }
           });
        urlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                alertMessage("Anúncio criado com sucesso!");
                String taskResult = String.valueOf(task.getResult());
                Log.d(TAG, "onComplete: taskResult = " + taskResult);
            }
        });
   }

    public void alertValidatePermissions(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permiss_o_negada);
        builder.setMessage(R.string.para_utilizar_a_aplica_o_necess_rio_aceitar_as_permiss_es);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MyAdvertsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alertMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void hideProgressDialog() {
        dialog.dismiss();
    }

    public void showProgressDialog() {
        dialog.show();
    }




    private Advert advertConfiguration() {

        String state = spinnerAdvertState.getSelectedItem().toString();
        String category = spinnerAdvertCategory.getSelectedItem().toString();
        String title = createAdvertTitle.getText().toString();
        String price = createAdvertPrice.getText().toString();
        String phone = createAdvertPhoneNumber.getText().toString();
        String description = createAdvertDescription.getText().toString();

        advert = new Advert();
        advert.setCategory(category);
        advert.setState(state);
        advert.setTitle(title);
        advert.setPrice(price);
        advert.setPhone(phone);
        advert.setDescription(description);

        return advert;
    }

    private void components() {
        createAdvertPrice  = binding.currencyEditTextCreateAdvertPrice;
        createAdvertPhoneNumber = binding.maskEditTextCreateAdvertPhone;
        createAdvertTitle = binding.editTextCreateAdvertTitle;
        createAdvertDescription = binding.editTextCreateAdvertDescription;
        createAdvert = binding.buttonCreateAdvert;

        imageViewAdvertA = binding.imageViewCreateAdvertA;
        imageViewAdvertB = binding.imageViewCreateAdvertB;
        imageViewAdvertC = binding.imageViewCreateAdvertC;

        spinnerAdvertCategory = binding.spinnerCreateAdvertCategory;
        spinnerAdvertState = binding.spinnerCreateAdvertStatus;

        imageViewAdvertA.setOnClickListener(this);
        imageViewAdvertB.setOnClickListener(this);
        imageViewAdvertC.setOnClickListener(this);

        Locale locale = new Locale("pt", "PT");
        createAdvertPrice.setTextLocale(locale);

    }
}