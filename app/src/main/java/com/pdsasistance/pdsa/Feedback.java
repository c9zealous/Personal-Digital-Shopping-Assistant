package com.pdsasistance.pdsa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.mail.Session;

public class Feedback extends Fragment implements View.OnClickListener {

    View view;
    TextView txt;
    public String message;
    TextView textView8;
    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    EditText name, email1, phone,description;
    String  nametsr, emailstr, phonestr, subject, descriptionstr;
    private Button buttonSend;
    public String email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.feedback,container,false);

        name = (EditText) view. findViewById(R.id.ET_name);
        email1 = (EditText) view. findViewById(R.id.ET_email);
        phone = (EditText) view. findViewById(R.id.ET_number);
        description=(EditText) view.findViewById(R.id.editText);


        buttonSend = (Button) view.findViewById(R.id.button3);

        buttonSend.setOnClickListener(this);

        return view;
    }



    @Override
    public void onClick(View v) {
        nametsr = name.getText().toString();
        emailstr = email1.getText().toString();
        phonestr = phone.getText().toString();
        descriptionstr=description.getText().toString();

        if (nametsr.equals("")) {
            Toast name = Toast.makeText(getActivity(), "Name field is Empty!", Toast.LENGTH_SHORT);
            name.show();
        } else if (emailstr.equals("")) {
            Toast email = Toast.makeText(getActivity(), "Email field is Empty!", Toast.LENGTH_SHORT);
            email.show();

        } else if (phonestr.equals("")) {
            Toast uname = Toast.makeText(getActivity(), "Contact field is Empty!", Toast.LENGTH_SHORT);
            uname.show();
        }
        else if(!nametsr.equals("") && !emailstr.equals("") && !phonestr.equals("")){


            if (!TextUtils.isEmpty(emailstr) && Utility.validate(emailstr) && Utility.isValidPhoneNumber(phonestr)) {

                GMailSender();


            } else {
                Toast.makeText(getActivity(), "Please enter valid email",
                        Toast.LENGTH_LONG).show();
            }

        }
        else{
            Toast password = Toast.makeText(getActivity(), "Not Succesfull!", Toast.LENGTH_SHORT);
            password.show();
        }
    }
    public void GMailSender() {

        nametsr = name.getText().toString();
        emailstr = email1.getText().toString();
        phonestr = phone.getText().toString();
        descriptionstr=description.getText().toString();
        String text="User Feedback,";
       // String text1="       A user on bloodcomfort is in an emergency and needs you to donate blood.\nFollowing are the user's contact details: ";
        // String link="https://play.google.com/store/apps/details?id=com.bloodcomfort.www.bloodcomfort";

        StringBuilder s = new StringBuilder(100);
        s.append(text);
      //  s.append("\n");
      //  s.append(text1);
        s.append("\n");
        s.append(nametsr);
        s.append("\n");
        s.append(emailstr);
        s.append("\n");
        s.append(phonestr);
        s.append("\n");
        s.append(descriptionstr);
        s.append("\n");
      //  s.append(link);
        subject="New Feedback By a Customer";
        //Creating SendMail object
        email="onlinemallassistant@gmail.com";
        GMailSender sm = new GMailSender(getContext(), email, subject, s);

        //Executing sendmail to send email
        sm.execute();

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
