package com.example.uberapp_tim13.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.example.uberapp_tim13.R;
import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Utils {

    public static String convertUriToBase64(Uri uri, Context context) throws IOException {
        Bitmap bitmapNotScaled= MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapNotScaled.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return "data:image/jpeg;base64, " + encoded;
    }

    public static Bitmap convertBase64ToBitmap(String data) {
        String encodedImage = data.split(", ")[1].trim();
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static void changeBtnToBlue(MaterialButton btn, Activity activity) {
        btn.setTextColor(activity.getResources().getColor(R.color.lighter_blue));
        btn.setIconTint(ColorStateList.valueOf(activity.getResources().getColor(R.color.lighter_blue)));
        btn.setStrokeColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.lighter_blue)));
    }

    public static void changeBtnToGray(MaterialButton btn, Activity activity) {
        btn.setTextColor(activity.getResources().getColor(R.color.disabled_gray));
        btn.setIconTint(ColorStateList.valueOf(activity.getResources().getColor(R.color.disabled_gray)));
        btn.setStrokeColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.disabled_gray)));
    }
}
