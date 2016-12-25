package vishnu.torch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.security.Policy;

public class MainActivity extends AppCompatActivity {

    ImageButton imageButton;
    private boolean flashOn;
    private boolean hasFlash;
    String cameraId;
    private CameraManager cameraManager;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        flashOn = false;

        //for flashlight availbility
        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle(" Error");
            alertDialog.setMessage(" Your Device Dosenot Support flash light");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }
            });
            alertDialog.show();
            return;
        }

        //for flashlight On-Off
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (flashOn) {
                        turnofflash();
                        flashOn = false;
                    } else {
                        turnonflash();
                        flashOn = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void turnofflash() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false);
                imageButton.setImageResource(R.drawable.btn_switch_off);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void turnonflash() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true);
                imageButton.setImageResource(R.drawable.btn_switch_on);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(flashOn)
        {
            turnofflash();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(flashOn)
        {
            turnofflash();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(flashOn)
        {
            turnonflash();
        }
    }
}
