package me.jagar.dialogcameraview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import me.jagar.dialogcameraviewlibrary.classes.CameraViewDialog;
import me.jagar.dialogcameraviewlibrary.interfaces.OnCaptureCancelled;
import me.jagar.dialogcameraviewlibrary.interfaces.OnCaptureDone;
import me.jagar.dialogcameraviewlibrary.interfaces.OnRecapturing;

public class MainActivity extends AppCompatActivity {

    private int ALL_PERMISSIONS = 1;

    private ImageView imageView;
    private Button btnCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imgViewMainActivity);
        btnCapture = findViewById(R.id.btnOpenCamera);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });


    }

    private void checkPermissions() {
        //Permissions we need
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        //Permissions that we will ask for
        ArrayList<String> needed_permissions = new ArrayList<>();

        //Check which is not granted yet
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) !=
                    PackageManager.PERMISSION_GRANTED){
                needed_permissions.add(permission);
            }
        }

        //Ask for multiple not granted permissions
        if(!needed_permissions.isEmpty())
            ActivityCompat.requestPermissions(MainActivity.this, needed_permissions.toArray(new String[needed_permissions.size()]), ALL_PERMISSIONS);
        else
            openCameraDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ALL_PERMISSIONS){
            if ((grantResults.length > 0) &&
                    (grantResults[0]
                            + grantResults[1]
                            + grantResults[2] == PackageManager.PERMISSION_GRANTED)){
                openCameraDialog();

            }else {
                Toast.makeText(MainActivity.this, "All permissions need to be granted", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    private void openCameraDialog() {

        CameraViewDialog cameraViewDialog = new CameraViewDialog();
        cameraViewDialog.init(MainActivity.this, Environment.getExternalStorageDirectory().getAbsolutePath()
        + File.separator + "photo.jpg");
        cameraViewDialog.setOnCaptureDoneListener(new OnCaptureDone() {
            @Override
            public void onDone(String imagePath) {
                imageView.setImageBitmap(BitmapFactory.decodeFile((new File(imagePath)).getAbsolutePath()));
            }
        });
        cameraViewDialog.setOnCaptureCancelledListener(new OnCaptureCancelled() {
            @Override
            public void OnCancelled() {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG)
                        .show();
            }
        });
        cameraViewDialog.setOnRecapturingListener(new OnRecapturing() {
            @Override
            public void onRecapturing(int times) {
                Toast.makeText(MainActivity.this, String.valueOf(times), Toast.LENGTH_SHORT)
                        .show();
            }
        });
        cameraViewDialog.startDialog();

    }
}
