package com.binaryit.syncusingretrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("students.php")
    Call<AddResponse> SendToServer(
            @Field("Name") String Name,
            @Field("Number") String Number,
            @Field("Age") String Age
    );



}
