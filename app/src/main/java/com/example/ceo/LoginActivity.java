package com.example.ceo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    String url = "18.224.27.15";

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
                    if(login()){
                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(intent);
                    }
                    else{
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
            URL url = new URL(myURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Content-Length", "" + Integer.toString(parammetrs.getBytes().length));
            OutputStream os = conn.getOutputStream();
            data = parammetrs.getBytes("UTF-8");
            os.write(data);
            System.out.println("data: " + data);
            data = null;

            conn.connect();
            int responseCode= conn.getResponseCode();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (responseCode == 200) {
                System.out.println("hihihihihi");
                is = conn.getInputStream();

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                data = baos.toByteArray();
                String resultString = new String(data, "UTF-8");
                if(resultString.equals("OK")){
                    return true;
                }
                else{
                    System.out.println("hehehehe");
                    return false;
                }
            } else {
                Toast.makeText(getBaseContext(), "Problems with server connection", Toast.LENGTH_LONG).show();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean login() throws IOException, URISyntaxException {

        if (!validate()) {
            onLoginFailed();
            return false;
        }

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        //makeHTTPGet(email, password, url);
        return true;
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