package exemple.udemy.java.olx.activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.santalu.maskara.widget.MaskEditText;

import java.util.Locale;
import java.util.Objects;
import exemple.udemy.java.olx.R;
import exemple.udemy.java.olx.databinding.ActivityCreateAdvertBinding;
import exemple.udemy.java.olx.databinding.ActivityMyAdvertsBinding;

public class CreateAdvertActivity extends AppCompatActivity {

    private ActivityCreateAdvertBinding binding;

    EditText createAdvertTitle;
    EditText createAdvertDescription;
    CurrencyEditText createAdvertPrice;
    MaskEditText createAdvertPhoneNumber;
    Button createAdvert;

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