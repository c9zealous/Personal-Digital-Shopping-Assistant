package com.pdsasistance.pdsa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ShoppingList extends Fragment  implements View.OnClickListener{
    Button newButton,saveButton,openButton;
    EditText text;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
       super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.shoppinglist,container,false);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        newButton=(Button)view.findViewById(R.id.newButton);
        newButton.setOnClickListener(this);

        saveButton=(Button)view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        openButton=(Button)view.findViewById(R.id.openButton);
        openButton.setOnClickListener(this);

        text=(EditText)view.findViewById(R.id.text);
      // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |     WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        return view;
    }


    @Override
    public void onClick(View v) {

            final EditText fileName=new EditText(getActivity());
            AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
            ad.setView(fileName);

            if (v.getId() == R.id.saveButton) {
                ad.setMessage("Save File");

                ad.setPositiveButton("Save",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            FileOutputStream fout=getActivity().openFileOutput(fileName.getText().toString()+".txt",getActivity().MODE_WORLD_READABLE);
                            fout.write(text.getText().toString().getBytes());
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error Occured: "+e,Toast.LENGTH_LONG).show();
                        }
                    }
                });

                ad.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                ad.show();

            }

            if(v.getId()==R.id.openButton) {
                ad.setMessage("Open File");

                ad.setPositiveButton("Open",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int c;
                        text.setText("");

                        try {
                            FileInputStream fin =getActivity().openFileInput(fileName.getText().toString()+".txt");

                            while ((c = fin.read()) != -1)
                            {
                                text.setText((text.getText().toString() + Character.toString((char) c)));
                            }
                        }catch (Exception e) {
                            Toast.makeText(getContext(), "Error Occured: "+e, Toast.LENGTH_LONG).show();
                        }
                    }
                });

                ad.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                ad.show();
            }

            if(v.getId()==R.id.newButton) {
                text.setText("");
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


}

