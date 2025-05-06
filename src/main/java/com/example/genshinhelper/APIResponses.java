package com.example.genshinhelper;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class APIResponses {

    public static class CharacterResponse {
        public String name;
        public cv cv;
        public String description;
        public String birthday;
        public Images images;
        public String weaponText;
        public String rarity;
        @SerializedName("elementText")
        public String element;
        public String getCv() {
            return cv.english;
        }

        public String getImageUrl() {
            return images.hoyolabAvatar;
        }

    }

    public static class cv {
        String english;
    }

    public static class Images {
        @SerializedName("hoyolab-avatar")
        public String hoyolabAvatar;
    }

    public static class ConstellationResponse {
        public ConstellationDetail c1;
        public ConstellationDetail c2;
        public ConstellationDetail c3;
        public ConstellationDetail c4;
        public ConstellationDetail c5;
        public ConstellationDetail c6;
    }

    public static class ConstellationDetail {
        public String name;
        public String description;
    }


}