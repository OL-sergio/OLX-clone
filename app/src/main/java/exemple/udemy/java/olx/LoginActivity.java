package exemple.udemy.java.olx;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;


public class LoginActivity extends AppCompatActivity {


    private Button buttonLogin;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private Switch switchAcess;
    private TextView textViewRegistration;

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

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textInputEditTextEmail.getText().toString();
                String password = textInputEditTextPassword.getText().toString();

                if(!email.isEmpty()  ){

                    if( !password.isEmpty() ){

                        if ( switchAcess.isChecked() ) {//Register


                        }else {//login

                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Introduzir email!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Introduzir email!", Toast.LENGTH_LONG).show();
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