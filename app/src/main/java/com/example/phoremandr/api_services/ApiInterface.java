package com.example.phoremandr.api_services;

import com.example.phoremandr.api_model.LoginResponse;
import com.example.phoremandr.api_model.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("p_register")
    Call<RegisterResponse> callRegisterApi(@Field("firstname") String firstname, @Field("lastname") String lastname, @Field("email") String email, @Field("password") String password, @Field("country") String country);


    @FormUrlEncoded
    @POST("p_login")
    Call<LoginResponse> callLoginApi( @Field("email") String email, @Field("password") String password, @Field("device_token") String device_token);



}
