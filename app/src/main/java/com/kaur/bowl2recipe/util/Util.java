package com.kaur.bowl2recipe.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class Util {

    public static String toCSV(JSONArray array) throws JSONException {
        String result = "";

        if (array.length() > 0) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < array.length(); i++) {
                String s = array.getString(i);
                sb.append(s).append(",");
            }

            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }

    public static String encodeImage(Bitmap bitmap) {
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();

        String encodedString = Base64.encodeToString(byte_arr, 0);
        return encodedString;
    }

    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public interface OnAsyncCompleteListener {

        void onComplete(String response);
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }
}
