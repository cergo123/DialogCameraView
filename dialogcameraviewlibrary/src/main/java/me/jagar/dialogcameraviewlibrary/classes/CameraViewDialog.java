package me.jagar.dialogcameraviewlibrary.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import me.jagar.dialogcameraviewlibrary.R;
import me.jagar.dialogcameraviewlibrary.interfaces.OnCaptureCancelled;
import me.jagar.dialogcameraviewlibrary.interfaces.OnCaptureDone;
import me.jagar.dialogcameraviewlibrary.interfaces.OnRecapturing;

public class CameraViewDialog {

    Context context;
    String imageTempPath;
    AlertDialog.Builder alertDialog;
    AlertDialog screen;

    //Listners
    OnCaptureDone onCaptureDone = null;
    OnCaptureCancelled onCaptureCancelled = null;
    OnRecapturing onRecapturing = null;
    int times = 0;

    public void init(Context context, String imageTempPath){
        this.context = context;
        this.imageTempPath = imageTempPath;
    }

    public void startDialog(){

        //Inflating the AlertDialog
        alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.camera_view_dialog, null);
        alertDialog.setView(dialogView);


        //CameraView UI
        screen = alertDialog.create();
        final CameraKitView cameraKitView = dialogView.findViewById(R.id.cameraKitView);
        final FloatingActionButton fabCapture = dialogView.findViewById(R.id.fabCapture);
        final FloatingActionButton fabDone = dialogView.findViewById(R.id.fabDone);
        final FloatingActionButton fabRepeat = dialogView.findViewById(R.id.fabRepeat);
        final FloatingActionButton fabSwitch = dialogView.findViewById(R.id.fabSwitch);
        final ImageView imageView = dialogView.findViewById(R.id.imgView);


        //Handler
        final File[] image = {null};

        //We start the camera
        cameraKitView.onStart();


        //Capturing action
        fabCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onImage(final CameraKitView cameraKitView, byte[] bytes) {
                        image[0] = new File(imageTempPath);
                        if (image[0].exists())
                            image[0].delete();

                        FileOutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(image[0].getPath(), false);
                            outputStream.write(bytes);
                            outputStream.flush();
                            outputStream.close();
                            imageView.setImageBitmap(BitmapFactory.decodeFile(image[0].getAbsolutePath()));
                            cameraKitView.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                            fabCapture.setVisibility(View.GONE);
                            fabSwitch.setVisibility(View.GONE);
                            fabDone.setVisibility(View.VISIBLE);
                            fabRepeat.setVisibility(View.VISIBLE);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

        //Recapturing
        fabRepeat.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.GONE);
                cameraKitView.setVisibility(View.VISIBLE);
                fabRepeat.setVisibility(View.GONE);
                fabDone.setVisibility(View.GONE);
                fabCapture.setVisibility(View.VISIBLE);
                fabSwitch.setVisibility(View.VISIBLE);

                if (onRecapturing != null){
                    times++;
                    onRecapturing.onRecapturing(times);
                }
            }
        });

        //Done, let's display it in the imageView
        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image[0] != null)
                    //TODO
                screen.dismiss();
                if (onCaptureDone != null){
                    onCaptureDone.onDone(imageTempPath);
                }

            }
        });

        //To let us start new capturing
        screen.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                cameraKitView.stopVideo();
                cameraKitView.onStop();

                if (onCaptureCancelled != null && !(new File(imageTempPath).exists())){
                    onCaptureCancelled.OnCancelled();
                }
            }
        });

        fabSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraKitView.getFacing() == CameraKit.FACING_BACK){
                    cameraKitView.setFacing(CameraKit.FACING_FRONT);
                }
                else{
                    cameraKitView.setFacing(CameraKit.FACING_BACK);
                }
            }
        });
        //Display the dialog
        screen.show();
        }

        public void setOnCaptureDoneListener(OnCaptureDone onCaptureDone){
            this.onCaptureDone = onCaptureDone;
        }
        public void setOnCaptureCancelledListener(OnCaptureCancelled onCaptureCancelled){
            this.onCaptureCancelled = onCaptureCancelled;
        }
        public void setOnRecapturingListener(OnRecapturing onRecapturing){
            this.onRecapturing = onRecapturing;
        }
    }

