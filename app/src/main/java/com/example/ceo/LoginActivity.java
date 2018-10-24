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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class LoginActivity extends AppCompatActivity {
    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    String url = "http://ec2-18-222-89-34.us-east-2.compute.amazonaws.com";
    //String url = getResources().getString(R.string.base_url);
    boolean flag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _loginButton = (Button) findViewById(R.id.btn_login);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                _emailText = (EditText) findViewById(R.id.input_email);
                _passwordText = (EditText) findViewById(R.id.input_password);
                try {
                    if (login()) {
                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getBaseContext(), "Login or password is wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void makeHTTPPost(String email, String password, String myURL) {
        VolleyLog.DEBUG = true;
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("login", email);
            jsonBody.put("password", password);

            JsonObjectRequest jr = new JsonObjectRequest(myURL, jsonBody, response -> {
                String res = response.toString();
                System.out.println("Response: " + res);
                /*try {
                    JSONArray arr = new JSONArray(res);
                    JSONObject jObj = arr.getJSONObject(0);
                    String str = jObj.getString("status");
                    if (str.equals("OK")){
                        flag = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                Log.i("VOLLEY response", response.toString());
            }, error -> {
                Log.e("VOLLEY", "Post request failed: " + error.toString(), error);
            });

            requestQueue.add(jr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean login() throws IOException, URISyntaxException {

        if (!validate()) {
            onLoginFailed();
            return false;
        }

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    flag = false;
                    //makeHTTPPost(email, password, url);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

        thread.start();
        //return flag;
        return true;  //TODO return flag when server is ready
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