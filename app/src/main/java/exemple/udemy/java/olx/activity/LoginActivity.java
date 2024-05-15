package exemple.udemy.java.olx.activity;

import static exemple.udemy.java.olx.R.string.introduzir_email;
import static exemple.udemy.java.olx.R.string.introduzir_palavra_pass;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

import exemple.udemy.java.olx.R;
import exemple.udemy.java.olx.helper.SettingsFirebase;


public class LoginActivity extends AppCompatActivity {


    private Button buttonLogin;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private Switch switchAcess;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.olx);
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getColor(R.color.hot_pink_200));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        components();
        auth = SettingsFirebase.getFirebaseAuth();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
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

                                                    //intenten to activity

                                                }else {
                                                    String exceptionError = "";
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
                                                    Toast.makeText(LoginActivity.this, exceptionError, Toast.LENGTH_SHORT).show();
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
    }

    private void components() {
        textInputEditTextEmail = findViewById(R.id.textView_LoginEmail);
        textInputEditTextPassword = findViewById(R.id.textView_LoginPassword);
        buttonLogin = findViewById(R.id.button_loginUser);
        switchAcess = findViewById(R.id.switch_longin);

    }
}