package com.example.wardrobe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wardrobe.model.GarmentsModel;
import com.example.wardrobe.model.entities.Garment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class NewGramentFragment extends Fragment {
    GarmentsViewModel viewModel;
    private static final String TAG = "NewGramentFragment";
    private static final String APP_NAME = "wardrobe";

    // permission section
    private static final int REQUEST_CAMERA_AND_STORAGE = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static String[] CAMERA_AND_STORAGE_PERMISSION = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private File tempFile;
    private EditText sizeText;
    private EditText typeText;
    private EditText colorText;
    private ImageView imgViewAdd;
    private Button btnAddPhoto;
    private Button btnSave;
    private String actionType;
    private String garment_id;

    public NewGramentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_grament, container, false);
        this.colorText = v.findViewById(R.id.new_garment_color);
        this.typeText = v.findViewById(R.id.new_garment_type);
        this.sizeText = v.findViewById(R.id.new_garment_size);
        this.imgViewAdd = v.findViewById(R.id.imgViewAdd);
        btnAddPhoto = v.findViewById(R.id.new_garment_photo_button);
        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                NewGramentFragment.this.takePicture();
            }
        });
        btnSave = v.findViewById(R.id.new_garment_save_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                NewGramentFragment.this.saveGarment();
            }
        });

        actionType = NewGramentFragmentArgs.fromBundle(getArguments()).getActionType();

        if(actionType.equals("Edit")){
            garment_id = NewGramentFragmentArgs.fromBundle(getArguments()).getGarmentId();

            viewModel.getGarment(garment_id, new GarmentsModel.Listener<Garment>() {
                @Override
                public void onComplete(Garment data) {
                    sizeText.setText(data.getSize());
                    typeText.setText(data.getType());
                    colorText.setText(data.getColor());
                    if (data.getImageUrl() != null && data.getImageUrl() != "") {
                        Picasso.get().load(data.getImageUrl()).into(imgViewAdd);
                    }

                    btnSave.setText("Update");
                }

            });
        }
        else {
            btnSave.setText("Save");
        }

        return v;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(GarmentsViewModel.class);
    }

    public void takePicture() {
        Log.d(TAG, "takePicture: check if app has permissions");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "takePicture: start camera activity");
            Intent takePictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                tempFile = createTmpFile();
                if (tempFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(),
                            "com.example.wardrobe.fileprovider",
                            tempFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        } else {
            Log.d(TAG, "takePicture: ask user for permissions");
            ActivityCompat.requestPermissions(getActivity(),CAMERA_AND_STORAGE_PERMISSION, REQUEST_CAMERA_AND_STORAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: get result from camera");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            // add image to gallery
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(tempFile);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);
            imgViewAdd.setImageURI(contentUri);
        } else {
            Log.e(TAG, "onActivityResult: could not get camera data");
            tempFile.delete();
            tempFile = null;
            Toast.makeText(getActivity(), "change it later", Toast.LENGTH_SHORT).show();
        }
    }

    private File createTmpFile() {
        String fileName = UUID.randomUUID().toString();
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_NAME);
        if (!isExternalStorageWritable()) {
            Log.e(TAG, "external storage is not writable");
            Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_LONG).show();
            return null;
        }
        if (!isExternalStorageReadable()) {
            Log.e(TAG, "external storage is not readable");
            Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_LONG).show();
            return null;
        }
        // make sure dir is exist
        storageDir.mkdirs();
        Log.d(TAG, "createTmpFile: create temp file");
        try {
            File image = File.createTempFile(
                    fileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            return image;
        } catch (IOException error) {
            Log.e(TAG, "createTmpFile: could not create temp file", error);
            Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // in case asked for camera and storage
        if (requestCode == REQUEST_CAMERA_AND_STORAGE) {
            if (verifyPermissions(grantResults)) {
                // in case all permission granted
                takePicture();
            } else {
                Toast.makeText(getActivity(), "enter text here!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void saveGarment() {
        if (actionType.equals("Add")) {
            viewModel.saveImage(tempFile, UUID.randomUUID().toString(), new OnSuccessListener<Object>() {
                @Override
                public void onSuccess(Object newImageUrl) {
                    btnAddPhoto.setEnabled(false);
                    btnSave.setEnabled(false);
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = auth.getCurrentUser();
                    if (currentUser != null) {
                        String uId = currentUser.getUid();
                        Garment garment = new Garment(newImageUrl.toString(), uId, NewGramentFragment.this.typeText.getText().toString(), NewGramentFragment.this.colorText.getText().toString(), NewGramentFragment.this.sizeText.getText().toString());
                        viewModel.addNewGarment(garment, new GarmentsModel.CompListener() {
                            @Override
                            public void onComplete() {
                                // Do something
                            }
                        });
                    }

                }
            });
        } else {
            // check if image was changed and them update garment

        }
    }
}
