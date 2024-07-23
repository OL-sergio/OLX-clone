package exemple.udemy.java.olx.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import exemple.udemy.java.olx.R;
import exemple.udemy.java.olx.adapter.AdapterAdverts;
import exemple.udemy.java.olx.databinding.ActivityMyAdvertsBinding;
import exemple.udemy.java.olx.helper.RecyclerItemClickListener;
import exemple.udemy.java.olx.helper.SettingsFirebase;
import exemple.udemy.java.olx.model.Advert;
import exemple.udemy.java.olx.utilities.CustomHorizontalProgressDialog;

public class MyAdvertsActivity extends AppCompatActivity {

    private ActivityMyAdvertsBinding binding;

    private RecyclerView recyclerViewAdverts;
    private final List<Advert> advertList = new ArrayList<>();
    private AdapterAdverts adapterAdverts;
    private DatabaseReference databaseReferenceUserReference;

    private CustomHorizontalProgressDialog dialogProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyAdvertsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbarMyAdverts;
        toolbar.setTitle("My Adverts");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AdvertsActivity.class);
            startActivity(intent);
            finish();
        });

        Objects.requireNonNull(getSupportActionBar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialogProgressBar = new CustomHorizontalProgressDialog(this);

        databaseReferenceUserReference = SettingsFirebase.getDatabaseReference()
                .child("my_adverts")
                .child(SettingsFirebase.getUserID());

        components();


       /* Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getColor(R.color.hot_pink_200));*/

        binding.fab.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), CreateAdvertActivity.class ));
            finish();
        });

        recyclerViewAdverts.setLayoutManager( new LinearLayoutManager(this));
        recyclerViewAdverts.setHasFixedSize(true);

        adapterAdverts = new AdapterAdverts(advertList, this);
        recyclerViewAdverts.setAdapter(adapterAdverts);
        advertList.clear();
        recoverAdverts();

        recyclerViewAdverts.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this, recyclerViewAdverts, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }


                    @Override
                    public void onLongItemClick(View view, int position) {
                        Advert selectedAdvert = advertList.get(position);
                        selectedAdvert.deleteMyAdvert();
                        selectedAdvert.deletePublicAdvert();

                        adapterAdverts.notifyDataSetChanged();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }

                ));


    }


    private void recoverAdverts() {

        dialogProgressBar.show();
        advertList.clear();

         databaseReferenceUserReference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    advertList.add(dataSnapshot.getValue(Advert.class));

                }

                Collections.reverse(advertList);
                adapterAdverts.notifyDataSetChanged();

                 dialogProgressBar.dismiss();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 alertRecoverAdvert("Erro ao recoperar os! Recarregue a página!");
                 Log.d(TAG, "onCancelled: " + error.getMessage());
             }
         });

    }

    public void alertRecoverAdvert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void components() {
        recyclerViewAdverts = binding.recyclerViewMyAdverts;
    }


}