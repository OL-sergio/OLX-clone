package exemple.udemy.java.olx.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import exemple.udemy.java.olx.databinding.DialogSpinnerBinding;
import exemple.udemy.java.olx.helper.SettingsFirebase;
import exemple.udemy.java.olx.model.Advert;
import exemple.udemy.java.olx.utilities.CustomHorizontalProgressDialog;

public class AdvertsActivity extends AppCompatActivity {

    private ActivityAdvertsBinding binding;
    private FirebaseAuth auth;

    private DatabaseReference databaseReferenceUserReference;
    private Spinner spinnerFilter;
    private Button buttonRegion, buttonCategory;
    private AdapterAdverts adapterAdverts;
    private CustomHorizontalProgressDialog dialogProgressBar;
    private RecyclerView recyclerViewPublicAdverts;
    private String filterState = "";
    private String filterCategory = "";
    private boolean verifyFilterByState = false;

    private final List<Advert> advertsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdvertsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbarAdverts;
        toolbar.setTitle(R.string.olx);
        setSupportActionBar(toolbar);

        dialogProgressBar = new CustomHorizontalProgressDialog(this);

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

        buttonRegion.setOnClickListener(v ->
                filterByState()
        );

        buttonCategory.setOnClickListener(v -> {
                filterByCategory();
        });
    }

    private void recoverPublicAdverts() {

        dialogProgressBar.show();
        advertsList.clear();

        databaseReferenceUserReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot states : snapshot.getChildren()){
                    for (DataSnapshot category : states.getChildren()){
                        for (DataSnapshot adverts : category.getChildren()){

                                Advert advert = adverts.getValue(Advert.class);
                                advertsList.add(advert);

                        }
                    }
                }

                Collections.reverse(advertsList);
                adapterAdverts.notifyDataSetChanged();
                dialogProgressBar.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void filterByState(){
        AlertDialog.Builder dialogRegion = new AlertDialog.Builder(this);
        dialogRegion.setTitle("Escolha a região desejada");

        DialogSpinnerBinding binding = DialogSpinnerBinding.inflate(getLayoutInflater());
        binding.getRoot();
        spinnerFilter = binding.spinnerFilterDialog;

        //View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
        //spinnerFilter = viewSpinner.findViewById(R.id.spinnerFilterDialog);

        String[] statesStrings = getResources().getStringArray(R.array.states);
        ArrayAdapter<String> adapterStates = new ArrayAdapter<String>(
                this, android.R.layout.select_dialog_item, statesStrings);
        adapterStates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapterStates);

        //dialogRegion.setView(viewSpinner);
        dialogRegion.setView(binding.getRoot());

        dialogRegion.setPositiveButton("Confirmar", (dialog, which) -> {
            filterState = spinnerFilter.getSelectedItem().toString();
            Log.d(TAG, "filterBySate: " + filterState);
            recoverFilteredAdvertsState();
            verifyFilterByState = true;

        });
        dialogRegion.setNegativeButton("Cancelar", (dialog, which) -> {

        });

        AlertDialog alertDialog = dialogRegion.create();
        alertDialog.show();
    }

    private void recoverFilteredAdvertsCategory() {
        databaseReferenceUserReference = SettingsFirebase.getDatabaseReference()
                .child("adverts")
                .child(filterState)
                .child(filterCategory);

        databaseReferenceUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialogProgressBar.show();
                advertsList.clear();

                        for (DataSnapshot adverts : snapshot.getChildren()){

                            Advert advert = adverts.getValue(Advert.class);
                            advertsList.add(advert);

                        }

                Collections.reverse(advertsList);
                adapterAdverts.notifyDataSetChanged();
                dialogProgressBar.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recoverFilteredAdvertsState() {
        databaseReferenceUserReference = SettingsFirebase.getDatabaseReference()
                .child("adverts")
                .child(filterState);
        databaseReferenceUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialogProgressBar.show();
                advertsList.clear();

                for (DataSnapshot category : snapshot.getChildren()){
                    for (DataSnapshot adverts : category.getChildren()){

                        Advert advert = adverts.getValue(Advert.class);
                        advertsList.add(advert);

                    }
                }

                Collections.reverse(advertsList);
                adapterAdverts.notifyDataSetChanged();
                dialogProgressBar.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void filterByCategory(){
        if (verifyFilterByState == true ){

            AlertDialog.Builder dialogRegion = new AlertDialog.Builder(this);
            dialogRegion.setTitle("Escolha a categoria desejada");

            DialogSpinnerBinding binding = DialogSpinnerBinding.inflate(getLayoutInflater());
            binding.getRoot();
            spinnerFilter = binding.spinnerFilterDialog;

            //View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
            //spinnerFilter = viewSpinner.findViewById(R.id.spinnerFilterDialog);

            String[] categoriesStrings = getResources().getStringArray(R.array.categories);
            ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, categoriesStrings);
            adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFilter.setAdapter(adapterCategories);

            //dialogRegion.setView(viewSpinner);
            dialogRegion.setView(binding.getRoot());

            dialogRegion.setPositiveButton("Confirmar", (dialog, which) -> {
                filterCategory = spinnerFilter.getSelectedItem().toString();
                Log.d(TAG, "filterCategory: " + filterCategory);
                recoverFilteredAdvertsCategory();


            });
            dialogRegion.setNegativeButton("Cancelar", (dialog, which) -> {

            });

            AlertDialog alertDialog = dialogRegion.create();
            alertDialog.show();

        }  else {
            alertMessage("Selecione uma região primeiro!");
        }

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

    private void alertMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void components() {
        recyclerViewPublicAdverts = binding.recyclerViewPublicAdverts;
        buttonRegion = binding.buttonFilterRegion;
        buttonCategory = binding.buttonFilterCategory;
    }

}