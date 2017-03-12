package com.pdsasistance.pdsa;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;

public class SmartBudget2 extends AppCompatActivity {

    String category,money;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_budget2);

        category=getIntent().getStringExtra("category");
        money= getIntent().getStringExtra("money");

        lv=(ListView)findViewById(R.id.listView);
        listItems=new ArrayList<String>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        lv.setAdapter(adapter);

        BackgroundTask back=new BackgroundTask();
        back.execute(category,money);
    }

    class BackgroundTask extends AsyncTask<String,Void,String> {
        ArrayList<String> list;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list=new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
            String login_url ="http://www.onlinemallassistant.com/smartbudget.php";
            String response="";
            String cat=params[0];
            String mon=params[1];

            try {
                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data = URLEncoder.encode("category", "UTF-8")+"="+URLEncoder.encode(cat,"UTF-8")+"&"+URLEncoder.encode("money","UTF-8")+"="+URLEncoder.encode(mon,"UTF-8");

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
                        StringBuilder s = new StringBuilder(100);
                        s.append(jsonObject.getString("name"));
                        list.add(String.valueOf(s));
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
            listItems.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }
}