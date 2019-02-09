package com.kaur.bowl2recipe.util;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import com.kaur.bowl2recipe.MainActivity;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ApiCallAsyncTask extends AsyncTask<Void,Void, String> {
    public static final String TAG = ApiCallAsyncTask.class.getSimpleName();
    String mPayload;
    boolean mIsRecipe;
    Bitmap mBitmap;
    Util.OnAsyncCompleteListener mOnAsyncCompleteListener;


    public ApiCallAsyncTask(String mPayload, boolean mIsRecipe, Bitmap bitmap, Util.OnAsyncCompleteListener mOnAsyncCompleteListener) {
        this.mPayload = mPayload;
        this.mIsRecipe = mIsRecipe;
        this.mOnAsyncCompleteListener = mOnAsyncCompleteListener;
        this.mBitmap = bitmap;
    }


    //TODO Add server api calls
    @Override
    protected String doInBackground(Void... voids) {
        Context context = (Context) mOnAsyncCompleteListener;
        String response = null; // = Util.loadJSONFromAsset(context);
        byte[] resByte = null;
        String result = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        Log.e("Api", "Pre-request to response");

        if (mIsRecipe) {
            try {
                URL url = new URL(this.mPayload);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputstream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputstream));
                while ((response = reader.readLine()) != null) {
                    Log.e("Response", "Line: " + response);
                    buffer.append(response + "\n");
                }
                if (inputstream == null) {
                    Log.e("check", "" + inputstream);
                    return null;
                }
                response = buffer.toString();
            } catch (IOException e) {
                Log.e("Api", "IO Exception in API" + e.getStackTrace());
            } catch (Exception e) {
                Log.e("Api", "Exception! Error: " + e.getMessage() + " ," + e.getStackTrace());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        Log.e("Api", "Error while closing the stream." + e.getStackTrace());
                    }
                }
            }
            Log.e("API", "response: " + response);
        } else {

//            Bitmap bitmapObtained = mBitmap;
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmapObtained.compress(Bitmap.CompressFormat.JPEG, 50, stream);
//            byte[] mByteArray = stream.toByteArray();
//            String newString = new String(mByteArray);
////            String mImageString = Base64.encodeToString(mByteArray, Base64.DEFAULT);
//
//            try {
//                URL url = new URL(this.mPayload);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("POST");
//                conn.setDoOutput(true);
//                conn.setDoInput(true);
//
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//                Log.e("Api", newString);
//                writer.write(newString);
//
//                writer.flush();
//                writer.close();
//                os.close();
//                int responseCode=conn.getResponseCode();
//
//                if (responseCode == HttpsURLConnection.HTTP_OK) {
//                    Log.d(TAG, "doInBackground: Successfull");
//                    String line;
//                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                    while ((line=br.readLine()) != null) {
//                        response+=line;
//                    }
//                }
//                else {
//                    response="";
//                }
//                Log.d(TAG, "doInBackground: RESPONSE : " + response);
//            } catch (Exception e) {
//                Log.e(TAG, "doInBackground: " + e.getMessage() );
//                e.printStackTrace();
//            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mOnAsyncCompleteListener.onComplete(s);
    }
}
