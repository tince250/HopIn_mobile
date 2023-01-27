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
import android.util.Patterns;
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

    private EditText nameET;
    private EditText surnameET;
    private EditText addressET;
    private EditText emailET;
    private EditText phoneET;


    private ImageView profilePictureIV;
    private ImageView editProfilePicBtn;

    private String chosenPictureEncoded = null;

    private View currView;

    public static AccountSettingsFragment newInstance() {
        return new AccountSettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        currView = view;

        fillGlobalLayoutVariables();
        fillUserInfo(view);
//        setSpinner(view);



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

    private void fillGlobalLayoutVariables() {
        nameET = (EditText) currView.findViewById(R.id.nameSettingsET);
        surnameET = (EditText) currView.findViewById(R.id.surnameSettingsET);
        emailET = (EditText) currView.findViewById(R.id.emailSettingsET);
        phoneET = (EditText) currView.findViewById(R.id.phoneSettingsET);
        addressET = (EditText) currView.findViewById(R.id.citySettingsET);
        profilePictureIV = (ImageView) currView.findViewById(R.id.driverAvatarSettingsImg);
        editProfilePicBtn = currView.findViewById(R.id.editProfilePictureBtn);
    }

//    private void setSpinner(View view) {
//        Spinner areaNumSpinner = view.findViewById(R.id.spinnerAreaNumSettings);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.area_number_options));
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        areaNumSpinner.setAdapter(adapter);
//    }

    private void fillUserInfo(View view) {
        nameET.setText(Globals.user.getName());
        surnameET.setText(Globals.user.getSurname());
        emailET.setText(Globals.user.getEmail());
        addressET.setText(Globals.user.getAddress());
        phoneET.setText(Globals.user.getTelephoneNumber());
        if (Globals.user.getProfilePicture() != null) {
            profilePictureIV.setImageBitmap(Utils.convertBase64ToBitmap(Globals.user.getProfilePicture()));
        }
    }

    public void saveChanges(View view) {
        if (!this.validateFields())
            return;


        UserDTO newUserInfo = this.copyNewInfo(view);

        if (Globals.userRole.equals("passenger")) {
            Call<UserDTO> call = RestUtils.passengerAPI.update(AuthService.tokenDTO.getAccessToken(), Globals.user.getId(), newUserInfo);
            call.enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if (response.isSuccessful()) {
                        Globals.user = response.body();
                        fillUserInfo(currView);
                        Toast.makeText(getActivity(), "Profile info successfully updated :)", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getActivity(), response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {

                }
            });
        } else {
            Call<UserDTO> call = RestUtils.driverAPI.update(AuthService.tokenDTO.getAccessToken(), Globals.user.getId(), newUserInfo);
            call.enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if (response.isSuccessful()) {
                        Globals.user = response.body();
                        fillUserInfo(currView);
                        Toast.makeText(getActivity(), "Profile info successfully updated :)", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getActivity(), response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {

                }
            });
        }
    }

    private boolean validateFields() {
        boolean valid = true;
        if (nameET.getText().toString().isEmpty()) {
            nameET.setError("Name can't be empty.");
            valid = false;
        } else {
            if (!nameET.getText().toString().matches("^([a-zA-Zčćđžš ]*)$")) {
                nameET.setError("Name can contain only letters.");
                valid = false;
            }
        }
        if (surnameET.getText().toString().isEmpty()) {
            surnameET.setError("Surname can't be empty.");
            valid = false;
        }{
            if (!surnameET.getText().toString().matches("^([a-zA-Zčćđžš ]*)$")) {
                surnameET.setError("Surame can contain only letters.");
                valid = false;
            }
        }
        if (addressET.getText().toString().isEmpty()) {
            addressET.setError("Address can't be empty.");
            valid = false;
        }
        if (emailET.getText().toString().isEmpty()) {
            emailET.setError("Email can't be empty.");
            valid = false;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(emailET.getText().toString()).matches()) {
                emailET.setError("Not a valid email address.");
                valid = false;
            }
        }
        if (phoneET.getText().toString().isEmpty()) {
            phoneET.setError("Phone can't be empty.");
            valid = false;
        }

        return valid;
    }

    private UserDTO copyNewInfo(View view) {
        UserDTO newUserInfo = new UserDTO();
        newUserInfo.setName(nameET.getText().toString());
        newUserInfo.setSurname(surnameET.getText().toString());
        newUserInfo.setAddress(addressET.getText().toString());
        newUserInfo.setEmail(emailET.getText().toString());
        newUserInfo.setTelephoneNumber(phoneET.getText().toString());
        if (chosenPictureEncoded != null)
            newUserInfo.setProfilePicture(chosenPictureEncoded);
        else
            newUserInfo.setProfilePicture(Globals.user.getProfilePicture());

        return newUserInfo;
    }


}
