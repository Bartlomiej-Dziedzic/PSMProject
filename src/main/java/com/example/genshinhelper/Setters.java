package com.example.genshinhelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class Setters {

    public static void setRarityImage(ImageView rarityImageView, String rarity) {
        switch (rarity) {
            case "4":
                rarityImageView.setImageResource(R.drawable.icon_4_star);
                break;
            case "5":
                rarityImageView.setImageResource(R.drawable.icon_5_star);
                break;
        }
    }

    public static void setArtifactRarityImages(LinearLayout container, Integer[] rarityList) {
        container.removeAllViews();

        for (int rarity : rarityList) {
            int starResId = getStarDrawable(rarity);
            ImageView star = new ImageView(container.getContext());
            star.setImageResource(starResId);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            star.setLayoutParams(params);
            container.addView(star);
        }
    }

    private static int getStarDrawable(int rarity) {
        switch (rarity) {
            case 1: return R.drawable.icon_1_star;
            case 2: return R.drawable.icon_2_star;
            case 3: return R.drawable.icon_3_star;
            case 4: return R.drawable.icon_4_star;
            case 5: return R.drawable.icon_5_star;
            default: return 0;
        }
    }

    public static void setWeaponTypeImage(ImageView weaponTypeImageView, String weaponType) {
        switch (weaponType) {
            case "Sword":
                weaponTypeImageView.setImageResource(R.drawable.icon_weapon_sword);
                break;
            case "Bow":
                weaponTypeImageView.setImageResource(R.drawable.icon_weapon_bow);
                break;
            case "Claymore":
                weaponTypeImageView.setImageResource(R.drawable.icon_weapon_claymore);
                break;
            case "Polearm":
                weaponTypeImageView.setImageResource(R.drawable.icon_weapon_polearm);
                break;
            case "Catalyst":
                weaponTypeImageView.setImageResource(R.drawable.icon_weapon_catalyst);
                break;
        }
    }

    @SuppressLint("ResourceAsColor")
    public static void setElementImage(ImageView elementImageView, TextView nameTextView, String element) {
        Context context = nameTextView.getContext();

        switch (element) {
            case "Pyro":
                elementImageView.setImageResource(R.drawable.icon_element_pyro);
                nameTextView.setTextColor(ContextCompat.getColor(context, R.color.pyro));
                break;
            case "Hydro":
                elementImageView.setImageResource(R.drawable.icon_element_hydro);
                nameTextView.setTextColor(ContextCompat.getColor(context, R.color.hydro));
                break;
            case "Electro":
                elementImageView.setImageResource(R.drawable.icon_element_electro);
                nameTextView.setTextColor(ContextCompat.getColor(context, R.color.electro));
                break;
            case "Anemo":
                elementImageView.setImageResource(R.drawable.icon_element_anemo);
                nameTextView.setTextColor(ContextCompat.getColor(context, R.color.anemo));
                break;
            case "Geo":
                elementImageView.setImageResource(R.drawable.icon_element_geo);
                nameTextView.setTextColor(ContextCompat.getColor(context, R.color.geo));
                break;
            case "Cryo":
                elementImageView.setImageResource(R.drawable.icon_element_cryo);
                nameTextView.setTextColor(ContextCompat.getColor(context, R.color.cryo));
                break;
            case "Dendro":
                elementImageView.setImageResource(R.drawable.icon_element_dendro);
                nameTextView.setTextColor(ContextCompat.getColor(context, R.color.dendro));
                break;
            default:
                elementImageView.setImageResource(R.drawable.icon_element_unaligned);
        }
    }
    public static void setLink(TextView linkTextView, String characterName) {
        StringBuilder formattedName = new StringBuilder();

        for (char character : characterName.toCharArray()) {
            if (character == ' ') {
                formattedName.append('_');
            } else {
                formattedName.append(character);
            }
        }

        String url = "https://genshin-impact.fandom.com/wiki/" + formattedName.toString();
        linkTextView.setText(url);
        linkTextView.setClickable(true);
        linkTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            v.getContext().startActivity(intent);
        });
    }
}
