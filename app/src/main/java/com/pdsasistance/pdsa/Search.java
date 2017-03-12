package com.pdsasistance.pdsa;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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

public class Search extends Fragment {

    Spinner sp,sp1;
    String store_name;
    ArrayList<String> listItems=new ArrayList<>();
    ArrayList<String> listItems1=new ArrayList<>();

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter1;

    Button buttonSearch;

    public String category,category_id;

    int category_id1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.search,container,false);

        sp=(Spinner)v.findViewById(R.id.spinner);
        sp1=(Spinner)v.findViewById(R.id.spinner1);

        adapter=new ArrayAdapter<String>(getActivity(),R.layout.spinner_layout,R.id.txt,listItems);
        adapter1=new ArrayAdapter<String>(getActivity(),R.layout.spinner_layout,R.id.txt,listItems1);

        sp.setAdapter(adapter);
        sp1.setAdapter(adapter1);

        buttonSearch=(Button)v.findViewById(R.id.buttonSearch);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_name=sp1.getSelectedItem().toString();
                Intent intent = new Intent(getActivity(), ProfileView.class);
                intent.putExtra("storename",store_name);
                startActivity(intent);
            }
        });

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int position, long id) {
                category = sp.getSelectedItem().toString();
                category_id1 = (int) sp.getItemIdAtPosition(position);
                category_id = String.valueOf(category_id1 );

                adapter1.clear();

                BackgroundTask backgroundTask1 = new BackgroundTask();
                backgroundTask1.execute(category_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        return v;

    }


    class BackgroundTask extends AsyncTask<String,Void,String> {
        ArrayList<String> list1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list1=new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
            String login_url2 ="http://www.onlinemallassistant.com/store.php";
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

                String data = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(id,"UTF-8");

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

                        StringBuilder s2 = new StringBuilder(100);

                        s2.append(jsonObject.getString("name"));


                        list1.add(String.valueOf(s2));

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
            listItems1.addAll(list1);
            adapter1.notifyDataSetChanged();
        }

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

    private class CategoryValue extends AsyncTask<Void,Void,Void> {
        ArrayList<String> list;
        protected void onPreExecute(){
            super.onPreExecute();
            list=new ArrayList<>();
        }
        protected Void doInBackground(Void...params){
            String login_url1 ="http://www.onlinemallassistant.com/category.php";
            try{

                URL url =new URL(login_url1);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response ="";
                String line="";
                while ((line=bufferedReader.readLine())!=null){

                    response+=line;
                }

                try{
                    JSONArray jArray =new JSONArray(response);
                    for(int i=0;i<jArray.length();i++){
                        JSONObject jsonObject=jArray.getJSONObject(i);
                        // add interviewee name to arraylist
                        StringBuilder s1 = new StringBuilder(100);
                        s1.append(jsonObject.getString("category"));

                        list.add(String.valueOf(s1));
                    }
                }
                catch(JSONException e){
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
        protected void onPostExecute(Void result){
            listItems.addAll(list);
            adapter.notifyDataSetChanged();

        }
    }

    public void onStart(){
        super.onStart();
        new CategoryValue().execute();
    }
}
