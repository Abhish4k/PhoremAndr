package com.phorem.api_services;

import com.phorem.api_model.ForgetPassResponse;
import com.phorem.api_model.LoginResponse;
import com.phorem.api_model.NewPassResponse;
import com.phorem.api_model.OtpVerifResponse;
import com.phorem.api_model.RegisterResponse;
import com.phorem.api_model.add_alarm.AddAlarmRequestModel;
import com.phorem.api_model.get_all_memo.GetAllMemoResponse;
import com.phorem.api_model.get_memo_by_id.GetMemoByIdResponse;
import com.phorem.api_model.get_user_profile.GetUserProfileResponse;
import com.phorem.api_model.update_user_profile.UpdateProfileResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("p_register")
    Call<RegisterResponse> callRegisterApi(@Field("firstname") String firstname, @Field("lastname") String lastname, @Field("email") String email, @Field("country") String country, @Field("password") String password);


    @FormUrlEncoded
    @POST("p_login")
    Call<LoginResponse> callLoginApi( @Field("email") String email, @Field("password") String password, @Field("device_token") String device_token, @Field("timezone") String timeZone);


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

    @FormUrlEncoded
    @POST("get_memo")
    Call<GetMemoByIdResponse> callGetMemoById(@Field("memo_id") String memo_id);

    @FormUrlEncoded
    @POST("create_memo")
    Call<RegisterResponse> callCreateMemoApi(@Field("name") String name, @Field("user_id") String user_id,
                                             @Field("phone_number") String phone_number, @Field("memo") String memo,
                                             @Field("reminder") String reminder);


    @Multipart
    @POST("create_memo")
    Call<RegisterResponse> callCreateMemoWithVoiceApi(@Part("name") RequestBody name, @Part("user_id") RequestBody user_id,
                                                      @Part("phone_number") RequestBody phone_number, @Part("memo") RequestBody memo,
                                                      @Part("reminder") RequestBody reminder, @Part MultipartBody.Part voice_memo);




    @FormUrlEncoded
    @POST("edit_memo")
    Call<GetMemoByIdResponse> callEditMemoApi(@Field("id") String id, @Field("name") String name, @Field("user_id") String user_id,
                                              @Field("phone_number") String phone_number, @Field("memo") String memo,
                                              @Field("reminder") String reminder);


    @Multipart
    @POST("edit_memo")
    Call<GetMemoByIdResponse> callEditMemoWithVoiceApi(@Part("id") RequestBody id, @Part("name") RequestBody name,
                                                       @Part("user_id") RequestBody user_id,
                                                       @Part("phone_number") RequestBody phone_number, @Part("memo") RequestBody memo,
                                                       @Part("reminder") RequestBody reminder,@Part MultipartBody.Part voice_memo);


    @FormUrlEncoded
    @POST("delete_memo")
    Call<RegisterResponse> callDeleteMemoApi(@Field("memo_id") String memo_id);


    @Multipart
    @POST("settings")
    Call<AddAlarmRequestModel> callAddAlarmApi(@Part("user_id") RequestBody user_id,
                                               @Part  MultipartBody.Part custom_sound,
                                               @Part("channel_id") RequestBody channel_id);



    @FormUrlEncoded
    @POST("update_timezone")
    Call<RegisterResponse> callUpdateTimeZoneApi(@Field("user_id") String userId, @Field("timezone") String timeZone);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<ForgetPassResponse> callForgotPasswordApi (@Field("email") String email );

    @FormUrlEncoded
    @POST("verify_otp")
    Call<OtpVerifResponse>callOtpVerifyApi(@Field("email")String email , @Field("verification_code") String verification_code);

    @FormUrlEncoded
    @POST("reset_password")
    Call<NewPassResponse>callResetPassApi(@Field("email")String email ,
                                          @Field("new_password") String new_password);


}
