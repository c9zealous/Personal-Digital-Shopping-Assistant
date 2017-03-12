package com.pdsasistance.pdsa;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class SmartBudget extends Fragment {
    Spinner sp;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems=new ArrayList<>();
    Button b;
    EditText et;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.smartbudget,container,false);
        sp=(Spinner)v.findViewById(R.id.spinner1);
        adapter=new ArrayAdapter<String>(getActivity(),R.layout.spinner_layout,R.id.txt,listItems);
        sp.setAdapter(adapter);
        et=(EditText)v.findViewById(R.id.editText1);
        b=(Button)v.findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=sp.getSelectedItem().toString();
                String s1=et.getText().toString();
                Intent l=new Intent(getActivity(), SmartBudget2.class);
                l.putExtra("category",s);
                l.putExtra("money",s1);
                startActivity(l);
            }
        });

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

    private class CategoryValue extends AsyncTask<Void,Void,Void> {
        ArrayList<String> list;
        protected void onPreExecute(){
            super.onPreExecute();
            list=new ArrayList<>();
        }
        protected Void doInBackground(Void...params){
            String login_url1 ="http://www.onlinemallassistant.com/category.php";
            InputStream is=null;
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
                        // s1.append(jsonObject.getString("id"));
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
