package com.pdsasistance.pdsa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Howto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.howto);
        String data="Internet connectivity is required to operate this application.\n" +
                "\n" +
                "Signup- Visitors need to register by providing the required information. After this, the registered \n" +
                "\n" +
                "user can login to access the services.\n" +
                "\n" +
                "Map- Select the floor and zoom in and out to locate the store without travelling to the store map \n" +
                "\n" +
                "at the terminal of the mall.\n" +
                "\n" +
                "Shopping list – Users can create can their own personalized shopping list by adding new items \n" +
                "\n" +
                "and saving it for future reference while shopping, thus reducing the memory load on users.\n" +
                "\n" +
                "Calculator- While shopping if users need to calculate the price of products after discount or any \n" +
                "\n" +
                "calculations are required one can use the calculator provided in the application itself.\n" +
                "\n" +
                "Feedback- If any further queries or doubts arise user can always provide us with feedback. They \n" +
                "\n" +
                "can mail us their feedback and we will reply within 24 hrs.\n" +
                "\n" +
                "Smart Budget - To personalize your search according to your needs and budget, you just need to \n" +
                "\n" +
                "enter your limit and we will provide with all the options available thus making it faster and \n" +
                "\n" +
                "convenient for you.\n" +
                "\n" +
                "Search- To find any store, restaurant, grocery shops etc. just select the category and the required \n" +
                "\n" +
                "store and the search will be performed. The search will direct you to your desired store page \n" +
                "\n" +
                "giving information about it.\n" +
                "\n" +
                " Compare- For comparing any two stores, select the stores and the ratings will be provided to \n" +
                "\n" +
                "you. This makes it easy for you to compare and take decisions appropriately.\n" +
                "\n" +
                "Once you leave the mall you can logout if you wish to.\n" +
                "\n" +
                "If you have any further questions or concerns, please contact us at \n" +
                "\n" +
                "onlinemallassisant@gmail.com";
        TextView t=(TextView)findViewById(R.id.textView1);
        t.setText(data);
    }
}
