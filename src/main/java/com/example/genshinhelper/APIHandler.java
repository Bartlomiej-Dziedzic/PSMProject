package com.example.genshinhelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIHandler {
    @GET("characters")
    Call<List<String>> getCharacterNames(
            @Query("query") String query,
            @Query("matchCategories") boolean matchCategories
    );
    @GET("characters")
    Call<APIResponses.CharacterResponse> getCharacterInfo(@Query("query") String name);

    @GET("constellations")
    Call<APIResponses.ConstellationResponse> getConstellations(@Query("query") String characterName);

    @GET("weapons")
    Call<List<String>> getWeaponNames(
            @Query("query") String query,
            @Query("matchCategories") boolean matchCategories
    );

    @GET("weapons")
    Call<APIResponses.WeaponResponse> getWeaponInfo(@Query("query") String name);

    @GET("artifacts")
    Call<List<String>> getArtifactNames(
            @Query("query") String query,
            @Query("matchCategories") boolean matchCategories
    );

    @GET("artifacts")
    Call<APIResponses.ArtifactResponse> getArtifactInfo(@Query("query") String name);

}