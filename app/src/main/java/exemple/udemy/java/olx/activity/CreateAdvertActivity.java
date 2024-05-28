package exemple.udemy.java.olx.activity;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.santalu.maskara.widget.MaskEditText;

import java.util.Locale;
import java.util.Objects;

import exemple.udemy.java.olx.R;
import exemple.udemy.java.olx.databinding.ActivityCreateAdvertBinding;

public class CreateAdvertActivity extends AppCompatActivity {

    private ActivityCreateAdvertBinding binding;

    EditText createAdvertTitle;
    EditText createAdvertDescription;
    CurrencyEditText createAdvertPrice;
    MaskEditText createAdvertPhoneNumber;
    Button createAdvert;

    private static final int STORAGE_PERMISSION_CODE = 23;

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

    private void saveAdvert() {
        //String value = createAdvertPrice.getHintString();
        //long value = createAdvertPrice.getRawValue();
        String value = createAdvertPrice.getText().toString();
        String price = createAdvertPhoneNumber.getText().toString();
        Log.d(TAG, "saveAdvert: " + value + " " + price);

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

    public void requestForStoragePermissions() {
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
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    STORAGE_PERMISSION_CODE
            );
        }
    }

    public final ActivityResultLauncher<Intent> storageActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>(){
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                                //Android is 11 (R) or above
                                if(Environment.isExternalStorageManager()){
                                    //Manage External Storage AndroidPermissions Granted
                                    Log.d(TAG, "onActivityResult: Manage External Storage AndroidPermissions Granted");
                                }else{
                                    Toast.makeText( CreateAdvertActivity.this, "Storage AndroidPermissions Denied", Toast.LENGTH_SHORT).show();
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
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (read && write) {
                    Toast.makeText(this, "Storage AndroidPermissions Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Storage AndroidPermissions Denied", Toast.LENGTH_SHORT).show();
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


    private void components() {
        createAdvertPrice  = binding.currencyEditTextCreateAdvertPrice;
        createAdvertPhoneNumber = binding.maskEditTextCreateAdvertPhone;
        createAdvertTitle = binding.editTextCreateAdvertTitle;
        createAdvertDescription = binding.editTextCreateAdvertDescription;
        createAdvert = binding.buttonCreateAdvert;

        Locale locale = new Locale("US", "en");
        createAdvertPrice.setTextLocale(locale);

    }
}