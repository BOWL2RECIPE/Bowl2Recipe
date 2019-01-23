package com.kaur.bowl2recipe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int GALLERY_REQUEST = 101;
    static final int REQUEST_TAKE_PHOTO = 102;
    Uri mPhotoUri;
    ImageView mCameraButton;
    ProgressDialog prgDialog;
    String encodedString;
//    RequestParams params = new RequestParams();
    String imgPath, fileName;
    Bitmap bitmap, lesimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCameraButton = findViewById(R.id.camera_button);
        final EditText recipeNameEditText = findViewById(R.id.recipe_edit_text_view);
        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipeName = recipeNameEditText.getText().toString();

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
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        mCameraButton.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;

                case REQUEST_TAKE_PHOTO:
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mPhotoUri);
                        mCameraButton.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
//
//            String hjkl=currentDateTimeString.replaceAll(" ", "_");
//            String hiop=hjkl.replaceAll(":", "-");
//            TIME_STAMP=hiop;
//            fileName=TIME_STAMP+".jpeg";
//            params.put("filename", fileName);
                    break;
            }
        }

    }


    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;

                bitmap=lesimg;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();

                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage("Calling Upload");
                prgDialog.show();

//                params.put("image", encodedString);

//                triggerImageUpload();
            }
        }.execute(null, null, null);
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
