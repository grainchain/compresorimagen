package com.example.compresomieda;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

import id.zelory.compressor.Compressor;

public class TomarComprimirImagen extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST_F = 1;
    private static final int PICK_IMAGE_REQUEST_T = 199;


    private ImageView actualImageView;
    private ImageView compressedImageView;
    private TextView actualSizeTextView;
    private TextView compressedSizeTextView;
    private File actualImage;
    private File compressedImage;


    private Button mAddPhotoButton;
    private Button mAddPhotoButton2;

    private String mCurrentPhotoPath;
    private Uri mPhotoURI;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ife);
        actualImageView = (ImageView) findViewById(R.id.actual_image);
        compressedImageView = (ImageView) findViewById(R.id.compressed_image);
        actualSizeTextView = (TextView) findViewById(R.id.actual_size);
        compressedSizeTextView = (TextView) findViewById(R.id.compressed_size);

        mAddPhotoButton = findViewById(R.id.tomarfoto1);
        mAddPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TomaImage(PICK_IMAGE_REQUEST_F);
            }
        });

        mAddPhotoButton2 = findViewById(R.id.tomarfoto2);
        mAddPhotoButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TomaImage(PICK_IMAGE_REQUEST_T);
            }
        });

        actualImageView.setBackgroundColor(getRandomColor());
        clearImage();
    }



    public void compressImage() throws IOException {
        if (actualImage == null) {
            showError("No se cargo la imagen");
        } else {
            compressedImage = new Compressor(this).compressToFile(actualImage);
        }
    }

    public void TomaImage (int s){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("ACTIVIDAD ", "Error ocurrido cuando se estaba creando el archivo de la imagen. Detalle: " + ex.toString());
            }

            if (photoFile != null) {
                mPhotoURI = FileProvider.getUriForFile(this,
                        "com.example.compresomieda.fileprovider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);
                startActivityForResult(intent,  s == 1 ? PICK_IMAGE_REQUEST_F : PICK_IMAGE_REQUEST_T );

                //startActivityForResult(intent, PICK_IMAGE_REQUEST_F);
            }
        }
    };

    private File createImageFile() throws IOException {
        mCurrentPhotoPath=null;
        String timeStamp = DateTimeUtils.formatDateForFileName(new Date());

        String prefix = "JPEG_" + timeStamp + "_";
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                prefix,
                ".jpg",
                directory
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        actualImage = image;
        return image;
    }


    private void clearImage() {
        actualImageView.setBackgroundColor(getRandomColor());
        actualImageView.setImageDrawable(null);
        compressedImageView.setImageDrawable(null);
        compressedImageView.setBackgroundColor(getRandomColor());
        compressedSizeTextView.setText("Size : -");

    }


    // FIXME: 21/04/2019 tengo que limpiar variables para cada foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST_F) {
            if (resultCode == RESULT_OK) {
                handleCameraPhoto( PICK_IMAGE_REQUEST_F);

            }
        }

        if (requestCode == PICK_IMAGE_REQUEST_T) {
            if (resultCode == RESULT_OK) {
                handleCameraPhoto( PICK_IMAGE_REQUEST_T);

            }
        }


    }


    private void handleCameraPhoto(int a) {


        if (mCurrentPhotoPath != null && actualImage!=null) {

            try {
                compressImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (a==PICK_IMAGE_REQUEST_F){
                actualImageView.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getPath()));
                actualSizeTextView.setText(String.format("Size : %s", getReadableFileSize(compressedImage.length())));

            }

            if (a==PICK_IMAGE_REQUEST_T){
                compressedImageView.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getPath()));
                compressedSizeTextView.setText(String.format("Size : %s", getReadableFileSize(compressedImage.length())));
            }

        }


    }

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private int getRandomColor() {
        Random rand = new Random();
        return Color.argb(100, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
