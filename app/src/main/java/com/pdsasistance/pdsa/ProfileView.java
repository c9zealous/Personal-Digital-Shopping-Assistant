package com.pdsasistance.pdsa;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

public class ProfileView extends AppCompatActivity {
    String name, image, contact, hours, tag, rating;
    ImageView img;
    TextView textName, textRate, textContact, textHours, textTag;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        img = (ImageView) findViewById(R.id.imageView);
        textName = (TextView) findViewById(R.id.textView5);
        textRate = (TextView) findViewById(R.id.textView6);
        textContact = (TextView) findViewById(R.id.tvNumber1);
        textHours = (TextView) findViewById(R.id.textView7);
        textTag = (TextView) findViewById(R.id.textView8);

        name = getIntent().getStringExtra("storename");

        BackgroundTask back = new BackgroundTask();
        back.execute(name);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout2);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + textContact.getText()));
                startActivity(callIntent);
            }
        });

    }

    class BackgroundTask extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String login_url2 ="http://www.onlinemallassistant.com/storedata.php";
            String response="";
            String id=params[0];

            try {
                URL url=new URL(login_url2);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data = URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(id,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String line="";
                while ((line=bufferedReader.readLine())!=null){

                    response+=line;
                }

                try{
                    JSONArray jArray =new JSONArray(response);
                    for(int i=0;i<jArray.length();i++){
                        JSONObject jsonObject=jArray.getJSONObject(i);
                        // add interviewee name to arraylist
                        image=jsonObject.getString("image");
                        contact=jsonObject.getString("contact");
                        hours=jsonObject.getString("openinghours");
                        tag=jsonObject.getString("tag");
                        rating=jsonObject.getString("rating");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return null;


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
            String image_URL="http://www.onlinemallassistant.com/images/"+image;
            new LoadImage().execute(image_URL);
            textName.setText(name);
            textRate.setText(rating);
            String s= textHours.getText().toString();
            textHours.setText(s+hours);
            textContact.setText(contact);
            s=textTag.getText().toString();
            textTag.setText(s+tag);
        }

    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if(image != null){
                img.setImageBitmap(image);
            }
        }
    }
}
