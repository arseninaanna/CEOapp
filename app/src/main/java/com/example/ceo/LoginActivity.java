package com.example.ceo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    String url = "http://ec2-18-206-98-189.compute-1.amazonaws.com";
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

    public boolean makeHTTPGet(String email, String password, String myURL) throws URISyntaxException, IOException {

        /*URI uri = new URIBuilder()
                .setScheme("http")
                .setHost(myURL)
                .setPort(8080)
                .addParameter("email", email)
                .addParameter("password", password)
                .build();


        HttpURLConnection connection = (HttpURLConnection)uri.toURL().openConnection();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return true;
        } else {
            return false;
        }*/

        String parammetrs = "email=" + email + "&password=" + password;
        byte[] data = null;
        InputStream is = null;

        try {
            URL url = new URL(myURL + ":80");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);

            conn.setRequestProperty("Content-Length", "" + Integer.toString(parammetrs.getBytes().length));
            OutputStream os = conn.getOutputStream();
            data = parammetrs.getBytes("UTF-8");
            os.write(data);
            System.out.println("data: " + data.toString());
            data = null;

            conn.connect();
            int responseCode = conn.getResponseCode();
            System.out.println("response code: " + responseCode);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (responseCode == 200) {
                System.out.println("everything is ok");
                is = conn.getInputStream();

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                data = baos.toByteArray();
                String resultString = new String(data, "UTF-8");
                if (resultString.equals("OK")) {
                    conn.disconnect();
                    return true;
                } else {
                    conn.disconnect();
                    return false;
                }
            } else {
                conn.disconnect();
                System.out.println("problems!");
                return false;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void makeHTTPPost(String email, String password, String myURL) {
        VolleyLog.DEBUG = true;
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);

            JsonObjectRequest jr = new JsonObjectRequest(myURL, jsonBody, response -> {
                //String res = response.toString();
                //System.out.println("Response: " + res);
                /*try {
                    JSONArray arr = new JSONArray(res);
                    JSONObject jObj = arr.getJSONObject(0);
                    String str = jObj.getString("response");
                    if (str.equals("OK")){
                        flag = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                Log.i("VOLLEY", response.toString());
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
                    makeHTTPPost(email, password, url);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

        thread.start();
        //return flag;
        return true;  //TODO this is just for now since server does not handle the requests yet
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