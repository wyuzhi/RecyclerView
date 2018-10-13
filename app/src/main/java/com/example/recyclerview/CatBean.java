package com.example.recyclerview;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CatBean {
    private String mImageUrl;
    private String mCreator;
    private int mLikes;

    public String getImageUrl() {
        return mImageUrl;
    }


    public String getCreator() {
        return mCreator;
    }


    public int getLikes() {
        return mLikes;
    }


    public CatBean(String mImageUrl, String mCreator, int mLikes) {
        this.mImageUrl = mImageUrl;
        this.mCreator = mCreator;
        this.mLikes = mLikes;
    }


    public static List<CatBean> extractCatBean(String info) {
        if (TextUtils.isEmpty(info)) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(info);
            if (jsonObject == null) {
                return null;
            }
            List<CatBean> mExampleList = new ArrayList();
            JSONArray jsonArray = jsonObject.optJSONArray("hits");
            if (jsonArray == null) {
                return null;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject hit = jsonArray.optJSONObject(i);
                String creatorName = hit.optString("user");
                String imageUrl = hit.optString("webformatURL");
                int likeCount = hit.optInt("likes");

                mExampleList.add(new CatBean(imageUrl, creatorName, likeCount));
            }
            return mExampleList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
