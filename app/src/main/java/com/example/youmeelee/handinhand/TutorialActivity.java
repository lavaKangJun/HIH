package com.example.youmeelee.handinhand;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TutorialActivity extends Activity
{
    static TextView mDotsText[];
    private int mDotsCount;
    private LinearLayout mDotsLayout;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_dot);

        //here we create the gallery and set our adapter created before
        Gallery gallery = (Gallery)findViewById(R.id.gallery);
        gallery.setAdapter(new TutorialAdapter(this));

        mDotsLayout = (LinearLayout)findViewById(R.id.image_count);

        //점 갯수 선언
        mDotsCount = gallery.getAdapter().getCount();

        //점 선언
        mDotsText = new TextView[mDotsCount];

        //here we set the dots
        for (int i = 0; i < mDotsCount; i++) {
            mDotsText[i] = new TextView(this);
            mDotsText[i].setText(".");
            mDotsText[i].setTextSize(45);
            mDotsText[i].setTypeface(null, Typeface.BOLD);
            mDotsText[i].setTextColor(android.graphics.Color.GRAY);
            mDotsLayout.addView(mDotsText[i]);
        }

        //점 이동 시 회색 변경
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int pos, long l) {

                for (int i = 0; i < mDotsCount; i++) {
                    TutorialActivity.mDotsText[i]
                            .setTextColor(Color.GRAY);
                }

                String strColor = "#abcbfc";
                TutorialActivity.mDotsText[pos]
                        .setTextColor(Color.parseColor(strColor));
            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {

            }
        });
    }

    public void onSkipButtonClicked(View view){
        Intent intent = new Intent(this,tempTutorialActivity.class);
        startActivity(intent);
        finish();
    }
}