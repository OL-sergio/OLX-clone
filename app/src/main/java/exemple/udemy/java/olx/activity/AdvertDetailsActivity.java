package exemple.udemy.java.olx.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import exemple.udemy.java.olx.adapter.ImageSliderAdapter;
import exemple.udemy.java.olx.R;
import exemple.udemy.java.olx.databinding.ActivityAdvertDetailsBinding;
import exemple.udemy.java.olx.model.Advert;

public class AdvertDetailsActivity extends AppCompatActivity {

    private ActivityAdvertDetailsBinding binding;

    private TextView advertTitle;
    private TextView advertPrice;
    private TextView advertDescription;
    private TextView advertLocation;
    private Button buttonViewPhoneNumber;


    private ImageSliderAdapter sliderAdapter;
    private Advert seletedAdvert;
    private Handler handler = new Handler();
    private Runnable imageSliderRunnable;
    private static final long IMAGE_SLIDER_DELAY = 4000; // 3 seconds delay for image slider


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdvertDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbarAdverts;
        toolbar.setTitle(R.string.decri_o_de_an_ncio);
        toolbar.setTitleTextColor(getColor(R.color.white_100));
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AdvertsActivity.class);
            startActivity(intent);
            finish();
        });

        Objects.requireNonNull(getSupportActionBar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        components();

        seletedAdvert = (Advert) getIntent().getSerializableExtra("advertSelected");


        if(seletedAdvert != null){
            advertTitle.setText(seletedAdvert.getTitle());
            advertPrice.setText(seletedAdvert.getPrice());
            advertLocation.setText(seletedAdvert.getState());
            advertDescription.setText("Descrição: \n\n" + seletedAdvert.getDescription());

            setupImageSlider();

            startImageSliderTimer();


        }
        viewPhoneNumber();
    }



    private void startImageSliderTimer() {
        handler = new Handler();
        imageSliderRunnable = new Runnable() {
            @Override
            public void run() {
                if (sliderAdapter != null && sliderAdapter.getItemCount() > 0) {
                    int currentItem = binding.viewPagerImages.getCurrentItem();
                    int nextItem = (currentItem + 1) % sliderAdapter.getItemCount();
                    binding.viewPagerImages.setCurrentItem(nextItem, true);
                }
                handler.postDelayed(this, IMAGE_SLIDER_DELAY); // Muda a imagem a cada 4 segundos
            }
        };
        handler.postDelayed(imageSliderRunnable, IMAGE_SLIDER_DELAY); // Inicia o runnable após 4 segundos
    };

    private void stopImageSliderTimer() {
        if (handler != null && imageSliderRunnable != null) {
            handler.removeCallbacks(imageSliderRunnable);
        }
    }


    private void setupImageSlider() {

        if (seletedAdvert.getPhotos() == null || seletedAdvert.getPhotos().isEmpty()) {
            Log.d("AdvertDetailsActivity", "A lista de imagens está vazia ou nula.");

        }

            sliderAdapter = new ImageSliderAdapter(seletedAdvert.getPhotos(), this);
            binding.viewPagerImages.setAdapter(sliderAdapter);

            new TabLayoutMediator(binding.tabLayoutIndicator, binding.viewPagerImages,
                    (tab, position) -> {
                        // Pode ser deixado em branco
                    }).attach();

    }

    private void viewPhoneNumber() {
        buttonViewPhoneNumber.setOnClickListener(v -> {
            Intent intent = new Intent( Intent.ACTION_DIAL, Uri.fromParts("tel", seletedAdvert.getPhone(), null ));
            startActivity(intent);
        });
    }

    private void components() {

        advertTitle = binding.textViewDetailsAdvertTitle;
        advertPrice = binding.textViewDetailsAdvertPrice;
        advertLocation = binding.textViewDetailsAdvertLocation;
        advertDescription = binding.textViewDetailsAdvertCharacteristics;
        buttonViewPhoneNumber = binding.buttonDetailsAdvertViewPhoneNumber;

    }

    @Override
    protected void onResume() {
        super.onResume();
        startImageSliderTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopImageSliderTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopImageSliderTimer();
        handler.removeCallbacks(imageSliderRunnable);
    }

}