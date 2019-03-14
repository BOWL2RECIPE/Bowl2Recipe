package com.kaur.bowl2recipe.util;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        String response = null;
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
                    if (response.contains("label")) {
                        Log.e("Response", "Line: " + response);
                        buffer.append(response + "\n");
                    }
                }
                if (inputstream == null) {
                    Log.e("check", "" + inputstream);
                    return null;
                }
                if (response == "[]\n") {
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
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mOnAsyncCompleteListener.onComplete(s);
    }
}
