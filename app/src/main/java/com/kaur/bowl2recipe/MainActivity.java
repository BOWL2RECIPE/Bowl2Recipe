package com.kaur.bowl2recipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kaur.bowl2recipe.util.ApiCallAsyncTask;
import com.kaur.bowl2recipe.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements Util.OnAsyncCompleteListener {
    static final int GALLERY_REQUEST = 101;
    static final int REQUEST_TAKE_PHOTO = 102;
    public static final String RECIPE_LIST_JSON = "recipe_list_json";
    public static final String RECIPE_INFO = "%s/recipe-info?recipe_name=%s";
    public static final String RECIPE_NAME = "%s/recipe-name";
    public static final String TAG = "MainActivity";
    public RequestQueue requestQueue;

    Uri mPhotoUri;
    ImageView mCameraButton;
    ProgressBar mProgressBar;

    public boolean isOnline(){
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if(netInfo == null || !netInfo.isConnected()){
            Toast.makeText(MainActivity.this, R.string.no_internet, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = getRequestQueue();

        mCameraButton = findViewById(R.id.camera_button);
        mProgressBar = findViewById(R.id.progressBar);
        final EditText recipeNameEditText = findViewById(R.id.recipe_edit_text_view);
        recipeNameEditText.setMaxWidth(recipeNameEditText.getWidth());

        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipeName = recipeNameEditText.getText().toString();
                try{
                    if(isOnline()) {
                        if (!TextUtils.isEmpty(recipeName)) {
                            String recipeInfoUrl = String.format(RECIPE_INFO, getString(R.string.api_call), URLEncoder.encode(recipeName, "UTF-8"));
                            ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(recipeInfoUrl, true, null, MainActivity.this);
                            apiCallAsyncTask.execute();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.type_valid_dish, Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (UnsupportedEncodingException e){
                    Log.e("Encoding", "Error Stack: "+e.getStackTrace());
                }
            }
        });
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        Button uploadImageButton = findViewById(R.id.upload_button);
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(v);
            }
        });
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }


    public void makeNetworkCall(String payload, boolean isRecipe, Bitmap bitmap) {
        if (Util.isNetworkAvailable(this)) {
            mProgressBar.setVisibility(View.VISIBLE);
            String recipeNameUrl = String.format(RECIPE_NAME, getString(R.string.api_call));
            ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(recipeNameUrl, isRecipe, bitmap, this);
            apiCallAsyncTask.execute();
        } else {
            Toast.makeText(this, R.string.check_network, Toast.LENGTH_LONG).show();
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.kaur.bowl2recipe.fileprovider",
                        photoFile);
                mPhotoUri = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    public void loadImagefromGallery(View view) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        getRecipeName(bitmap);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;

                case REQUEST_TAKE_PHOTO:
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mPhotoUri);
                        getRecipeName(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    private void getRecipeName(Bitmap bitmap) {
        JSONObject imageJson = null;

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            // Add the image
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
            byte[] imageArray = stream.toByteArray();
            String base64Image = Base64.encodeToString(imageArray, Base64.DEFAULT);
            Log.e(TAG, "[image content]" + base64Image);
            imageJson = new JSONObject();
            imageJson.put("content", base64Image);
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Exception Occured", e);
        }

        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            JsonObjectRequest recipeNameRequest = new JsonObjectRequest(Request.Method.POST,
                    String.format(RECIPE_NAME, getString(R.string.api_call)), imageJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e(TAG, "[Response]" + response.toString(2));
                                String recipe_name_str = response.toString();
                                JSONObject jsonResult = new JSONObject(recipe_name_str);
                                JSONArray labels = jsonResult.getJSONArray("labels");

                                JSONObject c = labels.getJSONObject(0);
                                String recipeName = c.getString("name");

                                Log.e(TAG, "[Response]" + recipeName);
                                try {
                                    if (!TextUtils.isEmpty(recipeName)) {
                                        String recipeInfoUrl = String.format(RECIPE_INFO, getString(R.string.api_call), URLEncoder.encode(recipeName, "UTF-8"));
                                        ApiCallAsyncTask apiCallAsyncTask = new ApiCallAsyncTask(recipeInfoUrl, true, null, MainActivity.this);
                                        apiCallAsyncTask.execute();
                                    } else {
                                        Toast.makeText(MainActivity.this, R.string.dish_not_found, Toast.LENGTH_LONG).show();
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    Log.e("Encoding", "Error Stack: " + e.getStackTrace());
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "Exception Occured", e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Exception in request", error);
                        }
                    });
            requestQueue.add(recipeNameRequest);
        }
    }

    @Override
    public void onComplete(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                Intent myIntent = new Intent(MainActivity.this, ResultsActivity.class);
                myIntent.putExtra(RECIPE_LIST_JSON, response); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                mProgressBar.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, R.string.dish_not_found, Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(MainActivity.this, R.string.dish_not_found, Toast.LENGTH_LONG).show();

        }
    }

}
