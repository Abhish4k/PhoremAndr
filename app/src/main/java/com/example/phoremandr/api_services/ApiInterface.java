package com.example.phoremandr.api_services;

import com.example.phoremandr.api_model.LoginResponse;
import com.example.phoremandr.api_model.RegisterResponse;
import com.example.phoremandr.api_model.get_all_memo.GetAllMemoResponse;
import com.example.phoremandr.api_model.get_user_profile.GetUserProfileResponse;
import com.example.phoremandr.api_model.update_user_profile.UpdateProfileResponse;

import java.security.SecureRandom;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("p_register")
    Call<RegisterResponse> callRegisterApi(@Field("firstname") String firstname, @Field("lastname") String lastname, @Field("email") String email, @Field("password") String password, @Field("country") String country);


    @FormUrlEncoded
    @POST("p_login")
    Call<LoginResponse> callLoginApi( @Field("email") String email, @Field("password") String password, @Field("device_token") String device_token);


    @FormUrlEncoded
    @POST("p_get_profile")
    Call<GetUserProfileResponse> callGetUserProfile(@Field("user_id") String userId);



    @Multipart
    @POST("p_edit_profile")
    Call<UpdateProfileResponse> callUpdateProfileWithImage(@Part("id") RequestBody userId, @Part("firstname") RequestBody firstName, @Part("lastname") RequestBody lastName,
                                                           @Part("email")RequestBody  email, @Part MultipartBody.Part profile_pic );



    @FormUrlEncoded
    @POST("p_edit_profile")
    Call<UpdateProfileResponse> callUpdateProfile(@Field("id") String userId, @Field("firstname") String firstName, @Field("lastname") String lastName,
                                                  @Field("email")String email);


    @FormUrlEncoded
    @POST("get_all_memos")
    Call<GetAllMemoResponse> callGetAllMemoApi(@Field("user_id") String userId);



}
