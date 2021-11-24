//package com.example.omok;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//
//import java.util.ArrayList;
//
//public class ThirdActivity extends AppCompatActivity {
//
//    GridView gridview;
//    int col = 15;
//    int row = 15;
//    int[] omokList = new int[col*row]; //0으로 초기화된 배
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        gridview = (GridView) findViewById(R.id.omokstage);
//
//        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), omokList);
//        gridview.setAdapter(customAdapter);
//
//
//
//        for (int i = 0; i < 15; i++) {
//            for (int j = 0; i < 15; i++) {
//                gridview.addView();
//            }
//        }
//    }
//}