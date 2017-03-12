package com.pdsasistance.pdsa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class PointCards extends Fragment{
    View v;
    TextView t1,t2,t3,t4;
    String id;
    private static final String MY_PREFS_NAME = "MyPrefsFile";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.pointcards,container,false);
        t1=(TextView)v.findViewById(R.id.textView1);
        t2=(TextView)v.findViewById(R.id.textView2);
        t3=(TextView)v.findViewById(R.id.textView3);
        t4=(TextView)v.findViewById(R.id.textView4);
        SharedPreferences editor = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        id = editor.getString("login_email", "");

        BackgroundTask back=new BackgroundTask();
        back.execute(id);
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    if (getFragmentManager().getBackStackEntryCount() == 0) {
                        Intent i = new Intent(getActivity(), Display.class);
                        startActivity(i);
                    } else {
                        getFragmentManager().popBackStack();
                    }
                    // handle back button's click listener
                    return true;
                }
                return false;
            }
        });
    }

    class BackgroundTask extends AsyncTask<String,Void,String> {
        String points,valid,name,useid;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String login_url ="http://www.onlinemallassistant.com/points.php";
            String response="";
            String userid=params[0];

            try {
                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(userid,"UTF-8");

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
                        points=jsonObject.getString("points");
                        valid=jsonObject.getString("validity");
                        name=jsonObject.getString("name");
                        useid=jsonObject.getString("id");
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
            String s=t1.getText().toString();
            t1.setText(s+name);
            s=t2.getText().toString();
            t2.setText(s+points);
            s=t3.getText().toString();
            s=s+valid.substring(8,10)+"/"+valid.substring(5,7)+"/"+valid.substring(0,4);
            t3.setText(s);
            s=t4.getText().toString();
            t4.setText(s+useid);
        }

    }
}
