package com.example.proyectoec03;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileUserFragment extends Fragment {

    ImageView imageProfile;
    ProgressDialog progressDialog;
    private static final int CAMERA_REQUEST = 100;
    private static final int IMAGE_PICK_CAMERA_REQUEST = 400;

    String cameraPermission[];
    Uri imageUri;
    String profileOrCoverImage;
    MaterialButton editImage;

    MaterialButton btn_save;
    TextInputLayout text_layout_name, text_layout_age, text_layout_location, text_layout_phone;
    TextInputEditText text_edit_name, text_edit_age, text_edit_location, text_edit_phone;

    TextView name, phone;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedStateInstance) {
        View view = inflater.inflate(R.layout.fragment_profile_user, parent, false);
        editImage = view.findViewById(R.id.edit_image);
        imageProfile = view.findViewById(R.id.profile_image);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);

        text_edit_name = view.findViewById(R.id.text_edit_name);
        text_edit_phone = view.findViewById(R.id.text_edit_phone);
        text_edit_location = view.findViewById(R.id.text_edit_location);
        text_edit_age = view.findViewById(R.id.text_edit_age);

        text_layout_name = view.findViewById(R.id.text_layout_name);
        text_layout_age = view.findViewById(R.id.text_layout_age);
        text_layout_location = view.findViewById(R.id.text_layout_location);
        text_layout_phone = view.findViewById(R.id.text_layout_phone);

        btn_save = view.findViewById(R.id.button_save);

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Updating Profile Picture");
                profileOrCoverImage = "image";
                showImagePicDialog();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_save.getText() == getResources().getString(R.string.btn_edit)) {
                    btn_save.setText(R.string.btn_save);
                    text_layout_name.setEnabled(true);
                    text_layout_age.setEnabled(true);
                    text_layout_location.setEnabled(true);
                    text_layout_phone.setEnabled(true);
                } else {
                    if(text_edit_name.getText().length() == 0 && text_edit_age.getText().length() == 0
                    && text_edit_location.getText().length() == 0 && text_edit_age.getText().length() == 0){
                        text_layout_name.setError("Complete este campo");
                        text_layout_age.setError("Complete este campo");
                        text_layout_location.setError("Complete este campo");
                        text_layout_age.setError("Complete este campo");
                    }else if (text_edit_name.getText().length() == 0) {
                        text_layout_name.setError("Complete este campo");
                    } else if (text_edit_age.getText().length() == 0) {
                        text_layout_age.setError("Complete este campo");
                    } else if (text_edit_location.getText().length() == 0) {
                        text_layout_location.setError("Complete este campo");
                    } else if (text_edit_phone.getText().length() == 0) {
                        text_layout_phone.setError("Complete este campo");
                    } else {
                        text_layout_name.setError(null);
                        text_layout_age.setError(null);
                        text_layout_location.setError(null);
                        text_layout_phone.setError(null);
                        name.setText("" + text_edit_name.getText());
                        phone.setText("" + text_edit_phone.getText());
                        text_layout_name.setEnabled(false);
                        text_layout_age.setEnabled(false);
                        text_layout_location.setEnabled(false);
                        text_layout_phone.setEnabled(false);
                        text_layout_name.requestFocus();
                        btn_save.setText(R.string.btn_edit);
                        Toast.makeText(getContext(), "Información guardada", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Glide.with(this).load(imageUri).into(imageProfile);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResult.length > 0) {
                    boolean cameraAccepted = grantResult[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResult[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getContext(), "Please enable camera and storage permission", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Something went wrong! try again...", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                }
            }
        });
        builder.create().show();
    }

    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp description");
        imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_REQUEST);
    }

}
