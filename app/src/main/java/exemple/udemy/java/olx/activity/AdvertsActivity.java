package exemple.udemy.java.olx.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import exemple.udemy.java.olx.R;
import exemple.udemy.java.olx.databinding.ActivityAdvertsBinding;
import exemple.udemy.java.olx.helper.SettingsFirebase;

public class AdvertsActivity extends AppCompatActivity {

    private ActivityAdvertsBinding binding;
    private FirebaseAuth auth;

    @SuppressLint("NewApi")
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
            startActivity(new Intent(getApplicationContext(), LoginActivity.class ));
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

}