package exemple.udemy.java.olx.activity;

import static exemple.udemy.java.olx.R.string.introduzir_email;
import static exemple.udemy.java.olx.R.string.introduzir_palavra_pass;
import static exemple.udemy.java.olx.helper.SettingsFirebase.getCurrentUser;
import static exemple.udemy.java.olx.helper.SettingsFirebase.getFirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import exemple.udemy.java.olx.R;
import exemple.udemy.java.olx.databinding.ActivityLoginRegisterBinding;


public class LoginRegisterActivity extends AppCompatActivity {

    private ActivityLoginRegisterBinding binding;

    private Button buttonLoginRegister;
    private Button buttonAccessWithoutAccount;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private Switch switchAcess;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbarMain;
        toolbar.setVisibility(View.GONE);
        toolbar.setTitle(R.string.olx);
        setSupportActionBar(toolbar);


       /* Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getColor(R.color.hot_pink_200));*/

        auth = getFirebaseAuth();

        components();
        getLoggeduser();
        customizeButton();

        buttonLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textInputEditTextEmail.getText().toString();
                String password = textInputEditTextPassword.getText().toString();

                if(!email.isEmpty()  ){
                    if( !password.isEmpty() ){
                        if ( switchAcess.isChecked() ) {//Register

                                auth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if ( task.isSuccessful() ){
                                                    Toast.makeText(getApplicationContext(),
                                                            R.string.registado_com_sucesso, Toast.LENGTH_LONG).show();
                                                }else {
                                                    String exceptionError;
                                                    try {
                                                        throw Objects.requireNonNull(task.getException());
                                                    } catch (FirebaseAuthWeakPasswordException e){
                                                        exceptionError = getString(R.string.intreduza_uma_senha_mais_forte);
                                                    } catch (
                                                            FirebaseAuthInvalidCredentialsException e) {
                                                        exceptionError = getString(R.string.intreduza_um_email_valido);
                                                    } catch (FirebaseAuthUserCollisionException e){
                                                        exceptionError = getString(R.string.esta_conta_j_existe);
                                                    }catch (Exception e) {
                                                        exceptionError = getString(R.string.erro_ao_criar_utilizador) + e.getMessage();
                                                        e.printStackTrace();
                                                    }
                                                    Toast.makeText(LoginRegisterActivity.this, exceptionError, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                        }else {//login
                            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),
                                                R.string.login_com_sucesso, Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), AdvertsActivity.class));
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(),
                                                R.string.erro_ao_fazer_login, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                getText(introduzir_palavra_pass), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            getText(introduzir_email), Toast.LENGTH_LONG).show();
                }
            }
        });


        buttonAccessWithoutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdvertsActivity.class));
            }
        });
    }

    private void customizeButton() {
        buttonLoginRegister.setText(R.string.login);
        buttonLoginRegister.setTextSize(18);
        switchAcess.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buttonAccessWithoutAccount.setVisibility(View.INVISIBLE);
                buttonLoginRegister.setText(R.string.registar);
                buttonLoginRegister.setPadding(0, 55, 0, 0);
                buttonLoginRegister.setTextSize(18);
            } else {
                buttonAccessWithoutAccount.setVisibility(View.VISIBLE);
                buttonLoginRegister.setText(R.string.login);
                buttonLoginRegister.setPadding(0, 0, 0, 0);
                buttonLoginRegister.setTextSize(18);
            }
        });
    }

    private void getLoggeduser() {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null){
            startActivity(new Intent(getApplicationContext(), AdvertsActivity.class));
            finish();
        }

    }

    private void components() {
        textInputEditTextEmail = binding.textViewLoginEmail;
        textInputEditTextPassword = binding.textViewLoginPassword;
        switchAcess = binding.switchLongin;
        buttonLoginRegister = binding.buttonLoginUserRegister;
        buttonAccessWithoutAccount = binding.buttonLoggWithNoUser;
    }
}