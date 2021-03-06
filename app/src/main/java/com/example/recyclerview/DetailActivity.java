package com.example.recyclerview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;


import static com.example.recyclerview.MainActivity.EXTRA_CREATOR;
import static com.example.recyclerview.MainActivity.EXTRA_LIKES;
import static com.example.recyclerview.MainActivity.EXTRA_URL;

public class DetailActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewCreator = findViewById(R.id.text_creator_detail);
        TextView textViewLikes = findViewById(R.id.text_like_detail);

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String creatorName = intent.getStringExtra(EXTRA_CREATOR);
        int likeCount = intent.getIntExtra(EXTRA_LIKES, 0);

        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(this).load(imageUrl).fit().centerInside().into(imageView);
        }
        if (!TextUtils.isEmpty(creatorName)) {
            textViewCreator.setText(creatorName);
        }
        textViewLikes.setText("Likes" + likeCount);


    }
}
