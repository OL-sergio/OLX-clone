package exemple.udemy.java.olx.activity;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.bumptech.glide.Glide;
import com.santalu.maskara.widget.MaskEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import exemple.udemy.java.olx.R;
import exemple.udemy.java.olx.databinding.ActivityCreateAdvertBinding;

public class CreateAdvertActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityCreateAdvertBinding binding;

    EditText createAdvertTitle;
    EditText createAdvertDescription;
    CurrencyEditText createAdvertPrice;
    MaskEditText createAdvertPhoneNumber;
    Button createAdvert;
    ImageView imageViewAdvertA;
    ImageView imageViewAdvertB;
    ImageView imageViewAdvertC;

    private static final int STORAGE_PERMISSION_CODE = 23;

    private List<String> listOfPhotos = new ArrayList<>();


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

        Objects.requireNonNull(getSupportActionBar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        components();
        saveAdvert();

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
            public void onClick(View v) {
                saveAdvert();
            }
        });


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.imageView_create_advert_a) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryActivityResultLauncherImageA.launch(intent);
        }
        if (view.getId() == R.id.imageView_create_advert_b) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryActivityResultLauncherImageB.launch(intent);
        }
        if (view.getId() == R.id.imageView_create_advert_c) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
             galleryActivityResultLauncherImageC.launch(intent);
        }

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
                        Glide.with(this)
                                .load(imageUrl)
                                .into(imageViewAdvertA);
                        listOfPhotos.add(imageUrl.toString());

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
                        Glide.with(this)
                                .load(imageUrl)
                                .into(imageViewAdvertB);
                        listOfPhotos.add(imageUrl.toString());

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
                        Glide.with(this)
                                .load(imageUrl)
                                .into(imageViewAdvertC);
                        listOfPhotos.add(imageUrl.toString());

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });



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

    private void saveAdvert() {
        //String value = createAdvertPrice.getHintString();
        //long value = createAdvertPrice.getRawValue();
        String value = createAdvertPrice.getText().toString();
        String price = createAdvertPhoneNumber.getText().toString();
        Log.d(TAG, "saveAdvert: " + value + " " + price);

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

        imageViewAdvertA.setOnClickListener(this);
        imageViewAdvertB.setOnClickListener(this);
        imageViewAdvertC.setOnClickListener(this);

        Locale locale = new Locale("pt", "PT");
        createAdvertPrice.setTextLocale(locale);

    }
}