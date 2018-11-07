package com.example.ceo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.ceo.requests.BackendAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;

    boolean flag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _loginButton = findViewById(R.id.btn_login);
        _loginButton.setOnClickListener(v -> {
            _emailText = findViewById(R.id.input_email);
            _passwordText = findViewById(R.id.input_password);

            login(() -> {
                if (flag) {
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Login or password is wrong", Toast.LENGTH_LONG).show();
                }
            });
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void makeHTTPPost(String email, String password, Runnable callback) {
        BackendAPI api = new BackendAPI(this);

        api.login(email, password, response -> {
            flag = response;
            System.out.println("return " + flag);

            callback.run();
        }, error -> {
        });
    }

    public void login(Runnable callback) {
        if (!validate()) {
            onLoginFailed();
            flag = false;
            callback.run();

            return;
        }

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        makeHTTPPost(email, password, callback);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}