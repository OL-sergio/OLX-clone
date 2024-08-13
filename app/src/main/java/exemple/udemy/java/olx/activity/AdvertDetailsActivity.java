package exemple.udemy.java.olx.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.Objects;

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
    private CarouselView carouselViewDetailsAdvert;

    private Advert seletedAdvert;

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

            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    String urlString = seletedAdvert.getPhotos().get(position);
                    Picasso.get().load(urlString).into(imageView);
                }
            };

            carouselViewDetailsAdvert.setPageCount(seletedAdvert.getPhotos().size());
            carouselViewDetailsAdvert.setImageListener(imageListener);
        }

        viewPhoneNumber();

    }

    private void viewPhoneNumber() {
        buttonViewPhoneNumber.setOnClickListener(v -> {
            Intent intent = new Intent( Intent.ACTION_DIAL, Uri.fromParts("tel", seletedAdvert.getPhone(), null ));
            startActivity(intent);
        });

    }

    private void components() {
        carouselViewDetailsAdvert = binding.carouselViewDetailsAdvert;
        advertTitle = binding.textViewDetailsAdvertTitle;
        advertPrice = binding.textViewDetailsAdvertPrice;
        advertLocation = binding.textViewDetailsAdvertLocation;
        advertDescription = binding.textViewDetailsAdvertCharacteristics;
        buttonViewPhoneNumber = binding.buttonDetailsAdvertViewPhoneNumber;

    }
}