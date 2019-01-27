package com.kaur.bowl2recipe.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.kaur.bowl2recipe.MainActivity;

public class ApiCallAsyncTask extends AsyncTask<Void,Void, String> {
    public static final String TAG = ApiCallAsyncTask.class.getSimpleName();
    String mPayload;
    boolean mIsRecipe;
    Util.OnAsyncCompleteListener mOnAsyncCompleteListener;


    public ApiCallAsyncTask(String mPayload, boolean mIsRecipe, Bitmap bitmap, Util.OnAsyncCompleteListener mOnAsyncCompleteListener) {
        this.mPayload = mPayload;
        this.mIsRecipe = mIsRecipe;
        this.mOnAsyncCompleteListener = mOnAsyncCompleteListener;
    }


    //TODO Add server api calls
    @Override
    protected String doInBackground(Void... voids) {
        Context context = (Context) mOnAsyncCompleteListener;
        String response = Util.loadJSONFromAsset(context);

        if (mIsRecipe) {

        } else {

        }


        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mOnAsyncCompleteListener.onComplete(s);
    }

    //    public void triggerImageUpload() {
//        makeHTTPCall();
//    }
//
//    public void makeHTTPCall() {
//        prgDialog.setMessage("Invoking JSP");
//        prgDialog.show();
//        AsyncHttpClient client = new AsyncHttpClient();
//
//        client.post("http://Your Ip Address or Localhost:8080/ImageUploadWebApp/uploadimg.jsp",
//                params, new AsyncHttpResponseHandler() {
//
//                    @Override
//                    public void onSuccess(String response) {
//
//                        prgDialog.hide();
//                        Toast.makeText(getApplicationContext(), response,
//                                Toast.LENGTH_LONG).show();
//                    }
//
//
//                    @Override
//                    public void onFailure(int statusCode, Throwable error,
//                                          String content) {
//
//                        prgDialog.hide();
//
//                        if (statusCode == 404) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Requested resource not found",
//                                    Toast.LENGTH_LONG).show();
//                        }
//
//                        else if (statusCode == 500) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Something went wrong at server end",
//                                    Toast.LENGTH_LONG).show();
//                        }
//
//                        else {
//                            Toast.makeText(
//                                    getApplicationContext(),
//                                    "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
//                                            + statusCode, Toast.LENGTH_LONG)
//                                    .show();
//                        }
//                    }
//                });
//    }
}
