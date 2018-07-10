package me.roseliu.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button btLogin;
    private Button btSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.etUsername);
        passwordInput = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        btSignup = findViewById(R.id.btSignup);

        if(ParseUser.getCurrentUser()!=null){
            final Intent intent = new Intent(LoginActivity.this, HomeActivity.class); //since inside a callback
            startActivity(intent);
        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                login(username, password);

            }
        });

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                signUp(username, password);

            }
        });

    }

    //don't use the log in function that runs on main thread

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (e == null) {
                    Log.d("LoginActivity", "Login Successful");
                    final Intent intent = new Intent(LoginActivity.this, HomeActivity.class); //since inside a callback
                    startActivity(intent);
                    finish();

                } else {
                    Log.d("LoginActivity", "Login Failure");
                    e.printStackTrace();
                }
            }
        });
    }

    private void signUp(String username, String password) {
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("LoginActivity", "Sign up Successful");
                    final Intent intent = new Intent(LoginActivity.this, HomeActivity.class); //since inside a callback
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("LoginActivity", "Sign up Failure");
                    e.printStackTrace();
                }
            }

        });
    }
}
