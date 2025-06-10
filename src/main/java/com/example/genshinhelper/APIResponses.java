package com.example.genshinhelper;

import com.google.gson.annotations.SerializedName;

public class APIResponses {

    public static class Images {
        @SerializedName("hoyolab-avatar")
        public String hoyolabAvatar;
        public String awakenicon;
        public String flower;
        public String plume;
        public String sands;
        public String goblet;
        public String circlet;

    }

    public static class cv {
        String english;
    }

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

    public static class WeaponResponse {
        public String name;
        public String description;
        public String story;
        public String weaponText;
        public String rarity;
        public String mainStatText;
        public String effectName;
        public Images images;

        @SerializedName("r1")
        public EffectDetail r1;
        @SerializedName("r2")
        public EffectDetail r2;
        @SerializedName("r3")
        public EffectDetail r3;
        @SerializedName("r4")
        public EffectDetail r4;
        @SerializedName("r5")
        public EffectDetail r5;

        public static class EffectDetail {
            public String description;
        }

        public String getImageUrl() {
            return images.awakenicon;
        }
    }

    public static class ArtifactResponse {
        public String name;
        public String description;
        public String story;
        public Integer[] rarityList;
        public String effect2Pc;
        public String effect4Pc;
        public Images images;

        public ArtifactPiece flower;
        public ArtifactPiece plume;
        public ArtifactPiece sands;
        public ArtifactPiece goblet;
        public ArtifactPiece circlet;

        public static class ArtifactPiece {
            public String name;
            public String description;
            public String story;
        }


        public String[] getImagesUrl() {
            return new String[] {
                    images.flower,
                    images.plume,
                    images.sands,
                    images.goblet,
                    images.circlet
            };
        }
    }


}