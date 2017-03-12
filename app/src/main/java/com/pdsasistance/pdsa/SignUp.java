package com.pdsasistance.pdsa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class SignUp extends ActionBarActivity {

    public   String Country_value,country_id1,State_value,state_id1;
    int country_id,state_id;

    private Toolbar toolbar;

    EditText name,email,uname,pass1,pass2,phoneNumber;
    String namestr,emailstr,unamestr,pass1str,pass2str,phonestr;

    public String country,state,city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name =(EditText)findViewById(R.id.TFname);
        email =(EditText)findViewById(R.id.TFemail);

        pass1 =(EditText)findViewById(R.id.TFpass1);
        pass2 =(EditText)findViewById(R.id.TFpass2);
        phoneNumber=(EditText)findViewById(R.id.phone);


    }

    public void userReg(View view) {

        namestr = name.getText().toString();
        emailstr = email.getText().toString();

        pass1str = pass1.getText().toString();
        pass2str = pass2.getText().toString();
        phonestr=phoneNumber.getText().toString();


        String method = "register";


        if (!pass1str.equals(pass2str)) {
            //popup message
            Toast pass = Toast.makeText(SignUp.this, "Passwords don't match!", Toast.LENGTH_SHORT);
            pass.show();
        } else if (namestr.equals("")) {
            Toast name = Toast.makeText(SignUp.this, "Name field is Empty!", Toast.LENGTH_SHORT);
            name.show();
        } else if (emailstr.equals("")) {
            Toast email = Toast.makeText(SignUp.this, "Email field is Empty!", Toast.LENGTH_SHORT);
            email.show();

        }  else if (pass1str.equals("") ) {
            Toast password = Toast.makeText(SignUp.this, "Passwords Field is Empty!", Toast.LENGTH_SHORT);
            password.show();
        }else if (phonestr.equals("") ) {
            Toast phone = Toast.makeText(SignUp.this, "Phone Field is Empty!", Toast.LENGTH_SHORT);
            phone.show();

        }
        else if(!namestr.equals("") && !emailstr.equals("")  && !pass1str.equals("")){


            if (!TextUtils.isEmpty(emailstr) && Utility.validate(emailstr) && Utility.isValidPhoneNumber(phonestr)) {

                BackgroundTask backgroundTask = new BackgroundTask(this);
                backgroundTask.execute(method, namestr, emailstr,phonestr,pass1str);
                finish();


            } else {
                Toast.makeText(SignUp.this, "Please enter valid email",
                        Toast.LENGTH_LONG).show();
            }

        }
        else{
            Toast password = Toast.makeText(SignUp.this, "Not Succesfull!", Toast.LENGTH_SHORT);
            password.show();
        }
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
            String reg_url ="http://www.onlinemallassistant.com/register.php";

            String method = params[0];
            if(method.equals("register")){
                String namestr = params[1];
                String emailstr = params[2];
                String phonestr = params[3];
                String country = params[4];
                try {

                    URL url =new URL(reg_url);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);

                    OutputStream OS =httpURLConnection.getOutputStream();


                    BufferedWriter bufferWriter =new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                    String data= URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(namestr,"UTF-8") + "&" +
                            URLEncoder.encode("email","UTF-8") + "=" + URLEncoder.encode(emailstr,"UTF-8") + "&" +
                            URLEncoder.encode("mobile","UTF-8") + "=" + URLEncoder.encode(phonestr,"UTF-8")+ "&" +
                            URLEncoder.encode("password","UTF-8") + "=" + URLEncoder.encode(pass1str,"UTF-8") ;

                    bufferWriter.write(data);
                    bufferWriter.flush();
                    bufferWriter.close();
                    OS.close();

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

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Data insertion error...")) {

                Toast alert = Toast.makeText(SignUp.this,"Already Registered With This Email-Id " ,Toast.LENGTH_SHORT);
                alert.show();
            }else if(result.equals("Data Insertion Success...")){
                Toast alert = Toast.makeText(SignUp.this,"Sign Up Successfull..." ,Toast.LENGTH_SHORT);
                alert.show();
            }

        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent a = new Intent(this,MainActivity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
