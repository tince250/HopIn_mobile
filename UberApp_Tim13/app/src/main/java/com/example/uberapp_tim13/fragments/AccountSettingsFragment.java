package com.example.uberapp_tim13.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountSettingsFragment extends Fragment {

    ImageView profilePictureIV;
    ImageView editProfilePicBtn;

    String chosenPictureEncoded = null;

    View currView;

    public static AccountSettingsFragment newInstance() {
        return new AccountSettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        profilePictureIV = (ImageView) view.findViewById(R.id.driverAvatarSettingsImg);
        editProfilePicBtn = view.findViewById(R.id.editProfilePictureBtn);

        fillUserInfo(view);
//        setSpinner(view);

        currView = view;

        ActivityResultLauncher<Intent> launcher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                    if(result.getResultCode()==RESULT_OK){
                        Uri uri=result.getData().getData();
                        profilePictureIV.setImageURI(uri);
                        try {
                            chosenPictureEncoded = Utils.convertUriToBase64(uri, getActivity());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if(result.getResultCode()==ImagePicker.RESULT_ERROR){
                        // Use ImagePicker.Companion.getError(result.getData()) to show an error
                    }
                });



        editProfilePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.with(getActivity())
                        .cropSquare()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent(intent -> {
                            launcher.launch(intent);
                            return null;
                        });
            }
        });

        view.findViewById(R.id.saveProfileChanges).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges(currView);
            }
        });

        return view;
    }

//    private void setSpinner(View view) {
//        Spinner areaNumSpinner = view.findViewById(R.id.spinnerAreaNumSettings);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.area_number_options));
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        areaNumSpinner.setAdapter(adapter);
//    }

    private void fillUserInfo(View view) {
        ((EditText) view.findViewById(R.id.nameSettingsET)).setText(Globals.user.getName());
        ((EditText) view.findViewById(R.id.surnameSettingsET)).setText(Globals.user.getSurname());
        ((EditText) view.findViewById(R.id.emailSettingsET)).setText(Globals.user.getEmail());
        ((EditText) view.findViewById(R.id.citySettingsET)).setText(Globals.user.getAddress());
        ((EditText) view.findViewById(R.id.phoneSettingsET)).setText(Globals.user.getTelephoneNumber());
        if (Globals.user.getProfilePicture() != null) {
            profilePictureIV.setImageBitmap(Utils.convertBase64ToBitmap(Globals.user.getProfilePicture()));
        }
    }

    public void saveChanges(View view) {
        UserDTO newUserInfo = new UserDTO();
        newUserInfo.setName(((EditText) view.findViewById(R.id.nameSettingsET)).getText().toString());
        newUserInfo.setSurname(((EditText) view.findViewById(R.id.surnameSettingsET)).getText().toString());
        newUserInfo.setAddress(((EditText) view.findViewById(R.id.citySettingsET)).getText().toString());
        newUserInfo.setEmail(((EditText) view.findViewById(R.id.emailSettingsET)).getText().toString());
        newUserInfo.setTelephoneNumber(((EditText) view.findViewById(R.id.phoneSettingsET)).getText().toString());
        if (chosenPictureEncoded != null)
            newUserInfo.setProfilePicture(chosenPictureEncoded);
        else
            newUserInfo.setProfilePicture(Globals.user.getProfilePicture());

        Call<UserDTO> call = RestUtils.passengerAPI.update(AuthService.tokenDTO.getAccessToken(), Globals.user.getId(), newUserInfo);
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    Globals.user = response.body();
                    fillUserInfo(currView);
                    Toast.makeText(getActivity(), "Profile info successfully updated :)", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getActivity(), "Oops! We couldn't update your info because of an error :(", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {

            }
        });
    }


}
