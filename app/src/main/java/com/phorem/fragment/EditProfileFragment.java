package com.phorem.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.phorem.R;
import com.phorem.api_model.get_user_profile.GetUserProfileResponse;
import com.phorem.api_model.get_user_profile.GetUserProfileResponseData;
import com.phorem.api_model.update_user_profile.UpdateProfileDataResponse;
import com.phorem.api_model.update_user_profile.UpdateProfileResponse;
import com.phorem.api_request_model.UpdateProfileRequestModel;
import com.phorem.base.BaseFragment;
import com.phorem.databinding.FragmentEditProfileBinding;
import com.phorem.utils.AppValidator;
import com.phorem.utils.SharedPreferencesKeys;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends BaseFragment {
    FragmentEditProfileBinding editProfileBinding;
    Bitmap bitmapImage = null;
    Uri imageUri;
    @Override
    public ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container) {
        editProfileBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_edit_profile, container, false);
        initView();
        return editProfileBinding;
    }



    void initView(){
        callGetProfileApi();
        editProfileBinding.editProfileToolbar.setVisibility(true);
        editProfileBinding.editProfileToolbar.setNameData(getString(R.string.edit_profile));
        editProfileBinding.editProfileToolbar.ivBack.setOnClickListener(v -> getFragmentManager().popBackStack());

        editProfileBinding.tvChangeAvatar.setOnClickListener(v -> pickImage());

        editProfileBinding.btnUpdateProfile.setOnClickListener(v -> updateProfile());
    }

    void pickImage(){

        if(checkPermission()){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(galleryIntent);

        }

    }


    void updateProfile(){
        UpdateProfileRequestModel updateProfileRequestModel = new UpdateProfileRequestModel(editProfileBinding.etFirstName.getText().toString().trim(),
                editProfileBinding.etLastName.getText().toString().trim(),
                editProfileBinding.etEmail.getText().toString().trim());

        if(AppValidator.validateProfile(requireContext(), updateProfileRequestModel)){
            if(bitmapImage != null){
                callUpdateProfileWithImageApi(updateProfileRequestModel);
            }else{
                callUpdateProfileApi(updateProfileRequestModel);
            }
        }
    }


    private File saveBitmap(Uri filename) {
        Cursor cursor = null;

        try {
            String[] proj = { MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION };
            cursor = requireActivity()
                    .managedQuery(filename, proj, null, null, null);
            int file_ColumnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int orientation_ColumnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
            if (cursor.moveToFirst()) {
                String orientation = cursor
                        .getString(orientation_ColumnIndex);
                return new File(cursor.getString(file_ColumnIndex));
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    Uri returnUri = data.getData();

                    try {
                        bitmapImage = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), returnUri);
                        editProfileBinding.ivUser.setImageBitmap(bitmapImage);
                        imageUri = returnUri;


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                }
            });


    boolean checkPermission(){
        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.TIRAMISU){
            Log.e("TIRAMISU permission", "check here");
            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
            ){
                List<String> listPermissionsNeeded = new ArrayList<>();
                listPermissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES);
                ActivityCompat.requestPermissions(requireActivity(),listPermissionsNeeded.toArray
                        (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
                return  false;
            }else {
                return true;
            }
        }else {
            Log.e("permission ====> ", "check here" + ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE));
            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                List<String> listPermissionsNeeded = new ArrayList<>();
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(requireActivity(),listPermissionsNeeded.toArray
                        (new String[listPermissionsNeeded.size()]),100);
                return  false;
            }else {
                return true;
            }

        }

    }


    public void  callGetProfileApi(){
        editProfileBinding.progressEdit.setVisibility(View.VISIBLE);
        Call<GetUserProfileResponse> call3 = apiInterface.callGetUserProfile(sharedPrefHelper.getValue(SharedPreferencesKeys.userId));

        call3.enqueue(new Callback<GetUserProfileResponse>() {
            @Override
            public void onResponse(@NotNull Call<GetUserProfileResponse> call, @NotNull Response<GetUserProfileResponse> response) {

                editProfileBinding.progressEdit.setVisibility(View.GONE);

                assert response.body() != null;
                AppValidator.showToast(requireActivity(), response.body().getStatus());

                GetUserProfileResponseData getUserProfileResponseData = response.body().getData();

                if(!getUserProfileResponseData.getProfilePic().isEmpty()){
                    Glide.with(requireActivity()).load(getUserProfileResponseData.getProfilePic()).into(editProfileBinding.ivUser);
                }

                if(!getUserProfileResponseData.getFirstname().isEmpty()){
                    editProfileBinding.etFirstName.setText(getUserProfileResponseData.getFirstname());
                }

                if(!getUserProfileResponseData.getLastname().isEmpty()){
                    editProfileBinding.etLastName.setText(getUserProfileResponseData.getLastname());
                }

                if(!getUserProfileResponseData.getEmail().isEmpty()){
                    editProfileBinding.etEmail.setText(getUserProfileResponseData.getEmail());
                }


            }
            @Override
            public void onFailure(@NotNull  Call<GetUserProfileResponse> call,@NotNull Throwable t) {
                editProfileBinding.progressEdit.setVisibility(View.GONE);
                AppValidator.logData("getUserProfile",""+t.getMessage());
            }
        });


    }



    public void  callUpdateProfileWithImageApi(UpdateProfileRequestModel updateProfileRequestModel){
        editProfileBinding.progressEdit.setVisibility(View.VISIBLE);

        File file = saveBitmap(imageUri);

        RequestBody firstName = RequestBody.create(updateProfileRequestModel.getFirstName(),MediaType.parse("text/plain"));
        RequestBody userId = RequestBody.create(sharedPrefHelper.getValue(SharedPreferencesKeys.userId),MediaType.parse("text/plain"));
        RequestBody lastName = RequestBody.create(updateProfileRequestModel.getLastName(),MediaType.parse("text/plain"));
        RequestBody email = RequestBody.create(updateProfileRequestModel.getEmail(),MediaType.parse("text/plain"));
        RequestBody requestFile = RequestBody.create(file,MediaType.parse("multipart/form-data"));
                MultipartBody.Part body = MultipartBody.Part.createFormData("profile_pic", file.getName(), requestFile);

                Log.e("profileImage","" + body);

        Call<UpdateProfileResponse> call3 = apiInterface.callUpdateProfileWithImage(userId, firstName,
                lastName,email, body);

        call3.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(@NotNull Call<UpdateProfileResponse> call, @NotNull Response<UpdateProfileResponse> response) {

                editProfileBinding.progressEdit.setVisibility(View.GONE);

                assert response.body() != null;
                AppValidator.showToast(requireActivity(), response.body().getStatus());

                UpdateProfileDataResponse getUserProfileResponseData = response.body().getData();

                if(!getUserProfileResponseData.getProfilePic().isEmpty()){
                    Glide.with(requireActivity()).load(getUserProfileResponseData.getProfilePic()).into(editProfileBinding.ivUser);
                }

                if(!getUserProfileResponseData.getFirstname().isEmpty()){
                    editProfileBinding.etFirstName.setText(getUserProfileResponseData.getFirstname());
                }

                if(!getUserProfileResponseData.getLastname().isEmpty()){
                    editProfileBinding.etLastName.setText(getUserProfileResponseData.getLastname());
                }

                if(!getUserProfileResponseData.getEmail().isEmpty()){
                    editProfileBinding.etEmail.setText(getUserProfileResponseData.getEmail());
                }


            }
            @Override
            public void onFailure(@NotNull  Call<UpdateProfileResponse> call,@NotNull Throwable t) {
                editProfileBinding.progressEdit.setVisibility(View.GONE);
                AppValidator.logData("getUserProfile",""+t.getMessage());
            }
        });



    }

    public void  callUpdateProfileApi(UpdateProfileRequestModel updateProfileRequestModel){
        editProfileBinding.progressEdit.setVisibility(View.VISIBLE);
        Call<UpdateProfileResponse> call3 = apiInterface.callUpdateProfile(sharedPrefHelper.getValue(SharedPreferencesKeys.userId), updateProfileRequestModel.getFirstName(),
                updateProfileRequestModel.getLastName(),
                updateProfileRequestModel.getEmail());

        call3.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(@NotNull Call<UpdateProfileResponse> call, @NotNull Response<UpdateProfileResponse> response) {

                editProfileBinding.progressEdit.setVisibility(View.GONE);

                assert response.body() != null;
                AppValidator.showToast(requireActivity(), response.body().getStatus());

                UpdateProfileDataResponse getUserProfileResponseData = response.body().getData();

                if(!getUserProfileResponseData.getProfilePic().isEmpty()){
                    Glide.with(requireActivity()).load(getUserProfileResponseData.getProfilePic()).into(editProfileBinding.ivUser);
                }

                if(!getUserProfileResponseData.getFirstname().isEmpty()){
                    editProfileBinding.etFirstName.setText(getUserProfileResponseData.getFirstname());
                }

                if(!getUserProfileResponseData.getLastname().isEmpty()){
                    editProfileBinding.etLastName.setText(getUserProfileResponseData.getLastname());
                }

                if(!getUserProfileResponseData.getEmail().isEmpty()){
                    editProfileBinding.etEmail.setText(getUserProfileResponseData.getEmail());
                }


            }
            @Override
            public void onFailure(@NotNull  Call<UpdateProfileResponse> call,@NotNull Throwable t) {
                editProfileBinding.progressEdit.setVisibility(View.GONE);
                AppValidator.logData("getUserProfile",""+t.getMessage());
            }
        });


    }

}
