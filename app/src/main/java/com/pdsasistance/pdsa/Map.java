package com.pdsasistance.pdsa;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class Map extends Fragment  {

    View view;
    Button button1,button2,button3,button4;
    ImageView img;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.map,container,false);

        button1=(Button)view.findViewById(R.id.button1);
        button2=(Button)view.findViewById(R.id.button2);
        button3=(Button)view.findViewById(R.id.button3);
        button4=(Button)view.findViewById(R.id.button4);
        img=(ImageView)view.findViewById(R.id.imageView1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageResource(R.drawable.map1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageResource(R.drawable.picture1);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageResource(R.drawable.picture2);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageResource(R.drawable.picture3);
            }
        });

      return view;
    }

    private void setContentView(TouchImageView img) {
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
