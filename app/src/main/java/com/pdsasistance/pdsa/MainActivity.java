package com.pdsasistance.pdsa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText ET_EMAIL,ET_PASS;
    private Toolbar toolbar;
    String login_pass;
    public String login_email;
    private static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ET_EMAIL=(EditText)findViewById(R.id.user_email);
        ET_PASS=(EditText)findViewById(R.id.user_pass);

        SharedPreferences editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String getStatus = editor.getString("register", "");
        if(getStatus.equals("true")) {
            Intent launchActivity1= new Intent(MainActivity.this,Display.class);
            startActivity(launchActivity1);
            MainActivity.this.finish();
        }
    }

    public void userReg(View view){
        startActivity(new Intent(this,SignUp.class));

    }

    public void userLogin(View view){
        login_email=ET_EMAIL.getText().toString();
        login_pass=ET_PASS.getText().toString();
        String method="login";


        BackgroundTask backgroundTask =new BackgroundTask(this);
        backgroundTask.execute(method, login_email, login_pass);

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("login_email", login_email);
        editor.commit();

    }

    class BackgroundTask extends AsyncTask<String,Void,String> {

        AlertDialog alertDialog;
        Context ctx;
        BackgroundTask(Context ctx){
            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle(("Login Information..."));
        }

        @Override
        protected String doInBackground(String... params) {
            String login_url ="http://www.onlinemallassistant.com/login.php";

            String login_email=params[1];
            String login_pass=params[2];

            try {
                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data = URLEncoder.encode("login_email", "UTF-8")+"="+URLEncoder.encode(login_email,"UTF-8")+"&"+
                        URLEncoder.encode("login_pass","UTF-8")+"="+URLEncoder.encode(login_pass,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response ="";
                String line="";
                while ((line=bufferedReader.readLine())!=null){

                    response+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Login Success Welcome")) {

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("key","false");
                editor.putString("register","true");
                editor.commit();

                Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, Display.class);
                startActivity(i);
            }else  {

                alertDialog.setMessage(result);
                alertDialog.show();
            }

        }

    }



    private static long back_pressed;

    @Override
    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }


}
