package exemple.udemy.java.olx.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exemple.udemy.java.olx.R;
import exemple.udemy.java.olx.adapter.AdapterAdverts;
import exemple.udemy.java.olx.databinding.ActivityAdvertsBinding;
import exemple.udemy.java.olx.helper.SettingsFirebase;
import exemple.udemy.java.olx.model.Advert;

public class AdvertsActivity extends AppCompatActivity {

    private ActivityAdvertsBinding binding;
    private FirebaseAuth auth;

    private RecyclerView recyclerViewPublicAdverts;
    private final List<Advert> advertsList = new ArrayList<>();
    private DatabaseReference databaseReferenceUserReference;

    private Button buttonRegion, buttonCategory;
    private AdapterAdverts adapterAdverts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdvertsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbarAdverts;
        toolbar.setTitle(R.string.olx);
        setSupportActionBar(toolbar);

        /*Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getColor(R.color.hot_pink_200));*/

        auth = SettingsFirebase.getFirebaseAuth();

        databaseReferenceUserReference = SettingsFirebase.getDatabaseReference()
                .child("adverts");

        components();

        recyclerViewPublicAdverts.setLayoutManager( new LinearLayoutManager(this ) );
        recyclerViewPublicAdverts.setHasFixedSize( true );

        adapterAdverts = new AdapterAdverts(advertsList, this);
        recyclerViewPublicAdverts.setAdapter(adapterAdverts);

        recoverPublicAdverts();
    }

    private void recoverPublicAdverts() {

        advertsList.clear();

        databaseReferenceUserReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot states : snapshot.getChildren()){

                    for (DataSnapshot category : states.getChildren()){

                        for (DataSnapshot adverts : category.getChildren()){

                                Advert advert = adverts.getValue(Advert.class);
                                advertsList.add(advert);

                                Collections.reverse(advertsList);
                                adapterAdverts.notifyDataSetChanged();

                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adverts, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {

        if ( auth.getCurrentUser() == null ){
            menu.setGroupVisible(R.id.group_not_logged, true);

        }else {
            menu.setGroupVisible(R.id.group_logged, true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if ( item.getItemId() == R.id.menu_register){
            startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class ));
        }

        if (item.getItemId() ==  R.id.menu_adverts){
            startActivity(new Intent(getApplicationContext(), MyAdvertsActivity.class ));
        }

        if (item.getItemId() ==  R.id.menu_logout){
            auth.signOut();
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    private void components() {
        recyclerViewPublicAdverts = binding.recyclerViewPublicAdverts;
        buttonRegion = binding.buttonFilterRegion;
        buttonCategory = binding.buttonFilterCategory;
    }
}