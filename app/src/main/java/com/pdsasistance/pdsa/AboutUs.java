package com.pdsasistance.pdsa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        String data="What\n" +
                "\n" +
                "Personal Digital Shopping Assistant, an application that is implemented on handheld devices such as \n" +
                "\n" +
                "Smart Phones and Tablets which provide information about store locations, information about sales and \n" +
                "\n" +
                "ongoing offers and discounts, smart budget option, comparing stores and other personalized options like \n" +
                "\n" +
                "calculator, feedback and shopping list for individual visitors, so as to assist visitors in the mall.\n" +
                "\n" +
                "Why\n" +
                "\n" +
                "The main objective of Personal Digital Shopping Assistant is to solve the difficulties of the \n" +
                "\n" +
                "customers while shopping. This application provides and easy and user friendly way to assist \n" +
                "\n" +
                "visitors. It makes shopping in a mall convenient, faster and a more enjoyable experience. Also, \n" +
                "\n" +
                "providing additional features about the ongoing events of the mall and much more hold the users \n" +
                "\n" +
                "interest.";
        TextView t=(TextView)findViewById(R.id.textView1);
        t.setText(data);
    }
}
